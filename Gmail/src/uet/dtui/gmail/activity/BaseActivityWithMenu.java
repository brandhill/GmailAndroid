package uet.dtui.gmail.activity;

import java.util.ArrayList;
import java.util.List;

import net.simonvt.widget.MenuDrawer;
import net.simonvt.widget.MenuDrawerManager;
import uet.dtui.gmail.R;
import uet.dtui.gmail.components.MenuAdapter;
import uet.dtui.gmail.model.Account;
import uet.dtui.gmail.model.FolderEmail;
import uet.dtui.gmail.model.ItemMenuAccount;
import uet.dtui.gmail.model.ItemMenuCategory;
import uet.dtui.gmail.model.ItemMenuFolder;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

public class BaseActivityWithMenu extends Activity implements OnClickListener{
	
	public static final String ACCOUNT_CATEGORY = "accounts";
	public static final String CATEGORIES_CATEGORY = "categoreis";
	public MenuDrawerManager menuDrawer;
	public String currentAccount = "kienvtqhi@gmail.com";
	public String currentFolder = "InBox";
	private ListView listView;
	public int mActivePosition = 1;
	private MenuAdapter mAdapter;
	private List<Object> mDatas;
	private static final String TAG = "Base Activity";
	private Boolean dataChanged = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		menuDrawer = new MenuDrawerManager(this, MenuDrawer.MENU_DRAG_WINDOW);
		menuDrawer.setContentView(R.layout.activity_main);
		menuDrawer.setMenuView(R.layout.menu_drawer);
		
		listView = (ListView) findViewById(R.id.list_menu);
		
		//TestData
		mDatas = new ArrayList<Object>();
		mDatas.add(new ItemMenuCategory(ACCOUNT_CATEGORY,R.drawable.category_menu_accounts));
		Account acc = new Account("kienvtqhi@gmail.com", "");
		mDatas.add(new ItemMenuAccount(acc));
		Account acc2 = new Account("john@gmail.com", "");
		FolderEmail folder = new FolderEmail("InBox", 123);
		mDatas.add(new ItemMenuAccount(acc2));
		mDatas.add(new ItemMenuCategory(CATEGORIES_CATEGORY, R.drawable.category_menu_categories));
		mDatas.add(new ItemMenuFolder(R.drawable.icon_inbox_folder, R.drawable.text_inbox_folder,folder));
		FolderEmail folder2 = new FolderEmail("InBox2", 123);
		mDatas.add(new ItemMenuFolder(R.drawable.icon_star_folder, R.drawable.text_important_folder,folder2));
		mDatas.add(new ItemMenuFolder(R.drawable.icon_sent_folder, R.drawable.text_sent_folder,folder2));
		mDatas.add(new ItemMenuFolder(R.drawable.icon_draft_folder, R.drawable.text_draft_folder,folder2));
		mDatas.add(new ItemMenuFolder(R.drawable.icon_delete_folder, R.drawable.text_delete_folder,folder2));
		
		Log.d(TAG, mDatas.size() + "");
		mAdapter = new MenuAdapter(mDatas, this);
		listView.setAdapter(mAdapter);
		//Set listener for onScroll
		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
			}
			
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				menuDrawer.getMenuDrawer().invalidate();
			}
		});
		
		//Set item click listener for listview
		listView.setOnItemClickListener(mItemClickListener);
	}



	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (mAdapter.getItemViewType(position) == MenuAdapter.ITEM_ACCOUNT) {
				ItemMenuAccount tmp = (ItemMenuAccount) mAdapter.getItem(position);
				currentAccount = tmp.account.email;
				dataChanged = true;
			} else if (mAdapter.getItemViewType(position) == MenuAdapter.ITEM_FOLDER)  {
				ItemMenuFolder fold = (ItemMenuFolder) mAdapter.getItem(position);
				currentFolder = fold.folder.name;
				mActivePosition = position;
				dataChanged = true;
	            menuDrawer.setActiveView(view, position);
			}
			mAdapter.notifyDataSetChanged();
            menuDrawer.closeMenu();
		}
	};
	
	@Override
    public void onBackPressed() {
        final int drawerState = menuDrawer.getDrawerState();
        if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
        	menuDrawer.closeMenu();
            return;
        }
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
}
