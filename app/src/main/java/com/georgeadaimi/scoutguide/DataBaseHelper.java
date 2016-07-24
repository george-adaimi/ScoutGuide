package com.georgeadaimi.scoutguide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DataBaseHelper extends SQLiteOpenHelper{
	public static final String KEY_ID = "_id";
	 public static final String KEY_TASKNAME = "taskName";
	 public static final String KEY_STATUS = "status";
	 
    //The Android's default system path of your application database.
    private static String DB_PATH;
 
    private static String DB_NAME = "TaskManager.db";
 
    private SQLiteDatabase myDataBase; 
 
    private final Context myContext;
    
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {
    	
    	super(context, DB_NAME, null, 1);
    	DB_PATH= context.getDatabasePath(DB_NAME).getPath();
    	this.myContext = context;
    }
    
 
  /**
     * Creates a empty database on the system
     * */
    public void createDataBase() throws IOException{
 
    	boolean dbExist = checkDataBase();
 
    	if(dbExist){
    		//do nothing - database already exist
    	}else{
 
    		//By calling this method and empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
        	this.getWritableDatabase();
 
        	try {
 
    			openDataBase();
 
    		} catch (SQLException e) {
 
        		throw new Error("Error copying database");
 
        	}
    	}
 
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    		Log.d("Alerttt","Checkinggg");
 
    	}catch(SQLiteException e){
 
    		//database does't exist yet.
    		Log.d("ALerttt", "Error in checking: "+ e.getMessage());
 
    	}
 
    	if(checkDB != null){

    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH ;//+ DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
 
    public void openDataBase() throws SQLException{
 
    	//Open the database
        String myPath = DB_PATH ;//+ DB_NAME;
        try{
        	if(myDataBase ==null){
        		myDataBase=this.getWritableDatabase();
        	}
        }
        catch(Exception e)
        {
        	Log.d("ALerttt", "Error in opening: "+ e.getMessage());
        }
 
    }
 
    @Override
	public synchronized void close() {
 
    	    if(myDataBase != null)
    		    myDataBase.close();
 
    	    super.close();
 
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
	
	
	public void createTable(String tableName)
	{
		String sql = "CREATE TABLE IF NOT EXISTS '" + tableName + "' ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_TASKNAME+ " TEXT, "
                + KEY_STATUS + " INTEGER)";
        myDataBase.execSQL(sql);
	}
	
	public boolean deleteTable(String tableName)
	{
		String sql = "DROP TABLE '"+tableName+"'";
		try{
			myDataBase.execSQL(sql);
			return true;
		}catch(Exception e)
		{
			return false;
		}
	}
	
	public void addTask(String tableName,Task task) {
		ContentValues values = new ContentValues();
		values.put(KEY_TASKNAME, task.getTaskName()); // task name
		// status of task- can be 0 for not done and 1 for done
		values.put(KEY_STATUS, task.getStatus());
		// Inserting Row
		myDataBase.insert("'"+tableName+"'", null, values);
		}
	
	public boolean deleteTask(String tableName,Task task)
	{
		return myDataBase.delete("'"+tableName+"'",KEY_ID + "="+ task.getId(), null)>0;
	}
	
	public void updateTask(String tableName,Task task) {
		// updating row
		ContentValues values = new ContentValues();
		values.put(KEY_TASKNAME, task.getTaskName());
		values.put(KEY_STATUS, task.getStatus());
		myDataBase.update("'"+tableName+"'", values, KEY_ID + " = ?",
		new String[]{String.valueOf(task.getId())});
		}
	
	public List<Task> getAllTasks(String tableName) {
		List<Task> taskList = new ArrayList<Task>();
		// Select All Query
		String selectQuery = "SELECT  * FROM '" + tableName+"'";
		Cursor cursor = myDataBase.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
		do {
		Task task = new Task();
		task.setId(cursor.getInt(0));
		task.setTaskName(cursor.getString(1));
		task.setStatus(cursor.getInt(2));
		// Adding contact to list
		taskList.add(task);
		} while (cursor.moveToNext());
		}
		// return task list
		
		cursor.close();
		return taskList;
		}
	
	public List<String> getTaskTitles()
	{

        List<String> titles = new ArrayList<String>();
		Cursor c = myDataBase.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
		if (c.moveToFirst()) {
            do {
                titles.add(c.getString(0));
            } while (c.moveToNext());
        }
         
        // closing connection
        c.close();
        return titles;

		
	}
}
 
        // Add your public helper methods to access and get content from the database.
       // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
       // to you to create adapters for your views.
 

