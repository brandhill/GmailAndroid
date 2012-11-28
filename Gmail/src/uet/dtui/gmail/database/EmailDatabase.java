package uet.dtui.gmail.database;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class EmailDatabase{
	// the Activity or Application that is creating an object from this class.
	Context context;
 
	// a reference to the database used by this application/object
	private SQLiteDatabase db;
 
	// These constants are specific to the database. 
	private final String DB_NAME = "email_db";
	private final int DB_VERSION = 1;
 
	// These constants are specific to the database table Account. 
	private final String TABLE_ACCOUNT_NAME = "Account";
	private final String TABLE_ACCOUNT_ROW_ID = "id";
	private final String TABLE_ACCOUNT_ROW_ONE = "email_address";
	private final String TABLE_ACCOUNT_ROW_TWO = "password";
	private final String TABLE_ACCOUNT_ROW_THREE = "display_name";
	private final String TABLE_ACCOUNT_ROW_FOUR = "is_own";
	
	// These constants are specific to the database table Folder.
	private final String TABLE_FOLDER_NAME = "Folder";
	private final String TABLE_FOLDER_ROW_ID = "id_folder";
	private final String TABLE_FOLDER_ROW_ONE = "id_account";
	private final String TABLE_FOLDER_ROW_TWO = "name_folder";
	private final String TABLE_FOLDER_ROW_THREE = "number_unread";
	private final String TABLE_FOLDER_ROW_FOUR = "number_total_mail";
			
	// These constants are specific to the database table Message. 
	private final String TABLE_MESSAGE_NAME = "Message";
	private final String TABLE_MESSAGE_ROW_ID = "id_message";
	private final String TABLE_MESSAGE_ROW_ONE = "id_folder";
	private final String TABLE_MESSAGE_ROW_TWO = "subject";
	private final String TABLE_MESSAGE_ROW_THREE = "from_address";
	private final String TABLE_MESSAGE_ROW_FOUR = "to_address";
	private final String TABLE_MESSAGE_ROW_FIVE = "context";
	private final String TABLE_MESSAGE_ROW_SIX = "date";
	private final String TABLE_MESSAGE_ROW_SEVEN = "attach";
 
	public EmailDatabase(Context context)
	{
		this.context = context;
 
		// create or open the database
		CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
		this.db = helper.getWritableDatabase();
	}
 
	/**********************************************************************
	 * FUNCTIONS FOR TABLE ACCOUNT.
	 *********************************************************************/
	/**********************************************************************
	 * ADDING A ROW TO THE DATABASE TABLE ACCOUNT
	 *
	 * @param id the value for the row's id column
	 * @param rowStringOne the value for the row's first column
	 * @param rowStringTwo the value for the row's second column 
	 * @param rowStringThree the value for the row's third column 
	 * @param rowStringFour the value for the row's forth column 
	 */
	public boolean addRowToTableAccount(long id, String rowStringOne, String rowStringTwo, String rowStringThree, int rowStringFour)
	{
		// check existing of id in the table.
		Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM ACCOUNT WHERE id=" + id + ";", null);
		mCount.moveToFirst();
		int count= mCount.getInt(0);
		mCount.close();
		if (count==0){
			// this is a key value pair holder used by android's SQLite functions
			ContentValues values = new ContentValues();
			values.put(TABLE_ACCOUNT_ROW_ID, id);
			values.put(TABLE_ACCOUNT_ROW_ONE, rowStringOne);
			values.put(TABLE_ACCOUNT_ROW_TWO, rowStringTwo);
			values.put(TABLE_ACCOUNT_ROW_THREE, rowStringThree);
			values.put(TABLE_ACCOUNT_ROW_FOUR, rowStringFour);
	 
			// ask the database object to insert the new data 
			try{
				db.insert(TABLE_ACCOUNT_NAME, null, values);
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
	 * DELETING A ROW FROM THE DATABASE TABLE ACCOUNT
	 * 
	 * @param rowID the SQLite database identifier for the row to delete.
	 */
	public boolean deleteRowFromTableAccountByID(long rowID)
	{
		// check existing of rowID in the table.
		Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM ACCOUNT WHERE id=" + rowID + ";", null);
		mCount.moveToFirst();
		int count= mCount.getInt(0);
		mCount.close();
		if (count != 0) {
			// ask the database manager to delete the row of given id
			try {
				db.delete(TABLE_FOLDER_NAME, TABLE_FOLDER_ROW_ONE + "=" + rowID, null);
				db.delete(TABLE_ACCOUNT_NAME, TABLE_ACCOUNT_ROW_ID + "=" + rowID, null);
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
	 * UPDATING A ROW IN THE DATABASE TABLE ACCOUNT
	 *
	 * @param rowID the SQLite database identifier for the row to update.
	 * @param rowStringOne the new value for the row's first column
	 * @param rowStringTwo the new value for the row's second column
	 * @param rowStringThree the value for the row's third column 
	 * @param rowStringFour the value for the row's forth column 
	 */ 
	public boolean updateRowToTableAccount(long rowID, String rowStringOne, String rowStringTwo, String rowStringThree, int rowStringFour)
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
			values.put(TABLE_ACCOUNT_ROW_ONE, rowStringOne);
			values.put(TABLE_ACCOUNT_ROW_TWO, rowStringTwo);
			values.put(TABLE_ACCOUNT_ROW_THREE, rowStringThree);
			values.put(TABLE_ACCOUNT_ROW_FOUR, rowStringFour);
			
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
	 * @param rowStringOne the value for the row's first column
	 * @param rowStringTwo the value for the row's second column 
	 * @param rowStringThree the value for the row's third column 
	 * @param rowStringFour the value for the row's forth column
	 */
	public boolean addRowToTableFolder(long idFolder, long rowStringOne, String rowStringTwo, int rowStringThree, int rowStringFour)
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
			values.put(TABLE_FOLDER_ROW_ONE, rowStringOne);
			values.put(TABLE_FOLDER_ROW_TWO, rowStringTwo);
			values.put(TABLE_FOLDER_ROW_THREE, rowStringThree);
			values.put(TABLE_FOLDER_ROW_FOUR, rowStringFour);
	 
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
				db.delete(TABLE_MESSAGE_NAME, TABLE_MESSAGE_ROW_ONE + "=" + rowID, null);
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
	 * @param rowID the SQLite database identifier for the row to update.
	 * @param rowStringOne the new value for the row's first column
	 * @param rowStringTwo the new value for the row's second column
	 * @param rowStringThree the value for the row's third column 
	 * @param rowStringFour the value for the row's forth column 
	 */ 
	public boolean updateRowToTableFolder(long rowID, long rowStringOne, String rowStringTwo, int rowStringThree, int rowStringFour)
	{
		// check existing of rowID in the table.
		Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM FOLDER WHERE id_folder=" + rowID + ";", null);
		mCount.moveToFirst();
		int count= mCount.getInt(0);
		mCount.close();
		if (count != 0) {
			// this is a key value pair holder used by android's SQLite functions
			ContentValues values = new ContentValues();
			values.put(TABLE_FOLDER_ROW_ONE, rowStringOne);
			values.put(TABLE_FOLDER_ROW_TWO, rowStringTwo);
			values.put(TABLE_FOLDER_ROW_THREE, rowStringThree);
			values.put(TABLE_FOLDER_ROW_FOUR, rowStringFour);
			
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
 
	/**********************************************************************
	 * FUNCTIONS FOR TABLE MESSAGE.
	 *********************************************************************/
	/**********************************************************************
	 * ADDING A ROW TO THE DATABASE TABLE MESSAGE
	 *
	 * @param idMessage the value for the row's id column
	 * @param rowStringOne the value for the row's first column
	 * @param rowStringTwo the value for the row's second column 
	 * @param rowStringThree the value for the row's third column 
	 * @param rowStringFour the value for the row's forth column 
	 * @param rowStringFive the value for the row's firth column 
	 * @param rowStringFive the value for the row's sixth column 
	 * @param rowStringFive the value for the row's seventh column
	 */
	public boolean addRowToTableMessage(long idMessage, long rowStringOne, String rowStringTwo, String rowStringThree, String rowStringFour, String rowStringFive, String rowStringSix, String rowStringSeven)
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
			values.put(TABLE_MESSAGE_ROW_ONE, rowStringOne);
			values.put(TABLE_MESSAGE_ROW_TWO, rowStringTwo);
			values.put(TABLE_MESSAGE_ROW_THREE, rowStringThree);
			values.put(TABLE_MESSAGE_ROW_FOUR, rowStringFour);
			values.put(TABLE_MESSAGE_ROW_FIVE, rowStringFive);
			values.put(TABLE_MESSAGE_ROW_SIX, rowStringSix);
			values.put(TABLE_MESSAGE_ROW_SEVEN, rowStringSeven);
	 
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
	 * @param rowID the SQLite database identifier for the row to update.
	 * @param rowStringOne the new value for the row's first column
	 * @param rowStringTwo the new value for the row's second column
	 * @param rowStringThree the value for the row's third column 
	 * @param rowStringFour the value for the row's forth column 
	 * @param rowStringFive the value for the row's firth column 
	 * @param rowStringSix the value for the row's sixth column 
	 * @param rowStringSeven the value for the row's seventh column
	 */ 
	public boolean updateRowToTableMessage(long rowID, long rowStringOne, String rowStringTwo, String rowStringThree, String rowStringFour, String rowStringFive, String rowStringSix, String rowStringSeven)
	{
		// check existing of rowID in the table.
		Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM MESSAFE WHERE id_message=" + rowID + ";", null);
		mCount.moveToFirst();
		int count= mCount.getInt(0);
		mCount.close();
		if (count != 0) {
			// this is a key value pair holder used by android's SQLite functions
			ContentValues values = new ContentValues();
			values.put(TABLE_MESSAGE_ROW_ONE, rowStringOne);
			values.put(TABLE_MESSAGE_ROW_TWO, rowStringTwo);
			values.put(TABLE_MESSAGE_ROW_THREE, rowStringThree);
			values.put(TABLE_MESSAGE_ROW_FOUR, rowStringFour);
			values.put(TABLE_MESSAGE_ROW_FIVE, rowStringFive);
			values.put(TABLE_MESSAGE_ROW_SIX, rowStringSix);
			values.put(TABLE_MESSAGE_ROW_SEVEN, rowStringSeven);
			
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
										TABLE_ACCOUNT_ROW_ONE + " TEXT NOT NULL," +
										TABLE_ACCOUNT_ROW_TWO + " TEXT NOT NULL," +
										TABLE_ACCOUNT_ROW_THREE + " text not null," +
										TABLE_ACCOUNT_ROW_FOUR + " INTEGER NOT NULL" +
										");";
			String newTableFolderQueryString = "CREATE TABLE IF NOT EXISTS " +
										TABLE_FOLDER_NAME +
										" (" +
										TABLE_FOLDER_ROW_ID + " LONG PRIMARY KEY NOT NULL," +
										TABLE_FOLDER_ROW_ONE + " LONG NOT NULL," +
										TABLE_FOLDER_ROW_TWO + " TEXT NOT NULL," +
										TABLE_FOLDER_ROW_THREE + " INTEGER NOT NULL," + 
										TABLE_FOLDER_ROW_FOUR + " INTEGER NOT NULL" +
										");";
			String newTableMessageQueryString = "CREATE TABLE IF NOT EXISTS " +
										TABLE_MESSAGE_NAME +
										" (" +
										TABLE_MESSAGE_ROW_ID + " LONG PRIMARY KEY NOT NULL," +
										TABLE_MESSAGE_ROW_ONE + " LONG NOT NULL," +
										TABLE_MESSAGE_ROW_TWO + " TEXT NOT NULL," +
										TABLE_MESSAGE_ROW_THREE + " TEXT NOT NULL," +
										TABLE_MESSAGE_ROW_FOUR + " TEXT NOT NULL," +
										TABLE_MESSAGE_ROW_FIVE + " TEXT NOT NULL," +
										TABLE_MESSAGE_ROW_SIX + " TEXT NOT NULL," +
										TABLE_MESSAGE_ROW_SEVEN + " TEXT NOT NULL" +
										");";

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