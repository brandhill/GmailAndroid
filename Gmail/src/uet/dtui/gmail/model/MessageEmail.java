package uet.dtui.gmail.model;

import java.util.Date;

public class MessageEmail {
	public long id;
	public long idFolder;
	public String subject;
	public String from;
	public String to;
	public Date date;
	public String content;
	public String fileName;
	
	public MessageEmail(long folder, String subject, String from, String to, Date date, String content, String file) {
		id = System.currentTimeMillis();
		this.idFolder = folder;
		this.subject = subject;
		this.from = from;
		this.to = to;
		this.date= date;
		this.content = content;
		this.fileName = file;
	}
}
