package com.maxpro.iguard.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.maxpro.iguard.R;
import com.maxpro.iguard.activity.ActAttendance;
import com.maxpro.iguard.utility.AppLog;
import com.maxpro.iguard.utility.Func;
import com.maxpro.iguard.utility.Key;
import com.maxpro.iguard.utility.Progress;
import com.maxpro.iguard.utility.Var;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ketan on 3/22/2015.
 */
public class FragAttendance extends Fragment implements View.OnClickListener {
    private LinearLayout linearIn, linearOut;
    private TextView txtIn, txtOut;
    private ImageView imgIn, imgOut;
    private MapView mapView;
    private GoogleMap googleMap;
    private double latitude, longitude;
    private String submitType = "1";
    private String submitTime = "07/02/2015 08:00";
    private String imageFilePath;
    private LatLng loc;

    private Progress progressDialog;
    private Bitmap photo;

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        ((ActAttendance) getActivity()).btnDone.setVisibility(View.VISIBLE);
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        ((ActAttendance) getActivity()).btnDone.setVisibility(View.GONE);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /*@Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_attendance, container, false);
        mapView = (MapView) rootView.findViewById(R.id.fragattend_mapview);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        init(rootView);
        if (!Func.isLocationEnable(getActivity())) {
            Func.showValidDialog(getActivity(), getString(R.string.msg_location_enable));
        }
        return rootView;
    }

    private void init(View rootView) {

        submitType = Func.getSpString(getActivity(), Var.TYPE_ATTEND, Var.TYPE_ATTEND_IN);

        progressDialog = new Progress(getActivity());
        imageFilePath = Func.getFilePath();
        ((ActAttendance) getActivity()).btnDone.setOnClickListener(this);
        imgIn = (ImageView) rootView.findViewById(R.id.fragattend_imgIn);
        imgOut = (ImageView) rootView.findViewById(R.id.fragattend_imgOut);
        txtIn = (TextView) rootView.findViewById(R.id.fragattend_txtIn);
        txtOut = (TextView) rootView.findViewById(R.id.fragattend_txtOut);
        linearIn = (LinearLayout) rootView.findViewById(R.id.fragattend_linearIn);
        linearOut = (LinearLayout) rootView.findViewById(R.id.fragattend_linearOut);
        googleMap = mapView.getMap();
        googleMap.setMyLocationEnabled(true);
        loc = new LatLng(latitude, longitude);
        final Marker mMarker = googleMap.addMarker(new MarkerOptions().position(loc));
        mMarker.setDraggable(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Log.e("current loc", "lat= " + latitude + " long= " + longitude);
                    loc = new LatLng(latitude, longitude);
                    mMarker.setPosition(loc);
                    if (googleMap != null) {
                        MapsInitializer.initialize(getActivity());
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 20));
                        // Zoom in, animating the camera.
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                        // Move the camera instantly to hamburg with a zoom of 15.

                    }
                    googleMap.setOnMyLocationChangeListener(null);
                }
            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String date = sdf.format(new Date());
        sdf = new SimpleDateFormat("hh:mm");
        String time = sdf.format(new Date());
        submitTime = date + " " + time;
        if (submitType.equals(Var.TYPE_ATTEND_OUT)) {
            linearOut.setOnClickListener(this);
            txtOut.setText(date + "\nOut time " + time);
            String img = Func.getSpString(getActivity(), Var.ATTEND_IMAGE, "");
            Bitmap bt = BitmapFactory.decodeFile(img);
            if (bt != null) {
                imgIn.setImageBitmap(bt);
            }
            txtIn.setText(Func.getSpString(getActivity(), Var.ATTEND_IN_TIME, ""));
        } else {
            linearIn.setOnClickListener(this);
            txtIn.setText(date + "\nIn time " + time);
            Func.setSpString(getActivity(), Var.ATTEND_IN_TIME, txtIn.getText().toString());

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragattend_linearIn:
                submitType = Var.TYPE_ATTEND_IN;
                selectImage();
                break;
            case R.id.fragattend_linearOut:
                submitType = Var.TYPE_ATTEND_OUT;
                selectImage();
                break;
            case R.id.header_btnDone:
                uploadToParse();
                break;
        }
    }

    private void selectImage() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(imageFilePath)));
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 100:
                    //photo = (Bitmap) data.getExtras().get("data");
                    photo = BitmapFactory.decodeFile(imageFilePath);
                    photo=Bitmap.createScaledBitmap(photo,200,200,false);
                    if (submitType.equals(Var.TYPE_ATTEND_OUT)) {
                        imgOut.setImageBitmap(photo);
                    } else {
                        imgIn.setImageBitmap(photo);
                    }
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadToParse() {
        if (photo == null) return;

        ParseUser user = ParseUser.getCurrentUser();
        ParseObject userPost=user.getParseObject(Key.User.post);
        ParseObject userShift=user.getParseObject(Key.User.shift);
        ParseObject userCompany=user.getParseObject(Key.User.company);
        ParseObject userSite=user.getParseObject(Key.User.site);
        ParseObject userSupervisor=user.getParseObject(Key.User.supervisor);
        if(userCompany==null){
            Func.showValidDialog(getActivity(),"You do not have assigned Company.");
            return;
        }
        if(userShift==null){
            Func.showValidDialog(getActivity(),"You do not have assigned Shift.");
            return;
        }
        if(userPost==null){
            Func.showValidDialog(getActivity(),"You do not have assigned Post.");
            return;
        }
        if(userSite==null){
            Func.showValidDialog(getActivity(),"You do not have assigned Site.");
            return;
        }
        if(userSupervisor==null){
            Func.showValidDialog(getActivity(),"You do not have assigned Supervisor.");
            return;
        }
        progressDialog.show();
        ParseGeoPoint geoPoint = new ParseGeoPoint(latitude, longitude);
        // bm = BitmapFactory.decodeFile(imageFilePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        ParseFile imageFile = new ParseFile("photoimage.png", b);

        ParseObject attendObject = new ParseObject(Key.Attendance.NAME);
        attendObject.put(Key.Attendance.submitTime, submitTime);
        attendObject.put(Key.Attendance.submitTimeGeoPoint, geoPoint);
        attendObject.put(Key.Attendance.submitType, submitType);
        attendObject.put(Key.Attendance.submitTimePhoto, imageFile);
        attendObject.put(Key.Attendance.usersPointer, user);
        attendObject.put(Key.Attendance.branch, user.getParseObject(Key.User.branch));
        attendObject.put(Key.Attendance.company, userCompany);
        attendObject.put(Key.Attendance.shift, userShift);
        attendObject.put(Key.Attendance.supervisor, userSupervisor);
        attendObject.put(Key.Attendance.site, userSite);
        attendObject.put(Key.Attendance.post, userPost);
        ParseGeoPoint postGeoPoint=userPost.getParseGeoPoint(Key.Post.postLocation);
        if (geoPoint != null && postGeoPoint != null) {
            double distance=geoPoint.distanceInKilometersTo(postGeoPoint);
            String dist=(distance*1000)+"";
            attendObject.put(Key.Attendance.statusByDistance, dist);
        }
        String currentDate = Func.getCurrentDate(Var.DF_DATE);
        String shiftTime=userShift.getString(Key.Shift.shiftInTime);
        long shiftInTime = Func.getMillis(Var.DF_DATETIME, currentDate + " " + shiftTime);
        long currentTime= Func.getMillis(Var.DF_DATETIME, Func.getCurrentDate(Var.DF_DATETIME));
        long diff=Math.abs(currentTime - shiftInTime);
        long minutes=diff/60000;
        attendObject.put(Key.Attendance.statusByTime,minutes+"");
        attendObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                progressDialog.dismiss();
                if (e == null) {
                    AppLog.d("Attendance Done");
                    if (submitType.equals(Var.TYPE_ATTEND_OUT)) {
                        Func.setSpString(getActivity(), Var.TYPE_ATTEND, Var.TYPE_ATTEND_IN);
                    } else {
                        Func.setSpString(getActivity(), Var.TYPE_ATTEND, Var.TYPE_ATTEND_OUT);
                        Func.setSpString(getActivity(), Var.ATTEND_IMAGE, imageFilePath);
                    }
                    Toast.makeText(getActivity(), "Attendance submitted Successfully.", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                } else {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Attendance submitted Successfully.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
