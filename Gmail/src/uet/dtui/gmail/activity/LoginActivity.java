package uet.dtui.gmail.activity;

import uet.dtui.gmail.R;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity{
	private EditText tfEmailAddr;
	private EditText tfPassword;
	private Button btnLogin;
	private Button btnCreateAccount;
	private Button btnForgotPw;
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
		//btnLogin = (Button) findViewById(R.id.btnLogin);
		btnForgotPw = (Button) findViewById(R.id.btnForgot);
		btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
		
		if (tfEmailAddr.getText().toString().equals(""))
			btnLogin.setEnabled(false);
		tfEmailAddr.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (tfEmailAddr.getText().toString().equals(""))
					tfEmailAddr.setCompoundDrawables(null, null, null, null);
				else {
					Drawable btnClear = getResources().getDrawable(R.drawable.btn_clear);
					tfEmailAddr.setCompoundDrawables(null, null, btnClear, null);
				}
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
}
