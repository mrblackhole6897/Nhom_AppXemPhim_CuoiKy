package com.example.appfilmnew;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



import java.util.List;

public class PlatfFormAdapter extends ArrayAdapter<PlatfForm> {
    Context context;
    int layoutResourceId;
    List<PlatfForm> data;
    public PlatfFormAdapter(Context context, int resource, List data) {
        super(context, resource, data);
        this.layoutResourceId = resource;
        this.context = context;
        this.data = data;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PlatfFormHolder holder = null;
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new PlatfFormHolder();
            holder.rv = (RecyclerView)row.findViewById(R.id.RecyclerViewPlatf);
            holder.txtTitle = (TextView)row.findViewById(R.id.textViewPlatf);
            row.setTag(holder);
        }
        else
        {
            holder = (PlatfFormHolder)row.getTag();
        }
        PlatfForm pf = data.get(position);
        holder.txtTitle.setText(pf.title);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        holder.rv.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(context, data.get(position).infos);

        holder.rv.setAdapter(adapter);
        return row;
    }
    class PlatfFormHolder
    {
        RecyclerView rv;
        TextView txtTitle;
    }
}
