package uet.dtui.gmail.components;

import uet.dtui.gmail.R;
import uet.dtui.gmail.activity.BaseActivityWithMenu;
import uet.dtui.gmail.database.EmailDatabase;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

@SuppressLint("NewApi")
public class AddAccountPopupWindow extends PopupWindow implements
		OnClickListener {
	BaseActivityWithMenu activity;
	View contentView;
	private Button btnClose;
	private Button btnSave;
	private EditText tfPass;
	private ClearableEditText tfEmail;
	private EmailDatabase database;

	public AddAccountPopupWindow(BaseActivityWithMenu activity, View contentView,
			int width, int height) {
		super(contentView, width, height, true);
		this.activity = activity;
		this.contentView = contentView;
		
		findViews();
		// show pop-up
		this.setAnimationStyle(R.style.AnimationPopup);
//		this.setFocusable(true);
		this.setOutsideTouchable(true);
		database = new EmailDatabase(activity.getApplicationContext());
		this.setBackgroundDrawable(new BitmapDrawable());
		this.showAtLocation(activity.findViewById(R.id.main), Gravity.CENTER,
				0, 0);
	}

	private void findViews() {
		btnClose = (Button) contentView.findViewById(R.id.btn_close_popup);
		btnClose.setOnClickListener(this);
		btnSave = (Button) contentView.findViewById(R.id.btn_save_acc);
		btnSave.setOnClickListener(this);
		tfPass = (EditText) contentView.findViewById(R.id.tf_password);
		tfEmail = (ClearableEditText) contentView.findViewById(R.id.tf_account);
		
		tfEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus && tfEmail.getText().toString().length() != 0) {
					tfEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.btn_clear, 0);
				} else {
					tfEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				}
			}
		});
	}

	public void onClick(View v) {
		if (v == btnClose) {
			this.dismiss();
		} else if (v == btnSave){
			saveAcc(tfEmail.getText().toString(), tfPass.getText().toString());
			this.dismiss();
		}
	}
	
	public void saveAcc(String user, String pass) {
		if (user.equals("") || pass.equals(""))
			Toast.makeText(activity.getApplicationContext(), "Username or password is invaild", 0).show();
		else {
			database.openDB();
			database.addRowToTableAccount(System.currentTimeMillis(), user, pass, user, Utils.TYPE_ACCOUNT_OWNER);
			database.closeDB();
			
			activity.getDataForMenu();
		}
	}

}
