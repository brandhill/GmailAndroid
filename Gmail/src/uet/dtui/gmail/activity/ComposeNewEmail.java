package uet.dtui.gmail.activity;

import uet.dtui.gmail.R;
import uet.dtui.gmail.components.SaveToDraftPopupWindow;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class ComposeNewEmail extends Activity implements OnClickListener{
	private Button btnBack;
	private Button btnSend;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.compose);
		
		findViews();
	}

	private void findViews() {
		btnBack = (Button) findViewById(R.id.btnBack);
		btnSend = (Button) findViewById(R.id.btnSend);
		
		btnBack.setOnClickListener(this);
		btnSend.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		Display display = getWindowManager().getDefaultDisplay();
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		new SaveToDraftPopupWindow(this, inflater.inflate(
				R.layout.popup_window_savedraft, null, false), display.getWidth(), display.getHeight());
	}

	public void onClick(View v) {
		if (v == btnBack) {
			this.finish();
		} else {
			Toast.makeText(this, "Sending ...", 0).show();
		}
		
	}
}
