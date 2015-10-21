package com.maxpro.iguard;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.maxpro.iguard.db.DbHelper;
import com.maxpro.iguard.utility.AppLog;
import com.maxpro.iguard.utility.Func;
import com.maxpro.iguard.utility.Var;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;
import com.parse.ParseObject;

import java.io.File;
import java.io.IOException;

/**
 * Created by Ketan on 3/22/2015.
 */
public class IGuard extends Application {
    public static ImageLoader imageLoader;
    private Thread.UncaughtExceptionHandler defaultUEH;
    public static SQLiteDatabase database;

    DbHelper dbHelper;
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, Var.PARSE_APPID, Var.PARSE_CLIENTKEY);
        defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(handler);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        createFolder();
        dbHelper=new DbHelper(this, "iguarddb.sqlite");
        database=dbHelper.openDataBase();
        /*try {
            Func.copyDataBase(this);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    private void createFolder() {
        File imgFolder = new File(Var.IMAGE_FOLDER);
        if (!imgFolder.exists()) {
            imgFolder.mkdirs();
        }
        File videoFolder = new File(Var.VIDEO_FOLDER);
        if (!videoFolder.exists()) {
            videoFolder.mkdirs();
        }
    }

    private Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            AppLog.e("TestApplication", "Uncaught exception is: ");
            ex.printStackTrace();
            // upload to parse server.
            uploadError(ex.getMessage());
            defaultUEH.uncaughtException(thread, ex);
        }
    };

    private void uploadError(String error){
        ParseObject object=new ParseObject("ErrorLog");
        object.put("error",error);
        object.put("platform","Android");
        object.put("version", Func.getAndroidVersion());
        object.put("Device",Func.getDeviceName());
        object.saveInBackground();
    }
}


