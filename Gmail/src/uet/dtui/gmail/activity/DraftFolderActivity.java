package uet.dtui.gmail.activity;

import javax.mail.Message;
import javax.mail.MessagingException;

import uet.dtui.gmail.R;
import uet.dtui.gmail.apis.MailDeleteAsyncTask;
import uet.dtui.gmail.components.EmailArrayAdapter;
import uet.dtui.gmail.components.Utils;
import uet.dtui.gmail.components.quickaction.ActionItem;
import uet.dtui.gmail.components.quickaction.QuickAction;
import uet.dtui.gmail.database.EmailDatabase;
import uet.dtui.gmail.model.MessageEmail;
import android.app.LauncherActivity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

public class DraftFolderActivity extends BaseListEmailActivity {
	public int ID_DELETE = 21;
	@Override
	public void getDataForList() {
		Log.d("Inbox", "Get data");
		Log.d("GET DATA FOR LIST", "RUNNING");
		database = new EmailDatabase(getApplicationContext());

		database.openDB();
		mail_list.clear();
		mail_list = database.getEmaiWithAccountandFolder(Utils.FOLDER_DRAFT,
				Utils.getCurrentAcc(getApplicationContext()));
		database.closeDB();
		adapter = new EmailArrayAdapter(this, R.layout.mail_row, mail_list);
		listview.setAdapter(adapter);
	}

	@Override
	public void createQuickAction() {
		ActionItem deleteItem = new ActionItem(ID_DELETE, "Delete",
				getResources().getDrawable(R.drawable.delete_button));
		quickAction = new QuickAction(this, QuickAction.HORIZONTAL);
		quickAction.addActionItem(deleteItem);
		quickAction.setOnActionItemClickListener(this);
	}

	@Override
	public void onItemClick(QuickAction source, int pos, int actionId) {
		ActionItem actionItem = quickAction.getActionItem(pos);
		MessageEmail email = mail_list.get(postionLongClicked);
		if (actionId == ID_DELETE) {
			long id[] = { email.id };
			
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
			Toast.makeText(getApplicationContext(),
					"Delete Message " + email.content, 0).show();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Intent compose = new Intent(getApplicationContext(), ComposeNewEmail.class);
		compose.putExtra(Utils.REPLY, mail_list.get(position));
		startActivity(compose);
	}
}
