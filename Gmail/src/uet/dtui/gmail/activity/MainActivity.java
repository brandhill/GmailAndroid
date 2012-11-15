package uet.dtui.gmail.activity;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

import uet.dtui.gmail.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	private String host = "pop.gmail.com";
	private String username = "kienvtqhi";
	private String password = "kienhien90";
	private String provider = "imap";
	private boolean textIsHtml = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Properties props = System.getProperties();
        Session session = Session.getDefaultInstance(props, null);
        Store store;
		try {
			store = session.getStore("imaps");
			store.connect("imap.gmail.com", "kienvtqhi",
	                "kienhien90");
	        Folder inbox = store.getFolder("Inbox");
	        inbox.open(Folder.READ_WRITE);
	        
	        Message message[] = inbox.getMessages(1, 10);

	        for (int i = 0; i < message.length; i++) {
	             Log.d("From", message[i].getFrom()[0] + "");
	             Log.d("Subject", message[i].getSubject() + "");
	             String content = message[i].getContent() + "";
	             Date sent = message[i].getSentDate();
	             if (sent != null) {
	            	 Log.d("Sent", "Sent :" + sent);
	             }
	             if (content.indexOf("MimeMultiPart") != -1) {
	            	 Multipart multipart = (Multipart) message[i].getContent();
		             for (int j =0; j<multipart.getCount(); j++) {
		            	 BodyPart body =  multipart.getBodyPart(j);
		            	 String disposition = body.getDisposition();
		            	 if (disposition != null && (disposition.equals(BodyPart.ATTACHMENT))) {
		            		 Log.w("Attachment","Mail have attach ment");
		            		 DataHandler handler = body.getDataHandler();
		            		 Log.w("File name", handler.getName());
		            	 } else {
		            		 Log.w("Content", getText(body));
		            	 }
		            	 
		             }
	             
	              } else {
	            	  Log.d("content no multipart", content + "");
	              }
	        }

		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        		
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private String getText(BodyPart p) throws
		    MessagingException, IOException {
		if (p.isMimeType("text/*")) {
		String s = (String)p.getContent();
		textIsHtml = p.isMimeType("text/html");
		return s;
		}
		
		if (p.isMimeType("multipart/alternative")) {
		// prefer html text over plain text
		Multipart mp = (Multipart)p.getContent();
		String text = null;
		for (int i = 0; i < mp.getCount(); i++) {
		    BodyPart bp = mp.getBodyPart(i);
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
		Multipart mp = (Multipart)p.getContent();
		for (int i = 0; i < mp.getCount(); i++) {
		    String s = getText(mp.getBodyPart(i));
		    if (s != null)
		        return s;
		}
		}
		
		return null;
		}
}
