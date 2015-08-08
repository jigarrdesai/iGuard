package com.maxpro.iguard.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.maxpro.iguard.R;
import com.maxpro.iguard.fragment.FragAddVisits;
import com.maxpro.iguard.fragment.FragAttendance;
import com.maxpro.iguard.fragment.FragAttendanceRecord;
import com.maxpro.iguard.fragment.FragHistorical;
import com.maxpro.iguard.fragment.FragVisits;
import com.maxpro.iguard.utility.Key;
import com.maxpro.iguard.utility.Var;
import com.parse.ParseFile;
import com.parse.ParseObject;

public class ActVisits extends ActDrawer  implements View.OnClickListener {
    private Button btnToday,btnAddVisit,btnHistorical;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_visits);
        initialize();
    }

    private void initialize() {
        setClickListener(click);
        setBackButtonClick(this);
        setHeaderText(getString(R.string.dashboard_visits));
        btnToday=(Button) findViewById(R.id.visit_btnToday);
        btnAddVisit=(Button) findViewById(R.id.visit_btnAddVisit);
        btnHistorical=(Button) findViewById(R.id.visit_btnHistorical);
        imgBack.setOnClickListener(this);
        btnAddVisit.setOnClickListener(this);
        btnHistorical.setOnClickListener(this);
        btnToday.setOnClickListener(this);
        btnToday.performClick();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_imgArrow:
                finish();
                break;
            case R.id.visit_btnAddVisit:
                getSupportFragmentManager().beginTransaction().replace(R.id.visit_frameContainer,new FragAddVisits()).commit();


                btnToday.setSelected(false);
                btnAddVisit.setSelected(true);
                btnHistorical.setSelected(false);
                break;
            case R.id.visit_btnToday:

                getSupportFragmentManager().beginTransaction().replace(R.id.visit_frameContainer,new FragVisits()).commit();


                btnToday.setSelected(true);
                btnAddVisit.setSelected(false);
                btnHistorical.setSelected(false);
                break;
            case R.id.visit_btnHistorical:

                getSupportFragmentManager().beginTransaction().replace(R.id.visit_frameContainer,new FragHistorical()).commit();


                btnToday.setSelected(false);
                btnAddVisit.setSelected(false);
                btnHistorical.setSelected(true);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    public void onVisitsClick(ParseObject visit){
        Bundle bundle=new Bundle();
        bundle.putString(Var.IntentObjId,visit.getObjectId());
        bundle.putString(Key.Visits.dateIn, visit.getString(Key.Visits.dateIn));
        bundle.putString(Key.Visits.dateOut, visit.getString(Key.Visits.dateOut));
        bundle.putString(Key.Visits.timeIn, visit.getString(Key.Visits.timeIn));
        bundle.putString(Key.Visits.timeOut, visit.getString(Key.Visits.timeOut));
        ParseFile file=visit.getParseFile(Key.Visits.visitPhoto);
        if(file!=null) {
            bundle.putString(Key.Visits.visitPhoto, file.getUrl());
        }
        bundle.putString(Key.Visits.visitor, visit.getString(Key.Visits.visitor));
        bundle.putString(Key.Visits.purpose, visit.getString(Key.Visits.purpose));
        bundle.putString(Key.Visits.peopleCount, visit.getString(Key.Visits.peopleCount));
        bundle.putString(Key.Visits.vehicleNum, visit.getString(Key.Visits.vehicleNum));

        FragAddVisits fragAddVisit=new FragAddVisits();
        fragAddVisit.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.visit_frameContainer,fragAddVisit).commit();


        btnToday.setSelected(false);
        btnAddVisit.setSelected(true);
        btnHistorical.setSelected(false);
    }
}
