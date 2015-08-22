package com.maxpro.iguard.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maxpro.iguard.R;
import com.maxpro.iguard.adapter.AdapterHistorical;
import com.maxpro.iguard.adapter.AdapterVisits;
import com.maxpro.iguard.utility.Key;
import com.maxpro.iguard.utility.Progress;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;
import java.util.List;

/**
 * Created by Ketan on 5/20/2015.
 */
public class FragHistorical extends Fragment {
    private RecyclerView recyclerView;
    Progress progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.frag_historical,container,false);
        init(rootView);
        return rootView;
    }

    private void init(View rootView) {
        progressDialog=new Progress(getActivity());
        recyclerView= (RecyclerView) rootView.findViewById(R.id.fraghistorical_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getHistoricalVisit();
    }

    private void getHistoricalVisit() {
        progressDialog.show();
        ParseQuery<ParseObject> parseQuery=ParseQuery.getQuery(Key.Visits.NAME);

        Date midnight = new Date();
        midnight.setTime(System.currentTimeMillis());
        midnight.setHours(0);
        midnight.setMinutes(0);
        midnight.setSeconds(0);
        //parseQuery.whereLessThan("createdAt", midnight);
        parseQuery.whereExists(Key.Visits.dateOut);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                progressDialog.dismiss();
                if (e == null) {
                    AdapterHistorical adapterHistorical=new AdapterHistorical(getActivity(),parseObjects);
                    recyclerView.setAdapter(adapterHistorical);
                }else{
                    e.printStackTrace();
                }
            }
        });
    }
}
