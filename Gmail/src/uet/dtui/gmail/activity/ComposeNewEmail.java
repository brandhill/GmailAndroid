package uet.dtui.gmail.activity;

import uet.dtui.gmail.R;
import uet.dtui.gmail.components.SaveToDraftPopupWindow;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.Button;

public class ComposeNewEmail extends BaseActivityWithMenu{
	private Button btnInbox;
	private Button btnSend;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.compose);
		
		findViews();
	}

	private void findViews() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBackPressed() {
		Display display = getWindowManager().getDefaultDisplay();
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		new SaveToDraftPopupWindow(this, inflater.inflate(
				R.layout.popup_window_savedraft, null, false), display.getWidth(), display.getHeight());
		super.onBackPressed();
	}
	
	

}
