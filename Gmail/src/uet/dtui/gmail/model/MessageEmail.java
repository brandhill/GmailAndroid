package uet.dtui.gmail.model;

import java.util.Date;

import javax.mail.Folder;

public class MessageEmail {
	public Folder folder;
	public String subject;
	public String from;
	public String to;
	public Date date;
	public String content;
	
	public MessageEmail(Folder folder, String subject, String from, String to, Date date, String content) {
		this.folder = folder;
		this.subject = subject;
		this.from = from;
		this.to = to;
		this.date= date;
		this.content = content;
	}
}
