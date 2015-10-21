package com.maxpro.iguard.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maxpro.iguard.IGuard;
import com.maxpro.iguard.R;
import com.maxpro.iguard.db.TblTask;
import com.maxpro.iguard.model.ModelTask;
import com.maxpro.iguard.utility.Func;
import com.maxpro.iguard.utility.ImplClick;
import com.maxpro.iguard.utility.Key;
import com.maxpro.iguard.utility.Progress;
import com.maxpro.iguard.utility.Var;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ActDrawer extends FragmentActivity {

    protected RelativeLayout fullLayout;
    protected FrameLayout frameLayout;
    private DrawerLayout drawerLayout;
    LinearLayout linearDrawer;
    private RelativeLayout relProfile;
    private TextView txtName, txtHeader,txtAboutus, txtShift, txtLogout, txtVisit, txtTasks, txtReplacement, txtTraining;
    protected ImageView imgProfile, imgMenu, imgBack;
    public Button btnDone;
    protected ParseUser currentUser;

private Progress progressDialog;
    private View selectedView;

    @Override
    public void setContentView(int layoutResID) {

        fullLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.act_drawer, null);
        frameLayout = (FrameLayout) fullLayout.findViewById(R.id.drawer_frame);

        getLayoutInflater().inflate(layoutResID, frameLayout, true);

        super.setContentView(fullLayout);



        progressDialog=new Progress(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        linearDrawer = (LinearLayout) findViewById(R.id.linearDrawer);
        relProfile=(RelativeLayout) findViewById(R.id.drawer_relProfile);
        imgMenu = (ImageView) findViewById(R.id.header_imgMenu);
        btnDone = (Button) findViewById(R.id.header_btnDone);
        txtName = (TextView) findViewById(R.id.drawer_txtName);
        txtHeader = (TextView) findViewById(R.id.header_txtTitle);
        txtLogout = (TextView) findViewById(R.id.drawer_txtLogout);
        txtVisit = (TextView) findViewById(R.id.drawer_txtVisit);
        txtAboutus = (TextView) findViewById(R.id.drawer_txtAboutus);
        txtTasks = (TextView) findViewById(R.id.drawer_txtTasks);
        txtShift = (TextView) findViewById(R.id.drawer_txtShift);
        txtReplacement = (TextView) findViewById(R.id.drawer_txtReplacement);
        txtTraining = (TextView) findViewById(R.id.drawer_txtTraining);
        imgBack = (ImageView) findViewById(R.id.header_imgArrow);
        imgProfile = (ImageView) findViewById(R.id.drawer_imgProfile);
        //Your drawer content...
        currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            try {
                currentUser.fetch();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            txtName.setText(currentUser.getString(Key.User.fullName));
            try {
                String shiftDesc=currentUser.getParseObject(Key.User.shift).fetchIfNeeded().getString("shiftDescription");
                txtShift.setText(shiftDesc);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try{
                currentUser.getParseObject(Key.User.supervisor).fetchIfNeeded();
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                currentUser.getParseObject(Key.User.post).fetchIfNeeded();
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                currentUser.getParseObject(Key.User.car).fetchIfNeeded();
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                currentUser.getParseObject(Key.User.company).fetchIfNeeded();
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                currentUser.getParseObject(Key.User.site).fetchIfNeeded();
            }catch (Exception e){
                e.printStackTrace();
            }

            ParseFile img = currentUser.getParseFile(Key.User.registrationPhoto);
            if (img != null) {
                IGuard.imageLoader.displayImage(img.getUrl(), imgProfile, Func.getDisplayOption());
            }
        }
        if (ActDrawer.this instanceof ActDashboard) {
            imgBack.setVisibility(View.INVISIBLE);
        }
    }

    protected View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.header_imgMenu:
                    Log.e("ActDrawer", "Menu Click");
                    toggleDrawer();
                    break;
                case R.id.drawer_txtVisit:
                    toggleMenuSelection(view);
                    toggleDrawer();
                    if (ActDrawer.this instanceof ActVisits) {
                        return;
                    }
                    startActivity(new Intent(ActDrawer.this, ActVisits.class));
                    if (!(ActDrawer.this instanceof ActDashboard)) {
                        finish();
                    }
                    break;
                case R.id.drawer_txtTasks:
                    toggleMenuSelection(view);
                    toggleDrawer();

                    startActivity(new Intent(ActDrawer.this, ActTask.class));
                    break;
                case R.id.drawer_txtReplacement:
                    toggleMenuSelection(view);
                    toggleDrawer();
                    if (ActDrawer.this instanceof ActReplaceList) {
                        return;
                    }
                    startActivity(new Intent(ActDrawer.this, ActReplaceList.class));
                    if (!(ActDrawer.this instanceof ActDashboard)) {
                        finish();
                    }
                    //startActivity(new Intent(ActDrawer.this, ActLeave.class));
                    break;
                case R.id.drawer_txtTraining:
                    toggleMenuSelection(view);
                    toggleDrawer();
                    if (ActDrawer.this instanceof ActVideoTraining) {
                    return;
                    }
                    startActivity(new Intent(ActDrawer.this, ActVideoTraining.class));
                    if (!(ActDrawer.this instanceof ActDashboard)) {
                        finish();
                    }

                    break;
                case R.id.drawer_txtAboutus:
                    toggleMenuSelection(view);
                    toggleDrawer();
                    if (ActDrawer.this instanceof ActAboutus) {
                        return;
                    }
                    startActivity(new Intent(ActDrawer.this, ActAboutus.class));
                    if (!(ActDrawer.this instanceof ActDashboard)) {
                        finish();
                    }

                    break;

                case R.id.drawer_txtLeave:
                    toggleMenuSelection(view);
                    toggleDrawer();

                    startActivity(new Intent(ActDrawer.this, ActLeave.class));
                    if (!(ActDrawer.this instanceof ActDashboard)) {
                        finish();
                    }
                    break;
                case R.id.drawer_txtLogout:
                    toggleMenuSelection(view);
                    //toggleDrawer();
                    ParseUser.logOut();
                    Func.clearSP(ActDrawer.this);
                    NotificationManager nm= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    nm.cancelAll();
                    Intent intent = new Intent(getApplicationContext(), ActLogin.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    // ParseUser.get
                    break;
                case R.id.drawer_relProfile:
                    startActivity(new Intent(ActDrawer.this,ActProfile.class));
                    toggleDrawer();
                    break;
            }
        }
    };

    protected void toggleDrawer() {
        if (drawerLayout.isDrawerOpen(linearDrawer)) {
            drawerLayout.closeDrawer(linearDrawer);
        } else {
            drawerLayout.openDrawer(linearDrawer);
        }
    }

    private void toggleMenuSelection(View v) {
        v.setSelected(true);
        if (selectedView != null) {
            selectedView.setSelected(false);
        }
        selectedView = v;
    }

    protected void setHeaderText(String headerText) {
        txtHeader.setText(headerText);
    }

    protected void setClickListener(View.OnClickListener click) {
        linearDrawer.setOnClickListener(click);
        relProfile.setOnClickListener(click);
        imgMenu.setOnClickListener(click);
        txtLogout.setOnClickListener(click);
        txtAboutus.setOnClickListener(click);
        findViewById(R.id.drawer_txtLeave).setOnClickListener(click);
        findViewById(R.id.drawer_txtTraining).setOnClickListener(click);
        findViewById(R.id.drawer_txtTasks).setOnClickListener(click);
        findViewById(R.id.drawer_txtReplacement).setOnClickListener(click);
        findViewById(R.id.drawer_txtVisit).setOnClickListener(click);

    }

    protected void setBackButtonClick(final Activity activity) {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ParseObject userShift=currentUser.getParseObject(Key.User.shift);
        if(userShift!=null) {
            String endTimeMin = userShift.getString(Key.Shift.shiftOutTime);
            String inTimeMin = userShift.getString(Key.Shift.shiftInTime);
            String currentHour=Func.getCurrentDate(Var.DF_DATE);
            long shiftMilli=Func.getMillis(Var.DF_DATETIME, currentHour + " " + endTimeMin);
            long shiftInMilli=Func.getMillis(Var.DF_DATETIME, currentHour + " " + inTimeMin);
            Date shiftEndDate=new Date(shiftMilli);
            Date shiftInDate=new Date(shiftInMilli);
            Date currentDate=new Date();
            if (!shiftEndDate.after(shiftInDate)) {
                Calendar cal=Calendar.getInstance();
                cal.setTime(shiftEndDate);
                cal.add(Calendar.DAY_OF_MONTH,1);
                shiftEndDate=cal.getTime();
            }
            if(!(currentDate.after(shiftInDate)&&currentDate.before(shiftEndDate))){

                Func.showValidDialog(ActDrawer.this, "Your shift is over. Please login again.", new ImplClick() {
                    @Override
                    public void onOkClick(View v) {
                        ArrayList<ModelTask> arrModelTask = TblTask.selectTask(currentUser.getObjectId());
                        if(arrModelTask!=null) {
                            ArrayList<ParseObject>arrParseObj=new ArrayList<>();
                            for (ModelTask model: arrModelTask) {
                                ParseObject parseObject=new ParseObject(Key.Task.NAME);
                                parseObject.setObjectId(model.objectId);
                                parseObject.put(Key.Task.isComplete, "");
                                arrParseObj.add(parseObject);
                            }
                            progressDialog.show();
                            ParseObject.saveAllInBackground(arrParseObj, new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    progressDialog.dismiss();
                                    if(e==null){
                                        txtLogout.performClick();
                                    }else{
                                        Func.showValidDialog(ActDrawer.this,e.getMessage());
                                    }

                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelClick(View v) {

                    }
                });
            }
        }
    }
    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_drawer);
    }*/
}