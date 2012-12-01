package uet.dtui.gmail.activity;

import java.util.ArrayList;
import java.util.List;

import uet.dtui.gmail.R;
import uet.dtui.gmail.components.AddAccountPopupWindow;
import uet.dtui.gmail.components.CheckFreqAdapter;
import uet.dtui.gmail.components.NameSettingPopupWindow;
import uet.dtui.gmail.components.SettingAdapter;
import uet.dtui.gmail.model.PairSaving;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
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
	private SettingAdapter adapter;
	public static String PREFERENCES_FILE = "gmailPrefs";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_layout);
		SharedPreferences prefs = getSharedPreferences(PREFERENCES_FILE, MODE_WORLD_READABLE);		
		
		listview = (ListView) findViewById(R.id.settinglist);
		item = new ArrayList<PairSaving>();
		
		// here we match title and it's info
		item.add(new PairSaving("Your name",prefs.getString("name", "Empty")));
		item.add(new PairSaving("Signature",""));
		item.add(new PairSaving("Inbox check frequency","15 minutes"));
		item.add(new PairSaving("Ringtone","None"));
		item.add(new PairSaving("Vibrate","None"));
		
		adapter = new SettingAdapter(this, R.layout.setting_row, item);
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
		
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.show();
		alertDialog.setContentView(R.layout.setting_name);	
		
		Button save = (Button) alertDialog.findViewById(R.id.setting_name_btnsave);
		Button cancel = (Button) alertDialog.findViewById(R.id.setting_name_btncancel);
		
        save.setOnClickListener(new Button.OnClickListener() {	
			@SuppressLint("WorldReadableFiles")
			public void onClick(View v) {
				
				final EditText name = (EditText) alertDialog.findViewById(R.id.setting_name_value);
				SharedPreferences pref =
		                getSharedPreferences(PREFERENCES_FILE, MODE_WORLD_READABLE);
                SharedPreferences.Editor myEditor = pref.edit();
                myEditor.putString("name", name.getText().toString());
                myEditor.commit();
                ((ArrayAdapter<?>) listview.getAdapter()).notifyDataSetChanged();
                alertDialog.dismiss();
			}
		});  
        cancel.setOnClickListener(new Button.OnClickListener() {	
			public void onClick(View v) {
				
				alertDialog.dismiss();
			}
		});     
	}
	//--------------------------------------------------------------------------//
	private void settingVbrate() {
		
		Toast.makeText(this, "Vibrate setting", 1).show();
	}
	//--------------------------------------------------------------------------//
	private void settingCheckFrequency() {
		// TODO Auto-generated method stub
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.show();
		alertDialog.setContentView(R.layout.setting_freq);	
		
		Button save = (Button) alertDialog.findViewById(R.id.setting_sign_btnsave);
		Button cancel = (Button) alertDialog.findViewById(R.id.setting_sign_btncancel);
		
		ExpandableListView time_listview = (ExpandableListView) alertDialog.findViewById(R.id.setting_freq_list);
		
		ArrayList<PairSaving> item_list = new ArrayList<PairSaving>();
		item_list.add(new PairSaving("1", "1 minute"));
		item_list.add(new PairSaving("2", "2 minute"));
		item_list.add(new PairSaving("3", "5 minute"));
		item_list.add(new PairSaving("4", "10 minute"));
		item_list.add(new PairSaving("5", "15 minute"));
		
		CheckFreqAdapter checkFreqAdapter = new CheckFreqAdapter(this, R.layout.setting_freq_row, item_list);
		time_listview.setAdapter((ListAdapter) checkFreqAdapter);
		time_listview.setOnItemClickListener(this);
		
	}
	//--------------------------------------------------------------------------//
	private void settingSignature() {
		// TODO Auto-generated method stub
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.show();
		alertDialog.setContentView(R.layout.setting_sign);	
		
		Button save = (Button) alertDialog.findViewById(R.id.setting_sign_btnsave);
		Button cancel = (Button) alertDialog.findViewById(R.id.setting_sign_btncancel);
		
        save.setOnClickListener(new Button.OnClickListener() {	
			public void onClick(View v) {
				
				final EditText name = (EditText) alertDialog.findViewById(R.id.setting_sign_value);
				SharedPreferences pref =
		                getSharedPreferences(PREFERENCES_FILE, MODE_WORLD_READABLE);
                SharedPreferences.Editor myEditor = pref.edit();
                myEditor.putString("signature", name.getText().toString());
                myEditor.commit();
                ((ArrayAdapter) listview.getAdapter()).notifyDataSetChanged();
                alertDialog.dismiss();
			}
		});  
        cancel.setOnClickListener(new Button.OnClickListener() {	
			public void onClick(View v) {
				
				alertDialog.dismiss();
			}
		});     
	}
	//--------------------------------------------------------------------------//
	private void settingRingtone() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Ringtone setting", 1).show();
	}

}
