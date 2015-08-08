package com.maxpro.iguard.activity;

import android.location.Location;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.maxpro.iguard.R;
import com.maxpro.iguard.utility.Func;
import com.maxpro.iguard.utility.Key;
import com.maxpro.iguard.utility.Progress;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ActAlert extends ActDrawer implements View.OnClickListener {
    private EditText editRemark;
    private Button btnRemark;
    private GoogleMap googleMap;
    private double latitude, longitude;
    private SimpleDateFormat sdf;
    private Progress progressDialog;
    private TextView txtTimer;
    private MyTimer myTimer;
    private long remainMilli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_alert);
        initialize();
    }

    private void initialize() {
        myTimer = new MyTimer(5 * 60 * 1000, 1000);
        myTimer.start();
        sdf = new SimpleDateFormat("hh:mm");
        progressDialog = new Progress(this);

        setHeaderText(getString(R.string.dashboard_alerts));
        setClickListener(click);
        setBackButtonClick(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.alert_mapFrag);
        googleMap = mapFragment.getMap();
        btnRemark = (Button) findViewById(R.id.alert_btnPost);
        editRemark = (EditText) findViewById(R.id.alert_edit);
        editRemark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                myTimer.cancel();
                txtTimer.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        txtTimer = (TextView) findViewById(R.id.alert_txtTimer);
        btnRemark.setOnClickListener(this);
        final Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)));
        marker.setDraggable(false);

        if (googleMap != null) {
            googleMap.setMyLocationEnabled(true);
            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    TextView textView = new TextView(ActAlert.this);
                    textView.setText("Alert\nTime:" + sdf.format(new Date()) + "\n" + currentUser.getString("company") + "," + currentUser.getString("userType") + "\n" + ParseUser.getCurrentUser().getUsername());
                    marker.showInfoWindow();
                    return textView;
                }
            });
            googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    if (location != null) {

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        LatLng loc = new LatLng(latitude, longitude);
                        marker.setPosition(loc);


                        MapsInitializer.initialize(ActAlert.this);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 20));
                        // Zoom in, animating the camera.
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                        // Move the camera instantly to hamburg with a zoom of 15.
                        googleMap.setOnMyLocationChangeListener(null);
                    }
                }

            });
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.alert_btnPost:
                clickRemark();
                break;
        }
    }

    private void clickRemark() {

        String remark = editRemark.getText().toString().trim();

        progressDialog.show();
        //ParseUser user= ParseUser.getCurrentUser();

        ParseGeoPoint geoPoint = new ParseGeoPoint(latitude, longitude);
        ParseObject parseObject = new ParseObject("alert");
        parseObject.put(Key.Alert.remark, remark);
        parseObject.put(Key.Alert.userPointer, currentUser.getUsername());
        parseObject.put(Key.Alert.usersPointer, currentUser);
        parseObject.put(Key.Alert.alertGeoPoint, geoPoint);
        parseObject.put(Key.Alert.branch, currentUser.getParseObject(Key.User.branch));
        parseObject.put(Key.Alert.company, currentUser.getParseObject(Key.User.company));
        parseObject.put(Key.Alert.supervisor, currentUser.getParseObject(Key.User.supervisor));
        parseObject.put(Key.Alert.post, currentUser.getParseObject(Key.User.post));
        parseObject.put(Key.Alert.status, "");
        parseObject.put(Key.Alert.site, currentUser.getParseObject(Key.User.site));
        parseObject.put(Key.Alert.read, false);
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                progressDialog.dismiss();
                if (e != null) {
                    Func.showValidDialog(ActAlert.this, "Alert submit failed.");
                    e.printStackTrace();
                } else {
                    Toast.makeText(ActAlert.this, "Alert submitted Successfully.", Toast.LENGTH_SHORT).show();
                    Log.e("ActAlert", "Done");
                    finish();
                }
            }
        });


    }

    private class MyTimer extends CountDownTimer {

        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            remainMilli = l;
            long min = (l / 1000) / 60;
            long sec = (l / 1000) % 60;
            String minute = "" + min;
            String second = "" + sec;
            if (min < 10) {
                minute = "0" + min;
            }
            if (sec < 10) {
                second = "0" + sec;
            }
            txtTimer.setText(minute + ":" + second);
        }

        @Override
        public void onFinish() {
            if (remainMilli < 2) {
                clickRemark();
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myTimer.cancel();
    }
}
