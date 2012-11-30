package uet.dtui.gmail.apis;

import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;

import uet.dtui.gmail.R;
import uet.dtui.gmail.activity.BaseListEmailActivity;

import com.sun.mail.imap.IMAPFolder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class GetMailTask extends AsyncTask<String, Long, Void>{
	Message msgs[];
	Properties props;
	Session session;
	Store store;
	Context context;
	int newMsgCount = 0;
	int currentLastestUID;
	public GetMailTask(Context cont){
		context = cont;
	}
	
	@Override
	protected void onPreExecute() {
		
		// TODO Auto-generated method stub
		msgs=null;
		props = System.getProperties();
        session = Session.getDefaultInstance(props, null);
		super.onPreExecute();
	}
	
	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
			store = session.getStore("imaps");
			store.connect("imap.gmail.com", "kienvtqhi@gmail.com", "kienhien90");
		    IMAPFolder folder =  (IMAPFolder) store.getFolder("INBOX");
		    folder.open(Folder.READ_WRITE);
			
		    Log.d("Here","Here");
		    
		    int thelastestUID = 0;
			
			msgs = folder.getMessages();
			thelastestUID  = (int)  folder.getUID(msgs[msgs.length-1]);
			while (true){
				Thread.sleep(300000);
				msgs = folder.getMessages();
				// count how many msgs are new
				currentLastestUID = (int)  folder.getUID(msgs[msgs.length-1]);
				Log.d("Newest email UID",Integer.toString(currentLastestUID));
				// if there's any new msg

				if (currentLastestUID > thelastestUID)  {
					newMsgCount += currentLastestUID - thelastestUID;
					msgs = folder.getMessagesByUID(thelastestUID, currentLastestUID);
					for (int i=msgs.length-1;i>=0;i--) Log.d("Mail",msgs[i].getSubject());
					publishProgress(null);
					thelastestUID = currentLastestUID;
				}
					
			}// end while
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//===========================================================================================//
	@Override
	protected void onProgressUpdate(Long... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		// notify here
		long when = System.currentTimeMillis();
		int notificationID = 1;
		
		String title=Integer.toString(newMsgCount)+" new message";
		if (newMsgCount>1) title+="s";
		String content="";
		try {
			content += msgs[msgs.length-1].getSubject();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		Intent intent = new Intent(context, BaseListEmailActivity.class);
		PendingIntent launchIntent = PendingIntent.getActivity(context,0,intent,0);   
		
		String serName = Context.NOTIFICATION_SERVICE;
		NotificationManager notificationManager = (NotificationManager)context.getSystemService(serName) ;
		Notification newEmailNotification = new Notification(R.drawable.mail_notification, title, when);
		newEmailNotification.flags |= Notification.FLAG_AUTO_CANCEL;
		newEmailNotification.setLatestEventInfo(context, title, content, launchIntent);

		notificationManager.notify(notificationID, newEmailNotification);

	}

}
