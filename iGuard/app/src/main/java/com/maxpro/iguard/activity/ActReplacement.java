package com.maxpro.iguard.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.maxpro.iguard.IGuard;
import com.maxpro.iguard.R;
import com.maxpro.iguard.utility.Func;
import com.maxpro.iguard.utility.Key;
import com.maxpro.iguard.utility.Progress;
import com.maxpro.iguard.utility.Var;
import com.maxpro.iguard.views.DrawingView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

public class ActReplacement extends ActDrawer implements View.OnClickListener {
    private ImageView img1, img2;
    private TextView txtGuardName1, txtGuardName2, txtTime1, txtTime2, txtClientName1, txtClientName2, txtLocation1, txtLocation2;
    private Button btnConfirm;
    private DrawingView drawingView;
    private Progress progressDialog;
    private String intentReplacedId;
    private ParseObject replacedUserPointer, replacementForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_replacement);
        init();
    }

    private void init() {
        intentReplacedId = getIntent().getStringExtra(Var.IntentObjId);
        Log.e("objectid", "objectId= " + intentReplacedId);
        progressDialog = new Progress(this);
        setHeaderText(getString(R.string.replacement_header));
        setClickListener(click);
        setBackButtonClick(this);
        img1 = (ImageView) findViewById(R.id.replacement_img1);
        img2 = (ImageView) findViewById(R.id.replacement_img2);
        txtGuardName1 = (TextView) findViewById(R.id.replacement_txtGuardName1);
        txtGuardName2 = (TextView) findViewById(R.id.replacement_txtGuardName2);
        txtTime1 = (TextView) findViewById(R.id.replacement_txtTime1);
        txtTime2 = (TextView) findViewById(R.id.replacement_txtTime2);
        txtClientName1 = (TextView) findViewById(R.id.replacement_txtClientName1);
        txtClientName2 = (TextView) findViewById(R.id.replacement_txtClientName2);
        txtLocation1 = (TextView) findViewById(R.id.replacement_txtLocation1);
        txtLocation2 = (TextView) findViewById(R.id.replacement_txtLocation2);

        btnConfirm = (Button) findViewById(R.id.replacement_btnConfirm);
        drawingView = (DrawingView) findViewById(R.id.replacement_sign);

        btnConfirm.setOnClickListener(this);


        progressDialog.show();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Key.Replacement.NAME);
        query.getInBackground(intentReplacedId, new GetCallback<ParseObject>() {
            public void done(ParseObject replacement, ParseException e) {
                progressDialog.dismiss();
                if (e == null) {
                    replacementForm = replacement;
                    txtGuardName1.setText(currentUser.getString(Key.User.fullName));
                    txtClientName1.setText(currentUser.getString(Key.User.fullName));
                    txtTime1.setText("In Time " + replacement.getString(Key.Replacement.replacedTime));
                    txtLocation1.setText(currentUser.getString(Key.User.address));
                    ParseFile imgFile = currentUser.getParseFile(Key.User.registrationPhoto);
                    if (imgFile != null) {
                        IGuard.imageLoader.displayImage(imgFile.getUrl(), img1, Func.getDisplayOption());
                    }

                    try {
                        replacedUserPointer = replacement.getParseObject(Key.Replacement.replacedUserPointer).fetchIfNeeded();
                        txtGuardName2.setText(replacedUserPointer.getString(Key.User.fullName));
                        txtTime2.setText("Out Time " + replacement.getString(Key.Replacement.replacedTime));
                        txtClientName2.setText(currentUser.getString(Key.User.fullName));
                        txtLocation2.setText(replacedUserPointer.getString(Key.User.address));
                        ParseFile imgFile2 = replacedUserPointer.getParseFile(Key.User.registrationPhoto);
                        if (imgFile2 != null) {
                            IGuard.imageLoader.displayImage(imgFile2.getUrl(), img2, Func.getDisplayOption());
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.replacement_btnConfirm:
                if(drawingView.isSigned()){
                    uploadToParse();
                }else {
                    Func.showValidDialog(ActReplacement.this,"Please add your sign.");
                }
                break;
        }
    }

    private void uploadToParse() {
        drawingView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(drawingView.getDrawingCache());
        drawingView.setDrawingCacheEnabled(false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        ParseFile signFile = new ParseFile("sign.png", b);
        replacementForm.put(Key.Replacement.sign, signFile);
        replacementForm.put(Key.Replacement.status, "CS");
        progressDialog.show();
        replacementForm.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                progressDialog.dismiss();
                if(e==null){
                    Toast.makeText(ActReplacement.this,"Replacement confirmed success.",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Func.showValidDialog(ActReplacement.this,"Replacement confirmed failed.");
                }
            }
        });
    }
}
