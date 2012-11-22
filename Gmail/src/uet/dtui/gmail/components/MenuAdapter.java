package uet.dtui.gmail.components;

import java.util.List;

import uet.dtui.gmail.activity.BaseActivityWithMenu;
import uet.dtui.gmail.model.ItemMenuAccount;
import uet.dtui.gmail.model.ItemMenuCategory;
import uet.dtui.gmail.model.ItemMenuFolder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MenuAdapter extends BaseAdapter{
	private List<Object> mItems;
	private BaseActivityWithMenu activity;
	private final int ITEM_ACCOUNT = 0;
	private final int ITEM_CATEGORY = 1;
	private final int ITEM_FOLDER = 2;
	
	public MenuAdapter(List<Object> items, BaseActivityWithMenu activity) {
		this.mItems = items;
		this.activity = activity;
	}

	public int getCount() {
		return mItems.size();
	}

	public Object getItem(int position) {
		return mItems.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public int getItemViewType(int position) {
		if (getItem(position) instanceof ItemMenuCategory)
			return ITEM_CATEGORY;
		else if (getItem(position) instanceof ItemMenuAccount)
			return ITEM_ACCOUNT;
		return ITEM_FOLDER;
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public boolean isEnabled(int position) {
		if (getItem(position) instanceof ItemMenuAccount || getItem(position) instanceof ItemMenuFolder)
			return true;
		return false;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
