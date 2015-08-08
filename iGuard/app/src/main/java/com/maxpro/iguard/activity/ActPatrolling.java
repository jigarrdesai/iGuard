package com.maxpro.iguard.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.maxpro.iguard.R;
import com.maxpro.iguard.adapter.AdapterLeave;
import com.maxpro.iguard.adapter.AdapterPatrolling;
import com.maxpro.iguard.adapter.AdapterTasks;
import com.maxpro.iguard.db.TblAutoPatrolling;
import com.maxpro.iguard.db.TblTask;
import com.maxpro.iguard.model.ModelAutoPatrolling;
import com.maxpro.iguard.model.ModelTask;
import com.maxpro.iguard.receiver.ReceiverAlarm;
import com.maxpro.iguard.utility.AppLog;
import com.maxpro.iguard.utility.Func;
import com.maxpro.iguard.utility.Key;
import com.maxpro.iguard.utility.Progress;
import com.maxpro.iguard.utility.Var;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ActPatrolling extends ActDrawer implements com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private RecyclerView recyclerView;
    private AdapterPatrolling adapterPatrolling;
    public String lastPatrollingFetch;
    Progress progressDialog;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    public double latitude, longitude;
    private LocationRequest mLocationRequest;
    public boolean isFromNoti=false;
    public String objId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_patrolling);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null){
            objId=bundle.getString(Var.IntentObjId);
            isFromNoti=true;
            Log.e("newIntent","onCreate objid"+objId);
        }
        init();

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("newIntent", "onNewIntent");
        Bundle bundle=intent.getExtras();
        if(bundle!=null){

            objId=bundle.getString(Var.IntentObjId);
            isFromNoti=true;
            Log.e("newIntent","onNewIntent objid"+objId);
        }
        init();
    }
    private void init() {
        progressDialog = new Progress(this);
        setHeaderText(getString(R.string.dashboard_patrolling));
        setClickListener(click);
        setBackButtonClick(this);
        recyclerView = (RecyclerView) findViewById(R.id.patrolling_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        lastPatrollingFetch = (String) Func.getSpString(this, Var.Last_FetchDatePatrolling, "01-01-1991");
        String today = Func.getCurrentDate(Var.DF_DATE);
        if (isFetch(today, lastPatrollingFetch)) {
            TblAutoPatrolling.deleteAllPatrolling();
            getAutoPatrollingListFromServer();
            Func.setSpString(this, Var.Last_FetchDatePatrolling, today);
        } else {
            getPatrollingListFromDb();
        }
        buildGoogleApiClient();
        createLocationRequest();

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }
    private void getAutoPatrollingListFromServer() {
        progressDialog.show();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Key.AutoPatrolling.NAME);
        ParseObject shift = currentUser.getParseObject(Key.User.shift);
        query.whereEqualTo(Key.AutoPatrolling.shift, shift);
        query.whereNotEqualTo(Key.Cars.deleted,true);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> autoPatrollingList, ParseException e) {
                if (e == null && autoPatrollingList != null) {
                    progressDialog.dismiss();
                    AppLog.d("Retrieved " + autoPatrollingList.size() + " autopatrolling");

                    for (ParseObject obj : autoPatrollingList) {
                        ModelAutoPatrolling model = new ModelAutoPatrolling();
                        //Log.e("task pointer","user= "+obj.getParseObject(Key.Task.userPointer).getObjectId());
                        model.objectId = obj.getObjectId();
                        try {
                            model.company = obj.getParseObject(Key.AutoPatrolling.company).fetchIfNeeded().getObjectId();
                            model.branch = obj.getParseObject(Key.AutoPatrolling.branch).fetchIfNeeded().getObjectId();
                            model.supervisor = obj.getParseObject(Key.AutoPatrolling.supervisor).fetchIfNeeded().getObjectId();
                            model.userPointer = obj.getParseObject(Key.AutoPatrolling.userPointer).fetchIfNeeded().getObjectId();
                            model.shift = obj.getParseObject(Key.AutoPatrolling.shift).fetchIfNeeded().getObjectId();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }


                        model.type = obj.getString(Key.AutoPatrolling.type);
                        model.patrollingDate = obj.getString(Key.AutoPatrolling.patrollingDate);
                        model.patrollingTime = obj.getString(Key.AutoPatrolling.patrollingTime);


                        model.patrollingEndTime = obj.getString(Key.AutoPatrolling.patrollingEndTime);

                        model.isComplete = "0";

                        String currentDate = Func.getCurrentDate(Var.DF_DATE);

                        // long start=Func.getMillis(Var.DF_DATETIME,currentDate+" "+model.patrollingTime);
                        // long end=Func.getMillis(Var.DF_DATETIME,currentDate+" "+model.patrollingEndTime);
                        long patrollingTime=Func.getMillis(Var.DF_DATETIME,currentDate+" "+model.patrollingTime);
                        String inTime = currentUser.getParseObject(Key.User.shift).getString("shiftInTime");
                        String outTime = currentUser.getParseObject(Key.User.shift).getString("shiftOutTime");
                        long start = Func.getMillis(Var.DF_DATETIME, currentDate + " " + inTime);
                        long end = Func.getMillis(Var.DF_DATETIME, currentDate + " " + outTime);
                        Random rand = new Random();
                        long diff = start + ((long) (rand.nextDouble() * (end - start)));
                        Log.e("milli", "start= " + start + " end= " + end + " diff= " + diff);
                        Log.e("milli", "objectId = " + model.objectId);
                        Log.e("milli", "start= " + Func.getStringFromMilli(Var.DF_DATETIME, start) + " end= " + Func.getStringFromMilli(Var.DF_DATETIME, end) + " diff= " + Func.getStringFromMilli(Var.DF_DATETIME, diff));
                        if (patrollingTime>start && patrollingTime<end && patrollingTime>= System.currentTimeMillis()) {
                            long randomTime= 6000 + ((long) (rand.nextDouble() * (15*60*1000 - 6000)));
                            Log.e("milli", "randomTime= " + Func.getStringFromMilli(Var.DF_DATETIME, randomTime));
                            Log.e("milli", "patrollingTime= " + Func.getStringFromMilli(Var.DF_DATETIME, patrollingTime));
                            Log.e("milli", "actualTime= " + Func.getStringFromMilli(Var.DF_DATETIME, patrollingTime-randomTime));
                            Log.e("milli", "future");
                            Intent myIntent = new Intent(ActPatrolling.this, ReceiverAlarm.class);
                            myIntent.putExtra(Var.IntentObjId, model.objectId);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(ActPatrolling.this, (int) System.currentTimeMillis(), myIntent, 0);

                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            alarmManager.set(AlarmManager.RTC, patrollingTime-randomTime, pendingIntent);
                        } else {
                            Log.e("milli", "past");
                        }
                        TblAutoPatrolling.insertPatrolling(model);
                    }
                    getPatrollingListFromDb();
                } else {
                    AppLog.d("Error: " + e.getMessage());
                }
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Var.REQ_CODE_CAPTURE:
                    //photo = (Bitmap) data.getExtras().get("data");
                    adapterPatrolling.onActivityResult(data);
                    break;
            }
        }else {
            objId="";
            isFromNoti=false;
            getPatrollingListFromDb();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isFetch(String todayDate, String lastDate) {
        boolean isFetch = false;
        try {

            SimpleDateFormat formatter = new SimpleDateFormat(Var.DF_DATE);

            Date date1 = formatter.parse(todayDate);
            Date date2 = formatter.parse(lastDate);

            if (date2.before(date1)) {
                isFetch = true;
            } else {
                isFetch = false;
            }

        } catch (java.text.ParseException e1) {
            e1.printStackTrace();
        }
        Log.e("autopatrolling", "todayDate= " + todayDate);
        Log.e("autopatrolling", "lastDate= " + lastDate);
        Log.e("autopatrolling", "isFetch= " + isFetch);
        return isFetch;
    }

    private void getPatrollingListFromDb() {

        progressDialog.show();
        ArrayList<ModelAutoPatrolling> arrModelPatrolling;
        if(isFromNoti){
            arrModelPatrolling = TblAutoPatrolling.selectCompletedOrSpecific(objId);
        }else {
            arrModelPatrolling = TblAutoPatrolling.selectCompletedPatrolling();
        }
        progressDialog.dismiss();


        adapterPatrolling = new AdapterPatrolling(ActPatrolling.this, arrModelPatrolling);
        recyclerView.setAdapter(adapterPatrolling);

    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        }
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }
}
