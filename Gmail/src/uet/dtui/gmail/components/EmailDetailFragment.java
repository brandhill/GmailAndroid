package uet.dtui.gmail.components;

import uet.dtui.gmail.R;
import uet.dtui.gmail.model.MessageEmail;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class EmailDetailFragment extends Fragment{
	private MessageEmail message;
	View row;
	public EmailDetailFragment(MessageEmail msg){
		super();
		message = msg;
	}
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		row = inflater.inflate(R.layout.email_detail_layout, container, false);
		TextView subject = (TextView) row.findViewById(R.id.mail_subject);
		TextView sender = (TextView) row.findViewById(R.id.mail_sender);
		TextView date = (TextView) row.findViewById(R.id.mail_date);
		TextView content = (TextView) row.findViewById(R.id.mail_content);
		ImageView icon_attach = (ImageView) row.findViewById(R.id.icon_attach);
		TextView attach = (TextView) row.findViewById(R.id.mail_attach);
		ImageView imageAtt = (ImageView) row.findViewById(R.id.imageAttachFile);
		WebView html = (WebView) row.findViewById(R.id.web_view);
		
		subject.setText(message.subject);
		sender.setText(message.from);
		date.setText(message.date);
		content.setText(message.content);
		if (!message.fileName.equals("")) {
			icon_attach.setVisibility(View.VISIBLE);
			imageAtt.setVisibility(View.VISIBLE);
		} else
			imageAtt.setVisibility(View.GONE);
		
		subject.setTypeface(AllerFont.get(getActivity().getApplicationContext(), "fonts/Aller_Bd.ttf"));
		sender.setTypeface(AllerFont.get(getActivity().getApplicationContext(), "fonts/Aller_Bd.ttf"));
		date.setTypeface(AllerFont.get(getActivity().getApplicationContext(), "fonts/Aller_Rg.ttf"));
		content.setTypeface(AllerFont.get(getActivity().getApplicationContext(), "fonts/Aller_Rg.ttf"));
		
		attach.setText(message.fileName);
		html.loadData(message.contentHtml, "text/html", "utf-8");
		return row;
	}
}
