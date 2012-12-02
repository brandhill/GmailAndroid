package uet.dtui.gmail.activity;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import net.simonvt.widget.MenuDrawer;
import uet.dtui.gmail.R;
import uet.dtui.gmail.apis.MailDeleteAsyncTask;
import uet.dtui.gmail.apis.MailReaderAsyncTask;
import uet.dtui.gmail.components.EmailArrayAdapter;
import uet.dtui.gmail.components.ListSerializable;
import uet.dtui.gmail.components.Utils;
import uet.dtui.gmail.components.quickaction.ActionItem;
import uet.dtui.gmail.components.quickaction.QuickAction;
import uet.dtui.gmail.components.quickaction.QuickAction.OnActionItemClickListener;
import uet.dtui.gmail.database.EmailDatabase;
import uet.dtui.gmail.model.MessageEmail;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class BaseListEmailActivity extends BaseActivityWithMenu implements
		OnRefreshListener, OnActionItemClickListener {

	private PullToRefreshListView listview;
	public EmailArrayAdapter adapter;
	private Button btnMenu;
	private Button btnCompose;
	private Button btnSearch;
	private Button btnSetting;
	private Button btnRefresh;
	private Button btnDelete;
	private ImageView title;

	private View loadmore;
	private Button btnLoadMore;
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
	private Intent recvIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		menuDrawer.setContentView(R.layout.layout_inbox);


		findViews();
		setActiveViewMenu();
		createQuickAction();

		database = new EmailDatabase(getApplicationContext());

		Intent checkNewEmailService = new Intent(this,
				CheckNewEmailService.class);
		// startService(checkNewEmailService);
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> av, View v, int position,
					long id) {
				Log.d("Clicked", mail_list.get(position).content);
				Toast.makeText(getApplicationContext(),
						mail_list.get(position).content, 1).show();
				Intent readMailIntent = new Intent(getApplicationContext(),ReadMailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("currentpos", position);
				bundle.putSerializable("maillist", new ListSerializable(mail_list));
				readMailIntent.putExtras(bundle);
				startActivity(readMailIntent);
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

	private void setActiveViewMenu() {
		recvIntent = getIntent();
		String titleActionBar = recvIntent.getStringExtra(Utils.TITLE);
		if (titleActionBar != null) {
			if (titleActionBar.equals(Utils.FOLDER_INBOX)) {
				getDataForList();
				listview.addFooterView(loadmore);
				title.setImageResource(R.drawable.title_inbox);
				this.currentFolder = Utils.FOLDER_INBOX;
				this.mAdapter.notifyDataSetChanged();
			} else if (titleActionBar.equals(Utils.FOLDER_DELETE)) {
				title.setImageResource(R.drawable.title_delete);
				this.currentFolder = Utils.FOLDER_DELETE;
				this.mAdapter.notifyDataSetChanged();
			} else if (titleActionBar.equals(Utils.FOLDER_DRAFT)) {
				title.setImageResource(R.drawable.title_draft);
				this.currentFolder = Utils.FOLDER_DRAFT;
				this.mAdapter.notifyDataSetChanged();
			} else if (titleActionBar.equals(Utils.FOLDER_IMPORTANT)) {
				title.setImageResource(R.drawable.title_important);
				this.currentFolder = Utils.FOLDER_IMPORTANT;
				this.mAdapter.notifyDataSetChanged();
			}else if (titleActionBar.equals(Utils.FOLDER_SENT)) {
				title.setImageResource(R.drawable.title_sent);
				this.currentFolder = Utils.FOLDER_SENT;
				this.mAdapter.notifyDataSetChanged();
			}
		}
		
			
	}

	private void findViews() {
		listview = (PullToRefreshListView) findViewById(R.id.mail_list);
		btnCompose = (Button) findViewById(R.id.btnCompose);
		btnDelete = (Button) findViewById(R.id.btnDelete);
		btnMenu = (Button) findViewById(R.id.btnMenu);
		btnRefresh = (Button) findViewById(R.id.btnRefresh);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSetting = (Button) findViewById(R.id.btnSettings);
		title = (ImageView) findViewById(R.id.imageView1);
		
		LayoutInflater inflater = getLayoutInflater();
		loadmore = inflater.inflate(R.layout.loadmore_layout, null);

		progressBar = (ProgressBar) loadmore.findViewById(R.id.progressBar);
		loadmore.setOnClickListener(this);
		btnLoadMore = (Button) loadmore.findViewById(R.id.btn_load_more);
		btnLoadMore.setOnClickListener(this);

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

		if (v == btnLoadMore) {
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
		MailReaderAsyncTask asyncReadEmail = new MailReaderAsyncTask(this, Utils.FOLDER_NAME_INBOX);
		asyncReadEmail.execute(null);
	}

	public void getDataForList() {
		Log.d("GET DATA FOR LIST", "RUNNING");
		database = new EmailDatabase(getApplicationContext());
		
			database.openDB();
			mail_list.clear();
			mail_list = database.getEmaiWithAccountandFolder(Utils.FOLDER_INBOX,
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
		MessageEmail email = mail_list.get(postionLongClicked);
		switch (actionId) {
		case ID_DELETE:
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
			launchComposeToForward(email);
			Toast.makeText(getApplicationContext(), "Forward Message", 0)
					.show();
			break;
		case ID_REPLY:
			launchCompose(email);
			Toast.makeText(getApplicationContext(), "Reply Message", 0).show();
			break;
		}
	}

	private void launchComposeToForward(MessageEmail email) {
		Intent compose = new Intent(getApplicationContext(), ComposeNewEmail.class);
		compose.putExtra(Utils.FORWARD, email);
		startActivity(compose);
	}

	private void launchCompose(MessageEmail se) {
		Intent compose = new Intent(getApplicationContext(), ComposeNewEmail.class);
		compose.putExtra(Utils.REPLY, se);
		startActivity(compose);
	}
}
