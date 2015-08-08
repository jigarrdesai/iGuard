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
import com.maxpro.iguard.activity.ActReplacement;
import com.maxpro.iguard.utility.Key;
import com.maxpro.iguard.utility.Var;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Ketan on 5/18/2015.
 */
public class AdapterReplace extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private List<ParseObject> replaceList;

    public AdapterReplace(Activity activity, List<ParseObject> replaceList) {
        this.activity = activity;
        this.replaceList = replaceList;
    }

    @Override
    public int getItemCount() {
        return (replaceList != null) ? replaceList.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_replace, parent, false);

        ItemHolder vh = new ItemHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ItemHolder itemHolder = (ItemHolder) holder;
        final ParseObject object = replaceList.get(position);

        itemHolder.txtReason.setText(object.getString(Key.Replacement.reason));
        String dateTime = object.getString(Key.Replacement.replacedDate) + " " + object.getString(Key.Replacement.replacedTime);
        itemHolder.txtDate.setText(dateTime);

        itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ActReplacement.class);
                try {
                    //ParseObject replacedUser = object.getParseObject(Key.Replacement.replacedUserPointer).fetchIfNeeded();
                    intent.putExtra(Var.IntentObjId, object.getObjectId());
                    activity.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        TextView txtReason, txtDate;


        public ItemHolder(View itemView) {
            super(itemView);
            txtReason = (TextView) itemView.findViewById(R.id.rowreplace_txtReason);
            txtDate = (TextView) itemView.findViewById(R.id.rowreplace_txtDate);
        }
    }

}
