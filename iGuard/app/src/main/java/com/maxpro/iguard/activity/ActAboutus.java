package com.maxpro.iguard.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxpro.iguard.IGuard;
import com.maxpro.iguard.R;
import com.maxpro.iguard.utility.Func;
import com.maxpro.iguard.utility.Key;
import com.maxpro.iguard.utility.Progress;
import com.maxpro.iguard.utility.Var;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class ActAboutus extends ActDrawer{
private ActAboutus thisActivity;
    private TextView txtName,txtDescription;
    private ImageView imgAbout;
    private Progress progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_aboutus);
        initialize();
        getAboutUsFromParse();
    }

    private void getAboutUsFromParse() {
        progressDialog.show();
        ParseQuery<ParseObject>query=ParseQuery.getQuery(Key.AboutUs.NAME);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        ParseObject object=list.get(0);
                        txtName.setText(object.getString(Key.AboutUs.name));
                        txtDescription.setText(object.getString(Key.AboutUs.description));
                        final ParseFile aboutFile = object.getParseFile(Key.AboutUs.image);
                        if (aboutFile != null) {
                            IGuard.imageLoader.displayImage(aboutFile.getUrl(), imgAbout, Func.getDisplayOption());
                            imgAbout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(thisActivity, ActFullImage.class);
                                    intent.putExtra(Var.IntentUrl, aboutFile.getUrl());
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                }else{
                    Func.showValidDialog(thisActivity,e.getMessage());
                }
                progressDialog.dismiss();
            }
        });
    }

    private void initialize() {
        thisActivity=this;
        progressDialog=new Progress(thisActivity);
        setHeaderText(getString(R.string.menu_aboutus));
        setClickListener(click);
        setBackButtonClick(this);
        imgAbout=(ImageView) findViewById(R.id.aboutus_imgAbout);
        txtDescription=(TextView) findViewById(R.id.aboutus_txtDesciption);
        txtName=(TextView) findViewById(R.id.aboutus_txtName);

    }
}
