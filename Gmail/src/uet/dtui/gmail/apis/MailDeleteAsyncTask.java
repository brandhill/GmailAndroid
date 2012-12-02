package uet.dtui.gmail.apis;

import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import com.sun.mail.imap.IMAPFolder;

import uet.dtui.gmail.activity.BaseListEmailActivity;
import uet.dtui.gmail.components.Utils;
import uet.dtui.gmail.database.EmailDatabase;
import uet.dtui.gmail.model.Account;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class MailDeleteAsyncTask extends AsyncTask<Void, Void, Void>{
	private Message[] messages;
	private long[] idMessages;
	public IMAPFolder folder;
	private String folderName;
	private Activity activity; 
	private String username;
	private String password;
	private Context context;
	private EmailDatabase database;
	
	public MailDeleteAsyncTask(Activity activity, String folderName, long[] idMessages) throws MessagingException{
		this.idMessages = idMessages;
		this.folderName = folderName;
		this.activity = activity;
		this.context = activity.getApplicationContext();
		database = new EmailDatabase(context);
		
		getUserAndPassWord();
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		try {
			Properties props = System.getProperties();
			props.setProperty("mail.store.protocol", "imaps");
			Session session = Session.getDefaultInstance(props, null);
			Store store = session.getStore("imaps");
			store.connect("imap.gmail.com", username, password);
			folder = (IMAPFolder) store.getFolder(folderName);
			folder.open(folder.READ_WRITE);
			arrayGmailDelete();
			//DataEmailDelete(arrayDataMailDelete());
			publishProgress(null);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void arrayGmailDelete() throws MessagingException{
		Log.v("folder",folder.getName());
		messages = folder.getMessagesByUID(idMessages);
		
		for (int i = 0; i < messages.length ; i++) {
			Log.d("UID and subject", folder.getUID(messages[i]) + "  " 
					+ messages[i].getSubject());
			messages[i].setFlag(Flags.Flag.DELETED, true);
		}
	}
	
	public void getUserAndPassWord() {
		this.username = Utils.getCurrentAcc(context);
		database.openDB();
		Account acc = database.getAccountFromEmail(username);
		this.password = acc.password;
		database.closeDB();
		Log.d("GET USER NAME AND PASS", "====>" + username + "  " + password);
	}

}
