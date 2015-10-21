package com.maxpro.iguard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.maxpro.iguard.R;
import com.maxpro.iguard.adapter.AdapterTasks;
import com.maxpro.iguard.db.TblTask;
import com.maxpro.iguard.model.ModelTask;
import com.maxpro.iguard.utility.AppLog;
import com.maxpro.iguard.utility.Func;
import com.maxpro.iguard.utility.ImplClick;
import com.maxpro.iguard.utility.Key;
import com.maxpro.iguard.utility.Progress;
import com.maxpro.iguard.utility.Var;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ActLogin extends Activity implements OnClickListener {
private ActLogin activity;
    private EditText editUsername,editPassword;
    private Progress progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_login);
        initialize();

	}

    private void initialize() {
        activity=this;
        progressDialog=new Progress(this);
        findViewById(R.id.login_btnLogin).setOnClickListener(this);
        editUsername=(EditText) findViewById(R.id.login_editEmail);
        editPassword=(EditText) findViewById(R.id.login_editPassword);
    }

    @Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btnLogin:
			clickLogin();
			break;

		}
	}

    private void clickLogin() {
        String username=editUsername.getText().toString().trim();
        String password=editPassword.getText().toString().trim();
        if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
            Toast.makeText(ActLogin.this, "Please Enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();
        ParseUser.logInInBackground(username,password,new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                progressDialog.dismiss();
                if (e == null) {
                    String userType=parseUser.getString("userType");
                    if(userType.equalsIgnoreCase("user")){
                        ParseObject userShift=parseUser.getParseObject(Key.User.shift);
                        try {
                            userShift.fetchIfNeeded();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        if(userShift!=null) {
                            String inTimeMin = userShift.getString(Key.Shift.shiftInTime);
                            String endTimeMin = userShift.getString(Key.Shift.shiftOutTime);
                            String currentHour=Func.getCurrentDate(Var.DF_DATE);
                            long shiftEndMilli=Func.getMillis(Var.DF_DATETIME, currentHour + " " + endTimeMin);
                            long shiftInMilli=Func.getMillis(Var.DF_DATETIME, currentHour + " " + inTimeMin);
                            Date shiftEndDate=new Date(shiftEndMilli);
                            Date shiftInDate=new Date(shiftInMilli);
                            Date currentDate=new Date();
                            if (!shiftEndDate.after(shiftInDate)) {
                                Calendar cal=Calendar.getInstance();
                                cal.setTime(shiftEndDate);
                                cal.add(Calendar.DAY_OF_MONTH,1);
                                shiftEndDate=cal.getTime();
                            }
                            if(currentDate.before(shiftInDate)||currentDate.after(shiftEndDate)){
                                Func.showValidDialog(activity, "You can login within your shift time only.", new ImplClick() {
                                    @Override
                                    public void onOkClick(View v) {
                                        ParseUser.logOut();
                                    }

                                    @Override
                                    public void onCancelClick(View v) {

                                    }
                                });
                            }else{
                                startActivity(new Intent(activity, ActDashboard.class));
                                finish();
                            }
                        }

                    }else{
                        Func.showValidDialog(ActLogin.this, "Only guard can login.");
                        ParseUser.logOut();
                    }
                }else{
                    //login failed
                    e.printStackTrace();
                    Func.showValidDialog(ActLogin.this, "Your username or password is incorrect.");
                }
            }
        });
    }


}
