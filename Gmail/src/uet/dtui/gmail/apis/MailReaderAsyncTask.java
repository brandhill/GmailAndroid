package uet.dtui.gmail.apis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;

import uet.dtui.gmail.model.MessageEmail;
import android.os.AsyncTask;

public class MailReaderAsyncTask extends AsyncTask<Void, Void, Void> {
	private Message[] messages;
	private String user;
	private String pass;

	public MailReaderAsyncTask(String user, String pass) {
		this.user = user;
		this.pass = pass;
	}

	@Override
	protected Void doInBackground(Void... params) {
		setMessagesEmail();
		publishProgress();
		return null;
	}

	private void setMessagesEmail() {
		List<MessageEmail> mail_list = new ArrayList<MessageEmail>();
		Properties props = System.getProperties();
		Session session = Session.getDefaultInstance(props, null);
		Store store;
		try {
			store = session.getStore("imaps");
			store.connect("imap.gmail.com", "kienvtqhi@gmail.com", "kienhien90");
			// IMAPFolder inbox = (IMAPFolder) store.getFolder("INBOX");

			Folder inbox = store.getFolder("Inbox");
			inbox.open(Folder.READ_ONLY);

			int maxPos = inbox.getMessageCount();
			if (maxPos > 20) {
				int start = maxPos - 20;
				int end = maxPos;
				maxPos = maxPos - 21;
				messages = inbox.getMessages(start, end);
			} else {
				int start = 1;
				int end = maxPos;
				maxPos = 0;
				messages = inbox.getMessages(start, end);
			}

			// for (int i = messages.length - 1; i > messages.length - 10; i--)
			// {
			// mail_list.add(new MessageEmail(messages[i].getMessageNumber(),
			// messages[i].getSubject(), InternetAddress
			// .toString(messages[i].getFrom()),
			// InternetAddress.toString(messages[i]
			// .getRecipients(Message.RecipientType.TO)),
			// messages[i].getSentDate(), getContent(messages[i])));
			// }
			inbox.close(false);
			store.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// ham lay text trong body
	private static String getText(Part p) throws MessagingException,
			IOException {
		if (p.isMimeType("text/*")) {
			String s = (String) p.getContent();
			p.isMimeType("text/html");
			return s;
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
}
