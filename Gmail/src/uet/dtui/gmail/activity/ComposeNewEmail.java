package uet.dtui.gmail.activity;

import uet.dtui.gmail.R;
import android.os.Bundle;
import android.view.Window;

public class ComposeNewEmail extends BaseActivityWithMenu{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.compose);
	}

}
