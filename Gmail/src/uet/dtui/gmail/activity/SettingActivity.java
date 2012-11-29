package uet.dtui.gmail.activity;

import java.util.ArrayList;
import java.util.List;

import uet.dtui.gmail.R;
import uet.dtui.gmail.components.SettingAdapter;
import uet.dtui.gmail.model.PairSaving;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SettingActivity extends Activity implements OnItemClickListener{
	
	private final static int SETTING_NAME = 0;
	private final static int SETTING_SIGN = 1;
	private final static int SETTING_TIME = 2;
	private final static int SETTING_RING = 3;
	private final static int SETTING_VIBR = 4;
	private ListView listview;
	private List<PairSaving> item;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		
		listview = (ListView) findViewById(R.id.setting);
		item = new ArrayList<PairSaving>();
		
		// here we match title and it's info
		item.add(new PairSaving("Your name","something"));
		item.add(new PairSaving("Signature",""));
		item.add(new PairSaving("Inbox check frequency","15 minutes"));
		item.add(new PairSaving("Ringtone","None"));
		item.add(new PairSaving("Vibrate","None"));
		
		SettingAdapter adapter = new SettingAdapter(this, R.layout.setting_row, item);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	public void onItemClick(AdapterView<?> arg0, View v, int pos, long id) {
		// TODO Auto-generated method stub
		switch (pos){
			case SETTING_NAME:
				settingName();
				break;
			case SETTING_RING:
				settingRingtone();
				break;
			case SETTING_SIGN:
				settingSignature();
				break;
			case SETTING_TIME:
				settingCheckFrequency();
				break;
			case SETTING_VIBR:
				settingVbrate();
				break;
		}
	}
	//--------------------------------------------------------------------------//

	private void settingName() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Name setting", 1).show();
	}

	private void settingVbrate() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Vibrate setting", 1).show();
	}

	private void settingCheckFrequency() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Mail check frequency setting", 1).show();
	}

	private void settingSignature() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Signature setting", 1).show();
	}

	private void settingRingtone() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Ringtone setting", 1).show();
	}

}
