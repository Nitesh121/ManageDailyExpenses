package com.android.anoop.managedailyexpenses;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ViewExpensesActivity extends AppCompatActivity {
    TextView mTextViewTotalExpenses,mTextViewTotalMember,mTextViewEachCost;

    String message="";

    double totalExpenses=0.0;
    double eachCost = 0.0;
    int totalMember = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expenses);
        mTextViewTotalExpenses = (TextView)findViewById(R.id.textViewTotalExpenses2);
        mTextViewTotalMember = (TextView)findViewById(R.id.textViewTotalMember2);
        mTextViewEachCost = (TextView)findViewById(R.id.textViewEachCost2);

        //fetching data from table
        MyDatabase mdb = new MyDatabase(this);
        SQLiteDatabase db = mdb.getWritableDatabase();

        //finding total member
        String cols1[] = {MyDatabase.TABLE_MEMBER_COL1};
        Cursor cursorMember = db.query(MyDatabase.TABLE_MEMBER,cols1,null,null,null,null,null);
        if(cursorMember.moveToFirst()){
            do{
                totalMember += 1;
            }while (cursorMember.moveToNext());
        }

        //finding total expenses
        String cols2[] = {MyDatabase.TABLE_DAILY_EXPENSES_COL4};
        Cursor cursorExpenses = db.query(MyDatabase.TABLE_DAILY_EXPENSES,cols2,null,null,null,null,null);
        if(cursorExpenses.moveToFirst()){
            do{
                totalExpenses += cursorExpenses.getDouble(0);
            }while (cursorExpenses.moveToNext());
        }

        //finding each cost
        eachCost = (totalExpenses/totalMember);

        //put data in textView
        mTextViewTotalExpenses.setText(totalExpenses+" Rs");
        mTextViewTotalMember.setText(totalMember+"");
        mTextViewEachCost.setText(eachCost+" Rs");

        message ="Total Expenses: "+totalExpenses+"\nTotal Member: "+totalMember+"\nEach Cost:"+eachCost;
    }
    public void sendMessage(View view){
        SmsManager smsManager = SmsManager.getDefault();

        //finding contact from database
        MyDatabase mdb = new MyDatabase(this);
        SQLiteDatabase db = mdb.getWritableDatabase();

        String cols[] = {MyDatabase.TABLE_MEMBER_COL2};
        Cursor cursor = db.query(MyDatabase.TABLE_MEMBER,cols,null,null,null,null,null);
        //sending message to every member
        if(cursor.moveToFirst()){
            do{
                long cno = cursor.getLong(0);
                String stringContact = cno+"";
                smsManager.sendTextMessage(stringContact,null,message,null,null);
            }while (cursor.moveToNext());
        }else{
            Toast.makeText(this,"Members Not Available",Toast.LENGTH_SHORT).show();
        }

    }
    public void backToHome(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
