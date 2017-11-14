package com.android.anoop.managedailyexpenses;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by anoop on 6/25/2017.
 */

public class MyDatabase extends SQLiteOpenHelper {
    Context context;

    public static final String DB_NAME = "daily_expenses_database";
    public static final int VERSION = 1;

    //table 1
    public static final String TABLE_MEMBER = "member";
    public static final String TABLE_MEMBER_COL1 = "member_name";
    public static final String TABLE_MEMBER_COL2 = "contact";
    public static final String TABLE_MEMBER_COL3 = "address";

    //table 2
    public static final String TABLE_DAILY_EXPENSES = "daily_expenses";
    public static final String TABLE_DAILY_EXPENSES_COL1 = "item_number";
    public static final String TABLE_DAILY_EXPENSES_COL2 = "item_name";
    public static final String TABLE_DAILY_EXPENSES_COL3 = "item_quantity";
    public static final String TABLE_DAILY_EXPENSES_COL4 = "amount";

    //table 3
    public static final String TABLE_ADVANCE_PAYMENT = "advance_payment";
    public static final String TABLE_ADVANCE_PAYMENT_COL1 = "member_name";
    public static final String TABLE_ADVANCE_PAYMENT_COL2 = "contact";
    public static final String TABLE_ADVANCE_PAYMENT_COL3 = "amount";


    public MyDatabase(Context context){
        super(context,DB_NAME,null,VERSION);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String qry1 = "create table "+TABLE_MEMBER+" ("+TABLE_MEMBER_COL1+" text,"+TABLE_MEMBER_COL2+" integer primary key,"+TABLE_MEMBER_COL3+" text)";
        String qry2 = "create table "+TABLE_DAILY_EXPENSES+" ("+TABLE_DAILY_EXPENSES_COL1+" integer primary key autoincrement,"+TABLE_DAILY_EXPENSES_COL2+" text,"+TABLE_DAILY_EXPENSES_COL3+" text,"+TABLE_DAILY_EXPENSES_COL4+" real)";
        String qry3 = "create table "+TABLE_ADVANCE_PAYMENT+" ("+TABLE_ADVANCE_PAYMENT_COL1+" text,"+TABLE_ADVANCE_PAYMENT_COL2+" integer,"+TABLE_ADVANCE_PAYMENT_COL3+" real)";

        try{
            sqLiteDatabase.execSQL(qry1);
            sqLiteDatabase.execSQL(qry2);
            sqLiteDatabase.execSQL(qry3);
            Toast.makeText(context,"Table's created",Toast.LENGTH_SHORT).show();
        }catch (SQLException e){
            Toast.makeText(context,"SQLException occur,Check string query",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
