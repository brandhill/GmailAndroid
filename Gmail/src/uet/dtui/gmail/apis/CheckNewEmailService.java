package uet.dtui.gmail.apis;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class CheckNewEmailService extends Service{

	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		GetMailTask backgroundTask = new GetMailTask(getApplicationContext());
		Log.d("Log","Service started");
		backgroundTask.execute(null);
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
