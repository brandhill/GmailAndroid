package uet.dtui.gmail.apis;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import android.os.AsyncTask;

public class MoveFolderAsyncTask extends AsyncTask<Void, Void, Void>{
	
	private Folder src;
	private Folder dest;
	private Message[] message;
	private int[] idMessageMove;
	public MoveFolderAsyncTask(Folder src, Folder dest, int[] idMessageMove){
		this.idMessageMove = idMessageMove;
		this.src = src;
		this.dest = dest;
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
			src.copyMessages(message, dest);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Message[] GetMessagesMove(int[] id){
		try {
			message = src.getMessages(id);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
}
