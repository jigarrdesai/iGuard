package com.maxpro.iguard.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.net.Uri;
import com.maxpro.iguard.R;
import com.maxpro.iguard.activity.ActContact;
import com.maxpro.iguard.utility.Key;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Ketan on 5/18/2015.
 */
public class AdapterContact extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ActContact actContact;
    private List<ParseObject> carList;

    public AdapterContact(ActContact actContact, List<ParseObject> carList) {
        this.actContact = actContact;
        this.carList = carList;
    }

    @Override
    public int getItemCount() {
        return (carList != null) ? carList.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_contact, parent, false);

        ItemHolder vh = new ItemHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ItemHolder itemHolder= (ItemHolder) holder;
        ParseObject car=carList.get(position);
        itemHolder.txtCarname.setText(car.getString(Key.Cars.carName));
        final String phoneNumber=car.getString(Key.Cars.contactNumber);
        itemHolder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                actContact.startActivity(intent);
            }
        });
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        TextView txtCarname;
        Button btnCall;

        public ItemHolder(View itemView) {
            super(itemView);
            txtCarname= (TextView) itemView.findViewById(R.id.rowcontact_txtCarname);
            btnCall= (Button) itemView.findViewById(R.id.rowcontact_btnCall);
        }
    }

}
