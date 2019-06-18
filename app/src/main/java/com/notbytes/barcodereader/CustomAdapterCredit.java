package com.notbytes.barcodereader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapterCredit extends BaseAdapter {
    Context mContext;
    ArrayList<String> strName;
    ArrayList<String> strQuantity;
    ArrayList<String> strTotal;

    public CustomAdapterCredit(Context context, ArrayList<String> names, ArrayList<String> strQuantity, ArrayList<String> strTotal) {
        this.mContext= context;
        this.strName = names;
        this.strQuantity = strQuantity;
        this.strTotal = strTotal;

    }


    public int getCount() {
        return strName.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null)
            view = mInflater.inflate(R.layout.row_credit, parent, false);

        TextView nameText = (TextView)view.findViewById(R.id.textView11);
        TextView quantityText = (TextView)view.findViewById(R.id.textView15);
        TextView totalText = (TextView) view.findViewById(R.id.textView16);
        nameText.setText("สินค้า : "+strName.get(position));
        quantityText.setText("จำนวน : "+strQuantity.get(position));
        totalText.setText("ราคารวม : "+strTotal.get(position));

        return view;
    }
}
