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

import uet.dtui.gmail.model.MessageEmail;
import android.os.AsyncTask;
import android.util.Log;

import com.sun.mail.imap.IMAPFolder;

public class MailReaderAsyncTask extends AsyncTask<Void, Void, Void> {
	private Message[] messages;
	private String username;
	private String password;
	private long UID;
	private long[] arrayUID;

	public MailReaderAsyncTask(String user, String pass, long UID) {
		this.username = user;
		this.password = pass;
		this.UID = UID;
	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
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
		IMAPFolder inbox = (IMAPFolder) store.getFolder("Inbox");
		inbox.open(inbox.READ_WRITE);
		//arrayUID = get20UID(UID, inbox);
		try{
			messages = inbox.getMessagesByUID(UID,UID+20);
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		for (int i = 0; i < messages.length; i++) {
//			mail_list.add(new MessageEmail(inbox.getUID(messages[i]), messages[i].getSubject().toString(),
//					InternetAddress.toString(messages[i].getFrom()),
//					InternetAddress.toString(messages[i]
//							.getRecipients(Message.RecipientType.TO)),
//							messages[i].getSentDate(), getContent(messages[i])));
//			Log.v("content", getContent(messages[i]));
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
}
