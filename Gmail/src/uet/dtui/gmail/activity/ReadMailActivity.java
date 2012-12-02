package uet.dtui.gmail.activity;

import java.util.ArrayList;

import javax.mail.MessagingException;

import uet.dtui.gmail.R;
import uet.dtui.gmail.apis.MailDeleteAsyncTask;
import uet.dtui.gmail.components.AllerFont;
import uet.dtui.gmail.components.ListSerializable;
import uet.dtui.gmail.components.ReadMailFragmentAdapter;
import uet.dtui.gmail.components.Utils;
import uet.dtui.gmail.database.EmailDatabase;
import uet.dtui.gmail.model.MessageEmail;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ReadMailActivity extends FragmentActivity implements OnClickListener{
	ArrayList<MessageEmail> mail_list; 
	private TextView indicator;
	private Button delete;
	private Button reply;
	private Button forward;
	private Button next;
	private Button prev;
	private int currentPos = 1;
	ViewPager mPager;
	ReadMailFragmentAdapter mAdapter;
	private EmailDatabase database;
	
	@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.read_mail_layout);
	        
	        Intent intentRecv = getIntent();
	        ListSerializable serial = (ListSerializable) intentRecv.getSerializableExtra("maillist"); 
	        currentPos = intentRecv.getIntExtra("currentpos", 1);
	        
	        mail_list = serial.getList();
	        mail_list.add(new MessageEmail());
	        Log.d("Size",Integer.toString(mail_list.size()));
	        
	        mAdapter = new ReadMailFragmentAdapter(mail_list, getSupportFragmentManager());
	        mPager = (ViewPager)findViewById(R.id.pager);
	        
	        indicator = (TextView) findViewById(R.id.indicator);
	        delete = (Button) findViewById(R.id.btnDelete);
	        reply = (Button) findViewById(R.id.btnReply);
	        forward = (Button) findViewById(R.id.btnForward);
	        next = (Button) findViewById(R.id.btnNext);
	        prev = (Button) findViewById(R.id.btnPrv);
	        indicator.setTypeface(AllerFont.get(getApplicationContext(), "fonts/Aller_Rg.ttf"));
	        
	        delete.setOnClickListener(this);
	        reply.setOnClickListener(this);
	        forward.setOnClickListener(this);
	        next.setOnClickListener(this);
	        prev.setOnClickListener(this);
	        mPager.setAdapter(mAdapter);
	        mPager.setCurrentItem(currentPos);
	        indicator.setText(currentPos + 1 + " of " + mail_list.size() );
	        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
				
				public void onPageSelected(int arg0) {
					
				}
				
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					indicator.setText(arg0 + 1 + " of " + mail_list.size() );
					currentPos = arg0;
					
				}
				
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub
					
				}
			});
	    }

	public void onClick(View v) {
		if (v == next) {
			if (currentPos == mail_list.size() -1)
				return;
			else {
				mPager.setCurrentItem(currentPos+1);
				currentPos = currentPos +1;
			}
				
		} else if (v == prev) {
			if (currentPos == 0)
				return;
			else {
				mPager.setCurrentItem(currentPos - 1);
				currentPos = currentPos -1;
			}
		} else if (v == reply) {
			launchCompose(mail_list.get(currentPos));
			Toast.makeText(getApplicationContext(), "Reply Message", 0).show();
		} else if (v == forward) {
			launchComposeToForward(mail_list.get(currentPos));
			Toast.makeText(getApplicationContext(), "Forward Message", 0)
					.show();
		} else if (v == delete)
			deleteEmail();
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
	
	public void deleteEmail() {
		if (mail_list.size() != 0) {
			database = new EmailDatabase(getApplicationContext());
			database.openDB();
			long idAcc = database.getIDAccountFromEmail(Utils.getCurrentAcc(getApplicationContext()));
			long idFolderDelete = database.getIdFolderWithCurrentAccount(Utils.FOLDER_DELETE, idAcc);
			MessageEmail email = mail_list.get(currentPos);
			Toast.makeText(getApplicationContext(), "Delete message " + email.id, 0).show();
			database.updateRowToTableMessage(email.id, idFolderDelete,
					email.subject, email.from, email.to, email.content,
					email.date, email.fileName, email.sourceFile,
					email.contentHtml);
			database.closeDB();
			mail_list.remove(email);
			mAdapter.notifyDataSetChanged();
			long id[] = {email.id};
			MailDeleteAsyncTask deleteAsync;
			try {
				deleteAsync = new MailDeleteAsyncTask(this,
						Utils.FOLDER_NAME_INBOX, id);
				deleteAsync.execute(null);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}