package com.android.anoop.managedailyexpenses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText mEditTextPhoneNumber;
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEditTextPhoneNumber = (EditText)findViewById(R.id.editTextToInputPhoneNumber);
        sp = getSharedPreferences("login",MODE_PRIVATE);
        spe = sp.edit();
    }

    public void signIn(View view){
           String stringContact = mEditTextPhoneNumber.getText().toString().trim();
           if(stringContact.isEmpty()){
               mEditTextPhoneNumber.setError("Enter Phone Number");
           }else if(stringContact.length()<10){
               mEditTextPhoneNumber.setError("Phone number must be of 10 digit");
           }else{
               MyDatabase md=new MyDatabase(this);
               SQLiteDatabase sqLiteDatabase = md.getWritableDatabase();
               String col[]={MyDatabase.TABLE_MEMBER_COL1};
               String selection = MyDatabase.TABLE_MEMBER_COL2+"=?";
               String selectionArgs[] = {stringContact};
               Cursor cursor = sqLiteDatabase.query(MyDatabase.TABLE_MEMBER,col,selection,selectionArgs,null,null,null);
               if(cursor.moveToFirst()){
                   spe.putString("phone",stringContact);
                   spe.commit();
                   Toast.makeText(this, "Welcome "+cursor.getString(0), Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(this,MainActivity.class);
                   startActivity(intent);
                   finish();
               }else{
                   Toast.makeText(this,"Invalid Phone Number",Toast.LENGTH_SHORT).show();
               }
           }
    }
    public void backToHome(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
