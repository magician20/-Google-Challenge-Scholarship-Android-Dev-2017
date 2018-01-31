package com.example.android.waitlist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.waitlist.data.WaitlistContract.*;

//  (1) extend the SQLiteOpenHelper class
public class WaitlistDbHelper extends SQLiteOpenHelper {
    //  (2) Create a static final String called DATABASE_NAME and set it to "waitlist.db"
    static final String DATABASE_NAME = "waitlist.db";
    //  (3) Create a static final int called DATABASE_VERSION and set it to 1
    static final int DATABASE_VERSION = 1;
    //  (4) Create a Constructor that takes a context and calls the parent constructor

    public WaitlistDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);//create the DB
    }

    //  (5) Override the onCreate method
    //  (6) Inside, create an String query called SQL_CREATE_WAITLIST_TABLE that will create the table
    //  (7) Execute the query by calling execSQL on sqLiteDatabase and pass the string query SQL_CREATE_WAITLIST_TABLE

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE" + WaitlistEntry.TABLE_NAME +
                "("
                + WaitlistEntry._ID + "INTEGER PRIMARY KEY AUTOINCREMENT,"
                + WaitlistEntry.COLUMN_GUEST_NAME + "TEXT NOT NULL,"
                + WaitlistEntry.COLUMN_PARTY_SIZE + "INTEGER  NOT NULL,"
                + WaitlistEntry.COLUMN_TIMESTAMP + "TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                + ");";
        db.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    //  (8) Override the onUpgrade method
    //  (9) Inside, execute a drop table query, and then call onCreate to re-create it
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //NO CHANGE ON STRUCTURE
        final String SQL_DROP_WAITLIST_TABLE = "DROP TABLE IF EXISTS" + WaitlistEntry.TABLE_NAME;
        db.execSQL(SQL_DROP_WAITLIST_TABLE);
        onCreate(db);
    }


}