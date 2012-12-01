package uet.dtui.gmail.activity;

import uet.dtui.gmail.R;
import uet.dtui.gmail.components.AllerFont;
import uet.dtui.gmail.components.ClearableEditText;
import uet.dtui.gmail.components.Utils;
import uet.dtui.gmail.database.EmailDatabase;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements View.OnClickListener {
	private ClearableEditText tfEmailAddr;
	private EditText tfPassword;
	private Button btnLogin;
	private Button btnCreateAccount;
	private Button btnSignUp;
	private Button btnForgotPw;
	private EmailDatabase database;

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
		btnSignUp.setOnClickListener(this);
	}

	private void findView() {
		tfEmailAddr = (ClearableEditText) findViewById(R.id.tfEmailAddr);
		tfPassword = (EditText) findViewById(R.id.tfPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnForgotPw = (Button) findViewById(R.id.btnForgot);
		btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
		btnSignUp = (Button) findViewById(R.id.btnSignup);

		tfEmailAddr.setTypeface(AllerFont.get(getApplicationContext(),
				"fonts/Aller_Rg.ttf"));
		tfPassword.setTypeface(AllerFont.get(getApplicationContext(),
				"fonts/Aller_Rg.ttf"));

		if (tfEmailAddr.getText().toString().equals(""))
			btnLogin.setEnabled(false);
		// Add text change listener for edit text email address
		tfEmailAddr.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
				if (tfEmailAddr.getText().toString().equals("")
						&& !tfEmailAddr.getClearable()) {
					tfEmailAddr.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							0, 0);
					tfEmailAddr.setClearable(false);
				} else {
					tfEmailAddr.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.btn_clear, 0);
					tfEmailAddr.setCompoundDrawablePadding(10);
					btnLogin.setEnabled(true);
					tfEmailAddr.setClearable(true);
				}

			}
		});

		// Add change focus listener for edit text email address
		tfEmailAddr.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus && tfEmailAddr.getText().toString().length() != 0) {
					tfEmailAddr.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.btn_clear, 0);
				} else {
					tfEmailAddr.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							0, 0);
					String email = tfEmailAddr.getText().toString();
					if (email.indexOf("@gmail.com") == -1 && !email.equals(""))
						tfEmailAddr.setText(email + "@gmail.com");
				}
			}
		});

	}

	public void onClick(View v) {
		if (v == btnLogin) {
			if (tfPassword.getText().toString().equals(""))
				Toast.makeText(getApplicationContext(), "Password is empty", 0)
						.show();
			else {
				if (!Utils.checkConnect(tfEmailAddr.getText().toString(),
						tfPassword.getText().toString())) {
					saveAccount(tfEmailAddr.getText().toString(), tfPassword
							.getText().toString());
					Intent goToInbox = new Intent(this,
							BaseListEmailActivity.class);
					startActivity(goToInbox);
					this.finish();
				} else {
					Toast.makeText(getApplicationContext(),
							"Email address or password is invalid", 1).show();
				}
			}
		}
		/*
		 * else if (v == btnCreateAccount) { TextView tv =
		 * Utils.createTextView(getApplicationContext(),
		 * tfEmailAddr.getText().toString()); BitmapDrawable bd =
		 * (BitmapDrawable) Utils.convertViewtoDrawable(tv); bd.setBounds(0, 0,
		 * bd.getIntrinsicWidth(),bd.getIntrinsicHeight()); String email =
		 * tfEmailAddr.getText().toString(); final SpannableStringBuilder sb =
		 * new SpannableStringBuilder(); sb.append(email + ","); sb.setSpan(new
		 * ImageSpan(bd), sb.length()-(email.length() + 1), sb.length() -1
		 * ,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		 * tfEmailAddr.setMovementMethod(LinkMovementMethod.getInstance());
		 * tfEmailAddr.setText(sb); ; }
		 */
		else if (v == btnForgotPw) {
			Uri uriUrl = Uri.parse("https://www.google.com/accounts/recovery");
			Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
			startActivity(launchBrowser);
		} else if (v == btnSignUp) {
			Uri uriUrl = Uri.parse("https://accounts.google.com/SignUp");
			Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
			startActivity(launchBrowser);
		}
	}

	public void saveAccount(String user, String pass) {
		Utils.setCurrenAcc(user, getApplicationContext());
		Log.d("LOGIN SUCCESS", "SAVE ACCOUNT");
		database = new EmailDatabase(getApplicationContext());
		database.openDB();
		long idAcc = System.currentTimeMillis();
		database.addRowToTableAccount(idAcc, user, pass, user, Utils.TYPE_ACCOUNT_OWNER);
		database.closeDB();
	}
}
