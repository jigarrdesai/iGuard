package com.maxpro.iguard.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxpro.iguard.IGuard;
import com.maxpro.iguard.R;
import com.maxpro.iguard.adapter.AdapterContact;
import com.maxpro.iguard.utility.AppLog;
import com.maxpro.iguard.utility.Func;
import com.maxpro.iguard.utility.Key;
import com.maxpro.iguard.utility.Progress;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class ActContact extends ActDrawer implements View.OnClickListener {
    private RecyclerView recyclerView;
    private TextView txtManagerName,txtCompanyName;
    private Button btnManagerCall,btnCompanyCall;
    private ImageView imgManagerPic,imgCompany;
    private AdapterContact adapterContact;
    private Progress progressDialog;
    private String managerPhoneNumber,companyPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_contact);
        init();
        getCarList();
    }

    private void init() {
        setHeaderText(getString(R.string.dashboard_contacts));
        setClickListener(click);
        setBackButtonClick(this);
        progressDialog=new Progress(this);
        btnManagerCall=(Button) findViewById(R.id.contact_btnManagerCall);
        imgManagerPic= (ImageView) findViewById(R.id.contact_imgManager);
        imgCompany=(ImageView) findViewById(R.id.contact_imgCompany);
        txtManagerName=(TextView) findViewById(R.id.contact_txtManagerName);
        txtCompanyName=(TextView) findViewById(R.id.contact_txtCompanyName);
        btnCompanyCall=(Button) findViewById(R.id.contact_btnCompanyCall);
        recyclerView= (RecyclerView) findViewById(R.id.contact_recyclerview);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        btnManagerCall.setOnClickListener(this);
        btnCompanyCall.setOnClickListener(this);
        ParseObject company=currentUser.getParseObject(Key.User.company);
        if (company != null) {
            companyPhoneNumber=company.getString(Key.Company.contactDetail);
            txtCompanyName.setText("Company No.-"+companyPhoneNumber);
            ParseFile img = company.getParseFile(Key.Company.compLogo);
            if (img != null) {
                IGuard.imageLoader.displayImage(img.getUrl(), imgCompany, Func.getDisplayOption());
            }
        }else{
            btnCompanyCall.setVisibility(View.GONE);
            txtCompanyName.setVisibility(View.GONE);
            imgCompany.setVisibility(View.GONE);
        }
        ParseObject manager=currentUser.getParseObject(Key.User.supervisor);
        managerPhoneNumber=manager.getString(Key.User.contact);
        txtManagerName.setText(manager.getString(Key.User.fullName) + "-" + managerPhoneNumber);

        ParseFile img = manager.getParseFile(Key.User.registrationPhoto);
        if (img != null) {
            IGuard.imageLoader.displayImage(img.getUrl(), imgManagerPic, Func.getDisplayOption());
        }

    }
    private void getCarList(){
        progressDialog.show();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Key.Cars.NAME);
        ParseObject branch=currentUser.getParseObject(Key.User.branch);
        query.whereEqualTo(Key.Cars.branch, branch);
        query.whereNotEqualTo(Key.Cars.deleted,true);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> carList, ParseException e) {
                progressDialog.dismiss();
                if (e == null) {
                    //AppLog.d("Retrieved " + carList.size() + " message");
                     adapterContact = new AdapterContact(ActContact.this,carList);
                    recyclerView.setAdapter(adapterContact);
                } else {
                    AppLog.d( "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.contact_btnManagerCall:

                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + managerPhoneNumber));
                startActivity(intent);
                break;
            case R.id.contact_btnCompanyCall:

                Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + companyPhoneNumber));
                startActivity(intent1);
                break;
        }
    }
}
