package com.maxpro.iguard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.maxpro.iguard.R;
import com.parse.ParseUser;

public class ActSplash extends Activity implements Runnable {

    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);
        mHandler=new Handler();
        mHandler.postDelayed(this,3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(this);
    }

    @Override
    public void run() {
        ParseUser user=ParseUser.getCurrentUser();
        if(user==null) {
            startActivity(new Intent(this, ActLogin.class));
            finish();
        }else{
            startActivity(new Intent(this, ActDashboard.class));
            finish();
        }
    }
}
