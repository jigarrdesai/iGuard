package com.maxpro.iguard.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.maxpro.iguard.R;
import com.maxpro.iguard.adapter.AdapterAttendRecord;
import com.maxpro.iguard.adapter.AdapterLeave;
import com.maxpro.iguard.utility.AppLog;
import com.maxpro.iguard.utility.Key;
import com.maxpro.iguard.utility.Progress;
import com.maxpro.iguard.utility.Var;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Ketan on 3/24/2015.
 */
public class ActLeave extends ActDrawer implements View.OnClickListener {
    private TextView txtFromDate, txtToDate, txtFromTime, txtToTime;
    private EditText editReason;
    private Button btnApply;
    private RecyclerView recyclerView;
    private int currentHour, currentMinute, currentDay, currentMonth, currentYear;
    private SimpleDateFormat dateFormatter;
    Progress progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_leave);
        initialize();
        getLeaveList();
    }

    private void initialize() {
        progressDialog = new Progress(this);
        setHeaderText(getString(R.string.leave_managemet));
        setClickListener(click);
        setBackButtonClick(this);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        Calendar currentCalendar = Calendar.getInstance();
        currentYear = currentCalendar.get(Calendar.YEAR);
        currentMonth = currentCalendar.get(Calendar.MONTH);
        currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = currentCalendar.get(Calendar.MINUTE);

        txtFromDate = (TextView) findViewById(R.id.leave_txtFromDt);
        txtToDate = (TextView) findViewById(R.id.leave_txtToDt);
        txtFromTime = (TextView) findViewById(R.id.leave_txtFromTime);
        txtToTime = (TextView) findViewById(R.id.leave_txtToTime);
        btnApply = (Button) findViewById(R.id.leave_btnApply);
        editReason = (EditText) findViewById(R.id.leave_editReason);
        txtFromDate.setOnClickListener(this);
        txtToDate.setOnClickListener(this);
        txtFromTime.setOnClickListener(this);
        txtToTime.setOnClickListener(this);
        btnApply.setOnClickListener(this);

        recyclerView=(RecyclerView) findViewById(R.id.leave_recyclerview);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.leave_btnApply:
                if (isValid()) {
                    uploadToParse();
                }
                break;
            case R.id.leave_txtFromDt:
                showDatePicker(txtFromDate);
                break;
            case R.id.leave_txtToDt:
                showDatePicker(txtToDate);
                break;
            case R.id.leave_txtFromTime:
                showTimePicker(txtFromTime);
                break;
            case R.id.leave_txtToTime:
                showTimePicker(txtToTime);
                break;


        }
    }

    private void uploadToParse() {
        progressDialog.show();
        ParseObject leaveObject = new ParseObject(Key.Leave.NAME);
        leaveObject.put(Key.Leave.leaveDate, txtFromDate.getText().toString());
        leaveObject.put(Key.Leave.leaveTime, txtToDate.getText().toString());
        leaveObject.put(Key.Leave.applyDateTime, txtFromTime.getText().toString());
        leaveObject.put(Key.Leave.leaveDateTime, txtToTime.getText().toString());
        leaveObject.put(Key.Leave.leaveReason, editReason.getText().toString());
        leaveObject.put(Key.Leave.status, Var.TYPE_LEAVE_P);
        leaveObject.put(Key.Leave.usersPointer, currentUser);
        leaveObject.put(Key.Leave.branch, currentUser.getParseObject(Key.User.branch));
        leaveObject.put(Key.Leave.company, currentUser.getParseObject(Key.User.company));
        leaveObject.put(Key.Leave.assignSupervisor, currentUser.getParseObject(Key.User.supervisor));
        leaveObject.put(Key.Leave.site, currentUser.getParseObject(Key.User.site));
        leaveObject.put(Key.Leave.shift, currentUser.getParseObject(Key.User.shift));
        leaveObject.put(Key.Leave.post, currentUser.getParseObject(Key.User.post));
        leaveObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                progressDialog.dismiss();
                if (e != null) {
                    e.printStackTrace();
                    Toast.makeText(ActLeave.this, "Leave submit failed", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("ActLeave", "Leave Done");
                    Toast.makeText(ActLeave.this, "Leave submitted Successfully.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    private boolean isValid() {
        if (TextUtils.isEmpty(txtFromDate.getText().toString().trim())) {
            Toast.makeText(this, "Please select From date.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(txtToDate.getText().toString().trim())) {
            Toast.makeText(this, "Please select To date.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(txtFromTime.getText().toString().trim())) {
            Toast.makeText(this, "Please select From time.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(txtToTime.getText().toString().trim())) {
            Toast.makeText(this, "Please select To time.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(editReason.getText().toString().trim())) {
            Toast.makeText(this, "Please enter leave reason.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showDatePicker(final TextView textView) {
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                textView.setText(dateFormatter.format(newDate.getTime()));
            }
        }, currentYear, currentMonth, currentDay);
        dpd.show();
    }

    private void showTimePicker(final TextView textView) {
        TimePickerDialog mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                textView.setText(selectedHour + ":" + selectedMinute);
            }
        }, currentHour, currentMinute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void getLeaveList(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Leave");

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> leaveList, ParseException e) {
                if (e == null) {
                    AppLog.d("Retrieved " + leaveList.size() + " leave");
                    AdapterLeave adapterLeave = new AdapterLeave(ActLeave.this,leaveList);
                    recyclerView.setAdapter(adapterLeave);
                } else {
                    AppLog.d( "Error: " + e.getMessage());
                }
            }
        });
    }
}
