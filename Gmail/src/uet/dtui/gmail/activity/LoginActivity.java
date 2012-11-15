package uet.dtui.gmail.activity;

import uet.dtui.gmail.R;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity{
	private EditText tfEmailAddr;
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
		
	}
	private void findView() {
		tfEmailAddr = (EditText) findViewById(R.id.tfEmailAddr);
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
				if (tfEmailAddr.getText().toString().equals(""))
					tfEmailAddr.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				else {
					tfEmailAddr.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.btn_clear, 0);
					btnLogin.setEnabled(true);
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
					if (tfEmailAddr.getText().toString().length() != 0) {
						String username = tfEmailAddr.getText().toString();
						if (username.indexOf(GMAIL) == -1) {
							Toast.makeText(getApplicationContext(), "Khong co", 0).show();
							tfEmailAddr.setText(username + GMAIL);
						} else {
							Toast.makeText(getApplicationContext(), "Co", 0).show();
						}
					}
				}
				
			}
		});
		
	}
	
}
