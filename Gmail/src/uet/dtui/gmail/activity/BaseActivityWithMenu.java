package uet.dtui.gmail.activity;

import java.util.ArrayList;
import java.util.List;

import uet.dtui.gmail.R;
import uet.dtui.gmail.components.MenuAdapter;
import uet.dtui.gmail.components.MenuListView;
import uet.dtui.gmail.model.ItemMenuCategory;
import net.simonvt.widget.MenuDrawer;
import net.simonvt.widget.MenuDrawerManager;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class BaseActivityWithMenu extends Activity implements OnClickListener{
	
	public static final String ACCOUNT_CATEGORY = "accounts";
	public static final String CATEGORIES_CATEGORY = "categoreis";
	public MenuDrawerManager menuDrawer;
	public String currentAccount = "kienvtqhi@gmail.com";
	private ListView listView;
	private int mActivePosition = -1;
	private MenuAdapter mAdapter;
	private List<Object> mDatas;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		menuDrawer = new MenuDrawerManager(this, MenuDrawer.MENU_DRAG_CONTENT);
		
		menuDrawer.setContentView(R.layout.login_layout);
		
		listView = (ListView) findViewById(R.id.list_menu);
		
		//TestData
		mDatas = new ArrayList<Object>();
		mDatas.add(new ItemMenuCategory(ACCOUNT_CATEGORY,R.drawable.category_menu_accounts));
		mDatas.add(new ItemMenuCategory(CATEGORIES_CATEGORY, R.drawable.category_menu_categories));
		
		mAdapter = new MenuAdapter(mDatas, this);
		listView.setAdapter(mAdapter);
		menuDrawer.setMenuView(listView);
		//Set listener for onScroll
/*		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				menuDrawer.getMenuDrawer().invalidate();
				
			}
		});*/
		
		//Set item click listener for listview
		//listView.setOnClickListener((OnClickListener) mItemClickListener);
	}



	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			mActivePosition = position;
            menuDrawer.setActiveView(view, position);
            menuDrawer.closeMenu();
		}
	};

}
