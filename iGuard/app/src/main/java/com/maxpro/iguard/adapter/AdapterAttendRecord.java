package com.maxpro.iguard.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maxpro.iguard.R;
import com.parse.ParseObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdapterAttendRecord extends Adapter<AdapterAttendRecord.ItemHolder> {
    private HashMap<String, List<ParseObject>> attendList;
    SimpleDateFormat sdfParse,sdfDate,sdfTime;
    ArrayList<String> keys;
    public AdapterAttendRecord(ArrayList<String> keys, HashMap<String, List<ParseObject>> attendList) {
        this.attendList = attendList;
        this.keys=keys;
        sdfParse=new SimpleDateFormat("dd/MM/yyyy hh:mm");
        sdfDate=new SimpleDateFormat("dd-MMMM-yyyy");
        sdfTime=new SimpleDateFormat("hh:mm");
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return (attendList != null) ? attendList.size() : 0;
    }

    @Override
    public void onBindViewHolder(ItemHolder arg0, int arg1) {
        List<ParseObject>arrayList=attendList.get(keys.get(arg1));
        String dateTime=arrayList.get(0).getString("submitTime");
        try {
            arg0.txtDate.setText(sdfDate.format(sdfParse.parse(dateTime)));
            arg0.txtTime.setText("Start Time "+sdfTime.format(sdfParse.parse(dateTime)));
            if(arrayList.size()>=2) {
                String dateTime2=arrayList.get(1).getString("submitTime");
                arg0.txtTime.append(" End Time " + sdfTime.format(sdfParse.parse(dateTime2)));
            }else{
                arg0.txtTime.append(" End Time - A");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int arg1) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_attendance_record, parent, false);

        ItemHolder vh = new ItemHolder(v);
        return vh;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtTime;

        public ItemHolder(View v) {
            super(v);
            txtDate = (TextView) v.findViewById(R.id.rowattendrecord_txtDate);
            txtTime = (TextView) v.findViewById(R.id.rowattendrecord_txtTime);
        }
    }

}
