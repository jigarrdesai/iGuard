package com.maxpro.iguard.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxpro.iguard.R;
import com.maxpro.iguard.utility.Key;
import com.maxpro.iguard.utility.Var;
import com.parse.ParseObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AdapterLeave extends Adapter<AdapterLeave.ItemHolder> {
    private List<ParseObject> leaveList;
    SimpleDateFormat sdfParse, sdfDate, sdfTime;
    Activity activity;

    public AdapterLeave(Activity activity, List<ParseObject> leaveList) {
        this.leaveList = leaveList;
        this.activity = activity;
        sdfParse = new SimpleDateFormat("dd-MM-yyyy");
        sdfDate = new SimpleDateFormat("dd-MMMM-yyyy");
        sdfTime = new SimpleDateFormat("hh:mm");
    }

    public void addRow(ParseObject obj) {
        if (leaveList == null)
            leaveList = new ArrayList<>();
        leaveList.add(obj);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return (leaveList != null) ? leaveList.size() : 0;
    }

    @Override
    public void onBindViewHolder(ItemHolder arg0, final int arg1) {
        String dateTime = leaveList.get(arg1).getString("leaveDate");
        String status = leaveList.get(arg1).getString("status");
        if (status.equals(Var.TYPE_LEAVE_A)) {
            arg0.txtStatus.setText("Approved");
            arg0.imgStatus.setImageResource(R.drawable.a_withtext);
        } else if (status.equals(Var.TYPE_LEAVE_P)) {
            arg0.txtStatus.setText("Pending");
            arg0.imgStatus.setImageResource(R.drawable.p_withtext);
        } else if (status.equals(Var.TYPE_LEAVE_W)) {
            arg0.txtStatus.setText(status);
            arg0.imgStatus.setImageResource(R.drawable.w_withtext);
        }
        try {
            arg0.txtDate.setText(sdfDate.format(sdfParse.parse(dateTime)));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        arg0.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLeaveDetail(leaveList.get(arg1));
            }
        });
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int arg1) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_leave, parent, false);

        ItemHolder vh = new ItemHolder(v);
        return vh;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtStatus;
        ImageView imgStatus;

        public ItemHolder(View v) {
            super(v);
            txtDate = (TextView) v.findViewById(R.id.rowleave_txtDate);
            txtStatus = (TextView) v.findViewById(R.id.rowleave_txtStatus);
            imgStatus = (ImageView) v.findViewById(R.id.rowleave_imgStatus);
        }
    }

    public void showLeaveDetail(ParseObject parseObject) {
        final Dialog dialog = new Dialog(activity);
        Window window = dialog.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_leavedetail);
        dialog.setCanceledOnTouchOutside(true);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


        TextView txtFrom = (TextView) dialog.findViewById(R.id.dialogleave_txtFrom);
        TextView txtTo = (TextView) dialog.findViewById(R.id.dialogleave_txtTo);
        TextView txtStatus = (TextView) dialog.findViewById(R.id.dialogleave_txtStatus);
        TextView txtReason = (TextView) dialog.findViewById(R.id.dialogleave_txtReason);

        String from = parseObject.getString(Key.Leave.leaveDate) + " " + parseObject.getString(Key.Leave.applyDateTime);
        String to = parseObject.getString(Key.Leave.leaveTime) + " " + parseObject.getString(Key.Leave.leaveDateTime);
        String reason = parseObject.getString(Key.Leave.leaveReason);
        txtFrom.setText(from);
        txtTo.setText(to);
        txtReason.setText(reason);

        String status = parseObject.getString(Key.Leave.status);
        if (status.equals(Var.TYPE_LEAVE_A)) {
            txtStatus.setText("Approved");
        } else if (status.equals(Var.TYPE_LEAVE_P)) {
            txtStatus.setText("Pending");
        } else if (status.equals(Var.TYPE_LEAVE_W)) {
            txtStatus.setText(status);
        }

        dialog.show();

    }
}
