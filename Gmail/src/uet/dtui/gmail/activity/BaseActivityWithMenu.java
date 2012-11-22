package uet.dtui.gmail.activity;

import net.simonvt.widget.MenuDrawerManager;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

public class BaseActivityWithMenu extends Activity implements OnClickListener{
	public static final String ACCOUNT_CATEGORY = "accounts";
	public static final String CATEGORIES_CATEGORY = "categoreis";
	public MenuDrawerManager menuDrawer;
	public String currentAccount = "kienvtqhi@gmail.com";
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
