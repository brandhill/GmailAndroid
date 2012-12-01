package uet.dtui.gmail.model;

import java.io.Serializable;

public class MessageEmail implements Serializable{
	public long id;
	public long idFolder;
	public String subject;
	public String from;
	public String to;
	public String date;
	public String content;
	public String fileName;
	public String sourceFile;
	public String contentHtml;

	public MessageEmail() {
		super();
		subject = "";
		from = "" ;to = ""; date = "";
		content = ""; fileName = ""; sourceFile = "";
		contentHtml = "";
		id = idFolder = -1;
	}

	public MessageEmail(long id, long folder, String subject, String from, String to,
			String date, String content, String nameFile, String contentHtml,
			String srcFile) {
		this.id = id;
		this.idFolder = folder;
		this.subject = subject;
		this.from = from;
		this.to = to;
		this.date = date;
		this.content = content;
		this.fileName = nameFile;
		this.contentHtml = contentHtml;
		this.sourceFile = srcFile;
	}
}
