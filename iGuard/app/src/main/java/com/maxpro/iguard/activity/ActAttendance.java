package com.maxpro.iguard.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.maxpro.iguard.R;
import com.maxpro.iguard.fragment.FragAttendance;
import com.maxpro.iguard.fragment.FragAttendanceRecord;

public class ActAttendance extends ActDrawer implements View.OnClickListener {
	private Button btnToday,btnRecord;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_attendance);
		initialize();
	}

    private void initialize() {
        setClickListener(click);
        setBackButtonClick(this);
        btnToday=(Button) findViewById(R.id.attend_btnToday);
        btnRecord=(Button) findViewById(R.id.attend_btnRecord);
        imgBack.setOnClickListener(this);
        btnRecord.setOnClickListener(this);
        btnToday.setOnClickListener(this);
        btnToday.performClick();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_imgArrow:
                finish();
                break;
            case R.id.attend_btnRecord:
                getSupportFragmentManager().beginTransaction().replace(R.id.attend_frameContainer,new FragAttendanceRecord()).commit();
                setHeaderText("Attendance Record");

                btnToday.setSelected(false);
                btnRecord.setSelected(true);

                break;
            case R.id.attend_btnToday:

                getSupportFragmentManager().beginTransaction().replace(R.id.attend_frameContainer,new FragAttendance()).commit();
                setHeaderText("Attendance");

                btnToday.setSelected(true);
                btnRecord.setSelected(false);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
