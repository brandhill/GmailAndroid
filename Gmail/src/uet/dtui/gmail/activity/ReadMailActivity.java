package uet.dtui.gmail.activity;

import java.util.ArrayList;

import uet.dtui.gmail.R;
import uet.dtui.gmail.components.ListSerializable;
import uet.dtui.gmail.components.ReadMailFragmentAdapter;
import uet.dtui.gmail.components.Utils;
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
	        
	        delete.setOnClickListener(this);
	        reply.setOnClickListener(this);
	        forward.setOnClickListener(this);
	        next.setOnClickListener(this);
	        prev.setOnClickListener(this);
	        mPager.setAdapter(mAdapter);
	        mPager.setCurrentItem(currentPos);
	        
	        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
				
				public void onPageSelected(int arg0) {
					indicator.setText(arg0 + " of " + mail_list.size() );
					currentPos = arg0;
				}
				
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub
					
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
		}
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

}