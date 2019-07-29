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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import static android.view.View.INVISIBLE;

public class CreditBill extends AppCompatActivity {
    FirebaseDatabase database;
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> infos = new ArrayList<String>();
    ArrayList<String> keys = new ArrayList<String>();
    public  String PASSWORD = "4488";
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_bill);
        final FrameLayout frame = (FrameLayout) findViewById(R.id.frameLay);
        frame.bringToFront();
        LinearLayout bottomBox = (LinearLayout)findViewById(R.id.bottomBox);
        bottomBox.setVisibility(View.GONE);
        final ListView listView = (ListView)findViewById(R.id.listview);
        Button datePicker = (Button) findViewById(R.id.button5);
        datePicker.setVisibility(View.GONE);
        final FloatingActionButton FABX = (FloatingActionButton)findViewById(R.id.fabx);
        FABX.setVisibility(INVISIBLE);
        FloatingActionButton FAB1 = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        FAB1.setVisibility(INVISIBLE);
        database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("creditList");
        final ProgressDialog dialog = new ProgressDialog(CreditBill.this);
        dialog.setMessage("กำลังโหลดข้อมูล รอสักครู่");
        dialog.show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(CreditBill.this);
                builder.setTitle("เปิดบิลใหม่");
                builder.setMessage("กรุณาใส่ชื่อบิล");
                final EditText input = new EditText(CreditBill.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(input.getText().toString().length() <= 1){
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(CreditBill.this);
                            builder2.setTitle("ผิดพลาด");
                            builder2.setMessage("กรุณาใส่ชื่อให้ถูกต้อง");
                            builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder2.show();
                        }else {
                            myRef.child(String.valueOf(randInt(234,9999999))).child("name").setValue(input.getText().toString());
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
            }
        });
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                names.clear();
                infos.clear();
                keys.clear();
                for(DataSnapshot data :dataSnapshot.getChildren()){
                    CreditGetter c = data.getValue(CreditGetter.class);
                    names.add(c.getName());
                    infos.add(c.getName());
                    keys.add(data.getKey());
                    //Toast.makeText(CreditBill.this,data.getKey().toString(),Toast.LENGTH_SHORT).show();
                }
                CustomAdapter adapter = new CustomAdapter(getApplicationContext(), names, infos , keys);
                listView.setAdapter(adapter);
                dialog.dismiss();
                frame.bringToFront();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CreditBill.this,CreditShow.class);
                Bundle b = new Bundle();
                b.putString("creditID",keys.get(i).toString()); //Your id
                b.putString("ncredit",names.get(i).toString()); //Your id
                intent.putExtras(b);
                startActivity(intent);
                //Toast.makeText(CreditBill.this,keys.get(i).toString(),Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(CreditBill.this);
                builder.setTitle("ลบบิล :"+names.get(i));
                builder.setMessage("กรุณาใส่รหัสผ่านเพื่อลบ");
                final EditText input = new EditText(CreditBill.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(input.getText().toString().equals(PASSWORD)){
                            myRef.child(keys.get(i)).removeValue();
                            Toast.makeText(CreditBill.this,"ลบบิล "+names.get(i)+"สำเร็จ",Toast.LENGTH_SHORT).show();
                        }else{
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(CreditBill.this);
                            builder2.setTitle("ผิดพลาด");
                            builder2.setMessage("รหัสผ่านไม่ถูกต้อง");
                            builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder2.show();
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
                return true;
            }
        });
        frame.bringToFront();


    }
    public static int randInt(int min, int max) {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
        //
        // In particular, do NOT do 'Random rand = new Random()' here or you
        // will get not very good / not very random results.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
