package com.android.anoop.managedailyexpenses;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class AdvancePaymentActivity extends AppCompatActivity {
    Spinner mSpinnerMemberContact;
    EditText mEditTextAdvancePayment,mEditTextMemberName;
    ArrayList<String> al ;
    String member="xyz";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_payment);
        mSpinnerMemberContact = (Spinner)findViewById(R.id.spinnerMembersContact);
        mEditTextAdvancePayment = (EditText) findViewById(R.id.editTextAdvancePayment);
        mEditTextMemberName = (EditText) findViewById(R.id.editTextMemberName);
        mEditTextMemberName.setEnabled(false);
        al = new ArrayList<String>();
        al.add("Select Member's contact");

        //fetching members from database
        MyDatabase mdb = new MyDatabase(this);
        SQLiteDatabase db = mdb.getWritableDatabase();

        String cols[] = {MyDatabase.TABLE_MEMBER_COL2};
        Cursor cursor = db.query(MyDatabase.TABLE_MEMBER,cols,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                al.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,al);
        mSpinnerMemberContact.setAdapter(aa);
        mSpinnerMemberContact.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MyDatabase myDatabase = new MyDatabase(AdvancePaymentActivity.this);
                SQLiteDatabase sqLiteDatabase = myDatabase.getWritableDatabase();

                //fetch member
                String col[]={MyDatabase.TABLE_MEMBER_COL1};
                String selection=MyDatabase.TABLE_MEMBER_COL2+"=?";
                String selectionArgs[]={al.get(i)};
                Cursor cursor = sqLiteDatabase.query(MyDatabase.TABLE_MEMBER,col,selection,selectionArgs,null,null,null,null);
                if(cursor.moveToFirst()){
                    member=cursor.getString(0);
                    mEditTextMemberName.setText(member);
                }else{

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void addAdvancePayment(View view){
        String membersContact = mSpinnerMemberContact.getSelectedItem().toString().trim();
        String stringAdvancePayment = mEditTextAdvancePayment.getText().toString().trim();

        if(membersContact.equals("Select Member's contact")){
            Toast t = Toast.makeText(this,"Select Member's Contact",Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER,0,20);
            t.show();
        }else if(stringAdvancePayment.isEmpty()){
            mEditTextAdvancePayment.requestFocus();
            mEditTextAdvancePayment.setError("Enter Advance Payment");
        } else{
            long advancePayment = Long.parseLong(stringAdvancePayment);
            long contact = Long.parseLong(membersContact);

            //database work
            MyDatabase myDatabase = new MyDatabase(this);
            SQLiteDatabase sqLiteDatabase = myDatabase.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(MyDatabase.TABLE_ADVANCE_PAYMENT_COL1,member);
            cv.put(MyDatabase.TABLE_ADVANCE_PAYMENT_COL2,contact);
            cv.put(MyDatabase.TABLE_ADVANCE_PAYMENT_COL3,advancePayment);

            Long result = sqLiteDatabase.insert(MyDatabase.TABLE_ADVANCE_PAYMENT,null,cv);
            if(result != -1){
                mEditTextAdvancePayment.setText("");
                mEditTextAdvancePayment.requestFocus();
                Toast.makeText(this,"Your Advance Payment Added",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Some error occurred so payment not added again try",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void backToHome(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
