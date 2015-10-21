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
import com.maxpro.iguard.IGuard;
import com.maxpro.iguard.R;
import com.maxpro.iguard.activity.ActAttendance;
import com.maxpro.iguard.utility.AppLog;
import com.maxpro.iguard.utility.Func;
import com.maxpro.iguard.utility.Key;
import com.maxpro.iguard.utility.Progress;
import com.maxpro.iguard.utility.Var;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import edu.sfsu.cs.orange.ocr.CaptureActivity;

/**
 * Created by Ketan on 3/22/2015.
 */
public class FragAttendance extends Fragment implements View.OnClickListener {
    private LinearLayout linearIn, linearOut;
    private TextView txtIn, txtOut,txtValidation;
    private ImageView imgIn, imgOut;
    private MapView mapView;
    private GoogleMap googleMap;
    private double latitude, longitude;
    private String submitType = "1";
    private String submitTime = "07/02/2015 08:00";
    private String imageFilePath,imageTagPath;
    private ParseFile submitPhoto;
    private LatLng loc;
    public final String DATETIME = "dd/MM/yyyy hh:mm";
    private Progress progressDialog;
    private Bitmap photo,tagPhoto;
    private String ocrId;

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

        //submitType = Func.getSpString(getActivity(), Var.TYPE_ATTEND, Var.TYPE_ATTEND_IN);

        progressDialog = new Progress(getActivity());
        imageFilePath = Func.getFilePath();
        ((ActAttendance) getActivity()).btnDone.setOnClickListener(this);
        imgIn = (ImageView) rootView.findViewById(R.id.fragattend_imgIn);
        imgOut = (ImageView) rootView.findViewById(R.id.fragattend_imgOut);
        txtIn = (TextView) rootView.findViewById(R.id.fragattend_txtIn);
        txtOut = (TextView) rootView.findViewById(R.id.fragattend_txtOut);
        txtValidation=(TextView) rootView.findViewById(R.id.fragattend_txtValidation);
        txtValidation.setOnClickListener(FragAttendance.this);
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
        getAttendanceFromParse();
    }

    private void getAttendanceFromParse() {

        progressDialog.show();
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseObject shift = currentUser.getParseObject(Key.User.shift);
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Key.Attendance.NAME);
        query.whereEqualTo(Key.Attendance.usersPointer, currentUser);
        if (shift != null) {
            String inTime = shift.getString(Key.Shift.shiftInTime);
            String today = Func.getCurrentDate(Var.DF_DATE);
            Date currentDate = new Date(Func.getMillis(Var.DF_DATETIME, today + " " + inTime));
            query.whereGreaterThan("createdAt", currentDate);
        }
        query.orderByDescending("createdAt");
        query.setLimit(1);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null && list != null && list.size() > 0) {
                    submitType = list.get(0).getString(Key.Attendance.submitType);
//Log.e("submitType","submitType= "+submitType);
                    if (submitType.equals(Var.TYPE_ATTEND_IN)) {
                        submitType = Var.TYPE_ATTEND_OUT;
                        submitTime = list.get(0).getString(Key.Attendance.submitTime);
                        submitPhoto = list.get(0).getParseFile(Key.Attendance.submitTimePhoto);
                    } else {
                        submitType = Var.TYPE_ATTEND_IN;
                        submitTime = Func.getCurrentDate(DATETIME);
                    }
                } else {
                    //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                    submitType = Var.TYPE_ATTEND_IN;
                    submitTime = Func.getCurrentDate(DATETIME);
                    //String date = sdf.format(new Date());
                }
                String date = submitTime.split(" ")[0];
                String time = submitTime.split(" ")[1];

                if (submitType.equals(Var.TYPE_ATTEND_OUT)) {
                    linearOut.setOnClickListener(FragAttendance.this);
                    txtOut.setText(date + "\nOut time " + time);
                    /*String img = Func.getSpString(getActivity(), Var.ATTEND_IMAGE, "");
                    Bitmap bt = BitmapFactory.decodeFile(img);
                    if (bt != null) {
                        imgIn.setImageBitmap(bt);
                    }*/
                    if (submitPhoto != null) {
                        IGuard.imageLoader.displayImage(submitPhoto.getUrl(), imgIn, Func.getDisplayOption());
                    }
                    //txtIn.setText(Func.getSpString(getActivity(), Var.ATTEND_IN_TIME, ""));
                    txtIn.setText(date + "\nIn time " + time);
                } else {
                    linearIn.setOnClickListener(FragAttendance.this);
                    txtIn.setText(date + "\nIn time " + time);
                    Func.setSpString(getActivity(), Var.ATTEND_IN_TIME, txtIn.getText().toString());

                }
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragattend_linearIn:
                submitType = Var.TYPE_ATTEND_IN;
                captureImage();
                break;
            case R.id.fragattend_linearOut:
                submitType = Var.TYPE_ATTEND_OUT;
                captureImage();
                break;
            case R.id.fragattend_txtValidation:
                selectImage();
                break;
            case R.id.header_btnDone:
                uploadToParse();
                break;
        }
    }

    private void selectImage() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        imageTagPath=Func.getFilePath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageTagPath);
        startActivityForResult(intent, 100);
    }
    private void captureImage() {
        imageFilePath=Func.getFilePath();
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(imageFilePath)));

        startActivityForResult(intent, 200);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 100:
                    ocrId = data.getStringExtra(Intent.EXTRA_TEXT);
                    txtValidation.setText(ocrId);

                     tagPhoto = BitmapFactory.decodeFile(imageTagPath);
                    tagPhoto = Bitmap.createScaledBitmap(tagPhoto, 200, 200, false);
                    break;
                case 200:
                    //photo = (Bitmap) data.getExtras().get("data");
                    photo = BitmapFactory.decodeFile(imageFilePath);
                    photo = Bitmap.createScaledBitmap(photo, 200, 200, false);

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
        if (photo == null||tagPhoto==null) return;

        ParseUser user = ParseUser.getCurrentUser();
        ParseObject userPost = user.getParseObject(Key.User.post);
        ParseObject userShift = user.getParseObject(Key.User.shift);
        ParseObject userCompany = user.getParseObject(Key.User.company);
        ParseObject userSite = user.getParseObject(Key.User.site);
        ParseObject userSupervisor = user.getParseObject(Key.User.supervisor);
        if (userCompany == null) {
            Func.showValidDialog(getActivity(), "You do not have assigned Company.");
            return;
        }
        if (userShift == null) {
            Func.showValidDialog(getActivity(), "You do not have assigned Shift.");
            return;
        }
        if (userPost == null) {
            Func.showValidDialog(getActivity(), "You do not have assigned Post.");
            return;
        }
        if (userSite == null) {
            Func.showValidDialog(getActivity(), "You do not have assigned Site.");
            return;
        }
        if (userSupervisor == null) {
            Func.showValidDialog(getActivity(), "You do not have assigned Supervisor.");
            return;
        }
        String postTag=userPost.getString(Key.Post.postTag);
        int tagPercent=Func.LevenshteinDistance(ocrId, postTag);
        int maxLength=0;
        if(postTag!=null&&ocrId!=null){
            maxLength=Math.max(postTag.length(),ocrId.length());
        }

        if(tagPercent>=maxLength/2){
            Func.showValidDialog(getActivity(), "Your post tag does not match.");
            return;
        }
        progressDialog.show();
        ParseGeoPoint geoPoint = new ParseGeoPoint(latitude, longitude);
        // bm = BitmapFactory.decodeFile(imageFilePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        ParseFile imageFile = new ParseFile("photoimage.png", b);

        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        tagPhoto.compress(Bitmap.CompressFormat.JPEG, 100, baos2); //bm is the bitmap object
        byte[] b2 = baos2.toByteArray();
        ParseFile imageTagFile2 = new ParseFile("tagphoto.png", b2);

        ParseObject attendObject = new ParseObject(Key.Attendance.NAME);
        attendObject.put(Key.Attendance.submitTime, Func.getCurrentDate(DATETIME));
        attendObject.put(Key.Attendance.submitTimeGeoPoint, geoPoint);
        attendObject.put(Key.Attendance.submitType, submitType);
        attendObject.put(Key.Attendance.submitTimePhoto, imageFile);
        attendObject.put(Key.Attendance.tagPhoto, imageTagFile2);
        attendObject.put(Key.Attendance.usersPointer, user);
        attendObject.put(Key.Attendance.branch, user.getParseObject(Key.User.branch));
        attendObject.put(Key.Attendance.company, userCompany);
        attendObject.put(Key.Attendance.shift, userShift);
        attendObject.put(Key.Attendance.supervisor, userSupervisor);
        attendObject.put(Key.Attendance.site, userSite);
        attendObject.put(Key.Attendance.post, userPost);
        attendObject.put(Key.Attendance.ocrId,ocrId);
        ParseGeoPoint postGeoPoint = userPost.getParseGeoPoint(Key.Post.postLocation);
        if (geoPoint != null && postGeoPoint != null) {
            double distance = geoPoint.distanceInKilometersTo(postGeoPoint);
            String dist = (distance * 1000) + "";
            attendObject.put(Key.Attendance.statusByDistance, dist);
        }
        String currentDate = Func.getCurrentDate(Var.DF_DATE);
        String shiftTime = userShift.getString(Key.Shift.shiftInTime);
        long shiftInTime = Func.getMillis(Var.DF_DATETIME, currentDate + " " + shiftTime);
        long currentTime = Func.getMillis(Var.DF_DATETIME, Func.getCurrentDate(Var.DF_DATETIME));
        long diff = Math.abs(currentTime - shiftInTime);
        long minutes = diff / 60000;
        attendObject.put(Key.Attendance.statusByTime, minutes + "");
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
