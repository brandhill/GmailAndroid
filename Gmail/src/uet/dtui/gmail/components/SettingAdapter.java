package uet.dtui.gmail.components;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import uet.dtui.gmail.R;
import uet.dtui.gmail.model.MessageEmail;
import uet.dtui.gmail.model.PairSaving;

import android.app.Activity;
import android.content.Context;
import android.opengl.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SettingAdapter extends ArrayAdapter {
	
	private Context context;
	private Activity activity;
	private int layoutResource;
	private List<PairSaving> datalist;
	private static LayoutInflater inflater = null;
	
	public SettingAdapter(Context context, int layout , List<PairSaving> data) {
		super(context, layout, data);
		this.context = context;
		activity = (Activity) context;
		datalist = data;
		layoutResource = layout;
		inflater = (LayoutInflater) activity
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}
	//======================================================================================//
	public View getView(int pos, View convertView, ViewGroup parents) {
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		View row = inflater.inflate(layoutResource, null);

		TextView setting_title = (TextView) row.findViewById(R.id.setting_title);
		TextView setting_info = (TextView) row.findViewById(R.id.setting_info);
		
		PairSaving setting_item = datalist.get(pos);
		
		setting_title.setText(setting_item.title);
		if (setting_item.info.length()==0) setting_info.setVisibility(View.INVISIBLE);
		else setting_info.setText(setting_item.info);
		
		return row;
	}

}
