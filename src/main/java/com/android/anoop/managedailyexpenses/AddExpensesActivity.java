package com.android.anoop.managedailyexpenses;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddExpensesActivity extends AppCompatActivity {
    EditText mEditTextItemName,mEditTextItemQuantity,mEditTextItemCost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenses);
        mEditTextItemName = (EditText)findViewById(R.id.editTextItemName);
        mEditTextItemCost = (EditText)findViewById(R.id.editTextItemCost);
        mEditTextItemQuantity = (EditText)findViewById(R.id.editTextItemQuantity);
    }
    public void addExpenses2(View view){
        String itemName = mEditTextItemName.getText().toString().trim();
        String itemQuantity = mEditTextItemQuantity.getText().toString().trim();
        String stringCost = mEditTextItemCost.getText().toString().trim();

        if(itemName.isEmpty()){
            mEditTextItemName.requestFocus();
            mEditTextItemName.setError("Enter Item Name");
        }else if(itemQuantity.isEmpty()){
            mEditTextItemQuantity.requestFocus();
            mEditTextItemQuantity.setError("Enter Quantity");
        }else if(stringCost.isEmpty()){
            mEditTextItemCost.requestFocus();
            mEditTextItemCost.setError("Enter Cost");
        }else{
            double cost = Double.parseDouble(stringCost);
            MyDatabase mdb = new MyDatabase(this);
            SQLiteDatabase db = mdb.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(MyDatabase.TABLE_DAILY_EXPENSES_COL2,itemName);
            cv.put(MyDatabase.TABLE_DAILY_EXPENSES_COL3,itemQuantity);
            cv.put(MyDatabase.TABLE_DAILY_EXPENSES_COL4,cost);

            long result = db.insert(MyDatabase.TABLE_DAILY_EXPENSES,null,cv);
            if(result != -1){
                mEditTextItemName.setText("");
                mEditTextItemQuantity.setText("");
                mEditTextItemCost.setText("");
                mEditTextItemName.requestFocus();
                Toast.makeText(this,"Item added",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Some error occured ,item not added . Again add",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void backToHome(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
