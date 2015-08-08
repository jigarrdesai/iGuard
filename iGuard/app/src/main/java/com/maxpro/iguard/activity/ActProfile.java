package com.maxpro.iguard.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.maxpro.iguard.R;
import com.maxpro.iguard.utility.Key;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ActProfile extends Activity {
    private TextView txtUsername, txtGuardname, txtCompany, txtShift, txtPost, txtSite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_profile);
        initialize();
    }

    private void initialize() {
        TextView txtHeader = (TextView) findViewById(R.id.header_txtTitle);
        txtHeader.setText(getString(R.string.profile_header));
        txtUsername = (TextView) findViewById(R.id.profile_txtUsername);
        txtGuardname = (TextView) findViewById(R.id.profile_txtGuardname);
        txtCompany = (TextView) findViewById(R.id.profile_txtCompany);
        txtShift = (TextView) findViewById(R.id.profile_txtShift);
        txtPost = (TextView) findViewById(R.id.profile_txtPost);
        txtSite = (TextView) findViewById(R.id.profile_txtSite);

        ParseUser parseUser = ParseUser.getCurrentUser();
        ParseObject company = parseUser.getParseObject(Key.User.company);
        ParseObject shift = parseUser.getParseObject(Key.User.shift);
        ParseObject site = parseUser.getParseObject(Key.User.site);
        ParseObject post = parseUser.getParseObject(Key.User.post);

        txtUsername.setText(parseUser.getUsername());
        txtGuardname.setText(parseUser.getString(Key.User.fullName));
        if (company != null) {
            txtCompany.setText(company.getString("companyName"));
        }
        if (site != null) {
            txtSite.setText(site.getString("siteName"));
        }
        if (post != null) {
            txtPost.setText(post.getString("postName"));
        }
        if (shift != null) {
            txtShift.setText(shift.getString("shiftName"));
        }
    }
}
