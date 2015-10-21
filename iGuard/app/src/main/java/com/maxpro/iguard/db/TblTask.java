package com.maxpro.iguard.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.maxpro.iguard.IGuard;
import com.maxpro.iguard.model.ModelTask;

public class TblTask {
    public static String TABLE = "Tasks";
    public static String ID = "id";
    public static String objectId = "objectId";
    public static final String taskName = "taskName";
    public static final String taskDescription = "taskDescription";
    public static final String taskDateTime = "taskDateTime";
    public static final String company = "company";
    public static final String site = "site";
    public static final String branch = "branch";
    public static final String userPointer = "userPointer";
    public static final String supervisor = "supervisor";
    public static String isComplete = "isComplete";
    public static final String performedTime = "performedTime";
    public static final String createdAt = "createdAt";

    public static ArrayList<ModelTask> selectTask(String userid) {
        SQLiteDatabase db = IGuard.database;
        String query = "select * from " + TABLE + " where " + userPointer + "='" + userid + "' order by " + taskDateTime;
        ArrayList<ModelTask> arrTask = null;
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()) {
            arrTask = new ArrayList<ModelTask>();
            do {
                ModelTask model = new ModelTask();
                //model.areaId = c.getInt(c.getColumnIndex(areaId)) + "";
                model.objectId = c.getString(c.getColumnIndex(objectId));
                model.taskName = c.getString(c.getColumnIndex(taskName));
                model.taskDescription = c.getString(c.getColumnIndex(taskDescription));
                model.taskDateTime = c.getString(c.getColumnIndex(taskDateTime));
                model.company = c.getString(c.getColumnIndex(company));
                model.site = c.getString(c.getColumnIndex(site));
                model.branch = c.getString(c.getColumnIndex(branch));
                model.userPointer = c.getString(c.getColumnIndex(userPointer));
                model.supervisor = c.getString(c.getColumnIndex(supervisor));
                model.isComplete = c.getString(c.getColumnIndex(isComplete));
                model.performedTime = c.getString(c.getColumnIndex(performedTime));
                model.createdAt = c.getString(c.getColumnIndex(createdAt));
                arrTask.add(model);
            } while (c.moveToNext());
            c.close();
        }
        return arrTask;
    }

    public static ModelTask selectTaskByTaskId(String objId) {
        SQLiteDatabase db = IGuard.database;
        String query = "select * from " + TABLE + " where objectId='" + objId + "'";
        ModelTask model = null;
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()) {

            model = new ModelTask();
            model.objectId = c.getString(c.getColumnIndex(objectId));
            model.taskName = c.getString(c.getColumnIndex(taskName));
            model.taskDescription = c.getString(c.getColumnIndex(taskDescription));
            model.taskDateTime = c.getString(c.getColumnIndex(taskDateTime));
            model.company = c.getString(c.getColumnIndex(company));
            model.site = c.getString(c.getColumnIndex(site));
            model.branch = c.getString(c.getColumnIndex(branch));
            model.userPointer = c.getString(c.getColumnIndex(userPointer));
            model.supervisor = c.getString(c.getColumnIndex(supervisor));
            model.isComplete = c.getString(c.getColumnIndex(isComplete));
            model.performedTime = c.getString(c.getColumnIndex(performedTime));
            model.createdAt = c.getString(c.getColumnIndex(createdAt));
            c.close();
        }
        return model;
    }

    public static long insertTask(ModelTask model) {
        SQLiteDatabase db = IGuard.database;
        ContentValues values = new ContentValues();
        values.put(objectId, model.objectId);
        values.put(taskName, model.taskName);
        values.put(taskDescription, model.taskDescription);
        values.put(taskDateTime, model.taskDateTime);
        values.put(company, model.company);
        values.put(site, model.site);
        values.put(branch, model.branch);
        values.put(userPointer, model.userPointer);
        values.put(supervisor, model.supervisor);
        values.put(isComplete, model.isComplete);
        values.put(performedTime, model.performedTime);
        values.put(createdAt, model.createdAt);

        return db.insertOrThrow(TABLE, null, values);
    }

    public static long updateTaskComplete(ModelTask model, String id,String userid) {
        SQLiteDatabase db = IGuard.database;
        ContentValues values = new ContentValues();

        values.put(performedTime, model.performedTime);
        values.put(isComplete, model.isComplete);

        return db.update(TABLE, values, objectId + "='" + id + "' and "+userPointer+"='"+userid+"'", null);
    }

    public static void deleteTask(String id) {
        SQLiteDatabase db = IGuard.database;
        db.delete(TABLE, ID + "=" + id, null);
    }

    public static void deleteAllTask(String userid) {
        SQLiteDatabase db = IGuard.database;
        db.delete(TABLE, userPointer+"='"+userid+"'", null);
    }

    public static boolean isExist(String currentDate, String userid) {
        SQLiteDatabase db = IGuard.database;
        String query = "select * from " + TABLE + " where " + userPointer + "='" + userid + "' and " + createdAt + "='" + currentDate + "'";
        ModelTask model = null;
        Cursor c = db.rawQuery(query, null);
        if (c != null) {
            return c.getCount() > 0;
        }

        return false;
    }
}
