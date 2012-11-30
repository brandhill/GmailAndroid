package uet.dtui.gmail.components;

import uet.dtui.gmail.R;
import uet.dtui.gmail.activity.BaseActivityWithMenu;
import uet.dtui.gmail.activity.SettingActivity;
import uet.dtui.gmail.database.EmailDatabase;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

@SuppressLint({ "NewApi", "WorldReadableFiles" })
public class NameSettingPopupWindow extends PopupWindow implements
		OnClickListener {
	SettingActivity activity;
	View contentView;
	private Button btnCancel;
	private Button btnSave;
	private ClearableEditText edt_name;

	public NameSettingPopupWindow(SettingActivity activity, View contentView,
			int width, int height) {
		super(contentView, width, height, true);
		this.activity = activity;
		this.contentView = contentView;
		
		findViews();
		// show pop-up
		this.setAnimationStyle(R.style.AnimationPopup);
//		this.setFocusable(true);
		this.setOutsideTouchable(true);
		
		this.setBackgroundDrawable(new BitmapDrawable());
		this.showAtLocation(activity.findViewById(R.id.main), Gravity.CENTER,
				0, 0);
	}

	private void findViews() {
		btnCancel = (Button) contentView.findViewById(R.id.setting_name_btncancel);
		btnCancel.setOnClickListener(this);
		btnSave = (Button) contentView.findViewById(R.id.setting_name_btnsave);
		btnSave.setOnClickListener(this);
		edt_name =  (ClearableEditText) contentView.findViewById(R.id.setting_name_value);
		
		edt_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus && edt_name.getText().toString().length() != 0) {
					edt_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.btn_clear, 0);
				} else {
					edt_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				}
			}
		});
	}

	public void onClick(View v) {
		if (v == btnCancel) {
			this.dismiss();
		} else if (v == btnSave){
//			save to SharedPreferences
			SharedPreferences pref =
	                activity.getSharedPreferences(SettingActivity.PREFERENCES_FILE, Context.MODE_WORLD_READABLE);
            SharedPreferences.Editor myEditor = pref.edit();
            myEditor.putString("name", edt_name.getText().toString());
            myEditor.commit();
			this.dismiss();
		}
	}

}
