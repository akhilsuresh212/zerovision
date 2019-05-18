package org.tensorflow.tensorflowdemo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.tensorflow.demo.R;

import java.util.ArrayList;

public class MyListAdapter extends BaseAdapter {

    private final Activity context;
    private ArrayList<Friend> list_friends;

    public MyListAdapter(Activity context, ArrayList<Friend> list_friends ) {


        this.context=context;
        this.list_friends=list_friends;

    }

    @Override
    public int getCount() {
        return list_friends.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View view, ViewGroup parent) {
        View v = view;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService
                    (context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.friend_list_layout, null);

        }

        TextView title  = (TextView)v.findViewById(R.id.title);
        TextView subtitle  = (TextView)v.findViewById(R.id.subtitle);

        title.setText(list_friends.get(position).getName());
        subtitle.setText(list_friends.get(position).getMob());

        return v;
    };

}
