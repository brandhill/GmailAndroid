package uet.dtui.gmail.components;

import java.util.List;

import uet.dtui.gmail.R;
import uet.dtui.gmail.activity.ComposeNewEmail;
import uet.dtui.gmail.database.EmailDatabase;
import uet.dtui.gmail.model.Account;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

public class ChooseAccountPopupWindow extends PopupWindow implements OnClickListener,OnItemClickListener{
	ComposeNewEmail activity;
	View contentView;
	private Button btnClose;
	private EmailDatabase database;
	private AdapterChooseAccount adapter;
	private ListView listView;
	private List<Account> datas;
	
	public ChooseAccountPopupWindow(final ComposeNewEmail activity, View contentView,
			int width, int height) {
		super(contentView, width, height, true);
		this.activity = activity;
		this.contentView = contentView;
		database = new EmailDatabase(activity.getApplicationContext());
		
		findViews();
		setDataforList();
		
		listView.setOnItemClickListener(this);
		// show pop-up
		this.setAnimationStyle(R.style.AnimationPopup);
		this.setOutsideTouchable(true);
		database = new EmailDatabase(activity.getApplicationContext());
		this.setBackgroundDrawable(new BitmapDrawable());
		this.showAtLocation(activity.findViewById(R.id.main), Gravity.CENTER,
				0, 0);
	}

	private void findViews() {
		listView = (ListView) contentView.findViewById(R.id.listView);
		btnClose = (Button) contentView.findViewById(R.id.btnClose);
		
		btnClose.setOnClickListener(this);
	}
	
	public void setDataforList() {
		database.openDB();
		datas = database.getAccountWithOwner(Utils.TYPE_ACCOUNT_OWNER);
		Log.d("SIZE OF DATAS", datas.size() + "");
		adapter = new AdapterChooseAccount(activity, R.layout.menu_item_account, datas);
		listView.setAdapter(adapter);
		database.closeDB();
	}
	
	public void onClick(View v) {
		if (v == btnClose) {
			this.dismiss();
		}
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		activity.setFromAccount(datas.get(arg2).displayName);
		this.dismiss();
	}
}
