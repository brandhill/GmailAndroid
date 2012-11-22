package uet.dtui.gmail.model;

public class FolderEmail {
	public String name;
	public Account account;
	public int numberEmailUnread;
	public int numberEmail;
	
	public FolderEmail(String name, Account account, int all, int unread) {
		this.name = name;
		this.account = account;
		this.numberEmail = all;
		this.numberEmailUnread = unread;
	}
	
	public FolderEmail (String name, Account account) {
		this.name = name;
		this.account = account;
		numberEmail = 0;
		numberEmailUnread = 0;
	}
}
