package uet.dtui.gmail.apis;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
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

public class MoveFolderAsyncTask extends AsyncTask<Void, Void, Void>{
	
	private String src;
	private String dest;
	private Message[] message;
	private int[] idMessageMove;
	private BaseListEmailActivity activity;
	private String username;
	private String password;
	private Context context;
	private EmailDatabase database;
	private IMAPFolder folderSrc;
	private IMAPFolder folderDest;
	
	public MoveFolderAsyncTask(BaseListEmailActivity activity, String src, String dest, int[] idMessageMove) throws MessagingException{
		this.idMessageMove = idMessageMove;
		this.src = src;
		this.dest = dest;
		this.activity = activity;
		this.context = activity.getApplicationContext();
		this.idMessageMove = idMessageMove;
		database = new EmailDatabase(context);
		
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		Session session = Session.getDefaultInstance(props, null);
		Store store = session.getStore("imaps");
		store.connect("imap.gmail.com", username, password);
		folderSrc = (IMAPFolder) store.getFolder(src);
		folderDest = (IMAPFolder) store.getFolder(dest);
		folderSrc.open(folderSrc.READ_WRITE);
		folderDest.open(folderDest.READ_WRITE);
		
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		moveFolder();
		publishProgress(null);
		return null;
	}
	
	private void moveFolder(){
		try {
			GetMessagesMove(idMessageMove);
			folderSrc.copyMessages(message, folderDest);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Message[] GetMessagesMove(int[] id){
		try {
			message = folderSrc.getMessages(id);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
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
