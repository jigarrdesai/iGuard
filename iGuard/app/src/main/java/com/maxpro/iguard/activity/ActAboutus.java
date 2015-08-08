package com.maxpro.iguard.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.maxpro.iguard.R;

public class ActAboutus extends ActDrawer{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_aboutus);
        initialize();
    }

    private void initialize() {
        setHeaderText(getString(R.string.menu_aboutus));
        setClickListener(click);
        setBackButtonClick(this);
    }
}
