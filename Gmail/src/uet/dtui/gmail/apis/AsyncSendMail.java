package uet.dtui.gmail.apis;

import uet.dtui.gmail.activity.ComposeNewEmail;
import uet.dtui.gmail.components.Utils;
import uet.dtui.gmail.database.EmailDatabase;
import uet.dtui.gmail.model.MessageEmail;
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
	private EmailDatabase database;

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
		database = new EmailDatabase(context);
		database.openDB();
		MessageEmail email = new MessageEmail();
		email.id = System.currentTimeMillis();
		long idAcc = database.getIDAccountFromEmail(Utils.getCurrentAcc(context));
		email.idFolder = database.getIdFolderWithNameAndAcc(idAcc, Utils.FOLDER_SENT);
		email.fileName = fileName;
		email.content = mailBody;
		email.contentHtml = "";
		email.from = fromUser;
		email.to = toUser;
		email.subject = subject;
		database.addMessage(email);
		database.closeDB();
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
