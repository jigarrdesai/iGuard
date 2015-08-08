package com.maxpro.iguard.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxpro.iguard.R;
import com.maxpro.iguard.utility.AppLog;
import com.maxpro.iguard.utility.Func;
import com.maxpro.iguard.utility.ImplClick;
import com.maxpro.iguard.utility.Var;
import com.parse.ParseFile;
import com.parse.ParseObject;
import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AdapterVideoTraining extends Adapter<AdapterVideoTraining.ItemHolder> {
    private List<ParseObject> videoList;
private Activity activity;

    public AdapterVideoTraining(Activity activity,List<ParseObject> videoList) {
        this.activity=activity;
        this.videoList = videoList;

    }

    public void addRow(ParseObject obj) {
        if (videoList == null)
            videoList = new ArrayList<>();
        videoList.add(obj);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return (videoList != null) ? videoList.size() : 0;
    }

    @Override
    public void onBindViewHolder(ItemHolder itemHolder, int arg1) {
        String description = videoList.get(arg1).getString("description");
        String videoTitle = videoList.get(arg1).getString("videoTitle");
        final ParseFile videoFile = videoList.get(arg1).getParseFile("videoFile");
        itemHolder.txtTitle.setText(videoTitle);
        itemHolder.txtDesc.setText(description);
        itemHolder.txtDate.setText(Func.getStringFromMilli(Var.DF_DATETIME, videoList.get(arg1).getCreatedAt().getTime()));

        if (videoFile != null) {
            String videourl=videoFile.getUrl();
            String fileName=videourl.substring(videourl.lastIndexOf("/") + 1);
            AppLog.e("Filename= "+fileName);
            final File file=new File(Var.VIDEO_FOLDER+"/"+fileName);

            itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        Intent intent;
                        if(file.exists()){
                            intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(file), "video/*");
                        }else {
                            intent= new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.parse(videoFile.getUrl()), "video/*");
                        }
                        activity.startActivity(intent);
                    }

            });
            if(file.exists()){
                itemHolder.imgDownload.setVisibility(View.GONE);
                itemHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Func.showConfirmDialog(activity,"Are you sure you want to delete this video?",new ImplClick() {
                            @Override
                            public void onOkClick(View v) {
                                file.delete();
                                notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelClick(View v) {
                            }
                        });
                        return true;
                    }
                });
            }else{
                itemHolder.itemView.setOnLongClickListener(null);
                itemHolder.imgDownload.setVisibility(View.VISIBLE);
            }
        }




        itemHolder.imgDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoFile != null) {
                    new DownloadFileFromURL().execute(videoFile.getUrl());
                }

            }
        });

    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int arg1) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_videotraining, parent, false);

        ItemHolder vh = new ItemHolder(v);
        return vh;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDesc,txtDate;
        ImageView imgThumb,imgDownload;

        public ItemHolder(View v) {
            super(v);
            txtTitle = (TextView) v.findViewById(R.id.rowvideotraining_txtTitle);
            txtDesc = (TextView) v.findViewById(R.id.rowvideotraining_txtDesc);
            txtDate = (TextView) v.findViewById(R.id.rowvideotraining_txtDate);
            imgThumb = (ImageView) v.findViewById(R.id.rowvideotraining_imgThumb);
            imgDownload= (ImageView) v.findViewById(R.id.rowvideotraining_imgDownload);
        }
    }
    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;
        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage("Downloading file. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String fileName=f_url[0].substring(f_url[0].lastIndexOf("/") + 1);
                // Output stream
                OutputStream output = new FileOutputStream(Var.VIDEO_FOLDER+"/"+fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                AppLog.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
           pDialog.dismiss();
notifyDataSetChanged();
            // Displaying downloaded image into image view
            // Reading image path from sdcard
            //String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.jpg";
            // setting downloaded into image view
            //my_image.setImageDrawable(Drawable.createFromPath(imagePath));
        }

    }
}
