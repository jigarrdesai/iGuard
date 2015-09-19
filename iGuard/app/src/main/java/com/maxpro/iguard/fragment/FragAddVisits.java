package com.maxpro.iguard.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.maxpro.iguard.IGuard;
import com.maxpro.iguard.R;
import com.maxpro.iguard.utility.Func;
import com.maxpro.iguard.utility.Key;
import com.maxpro.iguard.utility.Progress;
import com.maxpro.iguard.utility.Var;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Ketan on 5/20/2015.
 */
public class FragAddVisits extends Fragment implements View.OnClickListener {

    private TextView txtDateIn, txtDateOut, txtTimeIn, txtTimeOut;
    private EditText editVisitor, editPurpose, editPeople, editVehicle,editDocName,editDocNum;
    private Button btnUpdate;
    private ImageView imgPhoto;
    private int currentHour, currentMinute, currentDay, currentMonth, currentYear;
    private long currentMilli;
    private SimpleDateFormat dateFormatter;
    private Progress progressDialog;
    private String imageFilePath;
    private Bitmap photo;
    private String objId = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_addvisits, container, false);
        objId = "";
        init(rootView);
        Bundle bundle = getArguments();
        if (bundle != null) {
            objId = bundle.getString(Var.IntentObjId);
            String datein = bundle.getString(Key.Visits.dateIn);
            String dateout = bundle.getString(Key.Visits.dateOut);
            String timein = bundle.getString(Key.Visits.timeIn);
            String timeout = bundle.getString(Key.Visits.timeOut);
            String visitphoto = bundle.getString(Key.Visits.visitPhoto);
            if (visitphoto != null) {
                IGuard.imageLoader.displayImage(visitphoto, imgPhoto, Func.getDisplayOption());
            }
            String visitor = bundle.getString(Key.Visits.visitor);
            String purpose = bundle.getString(Key.Visits.purpose);
            String peopleCount = bundle.getString(Key.Visits.peopleCount);
            String vehicleNum = bundle.getString(Key.Visits.vehicleNum);
            String documentName = bundle.getString(Key.Visits.documentName);
            String documentNum = bundle.getString(Key.Visits.documentNumber);
            txtDateIn.setText(datein);
            txtDateOut.setText(dateout);
            txtTimeIn.setText(timein);
            txtTimeOut.setText(timeout);
            editVisitor.setText(visitor);
            editPeople.setText(peopleCount);
            editPurpose.setText(purpose);
            editVehicle.setText(vehicleNum);
            editDocName.setText(documentName);
            editDocNum.setText(documentNum);
            txtDateIn.setEnabled(false);
            txtTimeIn.setEnabled(false);
            editPurpose.setEnabled(false);
            editVehicle.setEnabled(false);
            editVisitor.setEnabled(false);
            editPeople.setEnabled(false);
            editDocNum.setEnabled(false);
            editDocName.setEnabled(false);
            imgPhoto.setEnabled(false);
        } else {
            txtDateOut.setEnabled(false);
            txtTimeOut.setEnabled(false);
        }
        return rootView;
    }

    private void init(View rootView) {
        progressDialog = new Progress(getActivity());

        imgPhoto = (ImageView) rootView.findViewById(R.id.fragaddvisit_image);
        txtDateIn = (TextView) rootView.findViewById(R.id.fragaddvisit_txtDateIn);
        txtDateOut = (TextView) rootView.findViewById(R.id.fragaddvisit_txtDateOut);
        txtTimeIn = (TextView) rootView.findViewById(R.id.fragaddvisit_txtTimeIn);
        txtTimeOut = (TextView) rootView.findViewById(R.id.fragaddvisit_txtTimeOut);

        editVisitor = (EditText) rootView.findViewById(R.id.fragaddvisit_editVisitor);
        editPurpose = (EditText) rootView.findViewById(R.id.fragaddvisit_editPurpose);
        editPeople = (EditText) rootView.findViewById(R.id.fragaddvisit_editPeoplecount);
        editVehicle = (EditText) rootView.findViewById(R.id.fragaddvisit_editVehicleno);
        editDocName = (EditText) rootView.findViewById(R.id.fragaddvisit_editDoc);
        editDocNum = (EditText) rootView.findViewById(R.id.fragaddvisit_editDocNumber);
        btnUpdate = (Button) rootView.findViewById(R.id.fragaddvisit_btnUpdate);

        imgPhoto.setOnClickListener(this);
        txtDateIn.setOnClickListener(this);
        txtDateOut.setOnClickListener(this);
        txtTimeIn.setOnClickListener(this);
        txtTimeOut.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        dateFormatter = new SimpleDateFormat(Var.DF_DATE);
        currentMilli=System.currentTimeMillis();
        Calendar currentCalendar = Calendar.getInstance();
        currentYear = currentCalendar.get(Calendar.YEAR);
        currentMonth = currentCalendar.get(Calendar.MONTH);
        currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = currentCalendar.get(Calendar.MINUTE);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragaddvisit_btnUpdate:
                if (isValid()) {
                    uploadToParse();
                }
                break;
            case R.id.fragaddvisit_txtDateIn:
                showDatePicker(txtDateIn);
                break;
            case R.id.fragaddvisit_txtDateOut:
                showDatePicker(txtDateOut);
                break;
            case R.id.fragaddvisit_txtTimeIn:
                showTimePicker(txtTimeIn);
                break;
            case R.id.fragaddvisit_txtTimeOut:
                showTimePicker(txtTimeOut);
                break;
            case R.id.fragaddvisit_image:
                captureImage();
                break;
        }
    }

    private void captureImage() {
        imageFilePath = Func.getFilePath();
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
                    photo=Bitmap.createScaledBitmap(photo, 200, 200, false);
                    imgPhoto.setImageBitmap(photo);

                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isValid() {
        if (TextUtils.isEmpty(objId) && TextUtils.isEmpty(txtDateIn.getText().toString().trim())) {
            Func.showValidDialog(getActivity(), "Please select Date In.");
            return false;
        }
        if ((!TextUtils.isEmpty(objId)) && TextUtils.isEmpty(txtDateOut.getText().toString().trim())) {
            Func.showValidDialog(getActivity(), "Please select Date Out.");
            return false;
        }
        if (TextUtils.isEmpty(objId) && TextUtils.isEmpty(txtTimeIn.getText().toString().trim())) {
            Func.showValidDialog(getActivity(), "Please select Time In.");
            return false;
        }
        if ((!TextUtils.isEmpty(objId)) && TextUtils.isEmpty(txtTimeOut.getText().toString().trim())) {
            Func.showValidDialog(getActivity(), "Please select Time Out.");
            return false;
        }
        if (TextUtils.isEmpty(editVisitor.getText().toString().trim())) {
            Func.showValidDialog(getActivity(), "Please enter Visitor.");
            return false;
        }
        if (TextUtils.isEmpty(editPurpose.getText().toString().trim())) {
            Func.showValidDialog(getActivity(), "Please enter Purpose.");
            return false;
        }
        if (TextUtils.isEmpty(editPeople.getText().toString().trim())) {
            Func.showValidDialog(getActivity(), "Please enter People Count.");
            return false;
        }
        /*if (TextUtils.isEmpty(editVehicle.getText().toString().trim())) {
            Func.showValidDialog(getActivity(), "Please enter Vehicle No.");
            return false;
        }*/
        if (TextUtils.isEmpty(editDocName.getText().toString().trim())) {
            Func.showValidDialog(getActivity(), "Please enter Document Name.");
            return false;
        }
        if (TextUtils.isEmpty(editDocNum.getText().toString().trim())) {
            Func.showValidDialog(getActivity(), "Please enter Document No.");
            return false;
        }
        /*if (TextUtils.isEmpty(objId) && photo == null) {
            Func.showValidDialog(getActivity(), "Please capture photo.");
            return false;
        }*/
        return true;
    }

    private void uploadToParse() {
        progressDialog.show();
        ParseFile imageFile=null;
        if (photo != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            imageFile = new ParseFile("visitphoto.png", b);
        }
        ParseObject visitObject;
        if (TextUtils.isEmpty(objId)) {
            visitObject = new ParseObject(Key.Visits.NAME);
            if(imageFile!=null) {
                visitObject.put(Key.Visits.visitPhoto, imageFile);
            }
        } else {
            visitObject = ParseObject.createWithoutData(Key.Visits.NAME, objId);
            visitObject.put(Key.Visits.timeOut, txtTimeOut.getText().toString());
            visitObject.put(Key.Visits.dateOut, txtDateOut.getText().toString());
        }
        visitObject.put(Key.Visits.dateIn, txtDateIn.getText().toString());

        visitObject.put(Key.Visits.timeIn, txtTimeIn.getText().toString());


        visitObject.put(Key.Visits.visitor, editVisitor.getText().toString());
        visitObject.put(Key.Visits.purpose, editPurpose.getText().toString());
        visitObject.put(Key.Visits.peopleCount, editPeople.getText().toString());
        visitObject.put(Key.Visits.vehicleNum, editVehicle.getText().toString());
        visitObject.put(Key.Visits.documentNumber, editDocNum.getText().toString());
        visitObject.put(Key.Visits.documentName, editDocName.getText().toString());
        ParseUser currentUser = ParseUser.getCurrentUser();
        visitObject.put(Key.Visits.userPointer, currentUser);
        visitObject.put(Key.Visits.branch, currentUser.getParseObject(Key.User.branch));
        visitObject.put(Key.Visits.company, currentUser.getParseObject(Key.User.company));
        visitObject.put(Key.Visits.supervisor, currentUser.getParseObject(Key.User.supervisor));
        visitObject.put(Key.Visits.site, currentUser.getParseObject(Key.User.site));
        visitObject.put(Key.Visits.post, currentUser.getParseObject(Key.User.post));
        visitObject.put(Key.Visits.shift, currentUser.getParseObject(Key.User.shift));
        visitObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                progressDialog.dismiss();
                if (e != null) {
                    e.printStackTrace();
                    Func.showValidDialog(getActivity(), "Visit submit failed");
                } else {
                    Log.e("ActLeave", "Leave Done");
                    Func.showValidDialog(getActivity(), "Visit submitted Successfully.");
                    resetViews();
                }
            }
        });

    }

    private void resetViews() {
        txtTimeIn.setText("");
        txtTimeOut.setText("");
        txtDateIn.setText("");
        txtDateOut.setText("");
        editVisitor.setText("");
        editPurpose.setText("");
        editPeople.setText("");
        editVehicle.setText("");
        editDocName.setText("");
        editDocNum.setText("");
        photo = null;
        imgPhoto.setImageResource(android.R.drawable.picture_frame);
    }


    private void showDatePicker(final TextView textView) {
        DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                textView.setText(dateFormatter.format(newDate.getTime()));
            }
        }, currentYear, currentMonth, currentDay);
        dpd.getDatePicker().setMinDate(currentMilli);
        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());

        dpd.show();
    }

    private void showTimePicker(final TextView textView) {
        TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                textView.setText(selectedHour + ":" + selectedMinute);
            }
        }, currentHour, currentMinute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
}
