package uet.dtui.gmail.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import uet.dtui.gmail.components.EmailArrayAdapter;
import uet.dtui.gmail.model.MessageEmail;
import uet.dtui.gmail.R;
import uet.dtui.gmail.components.quickaction.ActionItem;
import uet.dtui.gmail.components.quickaction.QuickAction;
import com.sun.mail.imap.IMAPFolder;

public class MailListActivity extends BaseActivityWithMenu {

	private ListView listview;
	private EmailArrayAdapter adapter;
	private static final int ID_UP    = 1;
	private static final int ID_DOWN   = 2;
	private static final int ID_SEARCH = 3;
	private static final int ID_INFO = 4;
	private static final int ID_ERASE  = 5;	
	private static final int ID_OK     = 6;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//============================================================================================//
		
		ActionItem nextItem 	= new ActionItem(ID_UP, "Read", getResources().getDrawable(R.drawable.menu_down_arrow));
		ActionItem prevItem 	= new ActionItem(ID_DOWN, "Unread", getResources().getDrawable(R.drawable.menu_up_arrow));
        ActionItem searchItem 	= new ActionItem(ID_SEARCH, "Reply", getResources().getDrawable(R.drawable.menu_search));
        ActionItem infoItem 	= new ActionItem(ID_INFO, "Forward", getResources().getDrawable(R.drawable.menu_info));
        ActionItem eraseItem 	= new ActionItem(ID_ERASE, "Delete", getResources().getDrawable(R.drawable.menu_eraser));
        ActionItem okItem 		= new ActionItem(ID_OK, "OK", getResources().getDrawable(R.drawable.menu_ok));
        
        prevItem.setSticky(true);
        nextItem.setSticky(true);
        
      //create QuickAction. Use QuickAction.VERTICAL or QuickAction.HORIZONTAL param to define layout 
        //orientation
		final QuickAction quickAction = new QuickAction(this, QuickAction.HORIZONTAL);
		
		//add action items into QuickAction
        quickAction.addActionItem(nextItem);
		quickAction.addActionItem(prevItem);
        quickAction.addActionItem(searchItem);
        quickAction.addActionItem(infoItem);
        quickAction.addActionItem(eraseItem);
        quickAction.addActionItem(okItem);
        
        //Set listener for action item clicked
		quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {			
			public void onItemClick(QuickAction source, int pos, int actionId) {				
				ActionItem actionItem = quickAction.getActionItem(pos);
                 
				//here we can filter which action item was clicked with pos or actionId parameter
				if (actionId == ID_SEARCH) {
					Toast.makeText(getApplicationContext(), "Let's do some search action", Toast.LENGTH_SHORT).show();
				} else if (actionId == ID_INFO) {
					Toast.makeText(getApplicationContext(), "I have no info this time", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), actionItem.getTitle() + " selected", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		//====================================================================================//
        
		// read mail and save to a list
		final List<MessageEmail> mail_list = new ArrayList<MessageEmail>();
		Properties props = System.getProperties();
        Session session = Session.getDefaultInstance(props, null);
        Store store;	
		try {
			store = session.getStore("imaps");
			store.connect("imap.gmail.com", "kienvtqhi",
	                "kienhien90");
			IMAPFolder inbox = (IMAPFolder) store.getFolder("INBOX");

			inbox.open(Folder.READ_WRITE);

			int newest = inbox.getMessageCount();
			System.out.println(newest);
			Message[] messages = inbox.getMessages();
			// FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), true);
			// messages = inbox.search(ft);

			for (int i = messages.length - 1; i > messages.length - 10; i--) {
				mail_list.add(new MessageEmail(messages[i].getMessageNumber(),
						messages[i].getSubject().toString(), messages[i]
								.getFrom().toString(), messages[i]
								.getRecipients(null).toString(), messages[i]
								.getSentDate(), messages[i].getContent()
								.toString(), ""));
			}
			inbox.close(false);
			store.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		mail_list.add(new MessageEmail(1, "subject subject subject subject subject subject subject subject", "from from from from from from from from from ", "to", new Date(1991, 11, 03), "content content content content content content content content content content content content content content content content content content content content content content content content content content content content content content content content content content content content content content content content content", ""));
		mail_list.add(new MessageEmail(1, "subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject subject ", "from from from from from from from from from ", "to", new Date(1991, 11, 03), "content content content content content content content content content content content content ", ""));
		mail_list.add(new MessageEmail(1, "subject subject subject subject subject subject subject subject", "from from from from from from from from from ", "to", new Date(2012, 11, 27), "content content content content content content content content content content content content ", ""));
		mail_list.add(new MessageEmail(1, "subject subject subject subject subject subject subject subject", "from from from from from from from from from ", "to", new Date(2012, 11, 06), "content content content content content content content content content content content content ", ""));
		mail_list.add(new MessageEmail(1, "subject subject subject subject subject subject subject subject", "from from from from from from from from from ", "to", new Date(1991, 11, 27), "content content content content content content content content content content content content ", ""));
		mail_list.add(new MessageEmail(1, "subject subject subject subject subject subject subject subject", "from from from from from from from from from ", "to", new Date(2012, 11, 03), "content content content content content content content content content content content content ", ""));
		Log.d("Size of data", mail_list.size() + "");
		
		listview = (ListView) findViewById(R.id.mail_list);
			
		adapter = new EmailArrayAdapter(this, R.layout.mail_row, mail_list);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> av, View v, int position,long id) {
				Log.d("Clicked",mail_list.get(position).content);
				Toast.makeText(getApplicationContext(), mail_list.get(position).content, 1).show();
			}
		});
		// set a Long item click listener
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View v,
					int a, long b) {
				quickAction.show(v);
				return false;
			}
		});
	}

}
