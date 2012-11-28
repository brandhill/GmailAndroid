package uet.dtui.gmail.components;

import uet.dtui.gmail.R;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

public class SaveToDraftPopupWindow extends PopupWindow implements OnClickListener{
	Activity activity;
	View contentView;
	private Button btnClose;
	private Button btnSave;
	
	public SaveToDraftPopupWindow(Activity activity, View contentView,
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
		btnClose = (Button) contentView.findViewById(R.id.btnClose);
		btnSave = (Button) contentView.findViewById(R.id.btnSave);
		
		btnClose.setOnClickListener(this);
		btnSave.setOnClickListener(this);
	}
	public void onClick(View v) {
		if (v == btnClose) {
			this.dismiss();
		} else if (v == btnSave){
			Toast.makeText(activity, "Save Account", 0).show();
			this.dismiss();
		}
		
	}

}
