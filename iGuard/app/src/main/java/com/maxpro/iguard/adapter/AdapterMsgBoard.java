package com.maxpro.iguard.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxpro.iguard.IGuard;
import com.maxpro.iguard.R;
import com.maxpro.iguard.activity.ActFullImage;
import com.maxpro.iguard.adapter.AdapterMsgBoard.ItemHolder;
import com.maxpro.iguard.utility.Func;
import com.maxpro.iguard.utility.Key;
import com.maxpro.iguard.utility.Var;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class AdapterMsgBoard extends Adapter<ItemHolder> {
    List<ParseObject> msgList;
    Activity activity;
    public AdapterMsgBoard(Activity activity,List<ParseObject> msgList) {
this.activity=activity;
        this.msgList = msgList;
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return (msgList != null) ? msgList.size() : 0;
    }

    @Override
    public void onBindViewHolder(ItemHolder itemHolder, int position) {
        ParseObject obj = msgList.get(position);

        itemHolder.txtTitle.setText(obj.getString(Key.MsgBoard.messageTitle));
        itemHolder.txtDesc.setText(obj.getString(Key.MsgBoard.messageDescription));
        itemHolder.txtDate.setText(Func.getStringFromMilli(Var.DF_DATETIME,obj.getCreatedAt().getTime()));
        final ParseFile imgFile = obj.getParseFile(Key.MsgBoard.messageImage);
        if (imgFile != null) {
            IGuard.imageLoader.displayImage(imgFile.getUrl(), itemHolder.imgMsg, Func.getDisplayOption());
        }
        itemHolder.imgMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(activity, ActFullImage.class);
                if (imgFile != null) {
                    intent.putExtra(Var.IntentUrl, imgFile.getUrl());
                }else{
                    intent.putExtra(Var.IntentUrl, "");
                }
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int arg1) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_msgboard, parent, false);

        ItemHolder vh = new ItemHolder(v);
        return vh;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {
        TextView txtDesc, txtTitle,txtDate;
        ImageView imgMsg;

        public ItemHolder(View v) {
            super(v);
            txtDesc = (TextView) v.findViewById(R.id.rowmsgboard_txtDesc);
            txtTitle = (TextView) v.findViewById(R.id.rowmsgboard_txtTitle);
            txtDate=(TextView) v.findViewById(R.id.rowmsgboard_txtDate);
            imgMsg = (ImageView) v.findViewById(R.id.rowmsgboard_img);
        }
    }

}
