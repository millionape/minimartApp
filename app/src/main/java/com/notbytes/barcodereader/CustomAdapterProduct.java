package com.notbytes.barcodereader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapterProduct extends BaseAdapter {
    Context mContext;
    ArrayList<ProductGetter> products;


    public CustomAdapterProduct(Context context, ArrayList<ProductGetter> products) {
        this.mContext= context;
        this.products = products;


    }

    public int getCount() {
        return products.size();
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
            view = mInflater.inflate(R.layout.row, parent, false);
        ImageView img = (ImageView)view.findViewById(R.id.imageView1);
        img.setVisibility(View.GONE);
        TextView nameText = (TextView)view.findViewById(R.id.name);
        TextView infoText = (TextView)view.findViewById(R.id.info);
        nameText.setText(products.get(position).getProduct_NAME());
        infoText.setText("ราคาทุน: "+products.get(position).getCost()+"  ราคาขาย: "+products.get(position).getPrice());

        return view;
    }
}
