package com.android.anoop.managedailyexpenses;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String contact;//Members Contact number
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences("login",MODE_PRIVATE);
        spe = sp.edit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        String phone = sp.getString("phone","PhoneNumberNotAvailable");
        if(phone.equals("PhoneNumberNotAvailable")){
             MenuInflater menuInflater = getMenuInflater();
             menuInflater.inflate(R.menu.member_login,menu);
        }else{
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.member_logout,menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch (id){
            case R.id.memberLogin:Intent intent = new Intent(this,LoginActivity.class);
                                  startActivity(intent);
                                  finish();
                                  break;
            case R.id.memberLogout: spe.putString("phone","PhoneNumberNotAvailable");
                                    spe.commit();
                                    Intent intent2 = new Intent(this,MainActivity.class);
                                    startActivity(intent2);
                                    finish();
                                    break;
            case R.id.memberProfile:Intent intent3 = new Intent(this,ProfileActivity.class);
                                    startActivity(intent3);
                                    finish();
        }
        return true;
    }

    public void addMember(View view){
        Intent intent = new Intent(this,AddMemberActivity.class);
        startActivity(intent);
    }

    public void removeMember(View view){
        if(isMember()) {
            Toast.makeText(this, "Remove Member", Toast.LENGTH_SHORT).show();
            Spinner spinner = new Spinner(this);
            final ArrayList<String> arrayList = new ArrayList<String>();

            //fetching Members contact From Database
            MyDatabase myDatabase = new MyDatabase(this);
            SQLiteDatabase sqLiteDatabase = myDatabase.getWritableDatabase();

            String cols[] = {MyDatabase.TABLE_MEMBER_COL2};

            Cursor cursor = sqLiteDatabase.query(MyDatabase.TABLE_MEMBER, cols, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    arrayList.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, arrayList);
            spinner.setAdapter(aa);

            //get selected item from spinner
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    contact = arrayList.get(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            //Creating DialogBox and add spinner
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Member Contact");
            builder.setView(spinner);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    _deleteMember(contact);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.setCancelable(false);
            builder.show();
        }else{
            showDialogForZeroMember();
        }
    }
    public void _deleteMember(final String stringContact){
        double totalExpenses=0.0;
        double eachCost = 0.0;
        int yourAdvance = 0;//Member Advance
        int totalMember = 0;
        double yourBalance = 0.0;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your Balance");

        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.expenses_detail,null);

        //fetching data from table
        MyDatabase mdb = new MyDatabase(this);
        final SQLiteDatabase db = mdb.getWritableDatabase();

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

        //finding advance
        String cols[] = {MyDatabase.TABLE_ADVANCE_PAYMENT_COL3};
        String selection=MyDatabase.TABLE_ADVANCE_PAYMENT_COL2+"=?";
        String[] selectionArgs={stringContact};
        Cursor cursorAdvancePayment = db.query(MyDatabase.TABLE_ADVANCE_PAYMENT,cols,selection,selectionArgs,null,null,null);
        if(cursorAdvancePayment.moveToFirst()){
            do{
                yourAdvance += cursorAdvancePayment.getDouble(0);
            }while (cursorAdvancePayment.moveToNext());
        }

        //finding each cost
        eachCost = (totalExpenses/totalMember);

        //finding balance of member
        yourBalance = yourAdvance-eachCost;


        //adding data in dialog textView
        TextView textViewTotalExpenses = (TextView)v.findViewById(R.id.textViewTotalExpenses);
        TextView textViewTotalMember = (TextView)v.findViewById(R.id.textViewTotalMember);
        TextView textViewEachCost = (TextView)v.findViewById(R.id.textViewEachCost);
        TextView textViewYourAdvance = (TextView)v.findViewById(R.id.textViewYourAdvance);
        TextView textViewBalance = (TextView)v.findViewById(R.id.textViewBalance);

        textViewTotalExpenses.setText(totalExpenses+" Rs");
        textViewTotalMember.setText(totalMember+"");
        textViewEachCost.setText(eachCost+" Rs");
        textViewYourAdvance.setText(yourAdvance+" Rs");
        textViewBalance.setText(yourBalance+" Rs");

        builder.setView(v);
        builder.setPositiveButton("Remove Member", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                  String whereClause=MyDatabase.TABLE_MEMBER_COL2+"=?";
                  String whereClause2=MyDatabase.TABLE_ADVANCE_PAYMENT_COL2+"=?";
                  String[] whereArgs={stringContact};
                  int result=db.delete(MyDatabase.TABLE_MEMBER,whereClause,whereArgs);
                  int result2=db.delete(MyDatabase.TABLE_ADVANCE_PAYMENT,whereClause2,whereArgs);
                  if(result>0 && result2>=0)
                      Toast.makeText(MainActivity.this,"Record Deleted",Toast.LENGTH_SHORT).show();
                  else
                      Toast.makeText(MainActivity.this,"Some error in record deletion again try",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();

    }

    public void clearAllExpenses(View view){
        if(isExpenses()){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure, delete all expenses");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabase md=new MyDatabase(MainActivity.this);
                SQLiteDatabase db=md.getWritableDatabase();
                int result = db.delete(MyDatabase.TABLE_DAILY_EXPENSES,null,null);
                Toast.makeText(MainActivity.this,"All expenses deleted",Toast.LENGTH_SHORT).show();
            }
        }) ;
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
       builder.setCancelable(false);
       builder.show();
        }else {
            Toast.makeText(MainActivity.this,"No expenses till now",Toast.LENGTH_SHORT).show();
        }
    }

    public void clearAdvance(View view){
        if(isAdvance()){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure, delete all advance");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabase md=new MyDatabase(MainActivity.this);
                SQLiteDatabase db=md.getWritableDatabase();
                int result = db.delete(MyDatabase.TABLE_ADVANCE_PAYMENT,null,null);
                Toast.makeText(MainActivity.this,"All advance deleted",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
        }else{
            Toast.makeText(MainActivity.this,"Advances are not available",Toast.LENGTH_SHORT).show();
        }
    }

    public void addExpenses(View view){
        if(isMember()){
        Toast.makeText(this,"add expenses",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,AddExpensesActivity.class);
        startActivity(intent);
        }else{
            showDialogForZeroMember();
        }
    }

    public void membersDetail(View view){
       if(isMember()){
        //Creating spinner

        Spinner spinner = new Spinner(this);
        final ArrayList<String> arrayList = new ArrayList<String>();

        //fetching Members contact From Database
        MyDatabase myDatabase = new MyDatabase(this);
        SQLiteDatabase sqLiteDatabase = myDatabase.getWritableDatabase();

        String cols[] = {MyDatabase.TABLE_MEMBER_COL2};

        Cursor cursor = sqLiteDatabase.query(MyDatabase.TABLE_MEMBER,cols,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                arrayList.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,arrayList);
        spinner.setAdapter(aa);

        //get selected item from spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                contact = arrayList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Creating DialogBox and add spinner
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Select Member Contact");
        builder.setView(spinner);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showExpenses(contact);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
       }else {
           showDialogForZeroMember();
       }
    }

    public void addAdvance(View view){
        if(isMember()){
        Intent intent = new Intent(this,AdvancePaymentActivity.class);
        startActivity(intent);
        }else {
            showDialogForZeroMember();
        }
    }

    public void viewExpenses(View view){
        if(isExpenses()){
        Intent intent = new Intent(this,ViewExpensesActivity.class);
        startActivity(intent);
        }else{
            Toast.makeText(this,"No Expenses are here",Toast.LENGTH_SHORT).show();
        }
    }

    public void showExpenses(String stringContact){
        if(isExpenses()){
        double totalExpenses=0.0;
        double eachCost = 0.0;
        int yourAdvance = 0;//Member Advance
        int totalMember = 0;
        double yourBalance = 0.0;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Expenses Detail");

        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.expenses_detail,null);

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

        //finding advance
        String cols[] = {MyDatabase.TABLE_ADVANCE_PAYMENT_COL3};
        String selection=MyDatabase.TABLE_ADVANCE_PAYMENT_COL2+"=?";
        String[] selectionArgs={stringContact};
        Cursor cursorAdvancePayment = db.query(MyDatabase.TABLE_ADVANCE_PAYMENT,cols,selection,selectionArgs,null,null,null);
        if(cursorAdvancePayment.moveToFirst()){
            do{
                yourAdvance += cursorAdvancePayment.getDouble(0);
            }while (cursorAdvancePayment.moveToNext());
        }

        //finding each cost
        eachCost = (totalExpenses/totalMember);

        //finding balance of member
        yourBalance = yourAdvance-eachCost;


        //adding data in dialog textView
        TextView textViewTotalExpenses = (TextView)v.findViewById(R.id.textViewTotalExpenses);
        TextView textViewTotalMember = (TextView)v.findViewById(R.id.textViewTotalMember);
        TextView textViewEachCost = (TextView)v.findViewById(R.id.textViewEachCost);
        TextView textViewYourAdvance = (TextView)v.findViewById(R.id.textViewYourAdvance);
        TextView textViewBalance = (TextView)v.findViewById(R.id.textViewBalance);

        textViewTotalExpenses.setText(totalExpenses+" Rs");
        textViewTotalMember.setText(totalMember+"");
        textViewEachCost.setText(eachCost+" Rs");
        textViewYourAdvance.setText(yourAdvance+" Rs");
        textViewBalance.setText(yourBalance+" Rs");

        builder.setView(v);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
        }else {
            Toast.makeText(MainActivity.this,"Expenses are not Available",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isMember(){
     MyDatabase md=new MyDatabase(this);
     SQLiteDatabase db = md.getWritableDatabase();
     Cursor cursor = db.query(MyDatabase.TABLE_MEMBER,null,null,null,null,null,null);
     if(cursor.moveToFirst()){
         return true;
     }
     else {
         return false;
     }
    }
    public boolean isExpenses(){
        MyDatabase md=new MyDatabase(this);
        SQLiteDatabase db = md.getWritableDatabase();
        Cursor cursor = db.query(MyDatabase.TABLE_DAILY_EXPENSES,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            return true;
        }
        else {
            return false;
        }
    }
    public boolean isAdvance(){
        MyDatabase md=new MyDatabase(this);
        SQLiteDatabase db = md.getWritableDatabase();
        Cursor cursor = db.query(MyDatabase.TABLE_ADVANCE_PAYMENT,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            return true;
        }
        else {
            return false;
        }
    }
    public void showDialogForZeroMember(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No member add here, first add Member");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
       builder.show();
    }
}
