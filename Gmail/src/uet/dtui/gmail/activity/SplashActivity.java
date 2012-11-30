package uet.dtui.gmail.activity;

import uet.dtui.gmail.R;
import uet.dtui.gmail.components.Utils;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;

public class SplashActivity extends Activity {
	private String currentAccount = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_screen);
		currentAccount = Utils.getCurrentAcc(getApplicationContext());
	}

	@Override
	protected void onStart() {
		super.onStart();
		Thread delay = new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (currentAccount.equals("None")) {
					Thread launch = new Thread(new Runnable() {
						public void run() {
							Intent main = new Intent(SplashActivity.this,
									LoginActivity.class);
							startActivity(main);
						}
					}); // runnable
					launch.start();
				} else {
					Thread launch = new Thread(new Runnable() {
						public void run() {
							Intent main = new Intent(SplashActivity.this,
									BaseListEmailActivity.class);
							startActivity(main);
						}
					}); // runnable
					launch.start();
				}
				finish();
			};
		}); // End of Runnable
		delay.start();
	}

}
