package com.notbytes.barcodereader;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import java.util.Date;

public class ItemCreditList extends AppCompatActivity {
    public  String name,id,date;
    TextView topText,totalText;
    FirebaseDatabase database;
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> quantiy = new ArrayList<String>();
    ArrayList<String> total = new ArrayList<String>();
    ArrayList<String> prices = new ArrayList<String>();
    ListView listView;
    public Boolean flag = false;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_bill);
        totalText = (TextView) findViewById(R.id.totalText);
        LinearLayout bottomBox = (LinearLayout)findViewById(R.id.bottomBox);
        bottomBox.setVisibility(View.VISIBLE);
        bottomBox.setClickable(true);
        listView = (ListView)findViewById(R.id.listview);
        FloatingActionButton FAB = (FloatingActionButton)findViewById(R.id.floatingActionButton2);
        FloatingActionButton FAB1 = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        final Button datePicker = (Button) findViewById(R.id.button5);
        topText = (TextView) findViewById(R.id.textView);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("creditList");
        //datePicker.setVisibility(View.GONE);
        final ProgressDialog dialog = new ProgressDialog(ItemCreditList.this);
        dialog.setMessage("กำลังโหลดข้อมูล รอสักครู่");
        dialog.show();
        Bundle b = getIntent().getExtras();
        if(b != null) {
            name = b.getString("ncredit");
            id = b.getString("creditID");
            date = b.getString("date");
            flag = b.getBoolean("flag");
            topText.setText("บิลเงินเชื่อของ :"+name);
            datePicker.setText("บิลวันที่ :"+date);
        }else{
            finish();
        }
        myRef.child(id).child("bills").child(date).child("payState").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String flag = dataSnapshot.getValue(String.class);
                if(flag!=null){
                    topText.setText(topText.getText().toString()+"\n("+flag+")");
                    topText.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        myRef.child(id).child("bills").child(date).child("mark").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String flag = dataSnapshot.getValue(String.class);
                if(flag!=null){
                    topText.setText(topText.getText().toString()+"\n**หมายเหตุ : "+flag+"");
                    topText.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        myRef.child(id).child("bills").addValueEventListener(new ValueEventListener() {
            Double totalSum = 0.0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dat:dataSnapshot.getChildren()){
                    names.clear();
                    quantiy.clear();
                    total.clear();
                    totalSum = 0.0;
                    for(DataSnapshot items:dat.child("items").getChildren()){
                        CreditItemGetter tmp = items.getValue(CreditItemGetter.class);
                        names.add(items.getKey());
                        quantiy.add(tmp.getQuantity());
                        total.add(tmp.getTotal());
                        prices.add(tmp.getPrice());
                        totalSum += Integer.valueOf(tmp.getTotal());
                        //Toast.makeText(ItemCreditList.this,items.getKey(),Toast.LENGTH_SHORT).show();
                    }
                    CustomAdapterCredit adapter = new CustomAdapterCredit(getApplicationContext(), names, quantiy , total);
                    listView.setAdapter(adapter);
                    totalText.setText(String.valueOf(totalSum));
                    //Toast.makeText(ItemCreditList.this,dat.child("item").getKey(),Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(flag == false) {
            FAB1.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ItemCreditList.this);
                    builder.setTitle("เช็คบิล");
                    builder.setMessage("ใส่ชื่อคนรับเงิน");
                    final EditText input = new EditText(ItemCreditList.this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(input.getText().toString().length()<=1){
                                aleartDialogOpen();
                            }else {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                final String currentDateandTime = sdf.format(new Date());
                                myRef.child(id).child("bills").child(date).child("payState").setValue(input.getText().toString() + currentDateandTime);
                                finish();
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                    return false;
                }
            });
            FAB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ItemCreditList.this, OftenProduct.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("creditID", id); //Your id
                    bundle.putString("ncredit", name); //Your id
                    bundle.putString("date", date); //Your id
                    i.putExtras(bundle);
                    startActivity(i);
                }
            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String itemName = names.get(i);
                    String itemPrice = prices.get(i);
                    openDialog(itemName, Integer.valueOf(itemPrice));
                    return false;
                }
            });
        }else {
            String original = topText.getText().toString();
            topText.setText(original+"\n (จ่ายแล้ว)");
        }





    }
    public  void aleartDialogOpen(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ItemCreditList.this);
        builder.setTitle("ผิดพลาด");
        builder.setMessage("โปรดใส่ชื่อคนรับเงิน");
//        final EditText input = new EditText(ItemCreditList.this);
//        input.setInputType(InputType.TYPE_CLASS_TEXT);
//        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        builder.show();

    }
    private void openDialog(final String pname, final int cost){
        LayoutInflater li = LayoutInflater.from(ItemCreditList.this);
        View promptsView = li.inflate(R.layout.custom_dialog, null);
        final TextView dateText = (TextView) promptsView.findViewById(R.id.textView2);
        final TextView nameText = (TextView) promptsView.findViewById(R.id.textView1);
        dateText.setVisibility(View.GONE);
        nameText.setVisibility(View.GONE);
        final TextView etc = (TextView) promptsView.findViewById(R.id.textView9);
        etc.setText("เพิ่มสินค้า: "+pname+" จำนวน?");
        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        userInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        userInput.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ItemCreditList.this);
        // set prompts.xml to alertdialog builder
        myRef.child(id).child("bills").child(date).child("items").child(pname).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CreditItemGetter tmp = dataSnapshot.getValue(CreditItemGetter.class);
                if(tmp!=null){
                    userInput.setText(tmp.getQuantity());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int did) {
                                String quan = userInput.getText().toString();
                                myRef.child(id).child("bills").child(date).child("items").child(pname).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        int quantity= Integer.valueOf(userInput.getText().toString());
                                        int totalTmp = quantity*cost;
                                        CreditItemGetter itemTmp = new CreditItemGetter(String.valueOf(quantity),String.valueOf(totalTmp),String.valueOf(cost));
                                        myRef.child(id).child("bills").child(date).child("items").child(pname).setValue(itemTmp);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                //Toast.makeText(OftenProduct.this,item.getQuantity()+item.getTotal(),Toast.LENGTH_SHORT).show();
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
}