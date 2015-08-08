package com.maxpro.iguard.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.maxpro.iguard.IGuard;
import com.maxpro.iguard.model.ModelAutoPatrolling;
import com.maxpro.iguard.model.ModelTask;

import java.util.ArrayList;

public class TblAutoPatrolling {

	public static String TABLE = "autopatrolling";
    public static String ID="id";
	public static String objectId = "objectId";
    public static final String company = "company";
    public static final String branch = "branch";
    public static final String supervisor = "supervisor";
    public static final String type = "type";
    public static final String patrollingDate = "patrollingDate";
    public static final String patrollingTime = "patrollingTime";
    public static final String userPointer = "userPointer";
    public static final String shift = "shift";
    public static final String patrollingEndTime = "patrollingEndTime";
public static final String imagePath="imagePath";
	public static String isComplete = "isComplete";


	public static ArrayList<ModelAutoPatrolling> selectPatrolling() {
		SQLiteDatabase db = IGuard.database;
		String query = "select * from " + TABLE;
		ArrayList<ModelAutoPatrolling> arrTask = null;
		Cursor c = db.rawQuery(query, null);
		if (c != null && c.moveToFirst()) {
            arrTask = new ArrayList<ModelAutoPatrolling>();
			do {
                ModelAutoPatrolling model = new ModelAutoPatrolling();
				//model.areaId = c.getInt(c.getColumnIndex(areaId)) + "";
                model.objectId = c.getString(c.getColumnIndex(objectId));
                model.company = c.getString(c.getColumnIndex(company));
                model.branch = c.getString(c.getColumnIndex(branch));
                model.supervisor = c.getString(c.getColumnIndex(supervisor));
                model.type = c.getString(c.getColumnIndex(type));
                model.patrollingDate = c.getString(c.getColumnIndex(patrollingDate));
                model.patrollingTime = c.getString(c.getColumnIndex(patrollingTime));
                model.userPointer = c.getString(c.getColumnIndex(userPointer));
                model.shift = c.getString(c.getColumnIndex(shift));
                model.patrollingEndTime = c.getString(c.getColumnIndex(patrollingEndTime));
                model.isComplete = c.getString(c.getColumnIndex(isComplete));
                model.imagePath = c.getString(c.getColumnIndex(imagePath));
                arrTask.add(model);
			} while (c.moveToNext());
			c.close();
		}
		return arrTask;
	}

    public static ArrayList<ModelAutoPatrolling> selectCompletedPatrolling() {
        SQLiteDatabase db = IGuard.database;
        String query = "select * from " + TABLE+" where "+isComplete+"='1'";
        ArrayList<ModelAutoPatrolling> arrTask = null;
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()) {
            arrTask = new ArrayList<ModelAutoPatrolling>();
            do {
                ModelAutoPatrolling model = new ModelAutoPatrolling();
                //model.areaId = c.getInt(c.getColumnIndex(areaId)) + "";
                model.objectId = c.getString(c.getColumnIndex(objectId));
                model.company = c.getString(c.getColumnIndex(company));
                model.branch = c.getString(c.getColumnIndex(branch));
                model.supervisor = c.getString(c.getColumnIndex(supervisor));
                model.type = c.getString(c.getColumnIndex(type));
                model.patrollingDate = c.getString(c.getColumnIndex(patrollingDate));
                model.patrollingTime = c.getString(c.getColumnIndex(patrollingTime));
                model.userPointer = c.getString(c.getColumnIndex(userPointer));
                model.shift = c.getString(c.getColumnIndex(shift));
                model.patrollingEndTime = c.getString(c.getColumnIndex(patrollingEndTime));
                model.isComplete = c.getString(c.getColumnIndex(isComplete));
                model.imagePath=c.getString(c.getColumnIndex(imagePath));
                arrTask.add(model);
            } while (c.moveToNext());
            c.close();
        }
        return arrTask;
    }
    public static ArrayList<ModelAutoPatrolling> selectCompletedOrSpecific(String objId) {
        SQLiteDatabase db = IGuard.database;
        String query = "select * from " + TABLE+" where "+isComplete+"='1' or "+objectId+"='"+objId+"'";
        ArrayList<ModelAutoPatrolling> arrTask = null;
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()) {
            arrTask = new ArrayList<ModelAutoPatrolling>();
            do {
                ModelAutoPatrolling model = new ModelAutoPatrolling();
                //model.areaId = c.getInt(c.getColumnIndex(areaId)) + "";
                model.objectId = c.getString(c.getColumnIndex(objectId));
                model.company = c.getString(c.getColumnIndex(company));
                model.branch = c.getString(c.getColumnIndex(branch));
                model.supervisor = c.getString(c.getColumnIndex(supervisor));
                model.type = c.getString(c.getColumnIndex(type));
                model.patrollingDate = c.getString(c.getColumnIndex(patrollingDate));
                model.patrollingTime = c.getString(c.getColumnIndex(patrollingTime));
                model.userPointer = c.getString(c.getColumnIndex(userPointer));
                model.shift = c.getString(c.getColumnIndex(shift));
                model.patrollingEndTime = c.getString(c.getColumnIndex(patrollingEndTime));
                model.isComplete = c.getString(c.getColumnIndex(isComplete));
                model.imagePath=c.getString(c.getColumnIndex(imagePath));
                arrTask.add(model);
            } while (c.moveToNext());
            c.close();
        }
        return arrTask;
    }
    public static ModelAutoPatrolling selectByPatrollingId(String objId) {
        SQLiteDatabase db = IGuard.database;
        String query = "select * from " + TABLE + " where objectId='" + objId+"'";
        ModelAutoPatrolling model=null;
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()) {

             model = new ModelAutoPatrolling();
            //model.areaId = c.getInt(c.getColumnIndex(areaId)) + "";
            model.objectId = c.getString(c.getColumnIndex(objectId));
            model.company = c.getString(c.getColumnIndex(company));
            model.branch = c.getString(c.getColumnIndex(branch));
            model.supervisor = c.getString(c.getColumnIndex(supervisor));
            model.type = c.getString(c.getColumnIndex(type));
            model.patrollingDate = c.getString(c.getColumnIndex(patrollingDate));
            model.patrollingTime = c.getString(c.getColumnIndex(patrollingTime));
            model.userPointer = c.getString(c.getColumnIndex(userPointer));
            model.shift = c.getString(c.getColumnIndex(shift));
            model.patrollingEndTime = c.getString(c.getColumnIndex(patrollingEndTime));
            model.isComplete = c.getString(c.getColumnIndex(isComplete));
            model.imagePath=c.getString(c.getColumnIndex(imagePath));
            c.close();
        }
        return model;
    }
	public static long insertPatrolling(ModelAutoPatrolling model) {
		SQLiteDatabase db = IGuard.database;
		ContentValues values = new ContentValues();
		values.put(objectId, model.objectId);
        values.put(company, model.company);
        values.put(branch, model.branch);
        values.put(supervisor, model.supervisor);
        values.put(type, model.type);
        values.put(patrollingDate, model.patrollingDate);
        values.put(patrollingTime, model.patrollingTime);
        values.put(userPointer, model.userPointer);
        values.put(shift, model.shift);
        values.put(patrollingEndTime, model.patrollingEndTime);
        values.put(isComplete, model.isComplete);

		return db.insertOrThrow(TABLE, null, values);
	}

	public static long updatePatrollingComplete(ModelAutoPatrolling model,String id) {
		SQLiteDatabase db = IGuard.database;
		ContentValues values = new ContentValues();


		values.put(isComplete, model.isComplete);
        values.put(imagePath, model.imagePath);
		return db.update(TABLE, values, objectId+"='"+id+"'", null);
	}

	public static void deletePatrolling(String id) {
		SQLiteDatabase db=IGuard.database;
		db.delete(TABLE,ID + "=" + id, null);
	}
    public static void deleteAllPatrolling() {
        SQLiteDatabase db=IGuard.database;
        db.delete(TABLE,null, null);
    }
}
