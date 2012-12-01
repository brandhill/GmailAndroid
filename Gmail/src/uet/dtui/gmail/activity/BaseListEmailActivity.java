package uet.dtui.gmail.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import net.simonvt.widget.MenuDrawer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import uet.dtui.gmail.apis.MailDeleteAsyncTask;
import uet.dtui.gmail.apis.MailReaderAsyncTask;
import uet.dtui.gmail.components.EmailArrayAdapter;
import uet.dtui.gmail.components.Utils;
import uet.dtui.gmail.model.MessageEmail;
import uet.dtui.gmail.R;
import uet.dtui.gmail.components.quickaction.ActionItem;
import uet.dtui.gmail.components.quickaction.QuickAction;
import uet.dtui.gmail.components.quickaction.QuickAction.OnActionItemClickListener;
import uet.dtui.gmail.database.EmailDatabase;

import com.sun.mail.imap.IMAPFolder;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class BaseListEmailActivity extends BaseActivityWithMenu implements
		OnRefreshListener, OnActionItemClickListener {

	private PullToRefreshListView listview;
	private String titleActionBar;
	public EmailArrayAdapter adapter;
	private Button btnMenu;
	private Button btnCompose;
	private Button btnSearch;
	private Button btnSetting;
	private Button btnRefresh;
	private Button btnDelete;

	private View loadmore;
	public ProgressBar progressBar;
	private List<MessageEmail> mail_list = new ArrayList<MessageEmail>();

	private MailReaderAsyncTask asyncReadEmail;
	private EmailDatabase database;

	private QuickAction quickAction;

	private static final int ID_DELETE = 1;
	private static final int ID_REPLY = 2;
	private static final int ID_FORWARD = 3;
	public boolean loading = false;
	private int postionLongClicked = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		menuDrawer.setContentView(R.layout.layout_inbox);

		findViews();
		createQuickAction();

		LayoutInflater inflater = getLayoutInflater();
		loadmore = inflater.inflate(R.layout.loadmore_layout, null);
		listview.addFooterView(loadmore);
		progressBar = (ProgressBar) loadmore.findViewById(R.id.progressBar);
		loadmore.setOnClickListener(this);
		database = new EmailDatabase(getApplicationContext());

		Intent checkNewEmailService = new Intent(this,
				CheckNewEmailService.class);
		// startService(checkNewEmailService);

		// read mail and save to a list
		asyncReadEmail = new MailReaderAsyncTask(this, Utils.FOLDER_NAME_INBOX);
		asyncReadEmail.execute(null);

		Log.d("Size of data", mail_list.size() + "");
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

		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				quickAction.show(arg1);
				postionLongClicked = arg2;
				return false;
			}
		});
	}

	private void findViews() {
		listview = (PullToRefreshListView) findViewById(R.id.mail_list);
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
		listview.setOnRefreshListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == btnSetting) {
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

		if (v == loadmore) {
			loadMoreMessages();
		}
		if (v == btnSearch) {
			Intent searchIntent = new Intent(this, SearchActivity.class);
			startActivity(searchIntent);
		}
		super.onClick(v);
	}

	public void loadMoreMessages() {
		Toast.makeText(getApplicationContext(), "Give me some", 1).show();
		asyncReadEmail.execute(null);
	}

	public void getDataForList() {
		Log.d("GET DATA FOR LIST", "RUNNING");
		database = new EmailDatabase(getApplicationContext());
		database.openDB();
		mail_list = database.getEmaiWithAccountandFolder("inbox",
				Utils.getCurrentAcc(getApplicationContext()));
		database.closeDB();
		adapter = new EmailArrayAdapter(this, R.layout.mail_row, mail_list);
		listview.setAdapter(adapter);
	}

	public void onRefresh() {
		Toast.makeText(getApplicationContext(), "Give me some", 1).show();
	}

	public void createQuickAction() {
		ActionItem replyItem = new ActionItem(ID_REPLY, "Reply", getResources()
				.getDrawable(R.drawable.reply_button));
		ActionItem deleteItem = new ActionItem(ID_DELETE, "Delete",
				getResources().getDrawable(R.drawable.delete_button));
		ActionItem forwardItem = new ActionItem(ID_FORWARD, "Forward",
				getResources().getDrawable(R.drawable.socail_button));

		quickAction = new QuickAction(this, QuickAction.HORIZONTAL);
		quickAction.addActionItem(replyItem);
		quickAction.addActionItem(forwardItem);
		quickAction.addActionItem(deleteItem);
		quickAction.setOnActionItemClickListener(this);
	}

	public void onItemClick(QuickAction source, int pos, int actionId) {
		ActionItem actionItem = quickAction.getActionItem(pos);
		switch (actionId) {
		case ID_DELETE:
			MessageEmail email = mail_list.get(postionLongClicked);
			long id[] = { email.id };

			try {
				database = new EmailDatabase(getApplicationContext());
				database.openDB();
				long idAcc = database.getIDAccountFromEmail(Utils.getCurrentAcc(getApplicationContext()));
				long idFolderDelete = database.getIdFolderWithCurrentAccount(Utils.FOLDER_DELETE, idAcc);
				database.updateRowToTableMessage(email.id, idFolderDelete,
						email.subject, email.from, email.to, email.content,
						email.date, email.fileName, email.sourceFile,
						email.contentHtml);
				database.closeDB();
				mail_list.remove(email);
				adapter.notifyDataSetChanged();
				MailDeleteAsyncTask deleteAsync = new MailDeleteAsyncTask(this,
						Utils.FOLDER_NAME_INBOX, id);
				deleteAsync.execute(null);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Toast.makeText(getApplicationContext(),
					"Delete Message " + email.content, 0).show();
			break;
		case ID_FORWARD:
			Toast.makeText(getApplicationContext(), "Forward Message", 0)
					.show();
			break;
		case ID_REPLY:
			Toast.makeText(getApplicationContext(), "Reply Message", 0).show();
			break;
		}
	}
}
