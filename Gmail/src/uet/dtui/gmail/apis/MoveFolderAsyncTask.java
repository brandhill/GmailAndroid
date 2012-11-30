package uet.dtui.gmail.apis;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import android.os.AsyncTask;

public class MoveFolderAsyncTask extends AsyncTask<Void, Void, Void>{
	
	private Folder src;
	private Folder dest;
	private Message[] message;
	
	public MoveFolderAsyncTask(Message[] message,Folder src, Folder dest){
		this.dest = dest;
		this.src = src;
		this.message = message;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		moveFolderStarred();
		publishProgress(null);
		return null;
	}
	
	private void moveFolderStarred(){
		try {
			src.copyMessages(message, dest);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
