package com.maxpro.iguard.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.maxpro.iguard.IGuard;
import com.maxpro.iguard.R;
import com.maxpro.iguard.utility.Func;
import com.maxpro.iguard.utility.Progress;
import com.maxpro.iguard.utility.Var;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ActFullImage extends Activity {
    private ImageView imageView;
private String imgUrl;
    private Progress progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_full_image);
        init();
    }

    private void init() {
        progressDialog=new Progress(this);
        imgUrl=getIntent().getStringExtra(Var.IntentUrl);
        if(TextUtils.isEmpty(imgUrl)){
            Toast.makeText(this,"Image not available.",Toast.LENGTH_SHORT).show();
            finish();
        }
        if(!imgUrl.startsWith("http")){
            imgUrl="file:///"+imgUrl;
        }
        imageView = (ImageView) findViewById(R.id.fullimage_imageView);
        progressDialog.show();
        IGuard.imageLoader.displayImage(imgUrl, imageView, Func.getDisplayOption(),new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                progressDialog.dismiss();
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                progressDialog.dismiss();
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                progressDialog.dismiss();
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                progressDialog.dismiss();
            }
        });
    }
}
