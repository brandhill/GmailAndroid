package uet.dtui.gmail.components;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import uet.dtui.gmail.R;
import android.content.Context;
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
}
