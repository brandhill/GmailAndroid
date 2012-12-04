package uet.dtui.gmail.activity;

import uet.dtui.gmail.R;
import uet.dtui.gmail.components.EmailArrayAdapter;
import uet.dtui.gmail.components.Utils;
import uet.dtui.gmail.database.EmailDatabase;
import android.util.Log;

public class DeleteFolderActivity extends BaseListEmailActivity{
	@Override
	public void loadMoreMessages() {
		// TODO Auto-generated method stub
		super.loadMoreMessages();
	}

	@Override
	public void getDataForList() {
		Log.d("Inbox", "Get data");
		Log.d("GET DATA FOR LIST", "RUNNING");
		database = new EmailDatabase(getApplicationContext());
		
			database.openDB();
			mail_list.clear();
			mail_list = database.getEmaiWithAccountandFolder(Utils.FOLDER_DELETE,
					Utils.getCurrentAcc(getApplicationContext()));
			database.closeDB();
		adapter = new EmailArrayAdapter(this, R.layout.mail_row, mail_list);
		listview.setAdapter(adapter);
	}

	@Override
	public void createQuickAction() {
		// TODO Auto-generated method stub
		super.createQuickAction();
	}
}
