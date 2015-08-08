package com.maxpro.iguard.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.maxpro.iguard.R;
import com.maxpro.iguard.activity.ActContact;
import com.maxpro.iguard.activity.ActVisits;
import com.maxpro.iguard.utility.Key;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Ketan on 5/18/2015.
 */
public class AdapterVisits extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ActVisits activity;
    private List<ParseObject> visitList;

    public AdapterVisits(ActVisits activity, List<ParseObject> visitList) {
        this.activity = activity;
        this.visitList = visitList;
    }

    @Override
    public int getItemCount() {
        return (visitList != null) ? visitList.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_visits, parent, false);

        ItemHolder vh = new ItemHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ItemHolder itemHolder = (ItemHolder) holder;
        final ParseObject visit = visitList.get(position);
        itemHolder.txtVisitor.setText(visit.getString(Key.Visits.visitor));
        itemHolder.txtDate.setText(visit.getString(Key.Visits.dateIn));
        String inTime = visit.getString(Key.Visits.timeIn);
        String outTime = visit.getString(Key.Visits.timeOut);
        itemHolder.txtTime.setText(inTime);
        itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onVisitsClick(visit);
            }
        });
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        TextView txtVisitor, txtTime, txtDate;


        public ItemHolder(View itemView) {
            super(itemView);
            txtVisitor = (TextView) itemView.findViewById(R.id.rowvisit_txtVisitor);
            txtTime = (TextView) itemView.findViewById(R.id.rowvisit_txtTime);
            txtDate = (TextView) itemView.findViewById(R.id.rowvisit_txtDate);
        }
    }

}
