package uet.dtui.gmail.activity;

import java.util.ArrayList;
import java.util.List;

import net.simonvt.widget.MenuDrawer;
import net.simonvt.widget.MenuDrawerManager;
import uet.dtui.gmail.R;
import uet.dtui.gmail.components.AddAccountPopupWindow;
import uet.dtui.gmail.components.MenuAdapter;
import uet.dtui.gmail.components.Utils;
import uet.dtui.gmail.database.EmailDatabase;
import uet.dtui.gmail.model.Account;
import uet.dtui.gmail.model.FolderEmail;
import uet.dtui.gmail.model.ItemMenuAccount;
import uet.dtui.gmail.model.ItemMenuCategory;
import uet.dtui.gmail.model.ItemMenuFolder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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

@SuppressLint("NewApi")
public class BaseActivityWithMenu extends Activity implements OnClickListener {

	public static final String ACCOUNT_CATEGORY = "accounts";
	public static final String CATEGORIES_CATEGORY = "categoreis";
	public MenuDrawerManager menuDrawer;
	public String currentAccount = "kienvtqhi@gmail.com";
	public String currentFolder = "InBox";
	private ListView listView;
	public int mActivePosition = -1;
	private MenuAdapter mAdapter;
	private List<Object> mDatas;
	private List<Account> listAcc;
	private List<FolderEmail> listFolder;
	private static final String TAG = "Base Activity";
	public int posArrow = -1;
	public EmailDatabase database;
	private int imageFolder[] = { R.drawable.icon_inbox_folder,
			R.drawable.icon_star_folder, R.drawable.icon_sent_folder,
			R.drawable.icon_draft_folder, R.drawable.icon_delete_folder };
	private int textFolder[] = { R.drawable.text_inbox_folder,
			R.drawable.text_important_folder, R.drawable.text_sent_folder,
			R.drawable.text_draft_folder, R.drawable.text_delete_folder };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		menuDrawer = new MenuDrawerManager(this, MenuDrawer.MENU_DRAG_WINDOW);
		menuDrawer.setMenuView(R.layout.menu_drawer);

		listView = (ListView) findViewById(R.id.list_menu);
		
		mDatas = new ArrayList<Object>();
		listAcc = new ArrayList<Account>();
		listFolder = new ArrayList<FolderEmail>();
		mAdapter = new MenuAdapter(mDatas, this);

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
				currentAccount = tmp.account.email;
			} else if (mAdapter.getItemViewType(position) == MenuAdapter.ITEM_FOLDER) {
				ItemMenuFolder fold = (ItemMenuFolder) mAdapter
						.getItem(position);
				currentFolder = fold.folder.name;
				mActivePosition = position;
				menuDrawer.setActiveView(view, position);
			}
			mAdapter.notifyDataSetInvalidated();
			menuDrawer.closeMenu();
		}
	};

	@Override
	public void onBackPressed() {
		closeMenu();
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

	public void closeMenu() {
		final int drawerState = menuDrawer.getDrawerState();
		Log.d("Popup window", "IS NULL");
		if (drawerState == MenuDrawer.STATE_OPEN
				|| drawerState == MenuDrawer.STATE_OPENING) {
			menuDrawer.closeMenu();
			return;
		}
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

	}
}
