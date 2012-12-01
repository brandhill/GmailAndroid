package uet.dtui.gmail.apis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Flags.Flag;
import javax.mail.internet.InternetAddress;

import uet.dtui.gmail.activity.BaseListEmailActivity;
import uet.dtui.gmail.components.Utils;
import uet.dtui.gmail.database.EmailDatabase;
import uet.dtui.gmail.model.Account;
import uet.dtui.gmail.model.MessageEmail;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.Contacts.Intents.UI;
import android.util.Log;
import android.view.View;

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
	private String fromEmailName;
	private boolean checkHTML = false;
	private String textPlain = "";
	private String textHTML = "";
	private String fileName = "";

	public MailReaderAsyncTask(BaseListEmailActivity activity, String nameFolder) {
		this.activity = activity;
		this.context = activity.getApplicationContext();
		this.nameFolder = nameFolder;
		database = new EmailDatabase(context);
		UID = this.getUIDMax(nameFolder) - 1;
	}
	
	

	@Override
	protected void onPreExecute() {
		activity.progressBar.setVisibility(View.VISIBLE);
		super.onPreExecute();
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
	
	

	@Override
	protected void onProgressUpdate(Void... values) {
		activity.getDataForList();
		activity.adapter.notifyDataSetChanged();
		super.onProgressUpdate(values);
	}
	
	

	@Override
	protected void onPostExecute(Void result) {
		activity.progressBar.setVisibility(View.GONE);
		super.onPostExecute(result);
	}



	private void getMessage() throws MessagingException, IOException {
		
		if (!activity.loading) {
			Log.d("DO IN BACK", UID + "Running.......");
			activity.loading = true;
			List<MessageEmail> mail_list = new ArrayList<MessageEmail>();
			Properties props = System.getProperties();
			props.setProperty("mail.store.protocol", "imaps");
			Session session = Session.getDefaultInstance(props, null);
			Store store = session.getStore("imaps");
			store.connect("imap.gmail.com", username, password);
			IMAPFolder folderImap = (IMAPFolder) store.getFolder(nameFolder);
			folderImap.open(folderImap.READ_WRITE);
			// arrayUID = get20UID(UID, inbox);
			try {
				int countMail = folderImap.getMessageCount();
				firstMessage = folderImap.getMessage(countMail);
				Log.d("UID MÃ", UID + "");
				if (UID == -1 || UID == 0) {
					UID = folderImap.getUID(firstMessage);
				} 
				if (UID > 20) {
					messages = folderImap.getMessagesByUID(UID - 20, UID);
				}
				if (UID < 20) {
					messages = folderImap.getMessagesByUID(1, UID);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			for (int i = 0; i < messages.length; i++) {
				MessageEmail mess = new MessageEmail();
				String cont = getContentMess(messages[i]);
				fromEmailName = InternetAddress.toString(messages[i].getFrom());
				mess.id = folderImap.getUID(messages[i]);
				mess.idFolder = getIdFolder(nameFolder);
				if (!messages[i].isSet(Flag.SEEN))
					Log.d("MESSAGE", "Xem roi");
				else
					Log.d("MESSAGE", "Chua xem");
				mess.subject = messages[i].getSubject();
				mess.from = extractEmailname(fromEmailName);
				mess.to = InternetAddress.toString(messages[i]
						.getRecipients(Message.RecipientType.TO));
				mess.date = formatterDate(messages[i].getSentDate().toString());
				if (cont.equals("<br>"))
					mess.content = "<This is email no body content>";
				else
					mess.content = cont;
				mess.fileName = fileName;
				mess.sourceFile = "";
				mess.contentHtml = "";
				textHTML = textPlain = "";
				saveMessageEmail(mess);
			}
			activity.loading = false;
		} else {
			
		}
		

	}
	
	public long getIdFolder(String nameFolder) {
		database = new EmailDatabase(context);
		database.openDB();
		String name = null;
		if (nameFolder.equals(Utils.FOLDER_NAME_INBOX))
			name = Utils.FOLDER_INBOX;
		else if (nameFolder.equals(Utils.FOLDER_IMPORTANT))
			name = Utils.FOLDER_IMPORTANT;
		else if (nameFolder.equals(Utils.FOLDER_NAME_DELETE))
			name = Utils.FOLDER_DELETE;
		else if (nameFolder.equals(Utils.FOLDER_NAME_SENT))
			name = Utils.FOLDER_SENT;
		long idAcc = database.getIDAccountFromEmail(Utils.getCurrentAcc(context));
		long idFolder = database.getIdFolderWithNameAndAcc(idAcc, name);
		database.closeDB();
		return idFolder;
	}

	private String getText(Part p) throws MessagingException, IOException {
		if (p.isMimeType("text/*")) {
			String s = (String) p.getContent();
			return s;
		}

		if (p.isMimeType("multipart/alternative")) {
			// prefer html text over plain text
			Multipart mp = (Multipart) p.getContent();
			String text = null;
			for (int i = 0; i < mp.getCount(); i++) {
				Part bp = mp.getBodyPart(i);
				if (bp.isMimeType("text/plain")) {
					if (text == null){
						text = getText(bp);
					}
					continue;
				} else if (bp.isMimeType("text/html")) {
					String s = getText(bp);
					if (s != null){
						return s;
					}
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
	
	private String getContentMess(Message message) throws IOException,
			MessagingException {
		String result = "";
		int index = message.getContent().toString().indexOf('.');
		if (index > 0) {
			String checkMessageContent = message.getContent().toString()
					.substring(0,index);
			if (checkMessageContent.equals("javax")) {
				
				Multipart multipart = (Multipart) message.getContent();
				// message.setFlag(Flags.Flag.DELETED, )
				for (int x = 0; x < multipart.getCount(); x++) {

					BodyPart bodyPart = multipart.getBodyPart(x);

					String disposition = bodyPart.getDisposition();

					if ((disposition != null)
							&& (disposition.equals("ATTACHMENT"))) {
						DataHandler handler = bodyPart.getDataHandler();
						fileName = handler.getName();
						
					} else {						
						result += getText(bodyPart) + "";
					}
				}
			} else {				
				result += message.getContent().toString();
				textHTML = result;
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
	}
	
	public long getUIDMax(String nameFoder) {
		String name = null;
		if (nameFolder.equals(Utils.FOLDER_NAME_INBOX))
			name = Utils.FOLDER_INBOX;
		else if (nameFolder.equals(Utils.FOLDER_IMPORTANT))
			name = Utils.FOLDER_IMPORTANT;
		else if (nameFolder.equals(Utils.FOLDER_NAME_DELETE))
			name = Utils.FOLDER_DELETE;
		else if (nameFolder.equals(Utils.FOLDER_NAME_SENT))
			name = Utils.FOLDER_SENT;
		
		database = new EmailDatabase(context);
		database.openDB();
		long uid = database.getIDMax(Utils.getCurrentAcc(context), name);
		database.closeDB();
		return uid;
	}

	private String extractDisplayname(String fromEmail) {
		String displayName = "";
		int index = fromEmail.indexOf('<');
		displayName = fromEmail.substring(0, index);
		return displayName;
	}

	private String extractEmailname(String fromEmail) {
		String emailName = "";
		int start = fromEmail.indexOf('<');
		int end = fromEmail.indexOf('>');
		if (start > 0 && end > 0)
			emailName = fromEmail.substring(start+1, end);
		return emailName;
	}

	private String formatterDate(String _date){
		String _dateFormat;

		String day = _date.substring(8,10);
		String month = _date.substring(4,7);
		String Hr = _date.substring(11,13);
		String Min = _date.substring(14,16);
		_dateFormat = Hr + ":" + Min + " " + month + " " + day; 
		return _dateFormat;
	}

}
