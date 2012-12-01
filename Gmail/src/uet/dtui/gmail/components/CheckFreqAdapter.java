package uet.dtui.gmail.components;

import java.util.List;

import uet.dtui.gmail.R;
import uet.dtui.gmail.model.PairSaving;
import android.app.Activity;
import android.app.ExpandableListActivity;
import android.app.ListActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

@SuppressWarnings("rawtypes")
public class CheckFreqAdapter extends ArrayAdapter implements ExpandableListAdapter{
	private Context context;
	private Activity activity;
	private int layoutResource;
	private List<PairSaving> datalist;
	private static LayoutInflater inflater = null;
	
	public CheckFreqAdapter(Context context, int layout , List<PairSaving> data) {
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
		TextView time_value = (TextView) row.findViewById(R.id.freq_value);
		PairSaving time_item = datalist.get(pos);
		time_value.setText(time_item.info);
		return row;
	}
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}
	public long getCombinedChildId(long groupId, long childId) {
		// TODO Auto-generated method stub
		return 0;
	}
	public long getCombinedGroupId(long groupId) {
		// TODO Auto-generated method stub
		return 0;
	}
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}
	public void onGroupCollapsed(int groupPosition) {
		// TODO Auto-generated method stub
		
	}
	public void onGroupExpanded(int groupPosition) {
		// TODO Auto-generated method stub
		
	}

}

