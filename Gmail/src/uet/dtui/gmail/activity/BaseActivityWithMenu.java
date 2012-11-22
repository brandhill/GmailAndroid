package uet.dtui.gmail.activity;

import java.util.List;

import uet.dtui.gmail.R;
import uet.dtui.gmail.components.MenuAdapter;
import uet.dtui.gmail.components.MenuListView;
import net.simonvt.widget.MenuDrawer;
import net.simonvt.widget.MenuDrawerManager;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class BaseActivityWithMenu extends Activity implements OnClickListener{
	
	public static final String ACCOUNT_CATEGORY = "accounts";
	public static final String CATEGORIES_CATEGORY = "categoreis";
	public MenuDrawerManager menuDrawer;
	public String currentAccount = "kienvtqhi@gmail.com";
	private MenuListView listView;
	private int mActivePosition = -1;
	private MenuAdapter mAdapter;
	private List<Object> mDatas;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		menuDrawer = new MenuDrawerManager(this, MenuDrawer.MENU_DRAG_CONTENT);
		menuDrawer.setMenuView(R.layout.menu_drawer);
		listView = (MenuListView) findViewById(R.id.list_menu);
	}



	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
