package com.maxpro.iguard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.maxpro.iguard.R;


public class ActDashboard extends ActDrawer implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_dashboard);
        setHeaderText(getString(R.string.app_name));
        setClickListener(click);
        findViewById(R.id.dashboard_linearAttendance).setOnClickListener(this);
        findViewById(R.id.dashboard_linearContact).setOnClickListener(this);
        findViewById(R.id.dashboard_linearPatrolling).setOnClickListener(this);
        findViewById(R.id.dashboard_linearChat).setOnClickListener(this);
        findViewById(R.id.dashboard_linearVisit).setOnClickListener(this);
        findViewById(R.id.dashboard_linearTask).setOnClickListener(this);
        findViewById(R.id.dashboard_linearAlert).setOnClickListener(this);
        findViewById(R.id.dashboard_linearMsgBoard).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.dashboard_linearAttendance:
                startActivity(new Intent(this, ActAttendance.class));
                break;
            case R.id.dashboard_linearContact:
                startActivity(new Intent(this, ActContact.class));
                break;
            case R.id.dashboard_linearPatrolling:
                startActivity(new Intent(this, ActPatrolling.class));
                break;
            case R.id.dashboard_linearChat:
                startActivity(new Intent(this, ActChatList.class));
                break;
            case R.id.dashboard_linearVisit:
                startActivity(new Intent(this, ActVisits.class));
                break;
            case R.id.dashboard_linearTask:
                startActivity(new Intent(this, ActTask.class));
                break;
            case R.id.dashboard_linearAlert:
                startActivity(new Intent(this, ActAlert.class));
                break;
            case R.id.dashboard_linearMsgBoard:
                startActivity(new Intent(this, ActMsgBoard.class));
                break;
        }
    }
}
