package uet.dtui.gmail.activity;

import uet.dtui.gmail.R;
import uet.dtui.gmail.components.ClearableEditText;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class SearchActivity extends Activity{
	private ClearableEditText searchContent;
	private Button btnDelete;
	private Button btnCancel;
	private Button btnFrom;
	private Button btnTo;
	private Button btnSubject;
	private Button btnAll;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_layout);
		
		findView();
	}
	
	private void findView() {
		searchContent =  (ClearableEditText) findViewById(R.id.search_content);
		btnDelete = (Button) findViewById(R.id.btn_delete);
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		btnFrom = (Button) findViewById(R.id.btn_from);
		btnTo = (Button) findViewById(R.id.btn_to);
		btnSubject= (Button) findViewById(R.id.btn_subject);
		btnAll = (Button) findViewById(R.id.btn_all);
		
		// When Search Content is empty, the Delete button is disabled
		if (searchContent.getText().toString().equals(""))
			btnDelete.setEnabled(false);
		
		// Chỗ này xử lý nếu người dùng nhập vào (ClearableEditText thay đổi) thì enable Delete button và xử lý 
		
		// When Delete button was clicked, all content in Search Content is deleted
		btnDelete.setOnClickListener(new OnClickListener()
		{
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Xoá nội dung trong ô search content
			}
			
		});
		
		// When Cancel button was clicked, finish the activity
		btnCancel.setOnClickListener(new OnClickListener()
	    {        
			
	        public void onClick(View v)
	        {
	            finish();
	        }
	    });
		
		btnFrom.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnFrom.setBackgroundResource(R.drawable.btn_from_checked);
				btnTo.setBackgroundResource(R.drawable.btn_to_notchecked);
				btnSubject.setBackgroundResource(R.drawable.btn_subject_notchecked);
				btnAll.setBackgroundResource(R.drawable.btn_all_notchecked);
			}
			
		});
		
		btnTo.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnFrom.setBackgroundResource(R.drawable.btn_from_notchecked);
				btnTo.setBackgroundResource(R.drawable.btn_to_checked);
				btnSubject.setBackgroundResource(R.drawable.btn_subject_notchecked);
				btnAll.setBackgroundResource(R.drawable.btn_all_notchecked);
			}
			
		});
		
		btnSubject.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnFrom.setBackgroundResource(R.drawable.btn_from_notchecked);
				btnTo.setBackgroundResource(R.drawable.btn_to_notchecked);
				btnSubject.setBackgroundResource(R.drawable.btn_subject_checked);
				btnAll.setBackgroundResource(R.drawable.btn_all_notchecked);
			}
			
		});
		
		btnAll.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnFrom.setBackgroundResource(R.drawable.btn_from_notchecked);
				btnTo.setBackgroundResource(R.drawable.btn_to_notchecked);
				btnSubject.setBackgroundResource(R.drawable.btn_subject_notchecked);
				btnAll.setBackgroundResource(R.drawable.btn_all_checked);
			}
			
		});
	}
	
}