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
    DatabaseReference myRef;
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


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b1:
                openDialog("เบียร์ลีโอ",55);
                break;
            case R.id.b2:
                openDialog("เบียร์ช้าง",52);
                break;
            case R.id.b3:
                openDialog("เบียร์สิงห์",62);
                break;
        }
    }
    private void openDialog(final String pname, final int cost){
        LayoutInflater li = LayoutInflater.from(OftenProduct.this);
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
        userInput.setText("1");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OftenProduct.this);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
            .setCancelable(false)
            .setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int did) {
                            String quan = userInput.getText().toString();
                            final int total = Integer.valueOf(userInput.getText().toString()) * cost ;
                            final CreditItemGetter item = new CreditItemGetter(quan,String.valueOf(total),String.valueOf(cost));
                            myRef.child(id).child("bills").child(date).child("items").child(pname).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    CreditItemGetter tmp = dataSnapshot.getValue(CreditItemGetter.class);
                                    if(tmp!=null){
                                        int totalcost = Integer.valueOf(tmp.getTotal())+Integer.valueOf(total);
                                        int totalquantity = Integer.valueOf(tmp.getQuantity()) + Integer.valueOf(item.getQuantity());
                                        CreditItemGetter sumofItem = new CreditItemGetter(String.valueOf(totalquantity),String.valueOf(totalcost),String.valueOf(cost));
                                        myRef.child(id).child("bills").child(date).child("items").child(pname).setValue(sumofItem);
                                    }else{
                                        myRef.child(id).child("bills").child(date).child("items").child(pname).setValue(item);
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
