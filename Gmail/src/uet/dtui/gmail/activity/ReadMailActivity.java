package uet.dtui.gmail.activity;

import java.util.ArrayList;

import uet.dtui.gmail.R;
import uet.dtui.gmail.components.ListSerializable;
import uet.dtui.gmail.components.ReadMailFragmentAdapter;
import uet.dtui.gmail.model.MessageEmail;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Window;

public class ReadMailActivity extends FragmentActivity{
	ArrayList<MessageEmail> mail_list; 
	//private TextView indicator;
	@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.read_mail_layout);
	        
	        Intent intentRecv = getIntent();
	        ListSerializable serial = (ListSerializable) intentRecv.getSerializableExtra("maillist");  
	        mail_list = serial.getList();
	        mail_list.add(new MessageEmail());
	        Log.d("Size",Integer.toString(mail_list.size()));
	        
	        ReadMailFragmentAdapter mAdapter = new ReadMailFragmentAdapter(mail_list, getSupportFragmentManager());
	        ViewPager mPager = (ViewPager)findViewById(R.id.pager);
	        //indicator = (TextView) findViewById(R.id.indicator);
	        mPager.setAdapter(mAdapter);
	    }

}