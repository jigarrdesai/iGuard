package com.maxpro.iguard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.maxpro.iguard.R;
import com.maxpro.iguard.adapter.AdapterChatHistory;
import com.maxpro.iguard.adapter.AdapterChatList;
import com.maxpro.iguard.utility.AppLog;
import com.maxpro.iguard.utility.Key;
import com.maxpro.iguard.utility.Progress;
import com.maxpro.iguard.utility.Var;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActChatHistory extends ActDrawer implements View.OnClickListener {
    private RecyclerView recyclerView;
    private EditText editMessageText;
    private ImageView imgSend;
    private AdapterChatHistory adapterChatHistory;
    private Progress progressDialog;
    private ParseObject supervisorObj;
    private Handler mHandler;
    boolean showProgress=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_chathistory);
        init();
        mHandler = new Handler();
        runnable.run();


    }

    private void init() {
        Intent intent = getIntent();
        String headerText = intent.getStringExtra(Var.IntentHeader);
        String supervisorObjId = intent.getStringExtra(Var.IntentObjId);
        supervisorObj = ParseObject.create(Key.User.NAME);
        supervisorObj.setObjectId(supervisorObjId);
        setHeaderText(headerText);
        setClickListener(click);
        setBackButtonClick(this);
        progressDialog = new Progress(this);
        recyclerView = (RecyclerView) findViewById(R.id.chathistory_recyclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        //layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        imgSend = (ImageView) findViewById(R.id.chathistory_imgSend);
        editMessageText = (EditText) findViewById(R.id.chathistory_editMessage);
        imgSend.setOnClickListener(this);
    }

    private void getChatList() {
        if (showProgress) {
            progressDialog.show();
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Key.Chats.NAME);
        query.whereEqualTo(Key.Chats.usersPointer, currentUser);
        query.whereEqualTo(Key.Chats.supervisor, supervisorObj);
        Date midnight = new Date();
        midnight.setTime(System.currentTimeMillis()-(1000*60*60*24*3));
        midnight.setHours(0);
        midnight.setMinutes(0);
        midnight.setSeconds(0);
        query.whereGreaterThan("createdAt", midnight);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> chatList, ParseException e) {
                if (showProgress) {
                    progressDialog.dismiss();
                    showProgress=false;
                }
                if (e == null) {
                    AppLog.d("Retrieved " + chatList.size() + " chatlist");

                    adapterChatHistory = new AdapterChatHistory(ActChatHistory.this, chatList);
                    recyclerView.setAdapter(adapterChatHistory);
                } else {
                    AppLog.d("Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chathistory_imgSend:

                if (adapterChatHistory != null) {
                    adapterChatHistory.addComment(editMessageText, supervisorObj);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(runnable);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getChatList();
            mHandler.postDelayed(runnable, 10000);
        }
    };

}