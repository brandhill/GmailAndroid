package uet.dtui.gmail.apis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;

import uet.dtui.gmail.activity.BaseListEmailActivity;
import uet.dtui.gmail.components.Utils;
import uet.dtui.gmail.database.EmailDatabase;
import uet.dtui.gmail.model.Account;
import uet.dtui.gmail.model.MessageEmail;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.sun.mail.imap.IMAPFolder;

public class MailReaderAsyncTask extends AsyncTask<Void, Void, Void> {
	private Message[] messages;
	private Message firstMessage;
	private String username;
	private String password;
	private long UID;
	private long[] arrayUID;
	private Context context;
	private String nameFolder;
	private EmailDatabase database;
	private BaseListEmailActivity activity;

	public MailReaderAsyncTask(BaseListEmailActivity activity, String nameFolder) {
		this.activity = activity;
		this.context = activity.getApplicationContext();
		this.UID = Utils.getMaxUid(context);
		this.nameFolder = nameFolder;
		database = new EmailDatabase(context);
	}

	@Override
	protected Void doInBackground(Void... params) {
		getUserAndPassWord();
		try {
			getMessage();
			publishProgress();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private void getMessage() throws MessagingException, IOException {
		List<MessageEmail> mail_list = new ArrayList<MessageEmail>();
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		Session session = Session.getDefaultInstance(props, null);
		Store store = session.getStore("imaps");
		store.connect("imap.gmail.com", username, password);
		IMAPFolder folderImap = (IMAPFolder) store.getFolder(nameFolder);
		folderImap.open(folderImap.READ_WRITE);
		//arrayUID = get20UID(UID, inbox);
		try{
			int countMail = folderImap.getMessageCount();
			firstMessage = folderImap.getMessage(countMail - 1);
			if (UID == -1) {
				UID = folderImap.getUID(firstMessage);
			}
			messages = folderImap.getMessagesByUID(UID-20,UID);
			Utils.setMaxUid(context, UID-21);
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		for (int i = 0; i < messages.length; i++) {
			MessageEmail mess = new MessageEmail();
			mess.id = folderImap.getUID(messages[i]);
			mess.idFolder = 1;
			mess.subject = messages[i].getSubject().toString();
			mess.from = InternetAddress.toString(messages[i].getFrom());
			mess.to = InternetAddress.toString(messages[i]
					.getRecipients(Message.RecipientType.TO));
			mess.date = messages[i].getSentDate().toString();
			mess.content = getContent(messages[i]);
			mess.fileName = "";
			mess.sourceFile = "";
			mess.contentHtml = "";
			saveMessageEmail(mess);
			
			Log.v("content", getContent(messages[i]));
		}
		
	}

	// ham lay text trong body
	private static String getText(Part p) throws MessagingException,
			IOException {
		if (p.isMimeType("text/*")) {
			String s = (String) p.getContent();
			if (s != null && p.isMimeType("text/html"))
				return s;
			return "";
		}

		if (p.isMimeType("multipart/alternative")) {
			Multipart mp = (Multipart) p.getContent();
			String text = null;
			for (int i = 0; i < mp.getCount(); i++) {
				Part bp = mp.getBodyPart(i);
				if (bp.isMimeType("text/plain")) {
					if (text == null)
						text = getText(bp);
					continue;
				} else if (bp.isMimeType("text/html")) {
					String s = getText(bp);
					if (s != null)
						return s;
				} else {
					return getText(bp);
				}
			}
			return text;

		} else if (p.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) p.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				String s = getText(mp.getBodyPart(i));
				if (s != null)
					return s;
			}
		}

		return null;
	}

	private String getContent(Message message) throws IOException,
			MessagingException {
		String result = "";

		Multipart multipart = (Multipart) message.getContent();
		// message.setFlag(Flags.Flag.DELETED, )
		for (int x = 0; x < multipart.getCount(); x++) {

			BodyPart bodyPart = multipart.getBodyPart(x);

			String disposition = bodyPart.getDisposition();

			if ((disposition != null) && (disposition.equals("ATTACHMENT"))) {

				// saveAttachmentToFile(bodyPart);
			} else {

				result += getText(bodyPart) + "\n";
			}
		}
		return result;
	}

	// ham lay UID 20 email mot lan
	private long[] get20UID(long UIDStart, IMAPFolder folder) {
		long UID[] = new long[5];
		int count = 0;
		while (count < 5) {
			try {
				UID[count++] = folder.getUIDNext();				
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return UID;
	}
	
	public void saveMessageEmail(MessageEmail mess) {
		database.openDB();
		database.addMessage(mess);
		database.closeDB();
	}
	
	public void getUserAndPassWord() {
		this.username = Utils.getCurrentAcc(context);
		database.openDB();
		Account acc = database.getAccountFromEmail(username);
		this.password = acc.password;
		database.closeDB();
		Log.d("GET USER NAME AND PASS","====>" + username + "  " + password );
	}
}
