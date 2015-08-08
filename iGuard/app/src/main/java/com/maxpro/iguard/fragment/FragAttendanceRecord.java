package com.maxpro.iguard.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maxpro.iguard.R;
import com.maxpro.iguard.adapter.AdapterAttendRecord;
import com.maxpro.iguard.adapter.AdapterMsgBoard;
import com.maxpro.iguard.utility.AppLog;
import com.maxpro.iguard.utility.Func;
import com.maxpro.iguard.utility.Key;
import com.maxpro.iguard.utility.Var;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ketan on 3/22/2015.
 */
public class FragAttendanceRecord extends Fragment{

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.frag_attendance_record,container,false);
        initialize(rootView);
        return rootView;
    }

    private void initialize(View view) {
        recyclerView=(RecyclerView) view.findViewById(R.id.fragattendrecord_recyclerview);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        getAttendanceList();

    }

    private void getAttendanceList(){
        ParseUser currentUser=ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("attendance");
        query.whereEqualTo(Key.Attendance.usersPointer, currentUser);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> attendList, ParseException e) {
                if (e == null) {
                   // AppLog.d("Retrieved " + attendList.size() + " attendance");

                    HashMap<String, List<ParseObject>> hashMap=new HashMap<String, List<ParseObject>>();
                    ArrayList<String> keys=new ArrayList<String>();
                    for(ParseObject po:attendList) {
                     String submitDate=po.getString(Key.Attendance.submitTime);
                        String date= Func.convertDateFormat("dd/MM/yyyy hh:mm",Var.DF_DATE,submitDate);
                        if (!hashMap.containsKey(date)) {
                            List<ParseObject> list = new ArrayList<ParseObject>();
                            list.add(po);
                            hashMap.put(date, list);
                            keys.add(date);
                        } else
                            hashMap.get(date).add(po);
                    }
                    AdapterAttendRecord mAdapter = new AdapterAttendRecord(keys,hashMap);
                    recyclerView.setAdapter(mAdapter);
                } else {
                    AppLog.d( "Error: " + e.getMessage());
                }
            }
        });
    }
}
