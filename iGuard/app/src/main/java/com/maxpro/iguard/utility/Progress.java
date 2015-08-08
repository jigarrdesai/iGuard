package com.maxpro.iguard.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

/**
 * Created by Ketan on 3/30/2015.
 */
public class Progress extends ProgressDialog {

    public Progress(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setProgressStyle(ProgressDialog.STYLE_SPINNER);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

    }
}