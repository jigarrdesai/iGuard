package com.maxpro.iguard.activity;

import com.maxpro.iguard.R;
import com.maxpro.iguard.adapter.AdapterLeave;
import com.maxpro.iguard.adapter.AdapterMsgBoard;
import com.maxpro.iguard.utility.AppLog;
import com.maxpro.iguard.utility.Key;
import com.maxpro.iguard.utility.Progress;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActMsgBoard extends ActDrawer {
	private RecyclerView recyclerView;
    Progress progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_msgboard);
       initialize();
        getMsgList();
	}

    private void initialize() {
        setHeaderText(getString(R.string.dashboard_messageboard));
        setClickListener(click);
        setBackButtonClick(this);
        progressDialog=new Progress(this);
        recyclerView=(RecyclerView) findViewById(R.id.msgboard_recyclerview);

        LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void getMsgList(){
        progressDialog.show();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Key.MsgBoard.NAME);
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery(Key.MsgBoard.NAME);
        Date midnight = new Date();
        midnight.setTime(System.currentTimeMillis()-(1000*60*60*24*15));
        midnight.setHours(0);
        midnight.setMinutes(0);
        midnight.setSeconds(0);
        query.whereGreaterThan("createdAt", midnight);
        query2.whereGreaterThan("createdAt", midnight);
        ParseObject branch=currentUser.getParseObject(Key.User.branch);
        query.whereEqualTo(Key.MsgBoard.branch, branch);
        query.whereNotEqualTo(Key.MsgBoard.deleted,true);
        query2.whereDoesNotExist(Key.MsgBoard.branch);
        query2.whereNotEqualTo(Key.MsgBoard.deleted,true);
        List<ParseQuery<ParseObject>>queries=new ArrayList<ParseQuery<ParseObject>>();
        queries.add(query);
        queries.add(query2);
        ParseQuery finalQuery=ParseQuery.or(queries);
        finalQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> msgList, ParseException e) {
                progressDialog.dismiss();
                if (e == null) {
                    //AppLog.d("Retrieved " + msgList.size() + " message");
                    AdapterMsgBoard adapterMsgBoard = new AdapterMsgBoard(ActMsgBoard.this,msgList);
                    recyclerView.setAdapter(adapterMsgBoard);
                } else {
                    AppLog.d( "Error: " + e.getMessage());
                }
            }
        });
    }
}
