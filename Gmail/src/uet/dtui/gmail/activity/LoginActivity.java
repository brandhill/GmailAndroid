package uet.dtui.gmail.activity;

import uet.dtui.gmail.R;
import uet.dtui.gmail.components.ClearableEditText;
import uet.dtui.gmail.components.Utils;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements View.OnClickListener{
	private ClearableEditText tfEmailAddr;
	private EditText tfPassword;
	private Button btnLogin;
	private Button btnCreateAccount;
	private Button btnForgotPw;
	private static final String GMAIL = "@gmail.com";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_layout);
		
		findView();
		btnLogin.setOnClickListener(this);
		btnCreateAccount.setOnClickListener(this);
		btnForgotPw.setOnClickListener(this);
	}
	private void findView() {
		tfEmailAddr =  (ClearableEditText) findViewById(R.id.tfEmailAddr);
		tfPassword = (EditText) findViewById(R.id.tfPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnForgotPw = (Button) findViewById(R.id.btnForgot);
		btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
		
		if (tfEmailAddr.getText().toString().equals(""))
			btnLogin.setEnabled(false);
		//Add text change listener for edit text email address
		tfEmailAddr.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			public void afterTextChanged(Editable s) {
				if (tfEmailAddr.getText().toString().equals("") && !tfEmailAddr.getClearable()) {
					tfEmailAddr.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
					tfEmailAddr.setClearable(false);
				}
				else {
					tfEmailAddr.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.btn_clear, 0);
					tfEmailAddr.setCompoundDrawablePadding(10);
					btnLogin.setEnabled(true);
					tfEmailAddr.setClearable(true);
				}

				
			}
		});
		
		//Add change focus listener for edit text email address
		tfEmailAddr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus && tfEmailAddr.getText().toString().length() != 0) {
					tfEmailAddr.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.btn_clear, 0);
				} else {
					tfEmailAddr.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				}
			}
		});
		
	}
	public void onClick(View v) {
		if (v == btnLogin) {
			if (tfPassword.getText().toString().equals(""))
				Toast.makeText(getApplicationContext(), "Password is empty", 0).show();
			else {
				Intent goToInbox = new Intent(this, MailListActivity.class);
				startActivity(goToInbox);
				this.finish();
			}
		} else if (v == btnCreateAccount) {
			TextView tv = Utils.createTextView(getApplicationContext(), tfEmailAddr.getText().toString());
			BitmapDrawable bd = (BitmapDrawable) Utils.convertViewtoDrawable(tv);
			bd.setBounds(0, 0, bd.getIntrinsicWidth(),bd.getIntrinsicHeight());
			String email = tfEmailAddr.getText().toString();
			final SpannableStringBuilder sb = new SpannableStringBuilder();
			sb.append(email + ",");
			sb.setSpan(new ImageSpan(bd), sb.length()-(email.length() + 1), sb.length() -1 ,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			tfEmailAddr.setMovementMethod(LinkMovementMethod.getInstance());
			tfEmailAddr.setText(sb);
;		} else if (v == btnForgotPw) {
			Toast.makeText(getApplicationContext(), "Reset Password Account", 0).show();
		}
	}
	
}
