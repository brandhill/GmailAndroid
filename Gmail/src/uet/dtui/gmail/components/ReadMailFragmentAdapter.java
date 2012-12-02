package uet.dtui.gmail.components;

import java.util.ArrayList;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import uet.dtui.gmail.activity.ReadMailActivity;
import uet.dtui.gmail.model.MessageEmail;

public class ReadMailFragmentAdapter extends FragmentPagerAdapter {
	ArrayList<MessageEmail> mail_list;
	ReadMailActivity activity;
	
	public ReadMailFragmentAdapter(ArrayList<MessageEmail> list,FragmentManager fm){
		super(fm);
		mail_list = list;
	}
	
	public ReadMailFragmentAdapter(FragmentManager fm) {
        super(fm);
    }
	
	public ReadMailFragmentAdapter(ReadMailActivity activity, FragmentManager fm) {
        super(fm);
        this.activity = activity;
    }

	public EmailDetailFragment getItem(int position) {
		EmailDetailFragment fragment = new EmailDetailFragment(mail_list.get(position));
        return fragment;
    }
	
	public int getCount() {
		Log.d("count",Integer.toString(mail_list.size()));
        return mail_list.size();
    }

}