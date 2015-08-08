package com.maxpro.iguard.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.maxpro.iguard.R;
import com.maxpro.iguard.adapter.AdapterReplace;
import com.maxpro.iguard.utility.Key;
import com.maxpro.iguard.utility.Progress;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class ActReplaceList extends ActDrawer {
    private RecyclerView recyclerView;
    private Progress progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_replacelist);

        init();
        getReplacementList();
    }

    private void init() {
        progressDialog = new Progress(this);
        setHeaderText(getString(R.string.replacement_replacement));
        setClickListener(click);
        setBackButtonClick(this);
        recyclerView = (RecyclerView) findViewById(R.id.replacelist_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getReplacementList() {
        progressDialog.show();
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(Key.Replacement.NAME);
        parseQuery.whereEqualTo(Key.Replacement.userPointer, currentUser);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                progressDialog.dismiss();
                if (e == null) {
                    AdapterReplace adapterReplace = new AdapterReplace(ActReplaceList.this, parseObjects);
                    recyclerView.setAdapter(adapterReplace);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
