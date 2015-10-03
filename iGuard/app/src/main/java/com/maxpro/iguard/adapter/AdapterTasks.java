package com.maxpro.iguard.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.maxpro.iguard.IGuard;
import com.maxpro.iguard.R;
import com.maxpro.iguard.activity.ActTask;
import com.maxpro.iguard.db.TblTask;
import com.maxpro.iguard.model.ModelTask;
import com.maxpro.iguard.utility.Func;
import com.maxpro.iguard.utility.Key;
import com.maxpro.iguard.utility.Progress;
import com.maxpro.iguard.utility.Var;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ketan on 4/20/2015.
 */
public class AdapterTasks extends RecyclerView.Adapter<AdapterTasks.ItemHolder> {
    ArrayList<ModelTask> taskList;
    private ActTask activity;
    private Progress progressDialog;

    public AdapterTasks(ActTask activity, ArrayList<ModelTask> taskList) {
        this.activity = activity;
        this.taskList = taskList;
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return (taskList != null) ? taskList.size() : 0;
    }

    @Override
    public void onBindViewHolder(final ItemHolder itemHolder, int position) {

        final ModelTask modelTask = taskList.get(position);
        itemHolder.txtTaskName.setText(modelTask.taskName);
        itemHolder.txtDateTime.setText("Scheduled Time: "+modelTask.taskDateTime);
        itemHolder.txtPerformedTime.setText("Performed Time: "+modelTask.performedTime);
        if (modelTask.isComplete.equalsIgnoreCase("0")) {
            itemHolder.chkStatus.setChecked(false);
            itemHolder.chkStatus.setEnabled(true);
        } else {
            itemHolder.chkStatus.setChecked(true);
            itemHolder.chkStatus.setEnabled(false);
        }

        itemHolder.chkStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    uploadTaskToParse(modelTask, itemHolder.chkStatus);
                }
            }
        });

    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int arg1) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_task, parent, false);

        ItemHolder vh = new ItemHolder(v);
        return vh;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {
        TextView txtTaskName, txtDateTime,txtPerformedTime;
        CheckBox chkStatus;

        public ItemHolder(View v) {
            super(v);
            txtTaskName = (TextView) v.findViewById(R.id.rowtask_txtTaskName);
            txtDateTime = (TextView) v.findViewById(R.id.rowtask_txtDateTime);
            chkStatus = (CheckBox) v.findViewById(R.id.rowtask_chkStatus);
            txtPerformedTime=(TextView) v.findViewById(R.id.rowtask_txtPerfomedTime);
        }
    }

    /*private boolean checkComplete(String strDate1,String dbDate){
        boolean isComplete=false;
        try{

            SimpleDateFormat formatter = new SimpleDateFormat(Var.DF_DATE);

            Date date1 = formatter.parse(strDate1);
            Date date2 = formatter.parse(dbDate);

            if (date1.compareTo(date2)<0)
            {
                System.out.println("dbDate is Greater than my date1");
                isComplete=false;
            }else{
                isComplete=true;
            }

        }catch (ParseException e1){
            e1.printStackTrace();
        }
        Log.e("taskdate","date1= "+strDate1);
        Log.e("taskdate","dbDate= "+dbDate);
        Log.e("taskdate","isComplete= "+isComplete);
        return isComplete;
    }
*/
    private void uploadTaskToParse(final ModelTask modelTask, final CheckBox chkStatus) {

        modelTask.performedTime=Func.getCurrentDate("HH:mm");
        ParseObject attendObject = new ParseObject(Key.TaskReport.NAME);

        attendObject.put(Key.TaskReport.taskName, modelTask.taskName);
        attendObject.put(Key.TaskReport.performedTime, modelTask.performedTime);
        //attendObject.put(Key.TaskReport.taskDescription, modelTask.taskDescription);
        attendObject.put(Key.TaskReport.taskDateTime, modelTask.taskDateTime);
        ParseObject taskPointer = ParseObject.create(Key.Task.NAME);
        taskPointer.setObjectId(modelTask.objectId);
        attendObject.put(Key.TaskReport.tastPointer, taskPointer);
        ParseObject company = ParseObject.create("Company");
        company.setObjectId(modelTask.company);
        attendObject.put(Key.TaskReport.company, company);

        ParseObject site = ParseObject.create("Site");
        site.setObjectId(modelTask.site);
        attendObject.put(Key.TaskReport.site, site);

        ParseObject branch = ParseObject.create("Branch");
        branch.setObjectId(modelTask.branch);
        attendObject.put(Key.TaskReport.branch, branch);

        /*ParseObject supervisor = ParseObject.create("_User");
        supervisor.setObjectId(modelTask.supervisor);
        attendObject.put(Key.TaskReport.supervisor, supervisor);*/

        ParseUser currentUser = ParseUser.getCurrentUser();
        attendObject.put(Key.TaskReport.userPointer, currentUser);


        attendObject.put(Key.TaskReport.post, currentUser.getParseObject(Key.User.post));

        // ParseUser currentUser = ParseUser.getCurrentUser();
        showProgress();
        attendObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                dismissProgress();
                if (e == null) {
                    chkStatus.setChecked(true);
                    chkStatus.setEnabled(false);

                    modelTask.isComplete = "1";

                    TblTask.updateTaskComplete(modelTask, modelTask.objectId);
                    Func.showValidDialog(activity, "Task submitted Successfully.");
                    notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                    Func.showValidDialog(activity, "Task submit failed." + e.getMessage());

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
