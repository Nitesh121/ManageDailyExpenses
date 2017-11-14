package com.android.anoop.managedailyexpenses;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddMemberActivity extends AppCompatActivity {
    TextInputLayout mTextInputLayoutMemberName,mTextInputLayoutMemberContact,mTextInputLayoutMemberAddress;
    EditText mEditTextMemberName,mEditTextMemberContact,mEditTextMemberAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        mTextInputLayoutMemberName = (TextInputLayout)findViewById(R.id.textInputLayoutMemberName);
        mTextInputLayoutMemberContact = (TextInputLayout)findViewById(R.id.textInputLayoutMemberContact);
        mTextInputLayoutMemberAddress = (TextInputLayout)findViewById(R.id.textInputLayoutMemberAddress);

        mEditTextMemberName = (EditText)findViewById(R.id.editTextMemberName);
        mEditTextMemberContact = (EditText)findViewById(R.id.editTextMemberContact);
        mEditTextMemberAddress = (EditText)findViewById(R.id.editTextMemberAddress);
    }

    public void saveMemberDetails(View view){
        String name = mEditTextMemberName.getText().toString().trim();
        String stringContact = mEditTextMemberContact.getText().toString().trim();
        String address = mEditTextMemberAddress.getText().toString().trim();

        if(name.isEmpty()){
            mEditTextMemberName.requestFocus();
            mTextInputLayoutMemberName.setError("Enter Name");
        }else if(stringContact.isEmpty()){
            mTextInputLayoutMemberName.setError("");
            mEditTextMemberContact.requestFocus();
            mTextInputLayoutMemberContact.setError("Enter Contact");
        }else if(stringContact.length()!=10){
            mTextInputLayoutMemberName.setError("");
            mEditTextMemberContact.requestFocus();
            mTextInputLayoutMemberContact.setError("Contact should be of 10 digit");
        }else if(address.isEmpty()){
            mTextInputLayoutMemberContact.setError("");
            mEditTextMemberAddress.requestFocus();
            mTextInputLayoutMemberAddress.setError("Enter Address");
        }else{
            mTextInputLayoutMemberContact.setError("");
            mTextInputLayoutMemberName.setError("");
            mTextInputLayoutMemberAddress.setError("");

            long contact = Long.parseLong(stringContact);
            //database work
            MyDatabase mdb = new MyDatabase(this);
            SQLiteDatabase sqLiteDatabase = mdb.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(MyDatabase.TABLE_MEMBER_COL1,name);
            cv.put(MyDatabase.TABLE_MEMBER_COL2,contact);
            cv.put(MyDatabase.TABLE_MEMBER_COL3,address);

            long result = sqLiteDatabase.insert(MyDatabase.TABLE_MEMBER,null,cv);
            if(result!=-1){
                mEditTextMemberName.setText("");
                mEditTextMemberContact.setText("");
                mEditTextMemberAddress.setText("");
                mEditTextMemberName.requestFocus();
                Toast.makeText(this,"New Member Added",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Error in adding new Member, Contact should be unique",Toast.LENGTH_SHORT).show();
            }
        }

    }
    public void backToHome(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
