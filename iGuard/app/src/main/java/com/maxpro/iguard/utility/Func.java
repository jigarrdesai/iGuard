package com.maxpro.iguard.utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.maxpro.iguard.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ketan on 3/29/2015.
 */
public class Func {
    public static String getFilePath() {
        String path;
        path = Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".png";
        return path;
    }

    public static void setSpBoolean(Activity activity, String key, boolean value) {
        SharedPreferences prefs = activity.getSharedPreferences(activity.getPackageName(), Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getSpBoolean(Activity activity, String key, boolean defaultVal) {
        SharedPreferences prefs = activity.getSharedPreferences(activity.getPackageName(), Activity.MODE_PRIVATE);
        return prefs.getBoolean(key, defaultVal);
    }

    public static void setSpString(Activity activity, String key, String value) {
        SharedPreferences prefs = activity.getSharedPreferences(activity.getPackageName(), Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getSpString(Activity activity, String key, String defaultVal) {
        SharedPreferences prefs = activity.getSharedPreferences(activity.getPackageName(), Activity.MODE_PRIVATE);
        return prefs.getString(key, defaultVal);
    }

    public static void clearSP(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(activity.getPackageName(), Activity.MODE_PRIVATE);
        prefs.edit().clear().commit();
    }
    public static DisplayImageOptions getDisplayOption() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()

                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .showImageForEmptyUri(R.drawable.contacts).showImageOnFail(R.drawable.contacts)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        return options;
    }
    public static void showValidDialog(Activity activity, String message){
        showValidDialog(activity,message,null);
    }
    public static void showValidDialog(Activity activity, String message,final ImplClick implClick) {
        final Dialog dialog = new Dialog(activity);
        Window window = dialog.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_validation);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        Button btnOk = (Button) dialog.findViewById(R.id.dialogvalid_btnOk);
        TextView txtMsg = (TextView) dialog.findViewById(R.id.dialogvalid_txtMsg);
        txtMsg.setText(message);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if(implClick!=null){
                    implClick.onOkClick(view);
                }
            }
        });
        dialog.show();

    }

    public static void showConfirmDialog(Activity activity, String message,final ImplClick implClick) {
        final Dialog dialog = new Dialog(activity);
        Window window = dialog.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_confirm);

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        Button btnOk = (Button) dialog.findViewById(R.id.dialogconfirm_btnOk);
        Button btnCancel = (Button) dialog.findViewById(R.id.dialogconfirm_btnCancel);
        TextView txtMsg = (TextView) dialog.findViewById(R.id.dialogconfirm_txtMsg);
        txtMsg.setText(message);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (implClick != null) {
                    implClick.onCancelClick(view);
                }
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if(implClick!=null){
                    implClick.onOkClick(view);
                }
            }
        });
        dialog.show();

    }

    public static String getCurrentDate(String format) {
        String currentDate = "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        currentDate = sdf.format(new Date());
        return currentDate;
    }
    public static String getStringFromMilli(String format,long milli) {
        String date = "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        date = sdf.format(new Date(milli));
        return date;
    }
    public static String getStringFromDate(String format,Date date) {
        String dateString = "";
        if(date!=null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            dateString = sdf.format(date);
        }
        return dateString;
    }

    public static String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release + ")";
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model;
        } else {
            return manufacturer + " " + model;
        }
    }

    public static boolean isLocationEnable(Context context) {

        boolean gps_enabled = false, network_enabled = false;

        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        return gps_enabled || network_enabled;
    }

    public static long getMillis(String format,String time){
        long milli=0;
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        try {
            milli=sdf.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return milli;
    }

    public static String convertDateFormat(String oldFormat,String newFormat,String dateString){
        SimpleDateFormat sdf=new SimpleDateFormat(oldFormat);
        try {
            Date date=sdf.parse(dateString);
            sdf=new SimpleDateFormat(newFormat);
            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static void copyDataBase(Context context) throws IOException {
        String packageName = context.getPackageName();
        String DB_PATH = String.format("//data//data//%s//databases//", packageName);
        String DB_NAME = "iguarddb.sqlite";
        // Open a stream for reading from our ready-made database
        // The stream source is located in the assets
        InputStream externalDbStream = new FileInputStream(DB_PATH + DB_NAME);

        // Path to the created empty database on your Android device
        String outFileName = Environment.getExternalStorageDirectory()+"/iguard.sqlite";

        // Now create a stream for writing the database byte by byte
        OutputStream localDbStream = new FileOutputStream(outFileName);

        // Copying the database
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = externalDbStream.read(buffer)) > 0) {
            localDbStream.write(buffer, 0, bytesRead);
        }
        // Don?t forget to close the streams
        localDbStream.close();
        externalDbStream.close();
    }
    public static int LevenshteinDistance (CharSequence lhs, CharSequence rhs) {
        if(TextUtils.isEmpty(lhs)||TextUtils.isEmpty(rhs)){
            return 0;
        }
        int len0 = lhs.length() + 1;
        int len1 = rhs.length() + 1;

        // the array of distances
        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        // initial cost of skipping prefix in String s0
        for (int i = 0; i < len0; i++) cost[i] = i;

        // dynamically computing the array of distances

        // transformation cost for each letter in s1
        for (int j = 1; j < len1; j++) {
            // initial cost of skipping prefix in String s1
            newcost[0] = j;

            // transformation cost for each letter in s0
            for(int i = 1; i < len0; i++) {
                // matching current letters in both strings
                int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;

                // computing cost for each transformation
                int cost_replace = cost[i - 1] + match;
                int cost_insert  = cost[i] + 1;
                int cost_delete  = newcost[i - 1] + 1;

                // keep minimum cost
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }

            // swap cost/newcost arrays
            int[] swap = cost; cost = newcost; newcost = swap;
        }

        // the distance is the cost for transforming all letters in both strings
        return cost[len0 - 1];
    }
}
