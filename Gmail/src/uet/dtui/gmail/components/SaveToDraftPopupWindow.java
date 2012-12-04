package uet.dtui.gmail.components;

import uet.dtui.gmail.R;
import uet.dtui.gmail.activity.ComposeNewEmail;
import uet.dtui.gmail.database.EmailDatabase;
import uet.dtui.gmail.model.MessageEmail;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.test.UiThreadTest;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

public class SaveToDraftPopupWindow extends PopupWindow implements OnClickListener{
	ComposeNewEmail activity;
	View contentView;
	private Button btnClose;
	private Button btnSave;
	private Button btnDiscard;
	private EmailDatabase database;
	
	public SaveToDraftPopupWindow(ComposeNewEmail activity, View contentView,
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
		btnDiscard = (Button) contentView.findViewById(R.id.btnDiscard);
		btnClose.setOnClickListener(this);
		btnSave.setOnClickListener(this);
		btnDiscard.setOnClickListener(this);
	}
	public void onClick(View v) {
		if (v == btnClose) {
			this.dismiss();
		} else if (v == btnSave){
			Toast.makeText(activity, "Save Message to Draft Folder", 0).show();
			this.dismiss();
			saveMessageToDraft();
			activity.finish();
		} else if (v == btnDiscard) {
			this.dismiss();
			activity.finish();
		}
		
	}
	private void saveMessageToDraft() {
		database = new EmailDatabase(activity.getApplicationContext());
		database.openDB();
		MessageEmail mess = new MessageEmail();
		mess.id = System.currentTimeMillis();
		long idAcc = database.getIDAccountFromEmail(Utils.getCurrentAcc(activity.getApplicationContext()));
		mess.idFolder = database.getIdFolderWithNameAndAcc(idAcc, Utils.FOLDER_DRAFT);
		Log.d("ID FOLDER", mess.idFolder + "");
		mess.from = activity.fromAcc;
		mess.to = activity.toAcc;
		mess.content = activity.bodyEmail;
		mess.contentHtml = "";
		mess.date = "";
		mess.subject = activity.subj;
		database.addMessage(mess);
		database.closeDB();
	}
}
