package uet.dtui.gmail.model;

import uet.dtui.gmail.components.Utils;


public class Account {
	public long id;
	public String displayName;
	public String email;
	public String password;
	public int isOwner;
	
	public Account(String email, String name, String pass, int owner) {
		id = System.currentTimeMillis();
		displayName = name;
		this.email = email;
		this.password = pass;
		isOwner = owner;
	}
	/**
	 * Contructor for account is not owner
	 * */
	public Account(String email, String pass) {
		id = System.currentTimeMillis();
		displayName = email;
		this.email = email;
		this.password = pass;
		isOwner = Utils.TYPE_ACCOUNT_NOT_ONWNER;
	}
}
