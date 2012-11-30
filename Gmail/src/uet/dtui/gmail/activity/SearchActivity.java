package uet.dtui.gmail.activity;

import java.util.ArrayList;
import java.util.List;

import uet.dtui.gmail.R;
import uet.dtui.gmail.components.AllerFont;
import uet.dtui.gmail.components.ClearableEditText;
import uet.dtui.gmail.components.EmailArrayAdapter;
import uet.dtui.gmail.components.Utils;
import uet.dtui.gmail.database.EmailDatabase;
import uet.dtui.gmail.model.MessageEmail;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchActivity extends ListActivity implements OnClickListener {
	private Button btnCancel;
	private Button btnFrom;
	private Button btnTo;
	private Button btnSubject;
	private Button btnAll;
	private ClearableEditText searchContent;
	private EmailArrayAdapter adapter;
	private EmailDatabase db;
	private int typeSearch = 1;
	private List<MessageEmail> mail_list = new ArrayList<MessageEmail>();
	private ListView listView;
	private TextView emptyView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_layout);
		db = new EmailDatabase(getApplicationContext());

		findView();
	}

	private void findView() {
		searchContent = (ClearableEditText) findViewById(R.id.search_content);
		btnCancel = (Button) findViewById(R.id.btn_search);
		btnFrom = (Button) findViewById(R.id.btn_from);
		btnTo = (Button) findViewById(R.id.btn_to);
		btnSubject = (Button) findViewById(R.id.btn_subject);
		btnAll = (Button) findViewById(R.id.btn_all);
		listView = getListView();
		emptyView = (TextView) findViewById(R.id.empty);
		
		emptyView.setTypeface(AllerFont.get(getApplicationContext(), "fonts/Aller_Bd.ttf"));
		searchContent.setTypeface(AllerFont.get(getApplicationContext(), "fonts/Aller_Rg.ttf"));
		// When Cancel button was clicked, finish the activity
		btnCancel.setOnClickListener(this);

		// When option button was selected
		btnFrom.setOnClickListener(this);
		btnTo.setOnClickListener(this);
		btnSubject.setOnClickListener(this);
		btnAll.setOnClickListener(this);
		
		//Add text change listener for edit text search content 
		searchContent.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			public void afterTextChanged(Editable s) {
				if (searchContent.getText().toString().equals("") && !searchContent.getClearable()) {
					searchContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
					searchContent.setClearable(false);
				}
				else {
					searchContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.btn_clear, 0);
					searchContent.setCompoundDrawablePadding(10);
					searchContent.setClearable(true);
				}

				
			}
		});
	}

	@SuppressLint("ShowToast")
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnFrom) {
			btnFrom.setBackgroundResource(R.drawable.btn_from_checked);
			btnTo.setBackgroundResource(R.drawable.btn_to_notchecked);
			btnSubject.setBackgroundResource(R.drawable.btn_subject_notchecked);
			btnAll.setBackgroundResource(R.drawable.btn_all_notchecked);
			typeSearch = 1;
		}
		if (v == btnTo) {
			btnFrom.setBackgroundResource(R.drawable.btn_from_notchecked);
			btnTo.setBackgroundResource(R.drawable.btn_to_checked);
			btnSubject.setBackgroundResource(R.drawable.btn_subject_notchecked);
			btnAll.setBackgroundResource(R.drawable.btn_all_notchecked);
			typeSearch = 2;
		}
		if (v == btnSubject) {
			btnFrom.setBackgroundResource(R.drawable.btn_from_notchecked);
			btnTo.setBackgroundResource(R.drawable.btn_to_notchecked);
			btnSubject.setBackgroundResource(R.drawable.btn_subject_checked);
			btnAll.setBackgroundResource(R.drawable.btn_all_notchecked);
			typeSearch = 3;
		}
		if (v == btnAll) {
			btnFrom.setBackgroundResource(R.drawable.btn_from_notchecked);
			btnTo.setBackgroundResource(R.drawable.btn_to_notchecked);
			btnSubject.setBackgroundResource(R.drawable.btn_subject_notchecked);
			btnAll.setBackgroundResource(R.drawable.btn_all_checked);
			typeSearch = 4;
		}
		if (v == btnCancel) {
			Log.d("Clicked", typeSearch + "");
			if (searchContent.getText().toString().equals("")) {
				Toast.makeText(this, "Please enter the content to search!", 1)
						.show();
			} else {
				if (typeSearch == 1) {
					db.openDB();
					mail_list = db.getEmailByFrom(searchContent.getText()
							.toString());
					db.closeDB();
				} else if (typeSearch == 2) {
					db.openDB();
					mail_list = db.getEmailByTo(searchContent.getText()
							.toString());
					db.closeDB();
				} else if (typeSearch == 3) {
					db.openDB();
					mail_list = db.getEmailBySubject(searchContent.getText()
							.toString());
					db.closeDB();
				} else if (typeSearch == 4) {
					db.openDB();
					mail_list = db.getEmail(searchContent.getText().toString());
					db.closeDB();
				}
				adapter = new EmailArrayAdapter(this, R.layout.mail_row,
						mail_list);
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> av, View v,
							int position, long id) {
						Log.d("Clicked", mail_list.get(position).content);
						Toast.makeText(getApplicationContext(),
								mail_list.get(position).content, 1).show();
					}
				});
			}
		}
	}
}
