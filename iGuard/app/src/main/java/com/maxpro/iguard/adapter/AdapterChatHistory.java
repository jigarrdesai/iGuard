package com.maxpro.iguard.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxpro.iguard.IGuard;
import com.maxpro.iguard.R;
import com.maxpro.iguard.activity.ActChatHistory;
import com.maxpro.iguard.utility.Func;
import com.maxpro.iguard.utility.Key;
import com.maxpro.iguard.utility.Key.*;
import com.maxpro.iguard.utility.Var;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class AdapterChatHistory extends Adapter<AdapterChatHistory.ItemHolder> {
    List<ParseObject> chatList;
    private ActChatHistory actChatHistory;

    public AdapterChatHistory(ActChatHistory actChatHistory, List<ParseObject> chatList) {
        this.chatList = chatList;
        this.actChatHistory = actChatHistory;
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return (chatList != null) ? chatList.size() : 0;
    }

    @Override
    public void onBindViewHolder(ItemHolder itemHolder, int position) {
        ParseObject obj = chatList.get(position);
        String message = obj.getString(Chats.message);
        String reply = obj.getString(Chats.supervisorReplyMessage);
        if (TextUtils.isEmpty(message)) {
            itemHolder.txtMessageRight.setVisibility(View.GONE);
        } else {
            itemHolder.txtMessageRight.setVisibility(View.VISIBLE);
            itemHolder.txtMessageRight.setText(message);
        }
        if (TextUtils.isEmpty(reply)) {
            itemHolder.txtMessageLeft.setVisibility(View.GONE);
        } else {
            itemHolder.txtMessageLeft.setVisibility(View.VISIBLE);
            itemHolder.txtMessageLeft.setText(reply);
        }
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int arg1) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_chathistory, parent, false);

        ItemHolder vh = new ItemHolder(v);
        return vh;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {
        TextView txtMessageLeft, txtMessageRight;


        public ItemHolder(View v) {
            super(v);
            txtMessageLeft = (TextView) v.findViewById(R.id.rowchathistory_txtMessageLeft);
            txtMessageRight = (TextView) v.findViewById(R.id.rowchathistory_txtMessageRight);

        }
    }

    public void addComment(final EditText editMessageText, ParseObject supervisorObj) {
        final String text = editMessageText.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            return;
        }
        final ParseObject chatObject = new ParseObject(Chats.NAME);
        chatObject.put(Chats.message, text);
        chatObject.put(Chats.messageTime, Func.getCurrentDate(Var.DF_TIME));
        chatObject.put(Chats.supervisorReplyMessage, "");
        chatObject.put(Chats.supervisor, supervisorObj);
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseObject company = currentUser.getParseObject(Key.User.company);
        ParseObject branch = currentUser.getParseObject(Key.User.branch);

        chatObject.put(Chats.company, company);
        chatObject.put(Chats.branch, branch);
        chatObject.put(Chats.usersPointer, currentUser);
        chatObject.put(Chats.messageDate, Func.getCurrentDate(Var.DF_DATE));
        chatObject.put(Chats.read, false);
        chatObject.put(Chats.site, currentUser.getParseObject(Key.User.site));
        chatObject.put(Chats.shift, currentUser.getParseObject(Key.User.shift));
        chatObject.put(Chats.post, currentUser.getParseObject(Key.User.post));
        editMessageText.setText("");
        chatList.add(chatObject);
        notifyDataSetChanged();

        chatObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null && (!actChatHistory.isFinishing())) {
                    editMessageText.setText(text);
                    chatList.remove(chatObject);
                    notifyDataSetChanged();
                }
            }
        });
    }
}
