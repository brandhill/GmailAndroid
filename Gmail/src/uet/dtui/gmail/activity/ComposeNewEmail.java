package uet.dtui.gmail.activity;

import uet.dtui.gmail.R;
import uet.dtui.gmail.apis.AsyncSendMail;
import uet.dtui.gmail.components.AllerFont;
import uet.dtui.gmail.components.ChooseAccountPopupWindow;
import uet.dtui.gmail.components.MessageSerializable;
import uet.dtui.gmail.components.SaveToDraftPopupWindow;
import uet.dtui.gmail.components.Utils;
import uet.dtui.gmail.database.EmailDatabase;
import uet.dtui.gmail.model.Account;
import uet.dtui.gmail.model.MessageEmail;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
	private Button spinner;
	private EmailDatabase database;
	public String subj, bodyEmail, fromAcc, passAcc, toAcc ,filename;
	private String currentAcc;
	private Intent recvIntent;
	private MessageEmail recvEmail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.compose);
		
		database = new EmailDatabase(getApplicationContext());
		findViews();
		
		if (!checkReplyOrCompose()) {
			checkForward();
		}
		
		currentAcc = Utils.getCurrentAcc(getApplicationContext());
		setDefaultFromAccount();
	}

	private void checkForward() {
		recvIntent = getIntent();
		recvEmail = (MessageEmail) recvIntent.getSerializableExtra(Utils.FORWARD);
		if (recvEmail != null) {
			subject.setText("Fwd:" + recvEmail.subject);
			String forwardStr = "\n\n ---------- Forwarded message ---------- \n";
			forwardStr = forwardStr + "From: " + recvEmail.from + "\nDate: " + recvEmail.date 
					+ "\nSubject: " + recvEmail.subject + "\nTo: " + recvEmail.to;
			forwardStr += "\nOn " + recvEmail.date + " ," + recvEmail.from + " wrote :\n" + recvEmail.content;
			content.setText(forwardStr);
			content.requestFocus();
			content.setSelection(0);
		}
	}

	private boolean checkReplyOrCompose() {
		recvIntent = getIntent();
		recvEmail = (MessageEmail) recvIntent.getSerializableExtra(Utils.REPLY);
		if (recvEmail != null) {
			subject.setText("Re:" + recvEmail.subject);
			to.setText(recvEmail.from);
			content.setText("\n\n On " + recvEmail.date + " ," + recvEmail.from + " wrote :\n" + recvEmail.content  );
			content.requestFocus();
			content.setSelection(0);
			return true;
		}
		return false;
	}

	private void findViews() {
		btnBack = (Button) findViewById(R.id.btnBack);
		btnSend = (Button) findViewById(R.id.btnSend);
		to = (EditText) findViewById(R.id.tfTo);
		subject = (EditText) findViewById(R.id.tfSubject);
		content = (EditText) findViewById(R.id.tfContent);
		spinner = (Button) findViewById(R.id.spinner);
		
		btnBack.setOnClickListener(this);
		btnSend.setOnClickListener(this);
		spinner.setOnClickListener(this);
		
		spinner.setTypeface(AllerFont.get(getApplicationContext(), "fonts/Aller_Rg.ttf"));
		
	}

	@Override
	public void onBackPressed() {
		showPopupWindow();
	}

	public void onClick(View v) {
		if (v == btnBack) {
			showPopupWindow();
		} else if (v == btnSend){
			subj = this.subject.getText().toString();
			bodyEmail = this.content.getText().toString();
			toAcc = this.to.getText().toString();
			filename = "";
			Log.d("SEND EMAIL", subj + bodyEmail + fromAcc + toAcc + passAcc + filename);
			AsyncSendMail sender = new AsyncSendMail(this,subj, bodyEmail, fromAcc, passAcc, toAcc, filename);
			sender.execute(null);
		} else if (v == spinner) {
			Toast.makeText(getApplicationContext(), "OK MEN", 0).show();
			showPopupWindowChooseAccount();
		}
	}
	
	public void showPopupWindow() {
		Display display = getWindowManager().getDefaultDisplay();
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		new SaveToDraftPopupWindow(this, inflater.inflate(
				R.layout.popup_window_savedraft, null, false), display.getWidth(), display.getHeight());
	}
	
	public void showPopupWindowChooseAccount() {
		subj = this.subject.getText().toString();
		bodyEmail = this.content.getText().toString();
		toAcc = this.to.getText().toString();
		filename = "";
		setDefaultFromAccount();
		Display display = getWindowManager().getDefaultDisplay();
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		new ChooseAccountPopupWindow(this, inflater.inflate(
				R.layout.popup_choose_account, null, false), display.getWidth(), display.getHeight());
	}
	
	public void setFromAccount(String acc) {
		spinner.setText(acc);
		fromAcc = spinner.getText().toString();
		database.openDB();
		Account fromAccount = database.getAccountFromEmail(fromAcc);
		int index = fromAcc.indexOf("@gmail.com");
		fromAcc = fromAcc.substring(0, index);
		passAcc = fromAccount.password;
		database.closeDB();
	}
	
	public void setDefaultFromAccount() {
		setFromAccount(currentAcc);
	}
}
