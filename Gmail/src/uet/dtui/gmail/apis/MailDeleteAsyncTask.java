<<<<<<< HEAD
package uet.dtui.gmail.apis;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;

import uet.dtui.gmail.model.MessageEmail;
import android.os.AsyncTask;

public class MailDeleteAsyncTask extends AsyncTask<Void, Void, Void>{
	Message[] messages;
	int[] idMessages;
	MessageEmail[] messagesEmail;
	public MailDeleteAsyncTask(Message[] messages, int[] idMessages, MessageEmail[] messagesEmail){
		this.idMessages = idMessages;
		this.messages = messages;
		this.messagesEmail = messagesEmail;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		try {
			GmailDelete(arrayGmailDelete());
			DataEmailDelete(arrayDataMailDelete());
			publishProgress(null);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private Message[] arrayGmailDelete(){
		Message[] messagesDelete = null;
		int count = 0;
		
		for (int i = 0; i < messages.length; i++)
			for (int j = 0; j < idMessages.length; j++)
				if(messages[i].getMessageNumber() == idMessages[j])
					messagesDelete[count++] = messages[i];
		
		return messagesDelete;
	}
	
	private MessageEmail[] arrayDataMailDelete(){
		MessageEmail[] messagesDelete = null;
		int count = 0;
		
		for (int i = 0; i < messagesEmail.length; i++)
			for (int j = 0; j < idMessages.length; j++)
				if(messagesEmail[i].id == idMessages[j])
					messagesDelete[count++] = messagesEmail[i];
		
		return messagesDelete;
	}
	
	private void GmailDelete(Message[] messages) throws MessagingException{
		for(int i = 0; i < messages.length; i++)
			messages[i].setFlag(Flags.Flag.DELETED, true);
	}	
	
	private void DataEmailDelete(MessageEmail[] messages){
		//xoa trong csdl
	}

}
=======
package uet.dtui.gmail.apis;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;

import uet.dtui.gmail.model.MessageEmail;
import android.os.AsyncTask;

public class MailDeleteAsyncTask extends AsyncTask<Void, Void, Void>{
	Message[] messages;
	int[] idMessages;
	MessageEmail[] messagesEmail;
	public MailDeleteAsyncTask(Message[] messages, int[] idMessages, MessageEmail[] messagesEmail){
		this.idMessages = idMessages;
		this.messages = messages;
		this.messagesEmail = messagesEmail;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		try {
			GmailDelete(arrayGmailDelete());
			DataEmailDelete(arrayDataMailDelete());
			publishProgress(null);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private Message[] arrayGmailDelete(){
		Message[] messagesDelete = null;
		int count = 0;
		
		for (int i = 0; i < messages.length; i++)
			for (int j = 0; j < idMessages.length; j++)
				if(messages[i].getMessageNumber() == idMessages[j])
					messagesDelete[count++] = messages[i];
		
		return messagesDelete;
	}
	
	private MessageEmail[] arrayDataMailDelete(){
		MessageEmail[] messagesDelete = null;
		int count = 0;
		
		for (int i = 0; i < messagesEmail.length; i++)
			for (int j = 0; j < idMessages.length; j++)
				if(messagesEmail[i].id == idMessages[j])
					messagesDelete[count++] = messagesEmail[i];
		
		return messagesDelete;
	}
	
	private void GmailDelete(Message[] messages) throws MessagingException{
		for(int i = 0; i < messages.length; i++)
			messages[i].setFlag(Flags.Flag.DELETED, true);
	}	
	
	private void DataEmailDelete(MessageEmail[] messages){
		//xoa trong csdl
	}

}
>>>>>>> ea30ef1ee88189aa5ad4027eae562554b0d1a65b
