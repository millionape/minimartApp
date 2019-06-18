package com.notbytes.barcodereader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OftenProduct extends AppCompatActivity implements View.OnClickListener{
    public  String name,id,date;
    FirebaseDatabase database;
    DatabaseReference myRef,productRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_often_product);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("creditList");

        Bundle b = getIntent().getExtras();
        if(b != null) {
            name = b.getString("ncredit");
            id = b.getString("creditID");
            date = b.getString("date");
        }else{
            finish();
        }
        findViewById(R.id.b1).setOnClickListener(this);
        findViewById(R.id.b2).setOnClickListener(this);
        findViewById(R.id.b3).setOnClickListener(this);
        findViewById(R.id.a1).setOnClickListener(this);
        findViewById(R.id.a2).setOnClickListener(this);
        findViewById(R.id.a3).setOnClickListener(this);
        findViewById(R.id.a4).setOnClickListener(this);
        findViewById(R.id.a5).setOnClickListener(this);
        findViewById(R.id.a6).setOnClickListener(this);
        findViewById(R.id.a7).setOnClickListener(this);
        findViewById(R.id.a8).setOnClickListener(this);
        findViewById(R.id.m1).setOnClickListener(this);
        findViewById(R.id.m2).setOnClickListener(this);
        findViewById(R.id.m3).setOnClickListener(this);
        findViewById(R.id.m4).setOnClickListener(this);
        findViewById(R.id.m5).setOnClickListener(this);
        findViewById(R.id.c1).setOnClickListener(this);
        findViewById(R.id.c2).setOnClickListener(this);
        findViewById(R.id.c3).setOnClickListener(this);
        findViewById(R.id.c4).setOnClickListener(this);
        findViewById(R.id.c5).setOnClickListener(this);
        findViewById(R.id.c6).setOnClickListener(this);
        findViewById(R.id.c7).setOnClickListener(this);
        findViewById(R.id.c8).setOnClickListener(this);
        findViewById(R.id.c9).setOnClickListener(this);
        findViewById(R.id.e1).setOnClickListener(this);
        findViewById(R.id.e2).setOnClickListener(this);
        findViewById(R.id.customItem).setOnClickListener(this);




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b1:
                openDialog("8850999141008",view.getId());
                break;
            case R.id.b2:
                openDialog("8851993616011",view.getId());
                break;
            case R.id.b3:
                openDialog("8850999111001",view.getId());
                break;
            case R.id.a1:
                openDialog("8852388000613",view.getId());
                break;
            case R.id.a2:
                openDialog("8852388000620",view.getId());
                break;
            case R.id.a3:

                openDialog("8855155000700",view.getId());
                break;
            case R.id.a4:
                openDialog("8855155000724",view.getId());
                break;
            case R.id.a5:
                openDialog("003",view.getId());
                break;
            case R.id.a6:
                openDialog("001",view.getId());
                break;
            case R.id.a7:
                openDialog("8854795000491",view.getId());
                break;
            case R.id.a8:
                openDialog("8854795000453",view.getId());
                break;
            case R.id.m1:
                openDialog("009",view.getId());
                break;
            case R.id.m2:
                openDialog("8850999220000",view.getId());
                break;
            case R.id.m3:
                openDialog("0000007",view.getId());
                break;
            case R.id.m4:
                openDialog("010",view.getId());
                break;
            case R.id.m5:
                openDialog("011",view.getId());
                break;
            case R.id.c1:
                openDialog("95509747",view.getId());
                break;
            case R.id.c2:
                openDialog("76164064",view.getId());
                break;
            case R.id.c3:
                openDialog("95509730",view.getId());
                break;
            case R.id.c4:
                openDialog("8850170000155",view.getId());
                break;
            case R.id.c5:
                openDialog("8886419700036",view.getId());
                break;
            case R.id.c6:
                openDialog("8850170000483",view.getId());
                break;
            case R.id.c7:
                openDialog("76164040",view.getId());
                break;
            case R.id.c8:
                openDialog("8850170000803",view.getId());
                break;
            case R.id.c9:
                openDialog("8850170000797",view.getId());
                break;
            case R.id.e1:
                openDialog("012",view.getId());
                break;
            case R.id.e2:
                openDialog("013",view.getId());
                break;
            case R.id.customItem:
                customItemAdd();
                break;
        }
    }
    public void customItemAdd(){
        LayoutInflater li = LayoutInflater.from(OftenProduct.this);
        View promptsView = li.inflate(R.layout.custom_item_add, null);
        final EditText pname = (EditText) promptsView.findViewById(R.id.editText8);
        final EditText pprice = (EditText) promptsView.findViewById(R.id.editText11);
        final TextView pquantity = (TextView) promptsView.findViewById(R.id.editText12);
        pquantity.setText("1");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OftenProduct.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int did) {

                                String name = pname.getText().toString();
                                String price = pprice.getText().toString();
                                String quantity = pquantity.getText().toString();
                                if(name.length() > 0 && price.length() > 0 && quantity.length() > 0){
                                    final Double total = Double.valueOf(price) * Integer.valueOf(quantity);
                                    CreditItemGetter sumofItem = new CreditItemGetter(String.valueOf(quantity),String.valueOf(total),String.valueOf(price));
                                    myRef.child(id).child("bills").child(date).child("items").child(name).setValue(sumofItem);
                                    Toast.makeText(OftenProduct.this,"เพิ่มข้อมูลสำเร็จ",Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    Toast.makeText(OftenProduct.this,"กรุณากรอกข้อมูลให้ถูกต้อง",Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
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

    private void openDialog(final String barcode ,int view){
        Button tmpBtn = (Button) findViewById(view);
        productRef = database.getReference("datas");
        LayoutInflater li = LayoutInflater.from(OftenProduct.this);
        View promptsView = li.inflate(R.layout.custom_dialog, null);
        final TextView dateText = (TextView) promptsView.findViewById(R.id.textView2);
        final TextView nameText = (TextView) promptsView.findViewById(R.id.textView1);
        dateText.setVisibility(View.GONE);
        nameText.setVisibility(View.GONE);
        final TextView etc = (TextView) promptsView.findViewById(R.id.textView9);
        etc.setText("เพิ่มสินค้า :"+tmpBtn.getText().toString()+" จำนวน ?");
        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        userInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        userInput.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        userInput.setText("1");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OftenProduct.this);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
            .setCancelable(false)
            .setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int did) {
                            productRef.child(barcode).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    ProductGetter itemP  = dataSnapshot.getValue(ProductGetter.class);
                                    if(itemP != null){
                                        final String ss = itemP.getProduct_NAME();
                                        final String pname = ss.replace(".","");
                                        pname.trim();
                                        final Double cost = Double.valueOf(itemP.getPrice());

                                        String quan = userInput.getText().toString();
                                        final Double total = Double.valueOf(userInput.getText().toString()) * cost ;
                                        final CreditItemGetter item = new CreditItemGetter(quan,String.valueOf(total),String.valueOf(cost));
                                        myRef.child(id).child("bills").child(date).child("items").child(pname).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                CreditItemGetter tmp = dataSnapshot.getValue(CreditItemGetter.class);
                                                if(tmp!=null){
                                                    Double totalcost = Double.valueOf(tmp.getTotal())+Double.valueOf(total);
                                                    Double totalquantity = Double.valueOf(tmp.getQuantity()) + Double.valueOf(item.getQuantity());
                                                    CreditItemGetter sumofItem = new CreditItemGetter(String.valueOf(totalquantity.intValue()),String.valueOf(totalcost),String.valueOf(cost));
                                                    myRef.child(id).child("bills").child(date).child("items").child(pname).setValue(sumofItem);
                                                }else{
                                                    myRef.child(id).child("bills").child(date).child("items").child(pname).setValue(item);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            finish();

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
