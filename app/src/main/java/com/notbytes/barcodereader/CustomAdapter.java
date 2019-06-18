package com.notbytes.barcodereader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<String> strName;
    ArrayList<String> strInfo;
    ArrayList<String> strKey;

    public CustomAdapter(Context context, ArrayList<String> names, ArrayList<String> infos, ArrayList<String> keys) {
        this.mContext= context;
        this.strName = names;
        this.strInfo = infos;
        this.strKey = keys;

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
            view = mInflater.inflate(R.layout.row, parent, false);

        TextView nameText = (TextView)view.findViewById(R.id.name);
        TextView infoText = (TextView)view.findViewById(R.id.info);
        nameText.setText(strName.get(position));
        infoText.setText(strInfo.get(position));

        return view;
    }
}
