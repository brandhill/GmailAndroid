package uet.dtui.gmail.model;


public class Account {
	public long id;
	public String displayName;
	public String email;
	public String password;
	/*State activity is true when account available in database else otherwide*/
	public Boolean stateActivite;
	/*State online is true when account online else otherwide*/
	public Boolean stateOnline;
	
	public Account(String email, String name, String pass) {
		id = System.currentTimeMillis();
		displayName = name;
		this.email = email;
		this.password = pass;
		stateActivite = true;
		stateOnline = true;
	}
	
	public Account(String email, String pass) {
		id = System.currentTimeMillis();
		displayName = email;
		this.email = email;
		this.password = pass;
		stateActivite = true;
		stateOnline = true;
	}
}
