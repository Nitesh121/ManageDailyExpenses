package com.android.anoop.managedailyexpenses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {
    TextView mTextViewName,mTextViewPhone,mTextViewAdvance;
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mTextViewName = (TextView)findViewById(R.id.profileTextViewName);
        mTextViewPhone = (TextView)findViewById(R.id.profileTextViewPhone);
        mTextViewAdvance = (TextView)findViewById(R.id.profileTextViewAdvance);
        sp=getSharedPreferences("login",MODE_PRIVATE);
        spe=sp.edit();
        String phone=sp.getString("phone","NoPhone");
        if(phone.equals("NoPhone")){
            Toast.makeText(this,"May be you not login again try",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            long advance = 0l;
            String name="";
            MyDatabase mdb = new MyDatabase(this);
            SQLiteDatabase sqLiteDatabase = mdb.getReadableDatabase();
            //get member name
            String cols1[]={MyDatabase.TABLE_MEMBER_COL1};
            String selection1=MyDatabase.TABLE_MEMBER_COL2+"=?";
            String selectionArgs1[]={phone};
            Cursor cursor1=sqLiteDatabase.query(MyDatabase.TABLE_MEMBER,cols1,selection1,selectionArgs1,null,null,null);
            if(cursor1.moveToFirst()){
                name=cursor1.getString(0);
            }
            //get total advance
            String cols[]={MyDatabase.TABLE_ADVANCE_PAYMENT_COL3};
            String selection=MyDatabase.TABLE_MEMBER_COL2+"=?";
            String selectionArgs[]={phone};
            Cursor cursor = sqLiteDatabase.query(MyDatabase.TABLE_ADVANCE_PAYMENT,cols,selection,selectionArgs,null,null,null);
            if(cursor.moveToFirst()){
                do{
                    advance+=cursor.getLong(0);
                }while (cursor.moveToNext());

            }
            //put data in TextView
            mTextViewName.setText(name);
            mTextViewPhone.setText(phone);
            mTextViewAdvance.setText(advance+"");
        }

    }
    public void backToHome(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
