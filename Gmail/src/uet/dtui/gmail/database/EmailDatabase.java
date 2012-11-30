package uet.dtui.gmail.database;
 
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import uet.dtui.gmail.components.Utils;
import uet.dtui.gmail.model.Account;
import uet.dtui.gmail.model.FolderEmail;
import uet.dtui.gmail.model.MessageEmail;
 
public class EmailDatabase{
	// the Activity or Application that is creating an object from this class.
	Context context;
 
	// a reference to the database used by this application/object
	private SQLiteDatabase db;
	private CustomSQLiteOpenHelper helper;
	// These constants are specific to the database. 
	private final String DB_NAME = "email_db";
	private final int DB_VERSION = 1;
 
	// These constants are specific to the database table Account. 
	private final String TABLE_ACCOUNT_NAME = "Account";
	private final String TABLE_ACCOUNT_ROW_ID = "id";
	private final String TABLE_ACCOUNT_EMAIL_ADDRESS = "email_address";
	private final String TABLE_ACCOUNT_PASSWORD = "password";
	private final String TABLE_ACCOUNT_DISPLAY_NAME = "display_name";
	private final String TABLE_ACCOUNT_IS_OWN = "is_own";
	
	// These constants are specific to the database table Folder.
	private final String TABLE_FOLDER_NAME = "Folder";
	private final String TABLE_FOLDER_ROW_ID = "id_folder";
	private final String TABLE_FOLDER_ID_ACCOUNT = "id_account";
	private final String TABLE_FOLDER_NAME_FOLDER = "name_folder";
	private final String TABLE_FOLDER_NUMBER_UNREAD = "number_unread";
	private final String TABLE_FOLDER_NUMBER_TOTAL_MAIL = "number_total_mail";
			
	// These constants are specific to the database table Message. 
	private final String TABLE_MESSAGE_NAME = "Message";
	private final String TABLE_MESSAGE_ROW_ID = "id_message";
	private final String TABLE_MESSAGE_ID_FOLDER = "id_folder";
	private final String TABLE_MESSAGE_SUBJECT = "subject";
	private final String TABLE_MESSAGE_FROM_ADDRESS = "from_address";
	private final String TABLE_MESSAGE_TO_ADDRESS = "to_address";
	private final String TABLE_MESSAGE_CONTEXT = "context";
	private final String TABLE_MESSAGE_DATE = "date";
	private final String TABLE_MESSAGE_FILE_NAME = "file_name";
	private final String TABLE_MESSAGE_SOURCE_FILE = "source_file";
	private final String TABLE_MESSAGE_CONTENT_HTML = "content_html";
 
	public EmailDatabase(Context context)
	{
		this.context = context;
	}
	
	public EmailDatabase openDB() {
		// create or open the database
		helper = new CustomSQLiteOpenHelper(context);
		this.db = helper.getWritableDatabase();
		return this;
	}
	
	public void closeDB() {
		helper.close();
	}
 
	/**********************************************************************
	 * FUNCTIONS FOR TABLE ACCOUNT.
	 *********************************************************************/
	/**********************************************************************
	 * ADDING A ROW TO THE DATABASE TABLE ACCOUNT
	 *
	 * @param id the value for the row's id column
	 * @param emailAddress the value for the row's first column
	 * @param password the value for the row's second column 
	 * @param displayName the value for the row's third column 
	 * @param isOwn the value for the row's forth column 
	 */
	public boolean addRowToTableAccount(long id, String emailAddress, String password, String displayName, int isOwn)
	{
		// check existing of id in the table.
		Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM ACCOUNT WHERE email_address=\"" + emailAddress + "\";", null);
		mCount.moveToFirst();
		int count= mCount.getInt(0);
		mCount.close();
		if (count==0){
			// this is a key value pair holder used by android's SQLite functions
			ContentValues values = new ContentValues();
			values.put(TABLE_ACCOUNT_ROW_ID, id);
			values.put(TABLE_ACCOUNT_EMAIL_ADDRESS, emailAddress);
			values.put(TABLE_ACCOUNT_PASSWORD, password);
			values.put(TABLE_ACCOUNT_DISPLAY_NAME, displayName);
			values.put(TABLE_ACCOUNT_IS_OWN, isOwn);
	 
			// ask the database object to insert the new data 
			try{
				db.insert(TABLE_ACCOUNT_NAME, null, values);
			}
			catch(Exception e)
			{
				Log.e("DB ERROR", e.toString());
				e.printStackTrace();
			}
			this.addRowToTableFolder(System.currentTimeMillis(), id, Utils.FOLDER_INBOX, 0, 0);
			this.addRowToTableFolder(System.currentTimeMillis(), id, Utils.FOLDER_IMPORTANT, 0, 0);
			this.addRowToTableFolder(System.currentTimeMillis(), id, Utils.FOLDER_SENT, 0, 0);
			this.addRowToTableFolder(System.currentTimeMillis(), id, Utils.FOLDER_DRAFT, 0, 0);
			this.addRowToTableFolder(System.currentTimeMillis(), id, Utils.FOLDER_DELETE, 0, 0);
		}
		else 
			return false;
		
		return true;
	}
	
	/*
	 * GET ACCOUNT
	 * */
	
	public List<Account> getAccountWithOwner(int owner) {
		List<Account> listAcc = new ArrayList<Account>();
		String sql = "SELECT * FROM ACCOUNT WHERE is_own=" + owner;
		Cursor cursor = db.rawQuery(sql, null);
		
		cursor.moveToFirst();
		 while (!cursor.isAfterLast()) {
			 Account acc = convertCursorToAccount(cursor);
			 listAcc.add(acc);
			 cursor.moveToNext();
		 }
		return listAcc;
	}
	
	public List<Account> getAllAccount() {
		List<Account> listAcc = new ArrayList<Account>();
		String sql = "SELECT * FROM ACCOUNT";
		Cursor cursor = db.rawQuery(sql, null);
		
		cursor.moveToFirst();
		 while (!cursor.isAfterLast()) {
			 Account acc = convertCursorToAccount(cursor);
			 listAcc.add(acc);
			 cursor.moveToNext();
		 }
		return listAcc;
	}
	
	public Account getAccountFromEmail(String email) {
		Account acc = new Account();
		String sql = "Select * from Account where email_address = \"" + email + "\"";
		Log.d("SQL", sql);
		Cursor cursor = db.rawQuery(sql, null);
		
		cursor.moveToFirst();
		acc = convertCursorToAccount(cursor);
		return acc;
	}
 
	private Account convertCursorToAccount(Cursor cursor) {
		Account acc = new Account();
		acc.id = cursor.getLong(0);
		acc.email = cursor.getString(1);
		acc.password = cursor.getString(2);
		acc.displayName = cursor.getString(3);
		acc.isOwner = cursor.getInt(4);
		return acc;
	}

	/**********************************************************************
	 * DELETING A ROW FROM THE DATABASE TABLE ACCOUNT
	 * 
	 * @param rowID the SQLite database identifier for the row to delete.
	 */
	public boolean deleteRowFromTableAccountByID(long rowID)
	{
		try {
			db.delete(TABLE_FOLDER_NAME, TABLE_FOLDER_ID_ACCOUNT + "=" + rowID, null);
			db.delete(TABLE_ACCOUNT_NAME, TABLE_ACCOUNT_ROW_ID + "=" + rowID, null);
		}
		catch (Exception e) {
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
			return false;
		}	
		return true;
	}
 
	/**********************************************************************
	 * UPDATING A ROW IN THE DATABASE TABLE ACCOUNT
	 *
	 * @param rowID the SQLite database identifier for the row to update.
	 * @param emailAddress the value for the row's first column
	 * @param password the value for the row's second column 
	 * @param displayName the value for the row's third column 
	 * @param isOwn the value for the row's forth column 
	 */ 
	public boolean updateRowToTableAccount(long rowID, String emailAddress, String password, String displayName, int isOwn)
	{
		// check existing of rowID in the table.
		Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM ACCOUNT WHERE id=" + rowID + ";", null);
		mCount.moveToFirst();
		int count= mCount.getInt(0);
		mCount.close();
		if (count != 0) {
			// this is a key value pair holder used by android's SQLite functions
			ContentValues values = new ContentValues();
			values.put(TABLE_ACCOUNT_ROW_ID, rowID);
			values.put(TABLE_ACCOUNT_EMAIL_ADDRESS, emailAddress);
			values.put(TABLE_ACCOUNT_PASSWORD, password);
			values.put(TABLE_ACCOUNT_DISPLAY_NAME, displayName);
			values.put(TABLE_ACCOUNT_IS_OWN, isOwn);
			
			// ask the database object to update the database row of given rowID
			try {
				db.update(TABLE_ACCOUNT_NAME, values, TABLE_ACCOUNT_ROW_ID + "=" + rowID, null);
			}
			catch (Exception e)
			{
				Log.e("DB Error", e.toString());
				e.printStackTrace();
			}
		}
		else 
			return false;
		
		return true;
	}
	
	/**********************************************************************
	 * FUNCTIONS FOR TABLE FOLDER.
	 *********************************************************************/
	/**********************************************************************
	 * ADDING A ROW TO THE DATABASE TABLE FOLDER
	 *
	 * @param idFolder the value for the row's id column
	 * @param idAccount the value for the row's first column
	 * @param nameFolder the value for the row's second column 
	 * @param numberUnread the value for the row's third column 
	 * @param numberTotalMail  the value for the row's forth column
	 */
	public boolean addRowToTableFolder(long idFolder, long idAccount, String nameFolder, int numberUnread, int numberTotalMail)
	{
		// check existing of idFolder in the table.
		Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM FOLDER WHERE id_folder=" + idFolder + ";", null);
		mCount.moveToFirst();
		int count= mCount.getInt(0);
		mCount.close();
		if (count == 0) {
			// this is a key value pair holder used by android's SQLite functions
			ContentValues values = new ContentValues();
			values.put(TABLE_FOLDER_ROW_ID, idFolder);
			values.put(TABLE_FOLDER_ID_ACCOUNT, idAccount);
			values.put(TABLE_FOLDER_NAME_FOLDER, nameFolder);
			values.put(TABLE_FOLDER_NUMBER_UNREAD, numberUnread);
			values.put(TABLE_FOLDER_NUMBER_TOTAL_MAIL, numberTotalMail);
	 
			// ask the database object to insert the new data 
			try{db.insert(TABLE_FOLDER_NAME, null, values);}
			catch(Exception e)
			{
				Log.e("DB ERROR", e.toString());
				e.printStackTrace();
			}
		}
		else
			return false;
		
		return true;
	}
 
	/**********************************************************************
	 * DELETING A ROW FROM THE DATABASE TABLE FOLDER
	 * 
	 * @param rowID the SQLite database identifier for the row to delete.
	 */
	public boolean deleteRowFromTableFolderByID(long rowID)
	{
		// check existing of rowID in the table.
		Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM FOLDER WHERE id_folder=" + rowID + ";", null);
		mCount.moveToFirst();
		int count= mCount.getInt(0);
		mCount.close();
		if (count != 0) {
			// ask the database manager to delete the row of given id
			try {
				db.delete(TABLE_MESSAGE_NAME, TABLE_MESSAGE_ID_FOLDER + "=" + rowID, null);
				db.delete(TABLE_FOLDER_NAME, TABLE_FOLDER_ROW_ID + "=" + rowID, null);
			}
			catch (Exception e)
			{
				Log.e("DB ERROR", e.toString());
				e.printStackTrace();
			}
		}
		else
			return false;
		
		return true;
	}
 
	/**********************************************************************
	 * UPDATING A ROW IN THE DATABASE TABLE FOLDER
	 *
	 * @param rowID the SQLite database identifier for the row to update
	 * @param idAccount the value for the row's first column
	 * @param nameFolder the value for the row's second column 
	 * @param numberUnread the value for the row's third column 
	 * @param numberTotalMail  the value for the row's forth column
	 */ 
	public boolean updateRowToTableFolder(long rowID, long idAccount, String nameFolder, int numberUnread, int numberTotalMail)
	{
		// check existing of rowID in the table.
		Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM FOLDER WHERE id_folder=" + rowID + ";", null);
		mCount.moveToFirst();
		int count= mCount.getInt(0);
		mCount.close();
		if (count != 0) {
			// this is a key value pair holder used by android's SQLite functions
			ContentValues values = new ContentValues();
			values.put(TABLE_FOLDER_ID_ACCOUNT, idAccount);
			values.put(TABLE_FOLDER_NAME_FOLDER, nameFolder);
			values.put(TABLE_FOLDER_NUMBER_UNREAD, numberUnread);
			values.put(TABLE_FOLDER_NUMBER_TOTAL_MAIL, numberTotalMail);
			
			// ask the database object to update the database row of given rowID
			try {
				db.update(TABLE_FOLDER_NAME, values, TABLE_FOLDER_ROW_ID + "=" + rowID, null);
			}
			catch (Exception e)
			{
				Log.e("DB Error", e.toString());
				e.printStackTrace();
			}
		}
		else
			return false;
		
		return true;
	}
	
	public List<FolderEmail> getAllFolderWithAcc(long idAcc) {
		List<FolderEmail> listFolder = new ArrayList<FolderEmail>();
		String sql = "SELECT * FROM Folder WHERE id_account = " +idAcc;
		Log.d("SQL", sql);
		Cursor cursor = db.rawQuery(sql, null);
		
		cursor.moveToFirst();
		 while (!cursor.isAfterLast()) {
			 FolderEmail folder = convertCursorToFolder(cursor);
			 listFolder.add(folder);
			 cursor.moveToNext();
		 }
		return listFolder;
	}
 
	private FolderEmail convertCursorToFolder(Cursor cursor) {
		FolderEmail folder= new FolderEmail();
		folder.id = cursor.getLong(0);
		folder.idAccount = cursor.getLong(1);
		folder.name = cursor.getString(2);
		folder.numberEmailUnread = cursor.getInt(3);
		folder.numberEmail = cursor.getInt(4);
		return folder;
	}
	
	public long getIdFolderWithCurrentAccount(String nameFolder, long idAcc) {
		long idFolder = -1;
		String sql = "SELECT * FROM Folder where name_folder = \"" + nameFolder + "\" and id_account = " + idAcc;
		Cursor cursor = db.rawQuery(sql, null);
		
		cursor.moveToFirst();
		idFolder = cursor.getLong(0);
		return idFolder;
	}

	/**********************************************************************
	 * FUNCTIONS FOR TABLE MESSAGE.
	 *********************************************************************/
	/**********************************************************************
	 * ADDING A ROW TO THE DATABASE TABLE MESSAGE
	 *
	 * @param idMessage the value for the row's id column
	 * @param idFolder the value for the row's first column
	 * @param subject the value for the row's second column 
	 * @param fromAddress the value for the row's third column 
	 * @param toAddress the value for the row's forth column 
	 * @param context the value for the row's firth column 
	* @param date the value for the row's sixth column 
	 * @param fileName the value for the row's seventh column
	 * @param sourceFile the value for the row's eighth column
	 * @param contentHTML the value for the row's ninth column
	 */
	
	public boolean addRowToTableMessage(long idMessage, long idFolder, String subject, String fromAddress, String toAddress, String context, String date, String fileName, String sourceFile, String contentHTML)
	{
		// check existing of idMessage in the table.
		Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM MESSAGE WHERE id_message=" + idMessage + ";", null);
		mCount.moveToFirst();
		int count= mCount.getInt(0);
		mCount.close();
		if (count == 0) {
			// this is a key value pair holder used by android's SQLite functions
			ContentValues values = new ContentValues();
			values.put(TABLE_MESSAGE_ROW_ID, idMessage);
			values.put(TABLE_MESSAGE_ID_FOLDER, idFolder);
			values.put(TABLE_MESSAGE_SUBJECT, subject);
			values.put(TABLE_MESSAGE_FROM_ADDRESS, fromAddress);
			values.put(TABLE_MESSAGE_TO_ADDRESS, toAddress);
			values.put(TABLE_MESSAGE_CONTEXT, context);
			values.put(TABLE_MESSAGE_DATE, date);
			values.put(TABLE_MESSAGE_FILE_NAME, fileName);
			values.put(TABLE_MESSAGE_SOURCE_FILE, sourceFile);
			values.put(TABLE_MESSAGE_CONTENT_HTML, contentHTML);
	 
			// ask the database object to insert the new data 
			try{
				db.insert(TABLE_MESSAGE_NAME, null, values);
			}
			catch(Exception e)
			{
				Log.e("DB ERROR", e.toString());
				e.printStackTrace();
			}
		}
		else
			return false;
		
		return true;
	}
 
 
 
	/**********************************************************************
	 * DELETING A ROW FROM THE DATABASE TABLE MESSAGE
	 * 
	 * @param rowID the SQLite database identifier for the row to delete.
	 */
	public boolean deleteRowFromTableMessageByID(long rowID)
	{// check existing of rowID in the table.
		Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM MESSAGE WHERE id_message=" + rowID + ";", null);
		mCount.moveToFirst();
		int count= mCount.getInt(0);
		mCount.close();
		if (count != 0) {
			// ask the database manager to delete the row of given id
			try {
				db.delete(TABLE_MESSAGE_NAME, TABLE_MESSAGE_ROW_ID + "=" + rowID, null);
			}
			catch (Exception e)
			{
				Log.e("DB ERROR", e.toString());
				e.printStackTrace();
			}
		}
		else
			return false;
		
		return true;
	}
 
	/**********************************************************************
	 * UPDATING A ROW IN THE DATABASE TABLE MESSAGE
	 *
	 * @param rowID the SQLite database identifier for the row to update
	 * @param idFolder the value for the row's first column
	 * @param subject the value for the row's second column 
	 * @param fromAddress the value for the row's third column 
	 * @param toAddress the value for the row's forth column 
	 * @param context the value for the row's firth column 
	 * @param date the value for the row's sixth column 
	 * @param fileName the value for the row's seventh column
	 * @param sourceFile the value for the row's eighth column
	 * @param contentHTML the value for the row's ninth column
	 */ 
	public boolean updateRowToTableMessage(long rowID, long idFolder, String subject, String fromAddress, String toAddress, String context, String date, String fileName, String sourceFile, String contentHTML)
	{
		// check existing of rowID in the table.
		Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM MESSAFE WHERE id_message=" + rowID + ";", null);
		mCount.moveToFirst();
		int count= mCount.getInt(0);
		mCount.close();
		if (count != 0) {
			// this is a key value pair holder used by android's SQLite functions
			ContentValues values = new ContentValues();
			values.put(TABLE_MESSAGE_ID_FOLDER, idFolder);
			values.put(TABLE_MESSAGE_SUBJECT, subject);
			values.put(TABLE_MESSAGE_FROM_ADDRESS, fromAddress);
			values.put(TABLE_MESSAGE_TO_ADDRESS, toAddress);
			values.put(TABLE_MESSAGE_CONTEXT, context);
			values.put(TABLE_MESSAGE_DATE, date);
			values.put(TABLE_MESSAGE_FILE_NAME, fileName);
			values.put(TABLE_MESSAGE_SOURCE_FILE, sourceFile);
			values.put(TABLE_MESSAGE_CONTENT_HTML, contentHTML);
			
			// ask the database object to update the database row of given rowID
			try {
				db.update(TABLE_MESSAGE_NAME, values, TABLE_MESSAGE_ROW_ID + "=" + rowID, null);
			}
			catch (Exception e)
			{
				Log.e("DB Error", e.toString());
				e.printStackTrace();
			}
		}
		else
			return false;
		
		return true;
	}
	
	public List<MessageEmail> getEmailByFrom(String searchContent) {
		List<MessageEmail> listMess = new ArrayList<MessageEmail>();
		String sql = "SELECT * FROM MESSAGE WHERE from_address LIKE \"%" + searchContent + "%\"";
		Cursor cursor = db.rawQuery(sql, null);
		
		cursor.moveToFirst();
		 while (!cursor.isAfterLast()) {
			 MessageEmail mess = convertCursorToMessageEmail(cursor);
			 listMess.add(mess);
			 cursor.moveToNext();
		 }
		return listMess;
	}
	
	public List<MessageEmail> getEmailByTo(String searchContent) {
		List<MessageEmail> listMess = new ArrayList<MessageEmail>();
		String sql = "SELECT * FROM MESSAGE WHERE to_address  LIKE \"%" + searchContent + "%\"";
		Cursor cursor = db.rawQuery(sql, null);
		
		cursor.moveToFirst();
		 while (!cursor.isAfterLast()) {
			 MessageEmail mess = convertCursorToMessageEmail(cursor);
			 listMess.add(mess);
			 cursor.moveToNext();
		 }
		return listMess;
	}
	
	public List<MessageEmail> getEmailBySubject(String searchContent) {
		List<MessageEmail> listMess = new ArrayList<MessageEmail>();
		String sql = "SELECT * FROM MESSAGE WHERE subject LIKE \"%" + searchContent +"%\"";
		Cursor cursor = db.rawQuery(sql, null);
		
		cursor.moveToFirst();
		 while (!cursor.isAfterLast()) {
			 MessageEmail mess = convertCursorToMessageEmail(cursor);
			 listMess.add(mess);
			 cursor.moveToNext();
		 }
		return listMess;
	}
	
	public List<MessageEmail> getEmail(String searchContent) {
		List<MessageEmail> listMess = new ArrayList<MessageEmail>();
		String sql = "SELECT * FROM MESSAGE WHERE from_address LIKE \"%" + searchContent + "%\" OR to_address LIKE \"%" + searchContent + "%\" OR subject LIKE \"%" + searchContent + "%\"";
		Cursor cursor = db.rawQuery(sql, null);
		
		cursor.moveToFirst();
		 while (!cursor.isAfterLast()) {
			 MessageEmail mess = convertCursorToMessageEmail(cursor);
			 listMess.add(mess);
			 cursor.moveToNext();
		 }
		return listMess;
	}
 
	private MessageEmail convertCursorToMessageEmail(Cursor cursor) {
		MessageEmail mess = new MessageEmail();
		mess.id = cursor.getLong(0);
		mess.idFolder = cursor.getLong(1);
		mess.subject = cursor.getString(2);
		mess.from = cursor.getString(3);
		mess.to = cursor.getString(4);
		mess.content = cursor.getString(5);
		mess.date = cursor.getString(6);
		mess.fileName = cursor.getString(7);
		mess.sourceFile = cursor.getString(8);
		mess.contentHtml = cursor.getString(9);
		return mess;
	}
	
	
	/**********************************************************************
	 * THIS IS THE BEGINNING OF THE INTERNAL SQLiteOpenHelper SUBCLASS.
	 *********************************************************************/
	private class CustomSQLiteOpenHelper extends SQLiteOpenHelper
	{
		public CustomSQLiteOpenHelper(Context context)
		{
			super(context, DB_NAME, null, DB_VERSION);
		}
 
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			// This strings is used to create the database.  
			String newTableAccountQueryString = "CREATE TABLE IF NOT EXISTS " +
										TABLE_ACCOUNT_NAME +
										" (" +
										TABLE_ACCOUNT_ROW_ID + " LONG PRIMARY KEY NOT NULL," +
										TABLE_ACCOUNT_EMAIL_ADDRESS + " TEXT NOT NULL," +
										TABLE_ACCOUNT_PASSWORD + " TEXT NOT NULL," +
										TABLE_ACCOUNT_DISPLAY_NAME + " text not null," +
										TABLE_ACCOUNT_IS_OWN + " INTEGER NOT NULL" +
										");";
			String newTableFolderQueryString = "CREATE TABLE IF NOT EXISTS " +
										TABLE_FOLDER_NAME +
										" (" +
										TABLE_FOLDER_ROW_ID + " LONG PRIMARY KEY NOT NULL," +
										TABLE_FOLDER_ID_ACCOUNT + " LONG NOT NULL," +
										TABLE_FOLDER_NAME_FOLDER + " TEXT NOT NULL," +
										TABLE_FOLDER_NUMBER_UNREAD + " INTEGER NOT NULL," + 
										TABLE_FOLDER_NUMBER_TOTAL_MAIL + " INTEGER NOT NULL" +
										");";
			String newTableMessageQueryString = "CREATE TABLE IF NOT EXISTS " +
										TABLE_MESSAGE_NAME +
										" (" +
										TABLE_MESSAGE_ROW_ID + " LONG PRIMARY KEY NOT NULL," +
										TABLE_MESSAGE_ID_FOLDER + " LONG NOT NULL," +
										TABLE_MESSAGE_SUBJECT + " TEXT NOT NULL," +
										TABLE_MESSAGE_FROM_ADDRESS + " TEXT NOT NULL," +
										TABLE_MESSAGE_TO_ADDRESS + " TEXT NOT NULL," +
										TABLE_MESSAGE_CONTEXT + " TEXT NOT NULL," +
										TABLE_MESSAGE_DATE + " TEXT NOT NULL," +
										TABLE_MESSAGE_FILE_NAME + " TEXT NOT NULL," +
										TABLE_MESSAGE_SOURCE_FILE + " TEXT NOT NULL," +
										TABLE_MESSAGE_CONTENT_HTML + " TEXT NOT NULL" +
										");";
			Log.d("EMAIL DATABSE", "CREATE TABLE");
			// execute the query strings to the database.
			db.execSQL(newTableAccountQueryString);
			db.execSQL(newTableFolderQueryString);
			db.execSQL(newTableMessageQueryString);
		}
 
 
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			Log.i("DataHelper", "Upgrading database, which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS" + TABLE_ACCOUNT_NAME + ";");
			db.execSQL("DROP TABLE IF EXISTS" + TABLE_FOLDER_NAME + ";");
			db.execSQL("DROP TABLE IF EXISTS" + TABLE_MESSAGE_NAME + ";");
		}
	}
}