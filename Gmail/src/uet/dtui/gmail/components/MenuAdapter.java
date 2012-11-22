package uet.dtui.gmail.components;

import java.util.List;

import uet.dtui.gmail.activity.BaseActivityWithMenu;
import uet.dtui.gmail.model.ItemMenuAccount;
import uet.dtui.gmail.model.ItemMenuCategory;
import uet.dtui.gmail.model.ItemMenuFolder;

import uet.dtui.gmail.R;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuAdapter extends BaseAdapter{
	private List<Object> mItems;
	private BaseActivityWithMenu activity;
	private final int ITEM_ACCOUNT = 0;
	private final int ITEM_CATEGORY = 1;
	private final int ITEM_FOLDER = 2;
	private Resources drawbleResource;
	
	public MenuAdapter(List<Object> items, BaseActivityWithMenu activity) {
		this.mItems = items;
		this.activity = activity;
		this.drawbleResource = activity.getResources();
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
		View row = convertView;
		Object item = getItem(position);
		CategoryViewHolder categoryHolder;
		AccountViewHolder accountHolder;
		FolderViewHolder folderHolder;
		
		if (item instanceof ItemMenuCategory) {
			if (row == null) {
				row = activity.getLayoutInflater().inflate(R.layout.menu_item_category, parent, false);
				
				categoryHolder = new CategoryViewHolder();
				categoryHolder.title = (ImageView) row.findViewById(R.id.category_image);
				categoryHolder.btnAddCount = (Button) row.findViewById(R.id.category_btn_add);
				
				if (((ItemMenuCategory)item).name.equals(BaseActivityWithMenu.ACCOUNT_CATEGORY)) {
					categoryHolder.title.setImageDrawable(activity.getResources().getDrawable(R.drawable.category_menu_categories));
					categoryHolder.btnAddCount.setVisibility(View.GONE);
				} else {
					categoryHolder.title.setImageDrawable(activity.getResources().getDrawable(R.drawable.category_menu_accounts));
					categoryHolder.btnAddCount.setOnClickListener(activity);
				}
			}
		} else if (item instanceof ItemMenuAccount) {
			if (row == null) {
				row = activity.getLayoutInflater().inflate(R.layout.menu_item_account, parent, false);
				
				accountHolder = new AccountViewHolder();
				accountHolder.iconAcc = (ImageView) row.findViewById(R.id.image_user);
				accountHolder.displayName = (TextView) row.findViewById(R.id.account_display);
				
				ItemMenuAccount itemAccount = (ItemMenuAccount) item;
				if (((ItemMenuAccount) item).account.email.equals(activity.currentAccount)) {
					accountHolder.iconAcc.setImageDrawable(drawbleResource.getDrawable(R.drawable.item_account_online));
					accountHolder.displayName.setTextColor(Color.WHITE);
				} else {
					accountHolder.iconAcc.setImageDrawable(drawbleResource.getDrawable(R.drawable.image_account));
					accountHolder.displayName.setTextColor(Color.parseColor("#8c8888"));
				}
			}
		} else if (item instanceof ItemMenuFolder) {
			if (row == null) {
				row = activity.getLayoutInflater().inflate(R.layout.menu_item_folder, parent, false);
				
				folderHolder = new FolderViewHolder();
				folderHolder.iconFolder = (ImageView) row.findViewById(R.id.icon_folder);
				folderHolder.nameFolder = (ImageView) row.findViewById(R.id.name_folder);
				folderHolder.unread = (TextView) row.findViewById(R.id.unread);
				folderHolder.total = (TextView) row.findViewById(R.id.total);
				
				ItemMenuFolder itemFolder = (ItemMenuFolder) item;
				if (itemFolder.folder.numberEmailUnread == 0)
					folderHolder.unread.setVisibility(View.GONE);
				else
					folderHolder.unread.setText(itemFolder.folder.numberEmailUnread);
				
				if (itemFolder.folder.numberEmail == 0)
					folderHolder.total.setVisibility(View.GONE);
				else
					folderHolder.total.setText(itemFolder.folder.numberEmail);
				
				folderHolder.iconFolder.setImageDrawable(drawbleResource.getDrawable(itemFolder.image));
				folderHolder.nameFolder.setImageDrawable(drawbleResource.getDrawable(itemFolder.imageName));
			}
		}
		return null;
	}
	
	private class CategoryViewHolder {
		ImageView title;
		Button btnAddCount;
	}
	
	private class AccountViewHolder {
		ImageView iconAcc;
		TextView displayName;
	}

	private class FolderViewHolder {
		ImageView iconFolder;
		ImageView nameFolder;
		TextView unread;
		TextView total;
	}
}