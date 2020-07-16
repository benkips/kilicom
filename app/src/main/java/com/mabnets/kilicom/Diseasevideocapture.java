package com.mabnets.kilicom;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iceteck.silicompressorr.SiliCompressor;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;
import net.gotev.uploadservice.UploadServiceSingleBroadcastReceiver;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.UUID;

public class Diseasevideocapture extends AppCompatActivity implements UploadStatusDelegate {
    private TextView tvidpath;
    private EditText etvidcrop;
    private EditText etviddesc;
    private Button vidbtn;
    private String phn;
    private SharedPreferences preferences;
    private ProgressDialog pd;
    private static final String TAG = "AndroidUploadService";
    private String img="";

    private UploadServiceSingleBroadcastReceiver uploadReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diseasevideocapture);
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        uploadReceiver = new UploadServiceSingleBroadcastReceiver(Diseasevideocapture.this);

        Intent intent = getIntent();
        img = intent.getStringExtra("video");

        pd=new ProgressDialog(Diseasevideocapture.this);
        pd.setMessage("uploading..");

        tvidpath = (TextView) findViewById(R.id.vidpath);
        etvidcrop = (EditText) findViewById(R.id.etvidcrop);
        etviddesc = (EditText) findViewById(R.id.etviddesc);
        vidbtn = (Button) findViewById(R.id.btnvidsend);

        preferences = getSharedPreferences("logininfo.conf", MODE_PRIVATE);
        phn = preferences.getString("phone", "");

        tvidpath.setText(img);
        compressor(img);
        vidbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c = etvidcrop.getText().toString().trim();
                String d = etviddesc.getText().toString().trim();
                reportdisease(img, c, d);
            }
        });


    }

    private void reportdisease(final String video, final String animcrop, final String details) {
        if (animcrop.isEmpty()) {
            etvidcrop.setError("Enter crop or the animal above");
            etvidcrop.requestFocus();
            return;
        } else if (details.isEmpty()) {
            etviddesc.setError("Please give a brief description");
            etviddesc.requestFocus();
            return;
        } else {
            if (video.isEmpty()) {
                Toast.makeText(Diseasevideocapture.this, "make sure you upload a video", Toast.LENGTH_LONG).show();
            } else {
                try {

                    String uploadId = UUID.randomUUID().toString();
                    uploadReceiver.setUploadID(uploadId);
                   /* http://www.kilicom.mabnets.com/android/savedisease.php*/
                    String url = "http://www.kilicom.co.ke/android/savedisease.php";
                    //Creating a multi part request
                    new MultipartUploadRequest(this, uploadId, url)
                            .addFileToUpload(video, "video") //Adding file
                            .addParameter("crop", animcrop) //Adding text parameter to the request
                            .addParameter("details", details) //Adding text parameter to the request
                            .addParameter("phone", phn) //Adding text parameter to the request
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(1)
                            .startUpload(); //Starting the upload

                } catch (Exception exc) {
                    Log.e(TAG, exc.getMessage(), exc);
                    Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public void onProgress(Context context, UploadInfo uploadInfo) {
        pd.show();
    }

    @Override
    public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
        pd.dismiss();
        Log.e(TAG, exception.getMessage(), exception);
        /*Toast.makeText(context, "Error while uploading"+exception.getMessage(), Toast.LENGTH_SHORT).show();*/
        AlertDialog.Builder alert=new AlertDialog.Builder(context);
        alert.setMessage("Error while uploading");
        alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        alert.show();
    }

    @Override
    public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
        pd.dismiss();
        AlertDialog.Builder alert=new AlertDialog.Builder(context);
        alert.setMessage("Upload completed"+serverResponse.getBodyAsString());
        alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        alert.show();
    }

    @Override
    public void onCancelled(Context context, UploadInfo uploadInfo) {
        AlertDialog.Builder alert=new AlertDialog.Builder(context);
        alert.setMessage("Upload Error");
        alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        uploadReceiver.register(Diseasevideocapture.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        uploadReceiver.unregister(Diseasevideocapture.this);
    }
    private void compressor(String path){
        File f = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/Silicompressor/videos");
        if (f.mkdirs() || f.isDirectory())
            //compress and output new video specs
            new VideoCompressAsyncTask(this).execute(path, f.getPath());
    }
    class VideoCompressAsyncTask extends AsyncTask<String, String, String> {

        Context mContext;

        public VideoCompressAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Compressing..");
            pd.show();
        }

        @Override
        protected String doInBackground(String... paths) {
            String filePath = null;
            try {

                filePath = SiliCompressor.with(mContext).compressVideo(paths[0], paths[1]);

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return filePath;

        }


        @Override
        protected void onPostExecute(String compressedFilePath) {
            super.onPostExecute(compressedFilePath);
            File imageFile = new File(compressedFilePath);
            float length = imageFile.length() / 1024f; // Size in KB
            String value;
            if (length >= 1024)
                value = length / 1024f + " MB";
            else
                value = length + " KB";
            String text = String.format(Locale.US, "%s\nName: %s\nSize: %s", "compression complete", imageFile.getName(), value);
            pd.setMessage("completed size"+text);

            Log.i("Silicompressor", "Path: " + compressedFilePath);
            img=compressedFilePath;
            vidbtn.setEnabled(true);
            pd.dismiss();
        }
    }
}
