package com.maxpro.iguard.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.maxpro.iguard.R;
import com.maxpro.iguard.adapter.AdapterChatList;
import com.maxpro.iguard.adapter.AdapterPatrolling;
import com.maxpro.iguard.utility.AppLog;
import com.maxpro.iguard.utility.Key;
import com.maxpro.iguard.utility.Progress;
import com.maxpro.iguard.utility.Var;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ActChatList extends ActDrawer {
    private RecyclerView recyclerView;
    private AdapterChatList adapterChatList;
    private Progress progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_chatlist);
        init();
        getChatList();
    }

    private void init() {
        setHeaderText(getString(R.string.dashboard_chat));
        setClickListener(click);
        imgBack.setImageResource(android.R.drawable.ic_menu_edit);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ActChatList.this, ActChatHistory.class);
                intent.putExtra(Var.IntentObjId, currentUser.getParseObject(Key.User.supervisor).getObjectId());
                intent.putExtra(Var.IntentHeader,currentUser.getParseObject(Key.User.supervisor).getString(Key.User.username));
                startActivity(intent);
            }
        });
        progressDialog = new Progress(this);
        recyclerView = (RecyclerView) findViewById(R.id.chatlist_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getChatList() {
        progressDialog.show();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Key.Chats.NAME);
        query.whereEqualTo(Key.Chats.usersPointer, currentUser);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> chatList, ParseException e) {
                progressDialog.dismiss();
                if (e == null) {
                    AppLog.d("Retrieved " + chatList.size() + " chatlist");
                    List<ParseObject> tempList = new ArrayList<ParseObject>();
                    ArrayList<String> objIds = new ArrayList<String>();
                    for (ParseObject obj : chatList) {
                        if (!objIds.contains(obj.getParseObject(Key.Chats.supervisor).getObjectId())) {
                            tempList.add(obj);
                            objIds.add(obj.getParseObject(Key.Chats.supervisor).getObjectId());
                        }
                    }
                    adapterChatList = new AdapterChatList(ActChatList.this,tempList);
                    recyclerView.setAdapter(adapterChatList);
                } else {
                    AppLog.d("Error: " + e.getMessage());
                }
            }
        });
    }
}
