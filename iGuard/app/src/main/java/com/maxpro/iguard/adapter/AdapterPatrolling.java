package com.maxpro.iguard.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.maxpro.iguard.IGuard;
import com.maxpro.iguard.R;
import com.maxpro.iguard.activity.ActPatrolling;
import com.maxpro.iguard.db.TblAutoPatrolling;
import com.maxpro.iguard.model.ModelAutoPatrolling;
import com.maxpro.iguard.utility.Func;
import com.maxpro.iguard.utility.Key;
import com.maxpro.iguard.utility.Progress;
import com.maxpro.iguard.utility.Var;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import edu.sfsu.cs.orange.ocr.CaptureActivity;

public class AdapterPatrolling extends Adapter<AdapterPatrolling.ItemHolder> {
    private List<ModelAutoPatrolling> autoPatrollingList;
    SimpleDateFormat sdfParse, sdfDate, sdfTime;
    ActPatrolling activity;
    private String imageFilePath;
    private ItemHolder currentItem;
    private ModelAutoPatrolling currentObj;
    private String patrollingTime;
    private Progress progressDialog;

    public AdapterPatrolling(ActPatrolling activity, List<ModelAutoPatrolling> autoPatrollingList) {
        this.autoPatrollingList = autoPatrollingList;
        sdfParse = new SimpleDateFormat("dd-MM-yyyy");
        sdfDate = new SimpleDateFormat("dd-MMMM-yyyy");
        sdfTime = new SimpleDateFormat("hh:mm");
        this.activity = activity;
    }

    public void addRow(ModelAutoPatrolling obj) {
        if (autoPatrollingList == null)
            autoPatrollingList = new ArrayList<>();
        autoPatrollingList.add(obj);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return (autoPatrollingList != null) ? autoPatrollingList.size() : 0;
    }

    @Override
    public void onBindViewHolder(final ItemHolder arg0, final int arg1) {
        ModelAutoPatrolling modelAutoPatrolling = autoPatrollingList.get(arg1);
        String dateTime = modelAutoPatrolling.patrollingTime;
        String title = modelAutoPatrolling.type;

        arg0.txtTime.setText(dateTime);
        arg0.txtTitle.setText(title);
        arg0.imgPatrolling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentItem = arg0;
                captureImage();
                currentObj = autoPatrollingList.get(arg1);

            }
        });
        if (modelAutoPatrolling.isComplete.equalsIgnoreCase("0")) {
            arg0.chkStatus.setChecked(false);
            arg0.chkStatus.setEnabled(false);
            arg0.imgPatrolling.setEnabled(true);
            arg0.txtTime.setVisibility(View.GONE);
        } else {
            arg0.chkStatus.setChecked(true);
            arg0.chkStatus.setEnabled(false);
            arg0.imgPatrolling.setEnabled(false);
            arg0.txtTime.setVisibility(View.VISIBLE);
            IGuard.imageLoader.displayImage("file:///" + modelAutoPatrolling.imagePath, arg0.imgPatrolling, Func.getDisplayOption());
        }
        // arg0.imgStatus.setImageResource(R.drawable.a_withtext);
        if (activity.isFromNoti) {
            if (activity.objId.equalsIgnoreCase(modelAutoPatrolling.objectId)) {
                arg0.imgPatrolling.performClick();
            }
        } else {
            arg0.imgPatrolling.setOnClickListener(null);
        }

    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int arg1) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_parolling, parent, false);

        ItemHolder vh = new ItemHolder(v);
        return vh;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {
        TextView txtTime, txtTitle;
        ImageView imgPatrolling;
        CheckBox chkStatus;

        public ItemHolder(View v) {
            super(v);
            txtTime = (TextView) v.findViewById(R.id.rowpatrolling_txtTime);
            txtTitle = (TextView) v.findViewById(R.id.rowpatrolling_txtTitle);
            imgPatrolling = (ImageView) v.findViewById(R.id.rowpatrolling_imgPatrolling);
            chkStatus = (CheckBox) v.findViewById(R.id.rowpatrolling_chkStatus);
        }
    }

    private void captureImage() {
        //Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        Intent intent = new Intent(activity, CaptureActivity.class);
        imageFilePath = Func.getFilePath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFilePath);
        activity.startActivityForResult(intent, Var.REQ_CODE_CAPTURE);
    }

    public void onActivityResult(Intent data) {
        if (currentItem != null) {
            Bitmap photo = BitmapFactory.decodeFile(imageFilePath);
            currentItem.imgPatrolling.setImageBitmap(photo);
            currentItem.chkStatus.setChecked(true);
            patrollingTime = currentItem.txtTime.getText().toString();
            String ocrId = data.getStringExtra(Intent.EXTRA_TEXT);
            uploadToParse(imageFilePath, photo, ocrId, currentObj);
        }
    }

    private void uploadToParse(final String imagePath, Bitmap patrollingPhoto, String ocrId, final ModelAutoPatrolling modelAutoPatrolling) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        patrollingPhoto.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        ParseFile imageFile = new ParseFile("patrollingPhoto.png", b);
        ParseObject attendObject = new ParseObject(Key.Patrolling.NAME);
        //attendObject.put(Key.Patrolling.userDetial, );
        attendObject.put(Key.Patrolling.patrollingDateTime, patrollingTime);
        attendObject.put(Key.Patrolling.patrollingPhoto, imageFile);

        ParseUser currentUser = ParseUser.getCurrentUser();

        attendObject.put(Key.Patrolling.usersPointer, currentUser);

        ParseObject company = currentUser.getParseObject(Key.User.company);
        ParseObject branch = currentUser.getParseObject(Key.User.branch);
        ParseObject supervisor = currentUser.getParseObject(Key.User.supervisor);
        ParseObject post = currentUser.getParseObject(Key.User.post);
        ParseObject site = currentUser.getParseObject(Key.User.site);

        if (branch != null) {
            attendObject.put(Key.Patrolling.branch, branch);
        }
        if (company != null) {
            attendObject.put(Key.Patrolling.company, company);
        }
        if (post != null) {
            attendObject.put(Key.Patrolling.post, post);
        }
        if (site != null) {
            attendObject.put(Key.Patrolling.site, site);
        }
        if (supervisor != null) {
            attendObject.put(Key.Patrolling.supervisor, supervisor);
        }
        attendObject.put(Key.Patrolling.status, "P");
        attendObject.put(Key.Patrolling.ocrId, ocrId);
        ParseGeoPoint patrollingPoint = new ParseGeoPoint(activity.latitude, activity.longitude);
        attendObject.put(Key.Patrolling.patrollingPoint, patrollingPoint);
        ParseObject autoPatrolling = ParseObject.create(Key.AutoPatrolling.NAME);
        autoPatrolling.setObjectId(modelAutoPatrolling.objectId);
        attendObject.put(Key.Patrolling.autoPatrolling, autoPatrolling);
        showProgress();
        attendObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                dismissProgress();
                if (e == null) {
                    activity.objId = "";
                    activity.isFromNoti = false;
                    currentItem.imgPatrolling.setEnabled(false);
                    currentItem.txtTime.setVisibility(View.VISIBLE);
                    modelAutoPatrolling.isComplete = "1";
                    modelAutoPatrolling.imagePath = imagePath;
                    TblAutoPatrolling.updatePatrollingComplete(modelAutoPatrolling, modelAutoPatrolling.objectId);
                    Func.showValidDialog(activity, "Patrolling submitted Successfully.");
                } else {
                    e.printStackTrace();
                    Func.showValidDialog(activity, "Patrolling submit failed." + e.getMessage());

                }
            }
        });

    }

    private void showProgress() {
        if (activity.isFinishing()) return;
        if (progressDialog == null) {
            progressDialog = new Progress(activity);
        }
        progressDialog.show();
    }

    private void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
