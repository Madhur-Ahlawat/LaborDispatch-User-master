package com.UsreLaborDispatch1.www.UsreLaborDispatch1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.UsreLaborDispatch1.www.sync.model.Job;
import com.UsreLaborDispatch1.www.sync.model.JobLog;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.UsreLaborDispatch1.www.UsreLaborDispatch1.ExportActivity.dateFormatForDatabase;

/**
 * Created by user on 2/15/2018.
 */

/**
 * Created by user on 2/15/2018.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Jobs.db";
    private static final String TABLE_NAME = "Jobnumbers";
    private static final String TABLE_NAME2 = "Transactions";
    private static final int DATABASE_VERSION = 12;
    private static final String UID = "_id";
    public static final String JOBNUMBER = "JOBNUMBER";
    private static final String JOBDESC = "JOBDESC";
    private static final String JOBHOURS = "JOBHOURS";
    private static final String JOBRATE = "JOBRATE";
    private static final String JOBFLAG = "JOBFLAG";
    private static final String STARTTIME = "STARTTIME";
    private static final String STOPTIME = "STOPTIME";
    private static final String STARTINTEG = "STARTINTEG";
    private static final String STOPINTEG = "STOPINTEG";
    private static final String STOPPED = "STOPPED";
    private static final String STOPPED2 = " ?";
    private static final String JOBPIN = "JOBPIN";
    private static final String TAG = "DatabaseHelper";
    public static final String PDF = "PDF";
    public static final String PDF_FILE_NAME = "PDF_FILE_NAME";

    public static final String IMAGE = "IMAGE";
    public static final String IMAGE_FILE_NAME = "IMAGE_FILE_NAME";

    public static final String COMPANY_ID = "COMPANY_ID";


    public static final String PRICE_QUOTE = "PRICE_QUOTE";
    public static final String QUANTITY = "QUANTITY";
    public static final String JOB_ADDRESS = "JOB_ADDRESS";
    public static final String START_DATE = "START_DATE";
    public static final String END_DATE = "END_DATE";
    public static final String JOB_DATE = "JOB_DATE";


    String t3;
    String t13;


    public static final String START_LATITUDE = "StartLatitude";
    public static final String START_LONGITUDE = "StartLongitude";


    public static final String STOP_LATITUDE = "StopLatitude";
    public static final String STOP_LONGITUDE = "StopLongitude";

    public Cursor getAllJobCursorTable2(String userPin,String companyId) {

        // Open the database for reading

//        String query = "SELECT * FROM " + TABLE_NAME2 + " WHERE  JOBPIN = ? AND COMPANY_ID = ? AND STARTTIME >= ?" + "ORDER BY STARTTIME DESC";
//        String[] selectionArgs = { userPin,companyId, "DATETIME('now', '-60 day')"};

        String query = "SELECT * FROM "+ TABLE_NAME2 +" WHERE JOBPIN = "+userPin+" AND COMPANY_ID = '"+companyId+"' AND JOB_DATE >=  DATETIME('now', '-60 day') ORDER BY STARTTIME DESC";

        return db.rawQuery(query, null);


    }

    public Cursor getAllJobCursorTransactopn(String userPin,String companyId) {

        String query = "SELECT * FROM "+ TABLE_NAME2 +" WHERE JOBPIN = "+userPin+" AND COMPANY_ID = '"+companyId+"' AND STARTTIME >=  DATETIME('now', '-60 day') ORDER BY STARTTIME DESC";

        return db.rawQuery(query, null);


    }

    // String currentdate = java.text.DateFormat.getDateTimeInstance().format(new Date ());

    public String getCurrentDateTime() {
        return dateFormatForDatabase.format(new Date());
    }

    //private static final int itime = (int) new Date ().getTime();
    //private static final int itime2 = itime/1000;
    //private static final Integer STOPPED3= itime2;
    private static DatabaseHelper databaseHelper = null;

    public static DatabaseHelper getInstance(Context context) {
        if (databaseHelper == null)
            databaseHelper = new DatabaseHelper(context);
        return databaseHelper;
    }

    SQLiteDatabase db;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME2 + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, JOBNUMBER VARCHAR(15) NOT NULL, JOBDESC VARCHAR(35), JOBHOURS DOUBLE, JOBRATE REAL, " +
                "JOBFLAG TEXT, STARTTIME DATETIME DEFAULT CURRENT_TIMESTAMP, STOPTIME DATETIME DEFAULT CURRENT_TIMESTAMP, STARTINTEG REAL, STOPINTEG REAL, JOBPIN VARCHAR(15),StartLongitude DOUBLE DEFAULT 0,StartLatitude DOUBLE DEFAULT 0,StopLatitude DOUBLE DEFAULT 0,StopLongitude DOUBLE DEFAULT 0,PDF_FILE_NAME VARCHAR(250),IMAGE VARCHAR(250),IMAGE_FILE_NAME VARCHAR(250),COMPANY_ID VARCHAR(250),PRICE_QUOTE VARCHAR(25),QUANTITY VARCHAR(25),JOB_DATE VARCHAR(25))");

        db.execSQL("create table " + TABLE_NAME + " (UID INTEGER PRIMARY KEY AUTOINCREMENT, JOBNUMBER VARCHAR(15) UNIQUE NOT NULL, JOBDESC VARCHAR(35), JOBHOURS DOUBLE, JOBRATE REAL, " +
                "JOBFLAG TEXT, STARTTIME DATETIME DEFAULT CURRENT_TIMESTAMP, STOPTIME DATETIME DEFAULT CURRENT_TIMESTAMP, STARTINTEG REAL, STOPINTEG REAL, JOBPIN VARCHAR(15),StartLongitude DOUBLE DEFAULT 0,StartLatitude DOUBLE DEFAULT 0,StopLatitude DOUBLE DEFAULT 0,StopLongitude DOUBLE DEFAULT 0,PDF VARCHAR(250),PDF_FILE_NAME VARCHAR(250),COMPANY_ID VARCHAR(250),PRICE_QUOTE VARCHAR(25),QUANTITY VARCHAR(25),JOB_ADDRESS VARCHAR(500),JOB_DATE VARCHAR(25))");

    }

    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public boolean insertData(String jobnumber, String jobdesc, String jobrate, String jobflag, String jobpin,String companyId) {
        try {

            ContentValues contentValues = new ContentValues();
            String currentdate = DateFormat.getDateTimeInstance().format(new Date());
            contentValues.put(JOBNUMBER, jobnumber);
            contentValues.put(JOBDESC, jobdesc);
            contentValues.put(JOBHOURS, 0.0000);
            contentValues.put(JOBRATE, jobrate);
            contentValues.put(JOBFLAG, "STOPPED");
            contentValues.put(JOBPIN, jobpin);

            contentValues.put(STARTTIME, "0");
            contentValues.put(STOPTIME, "0");
            contentValues.put(STARTINTEG, "0");
            contentValues.put(STOPINTEG, "0");
            contentValues.put(COMPANY_ID, companyId);
            // contentValues.put (STARTTIME, currentdate);
            //contentValues.put (STOPTIME, currentdate);

            long result = db.insert(TABLE_NAME, null, contentValues);
            if (result == -1)
                return false;
            else
                return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public Integer deleteData(String jobnumber) {

        return db.delete(TABLE_NAME, JOBNUMBER + " = ? ", new String[]{jobnumber});

    }

    public Integer deleteData(long id) {

        return db.delete(TABLE_NAME2, UID + " = ? ", new String[]{id + ""});

    }

    public Integer deleteDataTable2(String uid) {

        return db.delete(TABLE_NAME2, UID + " = ? AND " + UID + " = ?",
                new String[]{uid});

    }


    public Cursor getAllData() {

        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }


    public Cursor getAllDataTable2() {

        Cursor res = db.rawQuery("select * from " + TABLE_NAME2, null);
        return res;
    }

    public List<JobLog> getImages(String companyId, String jobPin) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME2 +" where  JOBPIN = ? AND COMPANY_ID = ? AND IMAGE!=''", new String[]{jobPin,companyId});
        ArrayList<JobLog> logs = new ArrayList<>();
        while (cursor.moveToNext()) {
            JobLog jobLog = new JobLog(
                    cursor.getString(cursor.getColumnIndex(JOBNUMBER)),
                    cursor.getString(cursor.getColumnIndex(JOBDESC)),
                    cursor.getString(cursor.getColumnIndex(JOBRATE)),
                    cursor.getString(cursor.getColumnIndex(JOBFLAG)),
                    cursor.getString(cursor.getColumnIndex(JOBPIN)),
                    cursor.getString(cursor.getColumnIndex(UID)),
                    cursor.getString(cursor.getColumnIndex(JOBHOURS)),
                    cursor.getString(cursor.getColumnIndex(STARTTIME)),
                    cursor.getString(cursor.getColumnIndex(STOPTIME)),
                    cursor.getString(cursor.getColumnIndex(STARTINTEG)),
                    cursor.getString(cursor.getColumnIndex(STOPINTEG)),
                    cursor.getString(cursor.getColumnIndex(START_LATITUDE)),
                    cursor.getString(cursor.getColumnIndex(START_LONGITUDE)),
                    cursor.getString(cursor.getColumnIndex(STOP_LATITUDE)),
                    cursor.getString(cursor.getColumnIndex(STOP_LONGITUDE)),
                    cursor.getString(cursor.getColumnIndex(PRICE_QUOTE)),
                    cursor.getString(cursor.getColumnIndex(QUANTITY)),
                    cursor.getString(cursor.getColumnIndex(PDF_FILE_NAME)),
                    cursor.getString(cursor.getColumnIndex(IMAGE_FILE_NAME)),
                    cursor.getString(cursor.getColumnIndex(IMAGE)),
                    cursor.getString(cursor.getColumnIndex(COMPANY_ID)),
                    cursor.getString(cursor.getColumnIndex(JOB_DATE))


            );
            logs.add(jobLog);
        }
        return logs;
    }

    public List<Job> getPdfFiles(String companyId, String jobPin) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME +" where  JOBPIN = ? AND COMPANY_ID = ? AND PDF!=''", new String[]{jobPin,companyId});
        ArrayList<Job> logs = new ArrayList<>();
        while (cursor.moveToNext()) {
            Job jobLog = new Job(
                    cursor.getString(cursor.getColumnIndex(JOBNUMBER)),
                    cursor.getString(cursor.getColumnIndex(JOBDESC)),
                    cursor.getString(cursor.getColumnIndex(JOBRATE)),
                    cursor.getString(cursor.getColumnIndex(JOBFLAG)),
                    cursor.getString(cursor.getColumnIndex(JOBPIN)),
                    cursor.getString(cursor.getColumnIndex(JOBHOURS)),
                    cursor.getString(cursor.getColumnIndex(STARTTIME)),
                    cursor.getString(cursor.getColumnIndex(STOPTIME)),
                    cursor.getString(cursor.getColumnIndex(STARTINTEG)),
                    cursor.getString(cursor.getColumnIndex(STOPINTEG)),
                    cursor.getString(cursor.getColumnIndex(START_LATITUDE)),
                    cursor.getString(cursor.getColumnIndex(START_LONGITUDE)),
                    cursor.getString(cursor.getColumnIndex(PRICE_QUOTE)),
                    cursor.getString(cursor.getColumnIndex(QUANTITY)),
                    cursor.getString(cursor.getColumnIndex(PDF)),
                    cursor.getString(cursor.getColumnIndex(PDF_FILE_NAME)),
                    cursor.getString(cursor.getColumnIndex((COMPANY_ID))),
                    cursor.getString(cursor.getColumnIndex((JOB_ADDRESS))),
                    cursor.getString(cursor.getColumnIndex((JOB_DATE)))
            );
            logs.add(jobLog);
        }
        return logs;
    }


    public Cursor getAllReportData(String userPin,String companyId) {

        String query = "SELECT * FROM transactions where  JOBPIN = ? AND COMPANY_ID = ? ORDER BY JOB_DATE DESC";
        Cursor res = db.rawQuery(query, new String[]{userPin,companyId});
        return res;
    }

    public Cursor getOneReportData(String jobnumber) {

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME2 + " WHERE " +
                JOBNUMBER + "=? ORDER BY STARTTIME DESC", new String[]{jobnumber});
        return res;
    }


    public boolean startajob(String jobnumber, String jobflag, String starttime) {

        ContentValues contentValues = new ContentValues();
        String currentdate = getCurrentDateTime();
        int itime = (int) new Date().getTime();
        int itime2 = (itime / 1000);
        contentValues.put(JOBNUMBER, jobnumber);
        contentValues.put(JOBFLAG, "STARTED");
        contentValues.put(STARTTIME, currentdate);
        //contentValues.put (STOPTIME, stoptime);
        contentValues.put(STARTINTEG, itime2);

        db.update(TABLE_NAME, contentValues, JOBNUMBER + " = ? AND " + JOBFLAG + " = ?",
                new String[]{jobnumber, "STOPPED"});

        return true;

    }


    public boolean startajob(String jobnumber, String jobflag, String starttime, double latitude, double longitude) {

        ContentValues contentValues = new ContentValues();
        String currentdate = getCurrentDateTime();
        int itime = (int) new Date().getTime();
        int itime2 = (itime / 1000);
        contentValues.put(JOBNUMBER, jobnumber);
        contentValues.put(JOBFLAG, "STARTED");
        contentValues.put(STARTTIME, currentdate);
        //contentValues.put (STOPTIME, stoptime);
        contentValues.put(STARTINTEG, itime2);

        contentValues.put(START_LATITUDE, latitude);
        contentValues.put(START_LONGITUDE, longitude);

        db.update(TABLE_NAME, contentValues, JOBNUMBER + " = ? AND " + JOBFLAG + " = ?",
                new String[]{jobnumber, "STOPPED"});

        return true;

    }

    ////////////////////////////////////////////

    public Boolean stopJob(String jobnumber, String jobflag, String stoptime) {
        int itime = (int) new Date().getTime();
        int itime2 = (itime / 1000);


        db.execSQL("UPDATE Jobnumbers SET JOBFLAG  ='" + STOPPED + "',  STOPINTEG  ='" + itime2 + "',  STOPTIME  ='" + getCurrentDateTime() + "', JOBHOURS = ((('" + itime2 + "'-STARTINTEG)/3600)+JOBHOURS)  WHERE JOBNUMBER =?", new String[]{jobnumber});

//        database.execSQL("INSERT INTO " + TABLE_NAME2 + "(JOBNUMBER, JOBDESC, JOBRATE, JOBFLAG, STARTTIME ,STOPTIME, STARTINTEG, STOPINTEG, JOBPIN) SELECT JOBNUMBER, JOBDESC, JOBRATE, JOBFLAG, STARTTIME ,STOPTIME, STARTINTEG, STOPINTEG, JOBPIN FROM " + TABLE_NAME + " WHERE JOBNUMBER =?", new String[]{jobnumber});
        String selectQuery = "SELECT JOBNUMBER, JOBDESC, JOBRATE, JOBFLAG, JOBHOURS, STARTTIME ,STOPTIME, STARTINTEG, STOPINTEG, JOBPIN FROM " + TABLE_NAME + " WHERE JOBNUMBER =?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{jobnumber});
        String[] colums = cursor.getColumnNames();
        cursor.moveToFirst();
        ContentValues contentValues = new ContentValues();

        contentValues.put(JOBNUMBER, cursor.getString(cursor.getColumnIndex(JOBNUMBER)));
        contentValues.put(JOBDESC, cursor.getString(cursor.getColumnIndex(JOBDESC)));
        contentValues.put(JOBRATE, cursor.getString(cursor.getColumnIndex(JOBRATE)));
        contentValues.put(JOBFLAG, cursor.getString(cursor.getColumnIndex(JOBFLAG)));
        contentValues.put(JOBHOURS, (itime2 - cursor.getLong(cursor.getColumnIndex(STARTINTEG))) / 3600f);
        contentValues.put(STARTTIME, cursor.getString(cursor.getColumnIndex(STARTTIME)));
        contentValues.put(STOPTIME, cursor.getString(cursor.getColumnIndex(STOPTIME)));
        contentValues.put(STARTINTEG, cursor.getString(cursor.getColumnIndex(STARTINTEG)));
        contentValues.put(STOPINTEG, cursor.getString(cursor.getColumnIndex(STOPINTEG)));
        contentValues.put(JOBPIN, cursor.getString(cursor.getColumnIndex(JOBPIN)));

        db.insert(TABLE_NAME2, null, contentValues);
        return true;
    }


    public Boolean stopJob(String jobnumber, String jobflag, String stoptime, double latitude, double longitude,
                           String imageFileName,String image,
                           String priceQuote,String quantity,String jobDate) {
        int itime = (int) new Date().getTime();
        int itime2 = (itime / 1000);
        try {


            String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE JOBFLAG='STARTED' AND JOBNUMBER =?";
            Cursor cursor = db.rawQuery(selectQuery, new String[]{jobnumber});
            cursor.moveToFirst();
            ContentValues contentValues = new ContentValues();
            contentValues.put(JOBNUMBER, cursor.getString(cursor.getColumnIndex(JOBNUMBER)));
            contentValues.put(JOBDESC, cursor.getString(cursor.getColumnIndex(JOBDESC)));
            contentValues.put(JOBRATE, cursor.getString(cursor.getColumnIndex(JOBRATE)));
            contentValues.put(JOBFLAG, STOPPED);
            contentValues.put(JOBHOURS, (itime2 - cursor.getLong(cursor.getColumnIndex(STARTINTEG))) / 3600f);
            contentValues.put(STARTTIME, cursor.getString(cursor.getColumnIndex(STARTTIME)));
            contentValues.put(STOPTIME, getCurrentDateTime());
            contentValues.put(STARTINTEG, cursor.getString(cursor.getColumnIndex(STARTINTEG)));
            contentValues.put(STOPINTEG, itime2);
            contentValues.put(JOBPIN, cursor.getString(cursor.getColumnIndex(JOBPIN)));
            contentValues.put(COMPANY_ID, cursor.getString(cursor.getColumnIndex(COMPANY_ID)));

            contentValues.put(START_LATITUDE, cursor.getString(cursor.getColumnIndex(START_LATITUDE)));
            contentValues.put(START_LONGITUDE, cursor.getString(cursor.getColumnIndex(START_LONGITUDE)));

            contentValues.put(STOP_LATITUDE, latitude);
            contentValues.put(STOP_LONGITUDE, longitude);
            contentValues.put(PDF_FILE_NAME,  cursor.getString(cursor.getColumnIndex(PDF_FILE_NAME)));


            contentValues.put(IMAGE_FILE_NAME, imageFileName);
            contentValues.put(IMAGE, image);

            contentValues.put(PRICE_QUOTE, priceQuote);
            contentValues.put(QUANTITY, quantity);
            contentValues.put(JOB_DATE, jobDate);

            db.insert(TABLE_NAME2, null, contentValues);


            db.execSQL("UPDATE Jobnumbers SET JOBFLAG  ='" + STOPPED + "',  STOPINTEG  ='" + itime2 + "',  STOPTIME  ='" + getCurrentDateTime() + "', JOBHOURS = ((('" + itime2 + "'-STARTINTEG)/3600)+JOBHOURS)  WHERE JOBFLAG='STARTED' AND JOBNUMBER =?", new String[]{jobnumber});
//        database.execSQL("INSERT INTO " + TABLE_NAME2 + "(JOBNUMBER, JOBDESC, JOBRATE, JOBFLAG, STARTTIME ,STOPTIME, STARTINTEG, STOPINTEG, JOBPIN) SELECT JOBNUMBER, JOBDESC, JOBRATE, JOBFLAG, STARTTIME ,STOPTIME, STARTINTEG, STOPINTEG, JOBPIN FROM " + TABLE_NAME + " WHERE JOBNUMBER =?", new String[]{jobnumber});

            return true;
        } catch (Exception ex) {
            return false;
        }

    }

    public boolean editJob(String jobnumber, String jobdesc, String jobpin, String jobhours, String jobrate, String priceQuote, String quantity) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(JOBNUMBER, jobnumber);

        contentValues.put(JOBDESC, jobdesc);
        contentValues.put(JOBPIN, jobpin);
        contentValues.put(JOBHOURS, jobhours);
        contentValues.put(JOBRATE, jobrate);
        contentValues.put(PRICE_QUOTE, priceQuote);
        contentValues.put(QUANTITY, quantity);

        db.update(TABLE_NAME, contentValues, JOBNUMBER + " = ? AND " + JOBNUMBER + " = ?",
                new String[]{jobnumber, jobnumber});

        return true;
    }


    public boolean editJob2(long id, String jobdesc, String jobpin, String jobhours, String jobrate, String priceQuote, String quantity) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(JOBDESC, jobdesc);
        contentValues.put(JOBPIN, jobpin);
        contentValues.put(JOBHOURS, jobhours);
        contentValues.put(JOBRATE, jobrate);
        contentValues.put(PRICE_QUOTE, priceQuote);
        contentValues.put(QUANTITY, quantity);



        db.update(TABLE_NAME2, contentValues, UID + " = ?",
                new String[]{id + ""});

        return true;
    }


    //  public boolean editJobTable2(String jobnumber, String jobpin, String uid, String jobdesc, String jobhours, String jobrate) {
    //
    //     ContentValues contentValues = new ContentValues();
    //     contentValues.put(UID, uid);
    //     contentValues.put(JOBNUMBER, jobnumber);
    //     contentValues.put(JOBDESC, jobdesc);
    //    contentValues.put(JOBPIN, jobpin);
    //    contentValues.put(JOBHOURS, jobhours);
    //    contentValues.put(JOBRATE, jobrate);
//
//
    //     db.update(TABLE_NAME2, contentValues, UID + " = ? AND " + UID+ " = ?",
    //          new String[]{uid, uid});
//
    //    return true;
    //   }


    public ArrayList<String> getAllJobcodes() {

        ArrayList<String> list = new ArrayList<String>();
        // Open the database for reading

        // Start the transaction.
        db.beginTransaction();


        try {

            String selectQuery = "SELECT * FROM " + TABLE_NAME;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    // Add province name to arraylist
                    String jobnumber = cursor.getString(cursor.getColumnIndex("JOBNUMBER"));
                    list.add(jobnumber);

                }


            }
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.


            // Close database
        }
        return list;


    }

    public ArrayList<String> getAllJobcodes1(String userPin,String companyId) {

        ArrayList<String> list = new ArrayList<String>();
        // Open the database for reading

        // Start the transaction.
        db.beginTransaction();


        try {
            // String selectQuery = "SELECT * FROM "+ TABLE_NAME;
            // Cursor cursor = db.rawQuery(selectQuery, null);

//            String query = "SELECT jobnumber, jobflag FROM jobnumbers WHERE jobflag = ? AND JOBPIN = ? AND COMPANY_ID = ? AND STARTTIME >= ?" + "ORDER BY STARTTIME DESC";
//            String[] selectionArgs = {"STOPPED", userPin,companyId, "DATETIME('now', '-60 day')"};

            String query = "SELECT * FROM jobnumbers WHERE jobflag = 'STOPPED' AND JOBPIN = "+userPin+" AND COMPANY_ID = '"+companyId+"' AND JOB_DATE >=  DATETIME('now', '-60 day') ORDER BY JOB_DATE DESC";
            Log.e("QUERY", query);
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    // Add province name to arraylist
                    String jobnumber = cursor.getString(cursor.getColumnIndex("JOBNUMBER"));
                    list.add(jobnumber);
                }

            }
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.


            // Close database
        }
        return list;


    }

    public ArrayList<String> getAllJobcodes2(String userPin,String companyId) {

        ArrayList<String> list = new ArrayList<String>();
        // Open the database for reading

        // Start the transaction.
        db.beginTransaction();


        try {

//            String query = "SELECT jobnumber, jobflag FROM jobnumbers WHERE jobflag = ? AND JOBPIN = ? AND COMPANY_ID = ?" + "ORDER BY STARTTIME ASC";
//            String[] selectionArgs = {"STARTED", userPin,companyId};

            String query = "SELECT jobnumber, jobflag FROM jobnumbers WHERE jobflag = 'STARTED' AND JOBPIN = "+userPin+" AND COMPANY_ID = '"+companyId+"' ORDER BY STARTTIME DESC";

//            String query = "SELECT jobnumber, jobflag FROM jobnumbers WHERE jobflag = ? AND JOBPIN = ? AND COMPANY_ID = ?" + "ORDER BY jobnumber";
//            String[] selectionArgs = {"STARTED", userPin,companyId};
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    // Add province name to arraylist
                    String jobnumber = cursor.getString(cursor.getColumnIndex("JOBNUMBER"));
                    list.add(jobnumber);
                }
            }
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.


            // Close database
        }
        return list;


    }

    public ArrayList<String> getAllJobcodes3Table2() {

        ArrayList<String> list = new ArrayList<String>();
        // Open the database for reading

        // Start the transaction.
        db.beginTransaction();


        try {

            String query = "SELECT jobnumber, jobflag FROM " + TABLE_NAME2 + " WHERE jobflag = ? OR jobflag = ?" + "ORDER BY jobnumber";
            String[] selectionArgs = {"STARTED", "STOPPED"};
            Cursor cursor = db.rawQuery(query, selectionArgs);


            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    // Add province name to arraylist
                    String jobnumber = cursor.getString(cursor.getColumnIndex("JOBNUMBER"));
                    list.add(jobnumber);

                }


            }
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.


            // Close database
        }
        return list;


    }

    public Cursor getName(String jobnumber) {

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " +
                JOBNUMBER + "=?", new String[]{jobnumber});
        return res;
    }

    public Cursor getName(String startDate, String endDate) {

        return db.rawQuery("SELECT * FROM " + TABLE_NAME2 + " WHERE (STARTTIME BETWEEN ? AND ?) AND (STOPTIME BETWEEN ? AND ?)", new String[]{startDate, endDate, startDate, endDate});
        //return db.rawQuery("SELECT * FROM Jobnumbers WHERE (STARTTIME BETWEEN strftime('%s', ?) AND strftime('%s', ?)) AND (STOPTIME BETWEEN strftime('%s', ?) AND strftime('%s', ?))", new String[]{startDate,endDate,startDate,endDate});
    }

    public Cursor getNameedit(String jobnumber) {

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " +
                JOBNUMBER + "=?", new String[]{jobnumber});
        return res;
    }

    public Cursor getTable2Name(String startDate, String endDate) {

        return db.rawQuery("SELECT * FROM " + TABLE_NAME2 + " WHERE (STARTTIME BETWEEN ? AND ?) AND (STOPTIME BETWEEN ? AND ?)", new String[]{startDate, endDate, startDate, endDate});
        //return db.rawQuery("SELECT * FROM Jobnumbers WHERE (STARTTIME BETWEEN strftime('%s', ?) AND strftime('%s', ?)) AND (STOPTIME BETWEEN strftime('%s', ?) AND strftime('%s', ?))", new String[]{startDate,endDate,startDate,endDate});
    }


    public Cursor getTable2Name(String jobnumber) {

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME2 + " WHERE " +
                JOBNUMBER + "=?", new String[]{jobnumber});
        return res;
    }

    ///////////////////////////////////edit2 BELOW//////////////////////////////////////
    public Cursor getTable2Name2(String jobnumber) {

        // Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME2 + " WHERE " +
        //        JOBNUMBER + "=?" + " AND " + UID + "=?", new String[]{jobnumber, uid});
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME2 + " WHERE " +
                JOBNUMBER + "=?", new String[]{jobnumber});
        return res;

    }


    public Cursor getTable2Name2(long id) {

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME2 + " WHERE " +
                UID + "=?", new String[]{id + ""});
        return res;
    }

///////////////////////////////////edit2 ABOVE//////////////////////////////////////


    public ArrayList<String> getAllJobcodes3(String userPin,String companyId) {

        ArrayList<String> list = new ArrayList<String>();
        // Open the database for reading

        // Start the transaction.
        db.beginTransaction();


        try {

//            String query = "SELECT jobnumber, jobflag FROM jobnumbers WHERE jobflag = ? OR JOBPIN = ? AND COMPANY_ID = ?" + "ORDER BY jobnumber";
//            String[] selectionArgs = {"STARTED", userPin,companyId};

//            String query = "SELECT jobnumber, jobflag FROM jobnumbers WHERE JOBPIN = ? AND COMPANY_ID = ? AND STARTTIME >= ?" + "ORDER BY STARTTIME DESC";
//            String[] selectionArgs = {userPin,companyId,"DATETIME('now', '-60 day')"};

            String query = "SELECT jobnumber, jobflag FROM jobnumbers WHERE JOBPIN = "+userPin+" AND COMPANY_ID = '"+companyId+"' AND JOB_DATE >=  DATETIME('now', '-60 day') ORDER BY JOB_DATE DESC";

            Cursor cursor = db.rawQuery(query, null);


            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    // Add province name to arraylist
                    String jobnumber = cursor.getString(cursor.getColumnIndex("JOBNUMBER"));
                    list.add(jobnumber);

                }


            }
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.


            // Close database
        }
        return list;


    }


    public ArrayList<String> getAllJobcodes1ForTable2() {

        ArrayList<String> list = new ArrayList<String>();
        // Open the database for reading

        // Start the transaction.
        db.beginTransaction();


        try {

            // String selectQuery = "SELECT * FROM "+ TABLE_NAME;
            // Cursor cursor = db.rawQuery(selectQuery, null);

            String query = "SELECT jobnumber, jobflag FROM " + TABLE_NAME2 + " WHERE jobflag = ? OR jobflag = ?" + "ORDER BY jobnumber, uid";
            String[] selectionArgs = {"STARTED", "STOPPED"};
            Cursor cursor = db.rawQuery(query, selectionArgs);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    // Add province name to arraylist
                    String jobnumber = cursor.getString(cursor.getColumnIndex("JOBNUMBER"));
                    list.add(jobnumber);

                }


            }
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.


            // Close database
        }
        return list;


    }

    public JobLog getJobLog() {


        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME2 + " ORDER BY " + UID + " DESC LIMIT 1", new String[]{});
        cursor.moveToFirst();
        JobLog jobLog = new JobLog(
                cursor.getString(cursor.getColumnIndex(JOBNUMBER)),
                cursor.getString(cursor.getColumnIndex(JOBDESC)),
                cursor.getString(cursor.getColumnIndex(JOBRATE)),
                cursor.getString(cursor.getColumnIndex(JOBFLAG)),
                cursor.getString(cursor.getColumnIndex(JOBPIN)),
                cursor.getString(cursor.getColumnIndex(UID)),
                cursor.getString(cursor.getColumnIndex(JOBHOURS)),
                cursor.getString(cursor.getColumnIndex(STARTTIME)),
                cursor.getString(cursor.getColumnIndex(STOPTIME)),
                cursor.getString(cursor.getColumnIndex(STARTINTEG)),
                cursor.getString(cursor.getColumnIndex(STOPINTEG)),
                cursor.getString(cursor.getColumnIndex(START_LATITUDE)),
                cursor.getString(cursor.getColumnIndex(START_LONGITUDE)),
                cursor.getString(cursor.getColumnIndex(STOP_LATITUDE)),
                cursor.getString(cursor.getColumnIndex(STOP_LONGITUDE)),
                cursor.getString(cursor.getColumnIndex(PRICE_QUOTE)),
                cursor.getString(cursor.getColumnIndex(QUANTITY)),
                cursor.getString(cursor.getColumnIndex(PDF_FILE_NAME)),
                cursor.getString(cursor.getColumnIndex(IMAGE_FILE_NAME)),
                cursor.getString(cursor.getColumnIndex(IMAGE)),
                cursor.getString(cursor.getColumnIndex(COMPANY_ID)),
                cursor.getString(cursor.getColumnIndex(JOB_DATE))

        );
        return jobLog;
    }


    public void pergeAllJobLogs() {
        db.delete(TABLE_NAME2, null, null);
    }

    public void addJobLog(JobLog jobLog) {
        try {


            ContentValues contentValues = new ContentValues();
            contentValues.put(JOBNUMBER, jobLog.getJobNumber());
            contentValues.put(JOBDESC, jobLog.getJobDesc());
            contentValues.put(JOBRATE, jobLog.getJobRate());
            contentValues.put(JOBFLAG, jobLog.getJobFlag());
            contentValues.put(JOBPIN, jobLog.getJobPin());
            contentValues.put(UID, jobLog.getUid());
            contentValues.put(JOBHOURS, jobLog.getJob_hours());
            contentValues.put(STARTTIME, jobLog.getStart_time());
            contentValues.put(STOPTIME, jobLog.getStop_time());
            contentValues.put(STARTINTEG, jobLog.getStarting_teg());
            contentValues.put(STOPINTEG, jobLog.getStoping_teg());
            contentValues.put(START_LATITUDE, jobLog.getStart_latitude());
            contentValues.put(START_LONGITUDE, jobLog.getStart_longitude());
            contentValues.put(STOP_LATITUDE, jobLog.getStop_latitude());
            contentValues.put(STOP_LONGITUDE, jobLog.getStop_longitude());
            contentValues.put(PDF_FILE_NAME, jobLog.getPdfFileName());

            contentValues.put(IMAGE_FILE_NAME, jobLog.getImageFileName());
            contentValues.put(IMAGE, jobLog.getImageUrl());

            contentValues.put(COMPANY_ID, jobLog.getCompanyId());

            contentValues.put(PRICE_QUOTE, jobLog.getPriceQuote());
            contentValues.put(QUANTITY, jobLog.getQuantity());
            contentValues.put(JOB_DATE, jobLog.getJobDate());



            long result = db.insert(TABLE_NAME2, null, contentValues);
            if (result == -1)
                updateJobLog(jobLog);
        } catch (Exception ex) {

        }

    }

    private void updateJobLog(JobLog jobLog) {
        try {

            ContentValues contentValues = new ContentValues();
            contentValues.put(JOBDESC, jobLog.getJobDesc());
            contentValues.put(JOBPIN, jobLog.getJobPin());
            contentValues.put(JOBHOURS, jobLog.getJob_hours());
            contentValues.put(JOBRATE, jobLog.getJobRate());

            db.update(TABLE_NAME2, contentValues, JOBNUMBER + " = ? AND " + UID + " = ?",
                    new String[]{jobLog.getJobNumber(), jobLog.getUid()});
        } catch (Exception ex) {

        }
    }

    public void pergeAllJobs() {
        db.delete(TABLE_NAME, null, null);
    }

    public void addJob(Job job) {
        try {

            ContentValues contentValues = new ContentValues();
            contentValues.put(JOBNUMBER, job.getJobNumber());
            contentValues.put(JOBDESC, job.getJobDesc());
            contentValues.put(JOBPIN, job.getJobPin());
            contentValues.put(JOBRATE, job.getJobRate());

            contentValues.put(JOBHOURS, job.getJob_hours());
            contentValues.put(JOBFLAG, job.getJobFlag());

            contentValues.put(STARTTIME, job.getStart_time());
            contentValues.put(STOPTIME, job.getStop_time());

            contentValues.put(STARTINTEG, job.getStarting_teg());
            contentValues.put(STOPINTEG, job.getStoping_teg());

            contentValues.put(START_LATITUDE, job.getStart_latitude());
            contentValues.put(START_LONGITUDE, job.getStart_longitude());

            contentValues.put(PDF, job.getPdfUrl());
            contentValues.put(PDF_FILE_NAME, job.getPdfFileName());

            contentValues.put(COMPANY_ID, job.getCompanyId());

            contentValues.put(PRICE_QUOTE, job.getPriceQuote());
            contentValues.put(QUANTITY, job.getQuantity());
            contentValues.put(JOB_ADDRESS, job.getAdress());
            contentValues.put(JOB_DATE, job.getJobDate());


            long result = db.insert(TABLE_NAME, null, contentValues);
            if (result == -1)
                updateJob(job);
        } catch (Exception ex) {

        }

    }

    private void updateJob(Job job) {
        try {

            ContentValues contentValues = new ContentValues();
            contentValues.put(JOBDESC, job.getJobDesc());
            contentValues.put(JOBPIN, job.getJobPin());
            contentValues.put(JOBHOURS, job.getJob_hours());
            contentValues.put(JOBRATE, job.getJobRate());
            contentValues.put(JOBFLAG, job.getJobFlag());

            contentValues.put(STARTTIME, job.getStart_time());
            contentValues.put(STOPTIME, job.getStop_time());


            int result = db.update(TABLE_NAME, contentValues, JOBNUMBER + " = ? ",
                    new String[]{job.getJobNumber()});
            Log.e(TAG, "ex.getLocalizedMessage()");
        } catch (Exception ex) {
            Log.e(TAG, ex.getLocalizedMessage());
        }
    }

    public Job getJob(String jobNumber) {

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " +
                JOBNUMBER + "=?", new String[]{jobNumber});
        cursor.moveToFirst();
        Job jobLog = new Job(
                cursor.getString(cursor.getColumnIndex(JOBNUMBER)),
                cursor.getString(cursor.getColumnIndex(JOBDESC)),
                cursor.getString(cursor.getColumnIndex(JOBRATE)),
                cursor.getString(cursor.getColumnIndex(JOBFLAG)),
                cursor.getString(cursor.getColumnIndex(JOBPIN)),
                cursor.getString(cursor.getColumnIndex(JOBHOURS)),
                cursor.getString(cursor.getColumnIndex(STARTTIME)),
                cursor.getString(cursor.getColumnIndex(STOPTIME)),
                cursor.getString(cursor.getColumnIndex(STARTINTEG)),
                cursor.getString(cursor.getColumnIndex(STOPINTEG)),
                cursor.getString(cursor.getColumnIndex(START_LATITUDE)),
                cursor.getString(cursor.getColumnIndex(START_LONGITUDE)),
                cursor.getString(cursor.getColumnIndex(PRICE_QUOTE)),
                cursor.getString(cursor.getColumnIndex(QUANTITY)),
                cursor.getString(cursor.getColumnIndex(PDF)),
                cursor.getString(cursor.getColumnIndex(PDF_FILE_NAME)),
                cursor.getString(cursor.getColumnIndex(COMPANY_ID)),
                cursor.getString(cursor.getColumnIndex(JOB_ADDRESS)),
                cursor.getString(cursor.getColumnIndex(JOB_DATE))

        );
        return jobLog;
    }

    public JobLog getJobLog(String uid) {


        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME2 + " WHERE " +
                UID + "=?", new String[]{uid});
        cursor.moveToFirst();
        JobLog jobLog = new JobLog(
                cursor.getString(cursor.getColumnIndex(JOBNUMBER)),
                cursor.getString(cursor.getColumnIndex(JOBDESC)),
                cursor.getString(cursor.getColumnIndex(JOBRATE)),
                cursor.getString(cursor.getColumnIndex(JOBFLAG)),
                cursor.getString(cursor.getColumnIndex(JOBPIN)),
                cursor.getString(cursor.getColumnIndex(JOBHOURS)),
                cursor.getString(cursor.getColumnIndex(STARTTIME)),
                cursor.getString(cursor.getColumnIndex(STOPTIME)),
                cursor.getString(cursor.getColumnIndex(STARTINTEG)),
                cursor.getString(cursor.getColumnIndex(STOPINTEG)),
                cursor.getString(cursor.getColumnIndex(UID)),
                cursor.getString(cursor.getColumnIndex(START_LATITUDE)),
                cursor.getString(cursor.getColumnIndex(START_LONGITUDE)),
                cursor.getString(cursor.getColumnIndex(STOP_LATITUDE)),
                cursor.getString(cursor.getColumnIndex(STOP_LONGITUDE)),
                cursor.getString(cursor.getColumnIndex(PRICE_QUOTE)),
                cursor.getString(cursor.getColumnIndex(QUANTITY)),
                cursor.getString(cursor.getColumnIndex(PDF_FILE_NAME)),
                cursor.getString(cursor.getColumnIndex(IMAGE_FILE_NAME)),
                cursor.getString(cursor.getColumnIndex(IMAGE)),
                cursor.getString(cursor.getColumnIndex(COMPANY_ID)),
                cursor.getString(cursor.getColumnIndex(JOB_DATE))

        );
        return jobLog;
    }
}

