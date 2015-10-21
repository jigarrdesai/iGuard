package com.maxpro.iguard.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.maxpro.iguard.R;
import com.maxpro.iguard.adapter.AdapterMsgBoard;
import com.maxpro.iguard.adapter.AdapterTasks;
import com.maxpro.iguard.db.TblTask;
import com.maxpro.iguard.model.ModelTask;
import com.maxpro.iguard.utility.AppLog;
import com.maxpro.iguard.utility.Func;
import com.maxpro.iguard.utility.Key;
import com.maxpro.iguard.utility.Progress;
import com.maxpro.iguard.utility.Var;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ketan on 4/20/2015.
 */
public class ActTask extends ActDrawer {
    private RecyclerView recyclerView;
    Progress progressDialog;
    public String lastTaskFetch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_task);
        initialize();

    }

    private void initialize() {

        setHeaderText(getString(R.string.dashboard_tasks));
        setClickListener(click);
        setBackButtonClick(this);
        progressDialog = new Progress(this);
        recyclerView = (RecyclerView) findViewById(R.id.task_recyclerview);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        lastTaskFetch = (String) Func.getSpString(this, Var.Last_FetchDateTask, "01-01-1991");
        String today = Func.getCurrentDate(Var.DF_DATE);
        if (!TblTask.isExist(Func.getCurrentDate(Var.DF_DATE),currentUser.getObjectId())) {
            TblTask.deleteAllTask(currentUser.getObjectId());
            getTaskListFromServer();
            Func.setSpString(this,Var.Last_FetchDateTask,today);
        }else{
            getTaskListFromDb();
        }
        /*if (isFetch(today, lastTaskFetch)) {
            TblTask.deleteAllTask();
            getTaskListFromServer();
            Func.setSpString(this,Var.Last_FetchDateTask,today);
        }else{
            getTaskListFromDb();
        }*/

    }

    private boolean isFetch(String todayDate, String lastDate) {
        boolean isFetch = false;
        try {

            SimpleDateFormat formatter = new SimpleDateFormat(Var.DF_DATE);

            Date date1 = formatter.parse(todayDate);
            Date date2 = formatter.parse(lastDate);

            if (date2.before(date1)) {
                isFetch = true;
            } else {
                isFetch = false;
            }

        } catch (java.text.ParseException e1) {
            e1.printStackTrace();
        }
        Log.e("taskdate", "todayDate= " + todayDate);
        Log.e("taskdate", "lastDate= " + lastDate);
        Log.e("taskdate", "isFetch= " + isFetch);
        return isFetch;
    }
    private void getTaskListFromServer(){
        progressDialog.show();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Key.Task.NAME);
        ParseObject post=currentUser.getParseObject(Key.User.post);
        ParseObject shift=currentUser.getParseObject(Key.User.shift);
        query.whereEqualTo(Key.Task.post, post);
        query.whereEqualTo(Key.Task.shift, shift);
        query.whereNotEqualTo(Key.Task.deleted,true);
        query.whereEqualTo(Key.Task.status, "Active");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> taskList, ParseException e) {
                progressDialog.dismiss();
                if (e == null&&taskList!=null) {
                    AppLog.d("Retrieved " + taskList.size() + " task");
                    for(ParseObject obj:taskList) {
                        ModelTask modelTask=new ModelTask();
                        //Log.e("task pointer","user= "+obj.getParseObject(Key.Task.userPointer).getObjectId());
                        modelTask.objectId=obj.getObjectId();
                        modelTask.taskName=obj.getString(Key.Task.taskName);
                        modelTask.taskDescription=obj.getString(Key.Task.taskDescription);
                        modelTask.taskDateTime=obj.getString(Key.Task.taskDateTime);
                        modelTask.company=obj.getParseObject(Key.Task.company).getObjectId();
                        modelTask.site =obj.getParseObject(Key.Task.site).getObjectId();
                        modelTask.branch=obj.getParseObject(Key.Task.branch).getObjectId();
                        modelTask.userPointer=currentUser.getObjectId();
                        modelTask.createdAt=Func.getCurrentDate(Var.DF_DATE);
                        //modelTask.supervisor=obj.getParseObject(Key.Task.supervisor).getObjectId();
                        if(Func.getCurrentDate(Var.DF_DATE).equalsIgnoreCase(Func.getStringFromDate(Var.DF_DATE,obj.getDate(Key.Task.performedTime))))
                        {
                            modelTask.isComplete=obj.getString(Key.Task.isComplete);
                            modelTask.performedTime=Func.getStringFromDate(Var.DF_DATETIME, obj.getDate(Key.Task.performedTime));
                        }


                        TblTask.insertTask(modelTask);
                    }

                } else {
                    AppLog.d("Error: " + e.getMessage());
                }
                getTaskListFromDb();
            }
        });
    }
    private void getTaskListFromDb() {

        progressDialog.show();
        ArrayList<ModelTask> arrModelTask = TblTask.selectTask(currentUser.getObjectId());

        progressDialog.dismiss();

       // AppLog.d("Retrieved " + arrModelTask.size() + " task");
        AdapterTasks adapterTaks = new AdapterTasks(ActTask.this, arrModelTask);
        recyclerView.setAdapter(adapterTaks);

    }
}
