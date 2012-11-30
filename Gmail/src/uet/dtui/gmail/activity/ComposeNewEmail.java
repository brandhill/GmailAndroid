package uet.dtui.gmail.activity;

import uet.dtui.gmail.R;
import uet.dtui.gmail.apis.AsyncSendMail;
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
import android.widget.EditText;
import android.widget.Toast;

public class ComposeNewEmail extends Activity implements OnClickListener{
	private Button btnBack;
	private Button btnSend;
	private EditText from;
	private EditText to;
	private EditText subject;
	private EditText content;
	
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
		from = (EditText) findViewById(R.id.tfFrom);
		to = (EditText) findViewById(R.id.tfTo);
		subject = (EditText) findViewById(R.id.tfSubject);
		content = (EditText) findViewById(R.id.tfContent);
		
		btnBack.setOnClickListener(this);
		btnSend.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		showPopupWindow();
	}

	public void onClick(View v) {
		if (v == btnBack) {
			showPopupWindow();
		} else {
			String subject, body, from, pass, to ,filename;
			subject = this.subject.getText().toString();
			body = this.content.getText().toString();
			from = this.from.getText().toString();
			to = this.to.getText().toString();
			pass = "kienhien90";
			filename = "";
			AsyncSendMail sender = new AsyncSendMail(this,subject, body, from, pass, to, filename);
			sender.execute(null);
		}	
	}
	
	public void showPopupWindow() {
		Display display = getWindowManager().getDefaultDisplay();
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		new SaveToDraftPopupWindow(this, inflater.inflate(
				R.layout.popup_window_savedraft, null, false), display.getWidth(), display.getHeight());
	}
}
