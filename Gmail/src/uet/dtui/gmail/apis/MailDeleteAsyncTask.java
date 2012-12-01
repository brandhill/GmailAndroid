package uet.dtui.gmail.apis;

import java.util.Properties;

import javax.mail.Flags;
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
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class MailDeleteAsyncTask extends AsyncTask<Void, Void, Void>{
	private Message[] messages;
	private int[] idMessages;
	public Folder folder; 
	private String nameFolder;
	private BaseListEmailActivity baseListActivity;
	private Context context;
	private EmailDatabase database;
	private String username;
	private String password;
	
	public MailDeleteAsyncTask(BaseListEmailActivity activity, String nameFolder, int[] idMessages){
		this.idMessages = idMessages;
		this.nameFolder = nameFolder;
		this.baseListActivity = activity;
		this.context = activity.getApplicationContext();
		database = new EmailDatabase(context);
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		try {
			getUserAndPassWord();
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
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		Session session = Session.getDefaultInstance(props, null);
		Store store = session.getStore("imaps");
		store.connect("imap.gmail.com", username, password);
		IMAPFolder folderImap = (IMAPFolder) store.getFolder(nameFolder);
		folderImap.open(folderImap.READ_WRITE);
		messages = folder.getMessages(idMessages);
		
		for (int i = 0; i < messages.length ; i++)
					messages[i].setFlag(Flags.Flag.DELETED, true);
	}
	
	
	private void GmailDelete(Message[] messages) throws MessagingException{
		for(int i = 0; i < messages.length; i++)
			messages[i].setFlag(Flags.Flag.DELETED, true);
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
