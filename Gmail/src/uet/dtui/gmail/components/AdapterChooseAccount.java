package uet.dtui.gmail.components;

import java.util.List;

import uet.dtui.gmail.R;
import uet.dtui.gmail.activity.ComposeNewEmail;
import uet.dtui.gmail.model.Account;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterChooseAccount extends ArrayAdapter<Account>{

	private ComposeNewEmail activity;
	private List<Account> datas;
	private int layoutResource;
	
	public AdapterChooseAccount(ComposeNewEmail activity, int textViewResourceId,
			List<Account> objects) {
		super(activity.getApplicationContext(), textViewResourceId, objects);
		this.activity = activity;
		this.layoutResource = textViewResourceId;
		this.datas = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = null;
		row = activity.getLayoutInflater().inflate(layoutResource, parent, false);
		
		AccountViewHolder holder = new AccountViewHolder();
		holder.iconAcc = (ImageView) row.findViewById(R.id.image_user);
		holder.displayName = (TextView) row.findViewById(R.id.account_display);
		
		holder.displayName.setText(datas.get(position).displayName);
		holder.displayName.setTypeface(AllerFont.get(activity.getApplicationContext(), "fonts/Aller_Rg.ttf"));
		
		return row;
	}
	
	private class AccountViewHolder {
		ImageView iconAcc;
		TextView displayName;
	}


}
