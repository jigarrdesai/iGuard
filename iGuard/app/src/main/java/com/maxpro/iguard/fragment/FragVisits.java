package com.maxpro.iguard.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maxpro.iguard.R;
import com.maxpro.iguard.activity.ActVisits;
import com.maxpro.iguard.adapter.AdapterVisits;
import com.maxpro.iguard.utility.Func;
import com.maxpro.iguard.utility.Key;
import com.maxpro.iguard.utility.Progress;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Ketan on 5/20/2015.
 */
public class FragVisits extends Fragment {
    private RecyclerView recyclerView;
    private Progress progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.frag_visits,container,false);
        init(rootView);
        return rootView;
    }
    private void init(View rootView) {
        progressDialog=new Progress(getActivity());
        recyclerView= (RecyclerView) rootView.findViewById(R.id.fragvisits_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getTodayVisit();
    }

    private void getTodayVisit() {
        progressDialog.show();
        ParseQuery<ParseObject> parseQuery=ParseQuery.getQuery(Key.Visits.NAME);
        Date midnight = new Date();
        midnight.setHours(0);
        midnight.setMinutes(0);
        midnight.setSeconds(0);

        Date elevenfiftynine = new Date();
        elevenfiftynine.setHours(23);
        elevenfiftynine.setMinutes(59);
        elevenfiftynine.setSeconds(59);

        parseQuery.whereGreaterThan("createdAt", midnight);
        parseQuery.whereLessThan("createdAt", elevenfiftynine);
        parseQuery.whereDoesNotExist(Key.Visits.dateOut);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                progressDialog.dismiss();
                if (e == null) {
                    AdapterVisits adapterVisits=new AdapterVisits((ActVisits)getActivity(),parseObjects);
                    recyclerView.setAdapter(adapterVisits);
                }else{
                    e.printStackTrace();
                }
            }
        });
    }
}
