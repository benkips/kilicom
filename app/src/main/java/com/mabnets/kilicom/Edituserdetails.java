package com.mabnets.kilicom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
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
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.caching.FileCacher;
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

public class Edituserdetails extends AppCompatActivity {
    private EditText uname;
    private EditText uemail;
    private EditText uphone;
    private EditText ucon;
    private Spinner ulocatin;
    private EditText ubio;
    private Button ubtn;
    private ImageButton uploadbtn;
    private ProgressDialog progressDialog;
    private Mycommand mycommand;
    private ImageView dpiv;
    private ArrayList<String> title;
    private String countys = "";
    final String Tag = this.getClass().getName();
    private ArrayAdapter<String> countyadapter;
    private Dialog mydialog;
    private CameraPhoto cameraPhoto;
    private GalleryPhoto galleryphoto;
    final int CAMERA_REQUEST = 12911;
    final int GALLERY_REQUEST = 12768;
    private String selectedphoto = "";
    private FileCacher<String> stringcacher;
    private SharedPreferences preferences;
    String  loco="";
    private  String p="";
    private String c="";
    private String phn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edituserdetails);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading..");
        mycommand = new Mycommand(this);
        mydialog = new Dialog(this);
        stringcacher=new FileCacher<>(getApplicationContext(),"nav.txt");
        preferences = getSharedPreferences("logininfo.conf", MODE_PRIVATE);
        phn = preferences.getString("phone", "");

        uname = (EditText) findViewById(R.id.eteuname);
        uemail = (EditText) findViewById(R.id.eteuemail);
        uphone = (EditText) findViewById(R.id.eteuphone);
        ulocatin = (Spinner) findViewById(R.id.speulocation);
        ubio = (EditText) findViewById(R.id.eteubio);
        ubtn = (Button) findViewById(R.id.btneuedit);
        dpiv = (ImageView) findViewById(R.id.iveudpx);
        ucon=(EditText)findViewById(R.id.eteucont);
        uploadbtn = (ImageButton) findViewById(R.id.iveuploadbtn);

        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }


        try {
            String t=stringcacher.readCache();
            ArrayList<farmerdetails> navlist=new JsonConverter<farmerdetails>().toArrayList(t,farmerdetails.class);
            ArrayList<String> title=new ArrayList<String>();
            for (farmerdetails value : navlist) {
                title.add(value.name);
                title.add(value.email);
                title.add(value.photo);
                title.add(value.location);
                title.add(value.bio);
                title.add(value.phone);
                title.add(value.contact);
            }
            uname.setText(title.get(0));
            uphone.setText("+254"+title.get(5));
            uemail.setText(title.get(1));
            uphone.setEnabled(false);
            loco =title.get(3);
            ubio.setText("\n"+title.get(4));
            String encodedstring=title.get(2);
            ucon.setText(title.get(6));

            p=title.get(5);
            c=title.get(6);
            /*Log.d(Tag, encodedstring);*/
            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage("http://www.kilicom.mabnets.com/photos/" + title.get(2), dpiv);



        } catch (IOException e) {
            e.printStackTrace();
        }

        getlocation();

        String finalP = p;
        String finalC = c;
        ubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedphoto.equals("")){
                    selectedphoto="none";
                    String u=uname.getText().toString().trim();
                    String b=ubio.getText().toString().trim();
                    String e=uemail.getText().toString().trim();
                    c=ucon.getText().toString().trim();

                    updateuserdetails(selectedphoto,u,e,countys,b, p, c);
                }else{
                    String u=uname.getText().toString().trim();
                    String b=ubio.getText().toString().trim();
                    String e=uemail.getText().toString().trim();
                    c=ucon.getText().toString().trim();
                    updateuserdetails(selectedphoto,u,e,countys,b, p, c);
                }

            }
        });
        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpopup();
            }
        });

        ulocatin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object subcounyitem = parent.getItemAtPosition(position);
                countys = (String) subcounyitem;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getlocation() {
        String url = "http://www.kilicom.mabnets.com/android/getlocations.php";
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
                countyadapter = new ArrayAdapter<String>(Edituserdetails.this, R.layout.spinnerlayout, title);
                ulocatin.setAdapter(countyadapter);
                int selectionPosition = countyadapter.getPosition(loco);
                ulocatin.setSelection(selectionPosition);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    progressDialog.dismiss();
                    Toast.makeText(Edituserdetails.this, "error time out ", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(Edituserdetails.this);
                    alert.setMessage("please check your internet connectivity");
                    alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alert.setCancelable(false);
                    alert.show();
                } else if (error instanceof NoConnectionError) {
                    progressDialog.dismiss();
                    Toast.makeText(Edituserdetails.this, "error no connection", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(Edituserdetails.this);
                    alert.setMessage("please check your internet connectivity");
                    alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alert.setCancelable(false);
                    alert.show();
                } else if (error instanceof NetworkError) {
                    progressDialog.dismiss();
                    Toast.makeText(Edituserdetails.this, "error network error", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(Edituserdetails.this);
                    alert.setMessage("please check your internet connectivity");
                    alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alert.setCancelable(false);
                    alert.show();
                } else if (error instanceof AuthFailureError) {
                    progressDialog.dismiss();
                    Toast.makeText(Edituserdetails.this, "errorin Authentication", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(Edituserdetails.this);
                    alert.setMessage("please check your internet connectivity");
                    alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alert.setCancelable(false);
                    alert.show();
                } else if (error instanceof ParseError) {
                    progressDialog.dismiss();
                    Toast.makeText(Edituserdetails.this, "error while parsing", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(Edituserdetails.this);
                    alert.setMessage("please check your internet connectivity");
                    alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alert.setCancelable(false);
                    alert.show();
                } else if (error instanceof ServerError) {
                    progressDialog.dismiss();
                    Toast.makeText(Edituserdetails.this, "error  in server", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(Edituserdetails.this);
                    alert.setMessage("please check your internet connectivity");
                    alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alert.show();
                } else if (error instanceof ClientError) {
                    progressDialog.dismiss();
                    Toast.makeText(Edituserdetails.this, "error with Client", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(Edituserdetails.this);
                    alert.setMessage("please check your internet connectivity");
                    alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alert.setCancelable(false);
                    alert.show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Edituserdetails.this, "error while loading", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(Edituserdetails.this);
                    alert.setMessage("please check your internet connectivity");
                    alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alert.setCancelable(false);
                    alert.show();
                }
            }
        });
        mycommand.add(stringRequest);
        progressDialog.show();
        mycommand.execute();
        mycommand.remove(stringRequest);

    }

    public void showpopup() {
        ImageView campop;
        final ImageView gallerypop;
        TextView closebtn;
        mydialog.setContentView(R.layout.camerapopup);
        campop = (ImageView) mydialog.findViewById(R.id.camerabtnp);
        campop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(Edituserdetails.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Edituserdetails.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    cameraPhoto = new CameraPhoto(Edituserdetails.this);

                    try {
                        startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
                        cameraPhoto.addToGallery();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(Edituserdetails.this, "permission is required", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST);
                    }

                }

            }
        });
        gallerypop = (ImageView) mydialog.findViewById(R.id.gallerybtnp);
        gallerypop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryphoto = new GalleryPhoto(Edituserdetails.this);
                startActivityForResult(galleryphoto.openGalleryIntent(), GALLERY_REQUEST);
            }
        });
        closebtn = (TextView) mydialog.findViewById(R.id.closebtn);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                String picpath = cameraPhoto.getPhotoPath();
                Bitmap bitmap = null;
                try {
                    bitmap = ImageLoader.init().from(picpath).requestSize(512, 512).getBitmap();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                File f=new File(picpath);
                Uri uri=Uri.fromFile(f);
                if(Build.VERSION.SDK_INT<=27) {
                    dpiv.setImageBitmap(bitmap);
                }else {
                    dpiv.setImageURI(uri);
                }

                selectedphoto = ImageBase64.encode(bitmap);
                mydialog.dismiss();
            } else {
                if (requestCode == GALLERY_REQUEST) {
                    Uri uri = data.getData();
                    galleryphoto.setPhotoUri(uri);
                    String picpath = galleryphoto.getPath();
                    Bitmap bitmap = null;
                    try {
                        bitmap = ImageLoader.init().from(picpath).requestSize(512, 512).getBitmap();
                        if(Build.VERSION.SDK_INT<=27) {
                        dpiv.setImageBitmap(bitmap);
                        }else{
                            dpiv.setImageURI(uri);
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    selectedphoto = ImageBase64.encode(bitmap);
                    mydialog.dismiss();
                    Log.d(Tag, selectedphoto);
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateuserdetails(String photo, String name, String email, String location, String bio,String ph,String co) {

        if (name.isEmpty()) {
            uname.setError("name is invalid");
            uname.requestFocus();
            return;
        } else if (email.isEmpty()) {
            uemail.setError("email is  is invalid");
            uemail.requestFocus();
            return;
        }  else if (bio.isEmpty()) {
            ubio.setError("bio  is invalid");
            ubio.requestFocus();
            return;
        }  else if (co.isEmpty()) {
            ucon.setError("contact is invalid");
            ucon.requestFocus();
            return;
        }else {
            if (!isValidEmail(email)) {
                uemail.setError("email is  is invalid");
                uemail.requestFocus();
                return;
            } else {

                String url = "http://www.kilicom.mabnets.com/android/editfarmer.php";
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.d(Tag, response);
                        if (!response.isEmpty()) {
                            if (response.contains("success")) {
                                Toast.makeText(Edituserdetails.this, response, Toast.LENGTH_SHORT).show();
                                androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(Edituserdetails.this);
                                alert.setMessage(response);
                                alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        /*startActivity(new Intent(Edituserdetails.this, MainActivity.class));
                                        Edituserdetails.this.finish();*/

                                        updateprofilebay();
                                    }
                                });
                                alert.setCancelable(false);
                                alert.show();
                            } else {
                                Toast.makeText(Edituserdetails.this, response, Toast.LENGTH_SHORT).show();
                                Log.d(Tag, response);

                            }

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {
                            Log.d(Tag, error.toString());
                            if (error instanceof TimeoutError) {
                                progressDialog.dismiss();
                                Toast.makeText(Edituserdetails.this, "error time out ", Toast.LENGTH_SHORT).show();

                            } else if (error instanceof NoConnectionError) {
                                progressDialog.dismiss();
                                Toast.makeText(Edituserdetails.this, "error no connection", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof NetworkError) {
                                progressDialog.dismiss();
                                Toast.makeText(Edituserdetails.this, "error network error", Toast.LENGTH_SHORT).show();

                            } else if (error instanceof AuthFailureError) {
                                progressDialog.dismiss();
                                Toast.makeText(Edituserdetails.this, "errorin Authentication", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ParseError) {
                                progressDialog.dismiss();
                                Toast.makeText(Edituserdetails.this, "error while parsing", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ServerError) {
                                progressDialog.dismiss();
                                Toast.makeText(Edituserdetails.this, "error  in server", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ClientError) {
                                progressDialog.dismiss();
                                Toast.makeText(Edituserdetails.this, "error with Client", Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(Edituserdetails.this, "error while loading", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("pic", photo);
                        params.put("name", name);
                        params.put("email", email);
                        params.put("location", location);
                        params.put("bio", bio);
                        params.put("phone", ph);
                        params.put("contact", co);
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
    public final static boolean isValidEmail(String target) {

        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public void  updateprofilebay(){

        String url = "http://www.kilicom.mabnets.com/android/farmerdetails.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d(Tag, response);
                String s=response;
                if(!s.isEmpty()) {
                    /* Toast.makeText(index.this, s, Toast.LENGTH_LONG).show();*/
                    Log.d(Tag, s);
                    try{
                        stringcacher.writeCache(s);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                if (error instanceof TimeoutError) {
                    progressDialog.dismiss();
                    Toast.makeText(Edituserdetails.this, "error time out ", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    progressDialog.dismiss();
                    Toast.makeText(Edituserdetails.this, "error no connection", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    progressDialog.dismiss();
                    Toast.makeText(Edituserdetails.this, "error network error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    progressDialog.dismiss();
                    Toast.makeText(Edituserdetails.this, "errorin Authentication", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    progressDialog.dismiss();
                    Toast.makeText(Edituserdetails.this, "error while parsing", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    progressDialog.dismiss();
                    Toast.makeText(Edituserdetails.this, "error  in server", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ClientError) {
                    progressDialog.dismiss();
                    Toast.makeText(Edituserdetails.this, "error with Client", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Edituserdetails.this, "error while loading", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone",phn);

                return params;
            }
        };
        mycommand.add(stringRequest);
        /*progressDialog.show();*/
        mycommand.execute();
        mycommand.remove(stringRequest);

    }
}
