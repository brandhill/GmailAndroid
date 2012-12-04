package uet.dtui.gmail.components;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import uet.dtui.gmail.R;
import uet.dtui.gmail.model.MessageEmail;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class EmailArrayAdapter extends ArrayAdapter {
	
	private Context context;
	private Activity activity;
	private int layoutResource;
	private List<MessageEmail> datalist;
	private static LayoutInflater inflater = null;
	
	public EmailArrayAdapter(Context context, int layout , List<MessageEmail> data) {
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

		TextView from = (TextView) row.findViewById(R.id.mail_item_from);
		TextView date = (TextView) row.findViewById(R.id.mail_item_date);
		TextView subject = (TextView) row.findViewById(R.id.mail_item_subject);
		TextView content = (TextView) row.findViewById(R.id.mail_item_content);
		ImageView attach = (ImageView) row.findViewById(R.id.mail_item_attachment);
		CheckBox unread = (CheckBox) row.findViewById(R.id.mail_item_read);
		
		MessageEmail mes_item = datalist.get(pos);
		
		from.setText(mes_item.from);
		date.setText(mes_item.date);
		subject.setText(mes_item.subject);
		content.setText(mes_item.content);
		
		if (mes_item.contentHtml.equals(""))
			unread.setChecked(false);
		else
			unread.setChecked(true);
		
		if (mes_item.fileName.equals(""))
			attach.setVisibility(View.INVISIBLE);
		else
			attach.setVisibility(View.VISIBLE);
		
		if (pos % 2 == 0) {
			row.setBackgroundResource(R.drawable.my_btn_ics_transfrent);
		} else 
			row.setBackgroundResource(R.drawable.my_ics_button_white);
		return row;
	}
}
