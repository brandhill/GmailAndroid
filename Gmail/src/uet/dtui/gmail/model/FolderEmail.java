package uet.dtui.gmail.model;

public class FolderEmail {
	public long id;
	public String name;
	public long idAccount;
	public int numberEmailUnread;
	public int numberEmail;
	
	public FolderEmail() {
		super();
	}
	
	public FolderEmail(String name, long account, int all, int unread) {
		id = System.currentTimeMillis();
		this.name = name;
		this.idAccount = account;
		this.numberEmail = all;
		this.numberEmailUnread = unread;
	}
	
	public FolderEmail (String name, long account) {
		id = System.currentTimeMillis();
		this.name = name;
		this.idAccount = account;
		numberEmail = 0;
		numberEmailUnread = 0;
	}
}
