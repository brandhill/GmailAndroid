package uet.dtui.gmail.apis;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import android.util.Log;
import android.widget.CheckBox;

public class MailSender extends javax.mail.Authenticator {

	private String mailhost = "smtp.gmail.com";
	private String user;
	private String password;
	private Session session;
	private CheckBox cb_attach;

	static {
		Security.addProvider(new JSSEProvider());
	}

	public MailSender(String user, String password) {
		this.user = user;
		this.password = password;

		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", mailhost);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.quitwait", "false");

		session = Session.getDefaultInstance(props, this);
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(user, password);
	}

	public synchronized void sendMail(String subject, String body,
			String sender, String recipients, String fileName, CheckBox cb)
			throws Exception {
		try {
			Log.d("SEND MAIL", subject + body + sender + recipients + fileName);
			MimeMessage message = new MimeMessage(session);
			Multipart mp = new MimeMultipart();
			DataHandler handlerBody = new DataHandler(new ByteArrayDataSource(
					body.getBytes(), "text/plain"));

			MimeBodyPart mbp1 = new MimeBodyPart();
			MimeBodyPart mbp2 = new MimeBodyPart();
			message.setSender(new InternetAddress(sender));
			message.setSubject(subject);
			mbp1.setDataHandler(handlerBody);
			// message.setDataHandler(handler);
			if (recipients.indexOf(',') > 0)
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(recipients));
			else
				message.setRecipient(Message.RecipientType.TO,
						new InternetAddress(recipients));

			mp.addBodyPart(mbp1);
			if (cb != null)
				if (!fileName.equals("") && cb.getLinksClickable() == true) {
					FileDataSource fds = new FileDataSource(fileName);
					mbp2.setDataHandler(new DataHandler(fds));
					mbp2.setFileName(fds.getName());
					mp.addBodyPart(mbp2);
				}
			message.setContent(mp);
			Transport.send(message);
			Log.d("SEND MAIL", "SENDED");

		} catch (Exception e) {
			Log.e("fail", e.getMessage(), e);
		}
	}

	public class ByteArrayDataSource implements DataSource {
		private byte[] data;
		private String type;

		public ByteArrayDataSource(byte[] data, String type) {
			super();
			this.data = data;
			this.type = type;
		}

		public ByteArrayDataSource(byte[] data) {
			super();
			this.data = data;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getContentType() {
			if (type == null)
				return "application/octet-stream";
			else
				return type;
		}

		public InputStream getInputStream() throws IOException {
			return new ByteArrayInputStream(data);
		}

		public String getName() {
			return "ByteArrayDataSource";
		}

		public OutputStream getOutputStream() throws IOException {
			throw new IOException("Not Supported");
		}
	}

}
