package com.maxpro.iguard.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.maxpro.iguard.R;
import com.maxpro.iguard.adapter.AdapterAttendRecord;
import com.maxpro.iguard.adapter.AdapterVideoTraining;
import com.maxpro.iguard.utility.AppLog;
import com.maxpro.iguard.utility.Key;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ActVideoTraining extends ActDrawer {
private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_videotraining);
        initialize();
        getVideoList();
    }

    private void initialize() {
        setBackButtonClick(this);
        setClickListener(click);
        setHeaderText(getString(R.string.videotraining_header));
        recyclerView=(RecyclerView) findViewById(R.id.videotraining_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void getVideoList(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Key.Video.NAME);
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery(Key.Video.NAME);
        List<ParseQuery<ParseObject>>queries=new ArrayList<ParseQuery<ParseObject>>();

        ParseObject branch=currentUser.getParseObject(Key.User.branch);

        query.whereEqualTo(Key.Video.branch, branch);
        query.whereNotEqualTo(Key.Video.deleted,true);

        query2.whereDoesNotExist(Key.Video.branch);
        query2.whereNotEqualTo(Key.Video.deleted,true);

        queries.add(query);
        queries.add(query2);
        ParseQuery finalQuery=ParseQuery.or(queries);
        finalQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> videoList, ParseException e) {
                if (e == null) {
                    AppLog.d("Retrieved " + videoList.size() + " video");
                    AdapterVideoTraining mAdapter = new AdapterVideoTraining(ActVideoTraining.this,videoList);
                    recyclerView.setAdapter(mAdapter);
                } else {
                    AppLog.d( "Error: " + e.getMessage());
                }
            }
        });
    }
}
