package uet.dtui.gmail.apis;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import android.os.AsyncTask;
import android.util.Log;

public class MailDeleteAsyncTask extends AsyncTask<Void, Void, Void>{
	private Message[] messages;
	private int[] idMessages;
	public Folder folder; 
	public MailDeleteAsyncTask(Folder folder, int[] idMessages){
		this.idMessages = idMessages;
		this.folder = folder;
		
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		try {
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
		messages = folder.getMessages(idMessages);
		
		for (int i = 0; i < messages.length ; i++)
					messages[i].setFlag(Flags.Flag.DELETED, true);
	}
	
	
	private void GmailDelete(Message[] messages) throws MessagingException{
		for(int i = 0; i < messages.length; i++)
			messages[i].setFlag(Flags.Flag.DELETED, true);
	}	
	
	

}
