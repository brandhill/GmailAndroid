package uet.dtui.gmail.components;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import uet.dtui.gmail.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.TextView;

public class Utils {
	
	public static final int TYPE_ACCOUNT_OWNER = 0;
	public static final int TYPE_ACCOUNT_NOT_ONWNER = 1;
	public static final String FOLDER_INBOX = "inbox";
	public static final String FOLDER_SENT = "sent";
	public static final String FOLDER_IMPORTANT = "important";
	public static final String FOLDER_DRAFT = "draft";
	public static final String FOLDER_DELETE = "delete";
	public static final String FILE_SHARE_PREFERENCES = "UET";
	public static final String CURRENT_ACC = "current";
	public static final String MAX_UID_EMAIL = "uid_max";
	public static final String LAST_UPDATED = "updated";
	
	//Name of folder gmal
	public static final String FOLDER_NAME_INBOX = "Inbox";
	public static final String FOLDER_NAME_IMPORTANT = "[Gmail]/Starred";
	public static final String FOLDER_NAME_DELETE = "[Gmail]/Trash";
	public static final String FOLDER_NAME_SENT = "[Gmail]/Sent mail";
	
	//Status of message
	public static final String UNREAD_EMAIL = "unread";
	public static final String READ_EMAIL = "read";
	
	//Bundle
	public static final String REPLY = "reply";
	public static final String FORWARD = "forward";
	public static final String TITLE = "title";
	
	//postion of view in menu
	public static final String VIEW_INBOX = "inbox view";
	public static final String VIEW_IMPORTANT = "important view";
	public static final String VIEW_DELETE = "delete view";
	public static final String VIEW_DRAFT = "draft view";
	public static final String VIEW_SENT = "sent view";
	
	public static TextView createTextView(Context context, String text) {
		TextView tv = new TextView(context);
		tv.setText(text);
		tv.setTextSize(30);
		tv.setTextColor(Color.WHITE);
		tv.setBackgroundResource(R.drawable.bg_bubble);
		tv.setCompoundDrawablesWithIntrinsicBounds(0, 0,
				android.R.drawable.presence_offline, 0);
		return tv;
	}

	public static Object convertViewtoDrawable(View view) {
		int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		view.measure(spec, spec);
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		Bitmap b = Bitmap.createBitmap(view.getMeasuredWidth(),
				view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		c.translate(-view.getScrollX(), -view.getScrollY());
		view.draw(c);
		view.setDrawingCacheEnabled(true);
		Bitmap cacheBmp = view.getDrawingCache();
		Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
		view.destroyDrawingCache();
		return new BitmapDrawable(viewBmp);
	}

	public static Boolean checkConnect(String user, String password) {
		Store store;
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		Session session = Session.getDefaultInstance(props, null);
		try {
			store = session.getStore("imaps");
			store.connect("imap.gmail.com", user, password);
			store.close();
			return true;
		} catch (MessagingException e) {
			return false;
		}
	}

	public static String getCurrentAcc(Context activity) {
		SharedPreferences pref = activity.getSharedPreferences(Utils.FILE_SHARE_PREFERENCES, activity.MODE_WORLD_READABLE);
		return pref.getString(Utils.CURRENT_ACC, "None");
	}
	
	public static void setCurrenAcc(String acc, Context activity) {
		SharedPreferences pref = activity.getSharedPreferences(Utils.FILE_SHARE_PREFERENCES, activity.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor myEditor = pref.edit();
		myEditor.putString(Utils.CURRENT_ACC, acc);
		myEditor.commit();
	}
}
