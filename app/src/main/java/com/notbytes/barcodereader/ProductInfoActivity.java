package com.notbytes.barcodereader;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;

public class ProductInfoActivity extends AppCompatActivity {
    public String recvBarr;
    EditText barT , nameT , costT , priceT , dateT , quantityT;
    Button submit_btn ,cancel_btn;
    private ProgressDialog dialog;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("datas");
        barT = (EditText) findViewById(R.id.editText);
        dateT = (EditText) findViewById(R.id.editText6);
        nameT = (EditText) findViewById(R.id.editText2);
        costT = (EditText) findViewById(R.id.editText3);
        priceT = (EditText) findViewById(R.id.editText4);
        quantityT = (EditText) findViewById(R.id.editText5);
        submit_btn = (Button) findViewById(R.id.button);
        cancel_btn = (Button) findViewById(R.id.button2);
        dialog = new ProgressDialog(ProductInfoActivity.this);
        Bundle b = getIntent().getExtras();

        if(b != null) {
            recvBarr = b.getString("barr");
            if(recvBarr.length() > 4){
                dialog.setMessage("กำลังค้นหาข้อมูล โปรดรอสักครู่..");
                dialog.setCancelable(false);
                dialog.show();
                barT.setText(recvBarr);
                disableEditText(barT);
                    myRef.child(recvBarr).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            ProductGetter product = dataSnapshot.getValue(ProductGetter.class);
                            if(product != null) {
                                dateT.setText(product.getDate());
                                nameT.setText(product.getProduct_NAME());
                                costT.setText(product.getCost());
                                priceT.setText(product.getPrice());
                                disableEditText(dateT);
                                if(dialog.isShowing()){
                                    dialog.dismiss();
                                }
                            }else{
                                if(dialog.isShowing()){
                                    dialog.dismiss();
                                }
                                disableEditText(dateT);
                                new AlertDialog.Builder(ProductInfoActivity.this)
                                    .setTitle("ผิดพลาด")
                                    .setMessage("ไม่สามารถโหลดข้อมูลได้ หรือ ไม่มีข้อมูลในฐานข้อมูล\nต้องการเพิ่มข้อมูลใหม่หรือไม่")
                                    .setCancelable(false)
                                    .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog1, int which) {
                                            dateT.setText(getDateNow());
                                            dialog1.dismiss();
                                        }
                                    }).setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finish();
                                    }
                                }).show();
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w("g", "Failed to read value.", error.toException());
                            if(dialog.isShowing()){
                                dialog.dismiss();
                            }
                        }
                    });

            }

        }
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setMessage("กำลังอัพเดทข้อมูล....");
                dialog.setCancelable(false);
                dialog.show();
                updateToFirebase();
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    private String getDateNow(){
        Calendar cal = Calendar.getInstance();
        String date = DateFormat.format("yyyy-MM-dd hh:mm:ss", cal).toString();
        return date;
    }
    private void updateToFirebase(){
        String bar  = barT.getText().toString();
        String name = nameT.getText().toString();
        String cost = costT.getText().toString();
        String price = priceT.getText().toString();
        String date = getDateNow(); //dateT.getText().toString();
        if(isNumeric(cost) && isNumeric(price) && isNumeric(bar) && !name.isEmpty() && !date.isEmpty()) {
                ProductGetter p = new ProductGetter(cost, date, price, name, bar);
                DatabaseReference setRef = database.getReference("datas");
                setRef.child(bar).setValue(p);
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
                finish();
        }else{
            if(dialog.isShowing()){
                dialog.dismiss();
            }
            new AlertDialog.Builder(ProductInfoActivity.this)
                .setTitle("แจ้งเตือน")
                .setMessage("กรุณาใส่ข้อมูลให้ถูกต้อง")
                .setCancelable(false)
                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                        dialog1.dismiss();
                    }
                }).show();
        }

    }
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }
}
