package uet.dtui.gmail.activity;

import java.util.ArrayList;
import java.util.List;

import net.simonvt.widget.MenuDrawer;
import net.simonvt.widget.MenuDrawerManager;
import uet.dtui.gmail.R;
import uet.dtui.gmail.components.AddAccountPopupWindow;
import uet.dtui.gmail.components.MenuAdapter;
import uet.dtui.gmail.components.Utils;
import uet.dtui.gmail.components.quickaction.ActionItem;
import uet.dtui.gmail.components.quickaction.QuickAction;
import uet.dtui.gmail.components.quickaction.QuickAction.OnActionItemClickListener;
import uet.dtui.gmail.database.EmailDatabase;
import uet.dtui.gmail.model.Account;
import uet.dtui.gmail.model.FolderEmail;
import uet.dtui.gmail.model.ItemMenuAccount;
import uet.dtui.gmail.model.ItemMenuCategory;
import uet.dtui.gmail.model.ItemMenuFolder;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class BaseActivityWithMenu extends ListActivity implements OnClickListener {

	public static final String ACCOUNT_CATEGORY = "accounts";
	public static final String CATEGORIES_CATEGORY = "categoreis";
	public MenuDrawerManager menuDrawer;
	public String currentAccount = null;
	public String currentFolder = "InBox";
	private ListView listView;
	public int mActivePosition = -1;
	public MenuAdapter mAdapter;
	private List<Object> mDatas;
	private List<Account> listAcc;
	private List<FolderEmail> listFolder;
	public int posArrow = -1;
	private int posLongClicked = 1;
	public EmailDatabase database;
	private int imageFolder[] = { R.drawable.icon_inbox_folder,
			R.drawable.icon_star_folder, R.drawable.icon_sent_folder,
			R.drawable.icon_draft_folder, R.drawable.icon_delete_folder };
	private int textFolder[] = { R.drawable.text_inbox_folder,
			R.drawable.text_important_folder, R.drawable.text_sent_folder,
			R.drawable.text_draft_folder, R.drawable.text_delete_folder };
	
	private QuickAction quickAction;
	private final int ID_DELETE = 11;
	
	public String titleActionBar = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		menuDrawer = new MenuDrawerManager(this, MenuDrawer.MENU_DRAG_WINDOW);
		menuDrawer.setMenuView(R.layout.menu_drawer);

		listView = (ListView) findViewById(R.id.list_menu);
		createQuickAction();
		
		mDatas = new ArrayList<Object>();
		listAcc = new ArrayList<Account>();
		listFolder = new ArrayList<FolderEmail>();
		mAdapter = new MenuAdapter(mDatas, this);
		
		currentAccount = Utils.getCurrentAcc(this);
		getDataForMenu();
		
		// Set listener for onScroll
		listView.setOnScrollListener(new AbsListView.OnScrollListener() {

			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				menuDrawer.getMenuDrawer().invalidate();
			}
		});

		// Set item click listener for listview
		listView.setOnItemClickListener(mItemClickListener);
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (mAdapter.getItemViewType(position) == MenuAdapter.ITEM_ACCOUNT) {
					quickAction.show(arg1);
					posLongClicked = position;
				}
				return false;
			}
		});
	}

	private void createQuickAction() {
		ActionItem deleteItem = new ActionItem(ID_DELETE, "Delete",
				getResources().getDrawable(R.drawable.delete_button));
		quickAction = new QuickAction(this,QuickAction.HORIZONTAL);
		quickAction.addActionItem(deleteItem);
		quickAction.setOnActionItemClickListener(new OnActionItemClickListener() {
			
			public void onItemClick(QuickAction source, int pos, int actionId) {
				ActionItem actionItem = quickAction.getActionItem(pos);
				switch (actionId) {
					case ID_DELETE:
						database = new EmailDatabase(getApplicationContext());
						Account acc = (Account) mDatas.get(posLongClicked);
						database.openDB();
						database.closeDB();
						Toast.makeText(getApplicationContext(), "Delete account " + acc.displayName, 0).show();
						break;
				}
				
			}
		});
		
	}

	public void onClick(View v) {
		if (v.getId() == R.id.category_btn_add) {
			Display display = getWindowManager().getDefaultDisplay();
			LayoutInflater inflater = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			new AddAccountPopupWindow(this, inflater.inflate(
					R.layout.popup_window, null, false), display.getWidth(),
					display.getHeight());
		}
	}

	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// Item clicked is item account
			if (mAdapter.getItemViewType(position) == MenuAdapter.ITEM_ACCOUNT) {
				ItemMenuAccount tmp = (ItemMenuAccount) mAdapter
						.getItem(position);
				Utils.setCurrenAcc(tmp.account.email, getApplicationContext());
				currentAccount = tmp.account.email;
				mAdapter.notifyDataSetInvalidated();
				menuDrawer.closeMenu();
			} else if (mAdapter.getItemViewType(position) == MenuAdapter.ITEM_FOLDER) {
				ItemMenuFolder fold = (ItemMenuFolder) mAdapter
						.getItem(position);
				currentFolder = fold.folder.name;
				mActivePosition = position;
				menuDrawer.setActiveView(view, position);
				if (currentFolder.equals(Utils.FOLDER_INBOX)) {
					launchInbox();
				} else if (currentFolder.equals(Utils.FOLDER_DELETE)) {
					launchDelete();
				} else if (currentFolder.equals(Utils.FOLDER_DRAFT)) {
					launchDraft();
				} else if (currentFolder.equals(Utils.FOLDER_IMPORTANT)) {
					launchImportant();
				} else if (currentFolder.equals(Utils.FOLDER_SENT)) {
					launchSent();
				}
				mAdapter.notifyDataSetInvalidated();
			}
			
		}

		private void launchSent() {
			Intent sentFolder = new Intent(getApplicationContext(), SentFolderActivity.class);
			sentFolder.putExtra(Utils.TITLE, Utils.FOLDER_SENT);
			startActivity(sentFolder);
		}

		private void launchDraft() {
			Intent draftFolder = new Intent(getApplicationContext(), DraftFolderActivity.class);
			draftFolder.putExtra(Utils.TITLE, Utils.FOLDER_DRAFT);
			startActivity(draftFolder);
			
		}

		private void launchImportant() {
			Intent importantFolder = new Intent(getApplicationContext(), ImportantFolderActivity.class);
			importantFolder.putExtra(Utils.TITLE, Utils.FOLDER_IMPORTANT);
			startActivity(importantFolder);
		}

		private void launchDelete() {
			Intent deleteFolder = new Intent(getApplicationContext(), DeleteFolderActivity.class);
			deleteFolder.putExtra(Utils.TITLE, Utils.FOLDER_DELETE);
			startActivity(deleteFolder);
		}

		private void launchInbox() {
			Intent inboxFolder = new Intent(getApplicationContext(), InboxActivity.class);
			inboxFolder.putExtra(Utils.TITLE, Utils.FOLDER_INBOX);
			startActivity(inboxFolder);
		}
	};

	@Override
	public void onBackPressed() {
		if (!closeMenu())
			super.onBackPressed();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
			final int drawerState = menuDrawer.getDrawerState();
			if (drawerState == MenuDrawer.STATE_OPEN
					|| drawerState == MenuDrawer.STATE_OPENING) {
				menuDrawer.closeMenu();
			} else {
				menuDrawer.toggleMenu();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	public boolean closeMenu() {
		final int drawerState = menuDrawer.getDrawerState();
		Log.d("Popup window", "IS NULL");
		if (drawerState == MenuDrawer.STATE_OPEN
				|| drawerState == MenuDrawer.STATE_OPENING) {
			menuDrawer.closeMenu();
			return true;
		}
		return false;
	}

	public void setFirstActiveView() {
		Log.w("SET FIRST ACTIVE VIEW", "RUNNING");
		for (int i = 0; i < mDatas.size(); i++)
			if (mAdapter.getItemViewType(i) == MenuAdapter.ITEM_FOLDER) {
				Log.w("SET FIRST ACTIVE VIEW", "OK");
				menuDrawer.setActiveView(listView.getChildAt(i));
				menuDrawer.getMenuDrawer().invalidate();
				break;
			}
	}

	public void getDataForMenu() {
		mDatas.clear();
		listView.setAdapter(mAdapter);
		// Get account owner
		database = new EmailDatabase(getApplicationContext());
		database.openDB();
		
		mDatas.add(new ItemMenuCategory(ACCOUNT_CATEGORY,
				R.drawable.category_menu_accounts));

		listAcc = database.getAccountWithOwner(Utils.TYPE_ACCOUNT_OWNER);
		Account cur = new Account();
		;
		for (int i = 0; i < listAcc.size(); i++) {
			cur = listAcc.get(0);
			mDatas.add(new ItemMenuAccount(listAcc.get(i)));
		}

		mDatas.add(new ItemMenuCategory(CATEGORIES_CATEGORY,
				R.drawable.category_menu_categories));
		// Get folder
		listFolder = database.getAllFolderWithAcc(cur.id);
		Log.d("SIZE OF LIST FOLDER", listFolder.size() + "");
		for (int i = 0; i < listFolder.size(); i++) {
			mDatas.add(new ItemMenuFolder(imageFolder[i], textFolder[i],
					listFolder.get(i)));
		}
		mAdapter.notifyDataSetChanged();
		database.closeDB();
		
		savePosViewOfMenu();
	}

	private void savePosViewOfMenu() {
		SharedPreferences pref = getSharedPreferences(Utils.FILE_SHARE_PREFERENCES, MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor myEditor = pref.edit();
		myEditor.putInt(Utils.VIEW_DELETE, mDatas.size()-1);
		myEditor.putInt(Utils.VIEW_DRAFT, mDatas.size()-2);
		myEditor.putInt(Utils.VIEW_SENT, mDatas.size()-3);
		myEditor.putInt(Utils.VIEW_IMPORTANT, mDatas.size()-4);
		myEditor.putInt(Utils.VIEW_INBOX, mDatas.size()-5);
		myEditor.commit();
	}

	@Override
	protected void onPause() {
		if (currentAccount != null)
			Utils.setCurrenAcc(currentAccount, getApplicationContext());
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void onItemClick(QuickAction source, int pos, int actionId) {
		
		
	}
	
	
}
