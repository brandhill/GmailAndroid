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
		
		if (pos == 0)
			row.setBackgroundResource(R.drawable.bg_first_row_email);
		else if (pos % 2 != 0)
			row.setBackgroundResource(R.drawable.bg_row_email_graycolor);
		else if (pos % 2 == 0)
			row.setBackgroundResource(R.drawable.bg_row_email_white_color);
		
		MessageEmail mes_item = datalist.get(pos);
		
		from.setText(mes_item.from);
		date.setText(summarizedDate(mes_item.date));
		subject.setText(mes_item.subject);
		content.setText(mes_item.content);
		return row;
	}
	//-------------------------------------------//
	public String summarizedDate(Date date){
		String str="";
		Calendar cal = Calendar.getInstance();
		if (cal.get(Calendar.DATE)==date.getDate() && cal.get(Calendar.MONTH)==date.getMonth()-1 && cal.get(Calendar.YEAR)==date.getYear())
			str += Integer.toString(cal.get(Calendar.HOUR_OF_DAY)) + ":" + Integer.toString(cal.get(Calendar.MINUTE));
		else {
			if (date.getMonth() == 1) str += "Jan ";
			if (date.getMonth() == 2) str += "Feb ";
			if (date.getMonth() == 3) str += "Mar ";
			if (date.getMonth() == 4) str += "Apr ";
			if (date.getMonth() == 5) str += "May ";
			if (date.getMonth() == 6) str += "Jun ";
			if (date.getMonth() == 7) str += "Jul ";
			if (date.getMonth() == 8) str += "Aug ";
			if (date.getMonth() == 9) str += "Sep ";
			if (date.getMonth() == 10) str += "Oct ";
			if (date.getMonth() == 11) str += "Nov ";
			if (date.getMonth() == 12) str += "Dec ";
			str += Integer.toString(date.getDate());
			if (cal.get(Calendar.YEAR)!=date.getYear()) str += ", " +Integer.toString(date.getYear());
		} 
		Log.d("Date",str);
		return str;
	}
}
