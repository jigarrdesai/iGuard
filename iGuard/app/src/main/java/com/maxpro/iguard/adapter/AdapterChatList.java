package com.maxpro.iguard.adapter;

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

import com.maxpro.iguard.activity.ActChatHistory;
import com.maxpro.iguard.activity.ActChatList;
import com.maxpro.iguard.utility.Func;
import com.maxpro.iguard.utility.Key;
import com.maxpro.iguard.utility.Var;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.List;

public class AdapterChatList extends Adapter<AdapterChatList.ItemHolder> {
    List<ParseObject> chatList;
private ActChatList actChatList;
    public AdapterChatList(ActChatList actChatList,List<ParseObject> chatList) {
        this.chatList = chatList;
        this.actChatList=actChatList;
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return (chatList != null) ? chatList.size() : 0;
    }

    @Override
    public void onBindViewHolder(ItemHolder itemHolder, int position) {
        ParseObject obj = chatList.get(position);

        itemHolder.txtMessage.setText(obj.getString(Key.Chats.message));
        try {
            final ParseObject supervisor=obj.getParseObject(Key.Chats.supervisor).fetchIfNeeded();
            itemHolder.txtName.setText(supervisor.getString(Key.User.username));
            itemHolder.txtUserType.setText(supervisor.getString(Key.User.userType));
            ParseFile imgFile = supervisor.getParseFile(Key.User.registrationPhoto);
            if (imgFile != null) {
                IGuard.imageLoader.displayImage(imgFile.getUrl(), itemHolder.imgMsg, Func.getDisplayOption());
            }

            itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(actChatList, ActChatHistory.class);
                    intent.putExtra(Var.IntentObjId,supervisor.getObjectId());
                    intent.putExtra(Var.IntentHeader,supervisor.getString(Key.User.username));
                    actChatList.startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int arg1) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_chatlist, parent, false);

        ItemHolder vh = new ItemHolder(v);
        return vh;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {
        TextView txtMessage, txtName, txtUserType;
        ImageView imgMsg;

        public ItemHolder(View v) {
            super(v);
            txtMessage = (TextView) v.findViewById(R.id.rowchatlist_txtMessage);
            txtName = (TextView) v.findViewById(R.id.rowchatlist_txtName);
            txtUserType = (TextView) v.findViewById(R.id.rowchatlist_txtUserType);
            imgMsg = (ImageView) v.findViewById(R.id.rowchatlist_img);
        }
    }

}
