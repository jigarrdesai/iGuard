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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.maxpro.iguard.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

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

    public static void showValidDialog(Activity activity, String message) {
        final Dialog dialog = new Dialog(activity);
        Window window = dialog.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_validation);

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        Button btnOk = (Button) dialog.findViewById(R.id.dialogvalid_btnOk);
        TextView txtMsg = (TextView) dialog.findViewById(R.id.dialogvalid_txtMsg);
        txtMsg.setText(message);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
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
                if(implClick!=null){
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


}
