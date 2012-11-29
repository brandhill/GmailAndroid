package uet.dtui.gmail.apis;

import uet.dtui.gmail.activity.ComposeNewEmail;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class AsyncSendMail extends AsyncTask<Void, Void, Void> {
	private String fileName;
	private String mailBody;
	private String fromUser;
	private String pass;
	private String subject;
	private String toUser;
	private ComposeNewEmail context;
	private ProgressDialog dialog;

	public AsyncSendMail(ComposeNewEmail cont, String subject, String body,
			String from, String pass, String to, String filname) {
		this.fileName = filname;
		this.subject = subject;
		this.mailBody = body;
		this.fromUser = from;
		this.toUser = to;
		this.pass = pass;
		this.context = cont;
	}

	protected void onPreExecute() {
		dialog = new ProgressDialog(context);
		this.dialog.setMessage("Sending ...");
		this.dialog.show();
	}

	@Override
	protected Void doInBackground(Void... params) {
		send();
		publishProgress();
		return null;
	}
	
	protected void onProgressUpdate(Long... values) {
		super.onProgressUpdate();
	}
	
	protected void onPostExecute(final Void unused) {
		if (this.dialog.isShowing()) {
		this.dialog.dismiss();
		}
		Toast.makeText(context, "Send is successed", 0).show();
		context.finish();
	}

	private void send() {
		try {
			MailSender sender = new MailSender(fromUser, pass);
			sender.sendMail(subject, mailBody, fromUser, toUser, fileName, null);
		} catch (Exception e) {
			Log.e("SendMail", e.getMessage(), e);
		}
	}
}
