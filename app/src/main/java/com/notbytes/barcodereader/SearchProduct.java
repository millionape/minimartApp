package com.notbytes.barcodereader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchProduct extends AppCompatActivity {
    EditText inp;
    TextView resultText;
    Button searchBtn;
    ListView listview;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<ProductGetter> lst = new ArrayList<ProductGetter>();
    ArrayList<ProductGetter> searchLst = new ArrayList<ProductGetter>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        inp = (EditText) findViewById(R.id.editText13);
        resultText = (TextView)findViewById(R.id.textView30);
        searchBtn = (Button) findViewById(R.id.button7);
        listview = (ListView) findViewById(R.id.listview);
        listview.setClickable(true);
        final ProgressDialog dialog = new ProgressDialog(SearchProduct.this);
        dialog.setMessage("กำลังโหลดข้อมูล รอสักครู่");
        dialog.show();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("datas");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lst.clear();
                for(DataSnapshot dt:dataSnapshot.getChildren()){
                    ProductGetter item = dt.getValue(ProductGetter.class);
                    if(item != null){
                        lst.add(item);
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog dialog2 = new ProgressDialog(SearchProduct.this);
                dialog2.setMessage("กำลังโหลดข้อมูล รอสักครู่");
                dialog2.show();
                int count = 0;
                searchLst.clear();
                String keyword = inp.getText().toString();
                for(ProductGetter item:lst){
                    if(item.getProduct_NAME().contains(keyword)){
                        searchLst.add(item);
                        count++;
                    }
                }
                resultText.setText("ผลการค้นหา "+String.valueOf(count)+" รายการ");
                Toast.makeText(SearchProduct.this,"ผลการค้นหา พบ "+String.valueOf(count)+" จำนวน",Toast.LENGTH_SHORT).show();
                CustomAdapterProduct adapter = new CustomAdapterProduct(getApplicationContext(),searchLst);
                listview.setAdapter(adapter);
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                dialog2.cancel();
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SearchProduct.this,ProductInfoActivity.class);
                Bundle b = new Bundle();
                b.putString("barr",searchLst.get(i).getProduct_NUMBER()); //Your id
                intent.putExtras(b);
                startActivity(intent);
            }
        });



    }

}
