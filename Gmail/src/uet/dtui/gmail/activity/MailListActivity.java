package uet.dtui.gmail.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

import net.simonvt.widget.MenuDrawer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import uet.dtui.gmail.components.EmailArrayAdapter;
import uet.dtui.gmail.model.MessageEmail;
import uet.dtui.gmail.R;
import uet.dtui.gmail.components.quickaction.ActionItem;
import uet.dtui.gmail.components.quickaction.QuickAction;
import com.sun.mail.imap.IMAPFolder;

public class MailListActivity extends BaseActivityWithMenu {

	private ListView listview;
	private EmailArrayAdapter adapter;
	private Button btnMenu;
	private Button btnCompose;
	private Button btnSearch;
	private Button btnSetting;
	private Button btnRefresh;
	private Button btnDelete;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		menuDrawer.setContentView(R.layout.layout_inbox);

		// read mail and save to a list
		final List<MessageEmail> mail_list = new ArrayList<MessageEmail>();
		mail_list
				.add(new MessageEmail(
						1,
						"subject subject subject subject subject subject subject subject",
						"from from from from from from from from from ",
						"to",
						new Date(1991, 11, 03),
						"content content content content content content content content content content content content content content content content content content content content content content content content content content content content content content content content content content content content content content content content content",
						""));
		mail_list
				.add(new MessageEmail(
						1,
						"subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject ",
						"from from from from from from from from from ",
						"to",
						new Date(1991, 11, 03),
						"content content content content content content content content content content content content ",
						""));
		mail_list
				.add(new MessageEmail(
						1,
						"subject subject subject subject subject subject subject subject",
						"from from from from from from from from from ",
						"to",
						new Date(2012, 11, 27),
						"content content content content content content content content content content content content ",
						""));
		mail_list
				.add(new MessageEmail(
						1,
						"subject subject subject subject subject subject subject subject",
						"from from from from from from from from from ",
						"to",
						new Date(2012, 11, 06),
						"content content content content content content content content content content content content ",
						""));
//		mail_list
//				.add(new MessageEmail(
//						1,
//						"subject subject subject subject subject subject subject subject",
//						"from from from from from from from from from ",
//						"to",
//						new Date(1991, 11, 27),
//						"content content content content content content content content content content content content ",
//						""));
//		mail_list
//				.add(new MessageEmail(
//						1,
//						"subject subject subject subject subject subject subject subject",
//						"from from from from from from from from from ",
//						"to",
//						new Date(2012, 11, 03),
//						"content content content content content content content content content content content content ",
//						""));
		Log.d("Size of data", mail_list.size() + "");

		findViews();

		adapter = new EmailArrayAdapter(this, R.layout.mail_row, mail_list);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> av, View v, int position,
					long id) {
				Log.d("Clicked", mail_list.get(position).content);
				Toast.makeText(getApplicationContext(),
						mail_list.get(position).content, 1).show();
			}
		});
	}

	private void findViews() {
		listview = (ListView) findViewById(R.id.mail_list);
		btnCompose = (Button) findViewById(R.id.btnCompose);
		btnDelete = (Button) findViewById(R.id.btnDelete);
		btnMenu = (Button) findViewById(R.id.btnMenu);
		btnRefresh = (Button) findViewById(R.id.btnRefresh);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSetting = (Button) findViewById(R.id.btnSettings);
		
		btnCompose.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		btnMenu.setOnClickListener(this);
		btnRefresh.setOnClickListener(this);
		btnSearch.setOnClickListener(this);
	    btnSetting.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == btnSetting){
			Intent settingIntent = new Intent(this, SettingActivity.class);
			startActivity(settingIntent);
		}
		if (v == btnCompose) {
			Intent intentCompose = new Intent(this, ComposeNewEmail.class);
			startActivity(intentCompose);
		} else if (v == btnMenu) {
			final int drawerState = menuDrawer.getDrawerState();
			Log.d("Popup window", "IS NULL");
			if (drawerState == MenuDrawer.STATE_OPEN
					|| drawerState == MenuDrawer.STATE_OPENING) {
				menuDrawer.closeMenu();
				return;
			} else {
				menuDrawer.openMenu();
			}
		}
		super.onClick(v);
	}
	
	

}
