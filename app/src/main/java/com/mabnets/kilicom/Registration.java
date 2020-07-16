package com.mabnets.kilicom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import maes.tech.intentanim.CustomIntent;

public class Registration extends AppCompatActivity {
    private EditText etnames;
    private EditText etemails;
    private EditText etphno;
    private EditText etpassword;
    private EditText etBio;
    private EditText etContact;
    private Spinner  countyspinner;
    private TextView  loginref;
    private Button regbtn;
    private  ImageView circiv;
    private ProgressDialog progressDialog;
    private Mycommand mycommand;
    private ImageButton farmer_uploadpic;
    private String countys="";
    final String Tag=this.getClass().getName();
    private ArrayAdapter<String> countyadapter;
    private ArrayList<String> title;
    private CameraPhoto cameraPhoto;
    private GalleryPhoto galleryphoto;
    final int CAMERA_REQUEST =12211;
    final int GALLERY_REQUEST =12868;
    String selectedphoto="";
    private Dialog mydialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        circiv=(ImageView)findViewById(R.id.ivcreg);
        etnames=(EditText)findViewById(R.id.etrfullnames);
        etemails=(EditText)findViewById(R.id.etremail);
        etphno=(EditText)findViewById(R.id.etrphone);
        etpassword=(EditText)findViewById(R.id.etrpass);
        regbtn=(Button) findViewById(R.id.btnreg);
        loginref=(TextView)findViewById(R.id.loginrefz);
        countyspinner=(Spinner)findViewById(R.id.spcounties);
        etBio=(EditText)findViewById(R.id.etbioinfo);
        etContact=(EditText)findViewById(R.id.etcontactinfo);
        farmer_uploadpic=(ImageButton)findViewById(R.id.farmer_uploadbtn);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("loading..");
        mycommand=new Mycommand(this);
        mydialog=new Dialog(this);

        getcounties();
        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String names=etnames.getText().toString().trim();
                String em=etemails.getText().toString().trim();
                String bioinf=etBio.getText().toString().trim();
                String continf=etContact.getText().toString().trim();
                String pass=etpassword.getText().toString().trim();
                String phone=etphno.getText().toString().trim();
                Validatedetails(names,em,phone,pass,bioinf,continf,countys,selectedphoto);
            }
        });
        loginref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this,Login.class));
                CustomIntent.customType(Registration.this,"left-to-right");
            }
        });

        farmer_uploadpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             showpopup();

            }
        });
        countyspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object subcounyitem = parent.getItemAtPosition(position);
                countys=(String) subcounyitem;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void Validatedetails(final String f, final String e,final String ph,final  String password,final  String b,final  String cont,final  String cnty,final  String pic) {

        if (f.isEmpty()) {
            etnames.setError("name is invalid");
            etnames.requestFocus();
            return;
        } else if (e.isEmpty()) {
            etemails.setError("email is  is invalid");
            etemails.requestFocus();
            return;
        } else if(b.isEmpty()){
            etBio.setError("Bio contact is invalid");
            etBio.requestFocus();
            return;
        }  else if(cont.isEmpty()){
            etContact.setError("Contact info is invalid");
            etContact.requestFocus();
            return;
        } else if (ph.isEmpty()) {
            etphno.setError("phone is invalid");
            etphno.requestFocus();
            return;
        }else if(password.isEmpty()){
            etpassword.setError("password is invalid");
            etpassword.requestFocus();
            return;
        }else if(cnty.isEmpty()){
            Toast.makeText(Registration.this, "County must be selected", Toast.LENGTH_SHORT).show();;
        }else if(pic.isEmpty()){
            androidx.appcompat.app.AlertDialog.Builder alert=new AlertDialog.Builder(Registration.this);
            alert.setMessage("Please upload a photo of yourself by clicking the icon above");
            alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert.show();
        }
        else {
            if (!isphone(ph) || (ph.length() != 10 || !ph.startsWith("07"))) {
                etphno.setError("phone is invalid");
                etphno.requestFocus();
                return;
            } else  if (password.length() <= 5) {
                etpassword.setError("password is must be 6 characters or more");
                etpassword.requestFocus();
                return;
            }else if (!isValidEmail(e)) {
                etemails.setError("email is invalid");
                etemails.requestFocus();
                return;
            } else {

                String url="http://www.kilicom.co.ke/android/savefarmer.php";
                StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.d(Tag,response);
                        if(!response.isEmpty()){
                            if(response.contains("success")){
                                Toast.makeText(Registration.this, response, Toast.LENGTH_SHORT).show();
                                androidx.appcompat.app.AlertDialog.Builder alert=new AlertDialog.Builder(Registration.this);
                                alert.setMessage(response);
                                alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(Registration.this,Login.class));
                                    }
                                });
                                alert.setCancelable(false);
                                alert.show();
                            } else {
                                Toast.makeText(Registration.this, response, Toast.LENGTH_SHORT).show();
                                Log.d(Tag, response);

                            }

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error!=null) {
                            Log.d(Tag, error.toString());
                            if (error instanceof TimeoutError) {
                                progressDialog.dismiss();
                                Toast.makeText(Registration.this, "error time out ", Toast.LENGTH_SHORT).show();

                            } else if (error instanceof NoConnectionError) {
                                progressDialog.dismiss();
                                Toast.makeText(Registration.this, "error no connection", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof NetworkError) {
                                progressDialog.dismiss();
                                Toast.makeText(Registration.this, "error network error", Toast.LENGTH_SHORT).show();

                            }else if (error instanceof AuthFailureError) {
                                progressDialog.dismiss();
                                Toast.makeText(Registration.this, "errorin Authentication", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ParseError) {
                                progressDialog.dismiss();
                                Toast.makeText(Registration.this, "error while parsing", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ServerError) {
                                progressDialog.dismiss();
                                Toast.makeText(Registration.this, "error  in server", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ClientError) {
                                progressDialog.dismiss();
                                Toast.makeText(Registration.this, "error with Client", Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(Registration.this, "error while loading", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> params=new HashMap<>();
                        params.put("username", f);
                        params.put("email", e);
                        params.put("bio", b);
                        params.put("location", cnty);
                        params.put("contact", cont);
                        params.put("password", password);
                        params.put("phone", ph);
                        params.put("photo", pic);
                        return params;
                    }
                };
                mycommand.add(request);
                progressDialog.show();
                mycommand.execute();
                mycommand.remove(request);

            }

        }
    }
    private void getcounties() {
        String url = "http://www.kilicom.co.ke/android/getlocations.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Tag, response);
                progressDialog.dismiss();

                ArrayList<county> codetails = new JsonConverter<county>().toArrayList(response, county.class);
                title = new ArrayList<String>();
                for (county value : codetails) {
                    title.add(value.county);
                }
                countyadapter = new ArrayAdapter<String>(Registration.this, R.layout.spinnerlayout, title);
                countyspinner.setAdapter(countyadapter);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    progressDialog.dismiss();
                    Toast.makeText(Registration.this, "error time out ", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert=new AlertDialog.Builder(Registration.this);
                    alert.setMessage("please check your internet connectivity");
                    alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    });
                    alert.setCancelable(false);
                    alert.show();
                } else if (error instanceof NoConnectionError) {
                    progressDialog.dismiss();
                    Toast.makeText(Registration.this, "error no connection", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert=new AlertDialog.Builder(Registration.this);
                    alert.setMessage("please check your internet connectivity");
                    alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    });
                    alert.setCancelable(false);
                    alert.show();
                } else if (error instanceof NetworkError) {
                    progressDialog.dismiss();
                    Toast.makeText(Registration.this, "error network error", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert=new AlertDialog.Builder(Registration.this);
                    alert.setMessage("please check your internet connectivity");
                    alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    });
                    alert.setCancelable(false);
                    alert.show();
                } else if (error instanceof AuthFailureError) {
                    progressDialog.dismiss();
                    Toast.makeText(Registration.this, "errorin Authentication", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert=new AlertDialog.Builder(Registration.this);
                    alert.setMessage("please check your internet connectivity");
                    alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    });
                    alert.setCancelable(false);
                    alert.show();
                } else if (error instanceof ParseError) {
                    progressDialog.dismiss();
                    Toast.makeText(Registration.this, "error while parsing", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert=new AlertDialog.Builder(Registration.this);
                    alert.setMessage("please check your internet connectivity");
                    alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    });
                    alert.setCancelable(false);
                    alert.show();
                } else if (error instanceof ServerError) {
                    progressDialog.dismiss();
                    Toast.makeText(Registration.this, "error  in server", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert=new AlertDialog.Builder(Registration.this);
                    alert.setMessage("please check your internet connectivity");
                    alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    });
                    alert.show();
                } else if (error instanceof ClientError) {
                    progressDialog.dismiss();
                    Toast.makeText(Registration.this, "error with Client", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert=new AlertDialog.Builder(Registration.this);
                    alert.setMessage("please check your internet connectivity");
                    alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    });
                    alert.setCancelable(false);
                    alert.show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Registration.this, "error while loading", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert=new AlertDialog.Builder(Registration.this);
                    alert.setMessage("please check your internet connectivity");
                    alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    });
                    alert.setCancelable(false);
                    alert.show();
                }
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mycommand.add(stringRequest);
        progressDialog.show();
        mycommand.execute();
        mycommand.remove(stringRequest);
    }
    public final static boolean isValidEmail(String target) {

        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public static boolean isphone(CharSequence target){
        return !TextUtils.isEmpty(target) && Patterns.PHONE.matcher(target).matches();
    }
    public void showpopup(){
        ImageView campop;
        final ImageView gallerypop;
        TextView closebtn;
        mydialog.setContentView(R.layout.camerapopup);
        campop=(ImageView) mydialog.findViewById(R.id.camerabtnp);
        campop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED ){
                    cameraPhoto=new CameraPhoto(Registration.this);

                    try {
                        startActivityForResult(cameraPhoto.takePhotoIntent(),CAMERA_REQUEST);
                        cameraPhoto.addToGallery();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(Registration.this, "permission is required", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},CAMERA_REQUEST);
                    }

                }

            }
        });
        gallerypop=(ImageView) mydialog.findViewById(R.id.gallerybtnp);
        gallerypop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED ){
                    galleryphoto=new GalleryPhoto(Registration.this);
                    startActivityForResult(galleryphoto.openGalleryIntent(),GALLERY_REQUEST);
                }else{
                    Toast.makeText(Registration.this, "permission is required", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},CAMERA_REQUEST);
                    }

                }

            }
        });
        closebtn=(TextView)mydialog.findViewById(R.id.closebtn);
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mydialog.dismiss();
            }
        });
        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mydialog.show();
    }

    @Override
    public void  onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                String picpath = cameraPhoto.getPhotoPath();
                Bitmap bitmap = null;
                try {
                    bitmap = ImageLoader.init().from(picpath).requestSize(512, 512).getBitmap();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                circiv.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
                File f=new File(picpath);
                Uri uri=Uri.fromFile(f);
                if(Build.VERSION.SDK_INT<=27) {
                    circiv.setImageBitmap(bitmap);
                }else {
                    circiv.setImageURI(uri);
                }


                selectedphoto =ImageBase64.encode(bitmap);
                mydialog.dismiss();
            } else {
                if (requestCode == GALLERY_REQUEST) {
                    Uri uri = data.getData();
                    galleryphoto.setPhotoUri(uri);

                    String picpath = galleryphoto.getPath();
                    Bitmap bitmap = null;
                    try {
                        bitmap = ImageLoader.init().from(picpath).requestSize(512, 512).getBitmap();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                   circiv.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
                    if(Build.VERSION.SDK_INT<=27) {
                        circiv.setImageBitmap(bitmap);
                    }else{
                        circiv.setImageURI(uri);
                    }

                    selectedphoto =ImageBase64.encode(bitmap);
                    mydialog.dismiss();
                    Log.d(Tag,selectedphoto);
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
