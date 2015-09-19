package com.maxpro.iguard.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxpro.iguard.IGuard;
import com.maxpro.iguard.R;
import com.maxpro.iguard.activity.ActFullImage;
import com.maxpro.iguard.utility.Func;
import com.maxpro.iguard.utility.Key;
import com.maxpro.iguard.utility.Var;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Ketan on 5/18/2015.
 */
public class AdapterHistorical extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private List<ParseObject> visitList;

    public AdapterHistorical(Activity activity, List<ParseObject> visitList) {
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

        ItemHolder itemHolder= (ItemHolder) holder;
        final ParseObject visit=visitList.get(position);
        itemHolder.txtVisitor.setText(visit.getString(Key.Visits.visitor));
        itemHolder.img.setImageResource(R.drawable.out_bg_withtext);
        itemHolder.txtDate.setText(visit.getString(Key.Visits.dateIn));
        String inTime = visit.getString(Key.Visits.timeIn);
        String outTime = visit.getString(Key.Visits.timeOut);
        itemHolder.txtTime.setText(inTime+" to "+outTime);
        itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showVisitDetail(visit);
            }
        });

    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        TextView txtVisitor,txtTime,txtDate;
        ImageView img;


        public ItemHolder(View itemView) {
            super(itemView);
            img=(ImageView) itemView.findViewById(R.id.rowvisit_imgStatus);
            txtVisitor= (TextView) itemView.findViewById(R.id.rowvisit_txtVisitor);
            txtTime= (TextView) itemView.findViewById(R.id.rowvisit_txtTime);
            txtDate= (TextView) itemView.findViewById(R.id.rowvisit_txtDate);
        }
    }
    public void showVisitDetail(ParseObject parseObject) {
        final Dialog dialog = new Dialog(activity);
        Window window = dialog.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_visitsdetail);
        dialog.setCanceledOnTouchOutside(true);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


        ImageView imgPhoto = (ImageView) dialog.findViewById(R.id.dialogvisit_image);
        TextView txtDateIn = (TextView) dialog.findViewById(R.id.dialogvisit_txtDateIn);
        TextView txtDateOut = (TextView) dialog.findViewById(R.id.dialogvisit_txtDateOut);
        TextView txtTimeIn = (TextView) dialog.findViewById(R.id.dialogvisit_txtTimeIn);
        TextView txtTimeOut = (TextView) dialog.findViewById(R.id.dialogvisit_txtTimeOut);

        TextView  txtVisitor = (TextView) dialog.findViewById(R.id.dialogvisit_txtVisitor);
        TextView txtPurpose = (TextView) dialog.findViewById(R.id.dialogvisit_txtPurpose);
        TextView txtPeople = (TextView) dialog.findViewById(R.id.dialogvisit_txtPeoplecount);
        TextView txtVehicle = (TextView) dialog.findViewById(R.id.dialogvisit_txtVehicleno);
        TextView  txtDocName = (TextView) dialog.findViewById(R.id.dialogvisit_txtDoc);
        TextView txtDocNum = (TextView) dialog.findViewById(R.id.dialogvisit_txtDocNumber);

        String datein = parseObject.getString(Key.Visits.dateIn);
        String dateout = parseObject.getString(Key.Visits.dateOut);
        String timein = parseObject.getString(Key.Visits.timeIn);
        String timeout = parseObject.getString(Key.Visits.timeOut);
        final ParseFile file=parseObject.getParseFile(Key.Visits.visitPhoto);
        if(file!=null) {
            IGuard.imageLoader.displayImage(file.getUrl(), imgPhoto, Func.getDisplayOption());

            imgPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ActFullImage.class);
                    intent.putExtra(Var.IntentUrl, file.getUrl());
                    activity.startActivity(intent);
                }
            });
        }
        String visitor = parseObject.getString(Key.Visits.visitor);
        String purpose = parseObject.getString(Key.Visits.purpose);
        String peopleCount = parseObject.getString(Key.Visits.peopleCount);
        String vehicleNum = parseObject.getString(Key.Visits.vehicleNum);
        String documentName = parseObject.getString(Key.Visits.documentName);
        String documentNum = parseObject.getString(Key.Visits.documentNumber);
        txtDateIn.setText(datein);
        txtDateOut.setText(dateout);
        txtTimeIn.setText(timein);
        txtTimeOut.setText(timeout);
        txtVisitor.setText(visitor);
        txtPeople.setText(peopleCount);
        txtPurpose.setText(purpose);
        txtVehicle.setText(vehicleNum);
        txtDocName.setText(documentName);
        txtDocNum.setText(documentNum);
        dialog.show();

    }
}
