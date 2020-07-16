package com.mabnets.kilicom;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import maes.tech.intentanim.CustomIntent;

public class Diseasecapture extends AppCompatActivity {
    private String img;
    private String encodedstring;
    private ImageView ivdcb;
    private EditText etcropsdc;
    private EditText etdetailsdc;
    private String phn;
    private Button senddc;
    private SharedPreferences preferences;
    private ProgressDialog progressDialog;
    private Mycommand mycommand;
    final String Tag=this.getClass().getName();
    private Bitmap bitmap=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diseasecapture);
        ivdcb=(ImageView)findViewById(R.id.ivdcpic);
        etdetailsdc=(EditText) findViewById(R.id.etdcdesc);
        etcropsdc=(EditText)findViewById(R.id.etdccrop);
        senddc=(Button)findViewById(R.id.btnsenddc);

        preferences=getSharedPreferences("logininfo.conf",MODE_PRIVATE);
        phn=preferences.getString("phone","");


        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("loading..");
        mycommand=new Mycommand(this);
        try {

            Intent intent = getIntent();
            img= intent.getStringExtra("Imagex");
            try {
                 bitmap= ImageLoader.init().from(img).requestSize(512,512).getBitmap();
                ivdcb.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }



        } catch(Exception e) {
            e.printStackTrace();
        }

        senddc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String c=etcropsdc.getText().toString().trim();
            String d=etdetailsdc.getText().toString().trim();
               /* encodedstring= ImageBase64.encode(bitmap);*/
            reportdisease(img,c,d);
            }
        });



    }
    private void  reportdisease(final String photo, final String animcrop, final String details){
        Bitmap bitmapx=null;
        try {
            bitmapx= ImageLoader.init().from(photo).requestSize(512,512).getBitmap();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (animcrop.isEmpty()) {
            etcropsdc.setError("Enter crop or the animal above");
            etcropsdc.requestFocus();
            return;
        } else if (details.isEmpty()) {
            etdetailsdc.setError("Please give a brief description");
            etdetailsdc.requestFocus();
            return;
        } else {
            if (photo.isEmpty()) {
                Toast.makeText(Diseasecapture.this,"make sure you upload a photo",Toast.LENGTH_LONG).show();
            }else {

                 encodedstring= ImageBase64.encode(bitmapx);
                String url = "http://www.kilicom.co.ke/android/savedisease.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.d(Tag, response);
                        if(response.equals("success")){
                            etdetailsdc.setText("");
                            etcropsdc.setText("");

                            androidx.appcompat.app.AlertDialog.Builder alert=new AlertDialog.Builder(Diseasecapture.this);
                            alert.setMessage("Submitted sucessfully the conditon you uploaded will be analyzed  and results sent to you in 48 hrs");
                            alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(Diseasecapture.this,MainActivity.class));
                                    Diseasecapture.this.finish();
                                }
                            });
                            alert.setCancelable(false);
                            alert.show();
                        } else {
                            Toast.makeText(Diseasecapture.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        if (error instanceof TimeoutError) {
                            progressDialog.dismiss();
                            Toast.makeText(Diseasecapture.this, "error time out  No intermnet", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NoConnectionError) {
                            progressDialog.dismiss();
                            Toast.makeText(Diseasecapture.this, "error no connection", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            progressDialog.dismiss();
                            Toast.makeText(Diseasecapture.this, "error network error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            progressDialog.dismiss();
                            Toast.makeText(Diseasecapture.this, "errorin Authentication", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            progressDialog.dismiss();
                            Toast.makeText(Diseasecapture.this, "error while parsing", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            progressDialog.dismiss();
                            Toast.makeText(Diseasecapture.this, "error  in server", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ClientError) {
                            progressDialog.dismiss();
                            Toast.makeText(Diseasecapture.this, "error with Client", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Diseasecapture.this, "error while loading", Toast.LENGTH_SHORT).show();
                        }

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("phone",phn);
                        params.put("crop",animcrop);
                        params.put("details",details);
                        params.put("photo",encodedstring);
                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                mycommand.add(stringRequest);
                progressDialog.show();
                mycommand.execute();
                mycommand.remove(stringRequest);

            }
        }
    }
}
