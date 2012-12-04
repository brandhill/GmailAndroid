package uet.dtui.gmail.apis;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;

import uet.dtui.gmail.R;
import uet.dtui.gmail.activity.BaseListEmailActivity;
import uet.dtui.gmail.components.Utils;
import uet.dtui.gmail.database.EmailDatabase;
import uet.dtui.gmail.model.Account;
import uet.dtui.gmail.model.MessageEmail;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.sun.mail.imap.IMAPFolder;

public class GetMailTask extends MailReaderAsyncTask {
	private Message msgs[];
	private Properties props;
	private Session session;
	private Store store;
	private Context context;
	private int newMsgCount = 0;
	private int currentLastestUID;
	IMAPFolder folder;
	private String username, password;

	public GetMailTask(Context cont) {
		super(cont);
		this.context = cont;
	}

	@Override
	protected void onPreExecute() {
		msgs = null;
		props = System.getProperties();
		session = Session.getDefaultInstance(props, null);
	}

	public void getUserAndPassWord() {
		this.username = Utils.getCurrentAcc(context);
		database.openDB();
		Account acc = database.getAccountFromEmail(username);
		this.password = acc.password;
		database.closeDB();
	}
	

	@Override
	protected Void doInBackground(Void... params) {
		getUserAndPassWord();
		Log.d("User name and password", username + " " + password);
		try {
			store = session.getStore("imaps");
			store.connect("imap.gmail.com",this.username, this.password);
			folder = (IMAPFolder) store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);

			Log.d("Here", "Here");

			int thelastestUID = 0;

			msgs = folder.getMessages();
			thelastestUID = (int) folder.getUID(msgs[msgs.length - 1]);
			while (true) {
				Thread.sleep(10000);
				msgs = folder.getMessages();
				// count how many msgs are new
				currentLastestUID = (int) folder.getUID(msgs[msgs.length - 1]);
				Log.d("Newest email UID", Integer.toString(currentLastestUID));
				// if there's any new msg

				if (currentLastestUID > thelastestUID) {

					newMsgCount += currentLastestUID - thelastestUID;
					// msgs is the array saves the new messages
					msgs = folder.getMessagesByUID(thelastestUID + 1,
							currentLastestUID);
					for (int i = msgs.length - 1; i >= 0; i--){
						saveMessageEmail(toMessageEmail(msgs[i]));
						Log.d("Mail", msgs[i].getSubject());
					}
					publishProgress(null);
					thelastestUID = currentLastestUID;
				}

			}// end while

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// ===========================================================================================//
	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		// notify here
		long when = System.currentTimeMillis();
		int notificationID = 1;

		String title = Integer.toString(newMsgCount) + " new message";
		if (newMsgCount > 1)
			title += "s";
		String content = "";
		try {
			content += msgs[msgs.length - 1].getSubject();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Intent intent = new Intent(context, BaseListEmailActivity.class);
		PendingIntent launchIntent = PendingIntent.getActivity(context, 0,intent, 0);

		String serName = Context.NOTIFICATION_SERVICE;
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(serName);
		Notification newEmailNotification = new Notification(
				R.drawable.mail_notification, title, when);
		newEmailNotification.flags |= Notification.FLAG_AUTO_CANCEL;
		newEmailNotification.setLatestEventInfo(context, title, content,
				launchIntent);

		notificationManager.notify(notificationID, newEmailNotification);

	}
	
	

	@Override
	protected void onPostExecute(Void result) {
	}

	// ==========================================================================//
	public MessageEmail toMessageEmail(Message msg) {
		MessageEmail mail = new MessageEmail();
	
		try {
			// get content
			mail.content = getContentMess(msg);
			// get date
			mail.date = msg.getReceivedDate().toString();
			// get attach file name
			if (msg.getFileName().length()>0) mail.fileName = msg.getFileName();
			else mail.fileName = msg.getFileName();
			//
			mail.from = extractEmailname(InternetAddress.toString(msg.getFrom()));
			mail.id = folder.getUID(msg);
			mail.idFolder = getIdFolder(Utils.FOLDER_NAME_INBOX);
			mail.sourceFile ="";
			mail.to = extractEmailname(InternetAddress.toString(msg
					.getRecipients(Message.RecipientType.TO)));
			mail.contentHtml = Utils.UNREAD_EMAIL;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return mail;
	}
}
