package com.notbytes.barcodereader;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.view.View.INVISIBLE;

public class CreditShow extends AppCompatActivity {
    public String creditID;
    public String whosCredit;
    FloatingActionButton fab;
    FirebaseDatabase database;
    ArrayList<String> dateList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_bill);
        LinearLayout bottomBox = (LinearLayout)findViewById(R.id.bottomBox);
        bottomBox.setVisibility(View.GONE);
        final ListView listView = (ListView)findViewById(R.id.listview);
        database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("creditList");
        TextView textView = (TextView) findViewById(R.id.textView);
        final FloatingActionButton FABX = (FloatingActionButton)findViewById(R.id.fabx);
        FABX.setVisibility(INVISIBLE);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        FloatingActionButton FAB1 = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        FAB1.setVisibility(View.INVISIBLE);
        final Button datePicker = (Button) findViewById(R.id.button5);
        datePicker.setVisibility(View.VISIBLE);
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        final ProgressDialog dialog = new ProgressDialog(CreditShow.this);
        dialog.setMessage("กำลังโหลดข้อมูล รอสักครู่");
        dialog.show();
        Bundle b = getIntent().getExtras();
        if(b != null) {
            creditID = b.getString("creditID");
            whosCredit = b.getString("ncredit");
            textView.setText("บิลเงินเชื่อ :"+whosCredit);
            if(creditID.length() <= 0){
                finish();
            }
        }else{
            finish();
        }


        myRef.child(creditID).child("bills").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dateList.clear();
                for(DataSnapshot items:dataSnapshot.getChildren()){
                    String mark = items.child("mark").getValue(String.class);
                    String state = items.child("payState").getValue(String.class);
                    if(state != null) {
                        dateList.add(items.getKey()+"> (จ่ายแล้ว)");
                    }else{
                        dateList.add(items.getKey());
                    }
                    //Toast.makeText(CreditShow.this,items.getKey(),Toast.LENGTH_SHORT).show();
                }
                final StableArrayAdapter adapter = new StableArrayAdapter(CreditShow.this, android.R.layout.simple_list_item_1, dateList);
                Collections.reverse(dateList);
                listView.setAdapter(adapter);
                dialog.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it = new Intent(CreditShow.this,ItemCreditList.class);
                Bundle b = new Bundle();
                b.putString("creditID",creditID); //Your id
                b.putString("ncredit",whosCredit); //Your id
                String split[] = dateList.get(i).split(">", 0);
                b.putString("date",split[0]); //Your id
                String arr[] = dateList.get(i).split(">",0);
                if(split.length > 1){
                    b.putBoolean("flag",true);
                }else{
                    b.putBoolean("flag",false);
                }
                it.putExtras(b);
                startActivity(it);
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                final String currentDateandTime = sdf.format(new Date());
                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
                final String timeFormat = sdf2.format(new Date());
                LayoutInflater li = LayoutInflater.from(CreditShow.this);
                View promptsView = li.inflate(R.layout.custom_dialog, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreditShow.this);
                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                final TextView dateText = (TextView) promptsView.findViewById(R.id.textView2);
                final TextView nameText = (TextView) promptsView.findViewById(R.id.textView1);
                final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
                nameText.setText("เปิดบิลใหม่ของ :"+whosCredit);
                dateText.setText("วันที่ :"+currentDateandTime+","+timeFormat);
                //Toast.makeText(CreditShow.this,currentDateandTime2,Toast.LENGTH_SHORT).show();
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                        //Toast.makeText(CreditShow.this,userInput.getText(),Toast.LENGTH_SHORT).show();
                                        if(userInput.getText().toString().length() <= 0){
                                            myRef.child(creditID).child("bills").child(currentDateandTime+",  "+timeFormat).setValue("NULL");
                                        }else{
                                            myRef.child(creditID).child("bills").child(currentDateandTime+",  "+timeFormat).child("mark").setValue(userInput.getText().toString());
                                        }

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreditShow.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                datePicker.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }
    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}

