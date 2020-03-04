package com.mabnets.kilicom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Snapshot;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.kosalgeek.android.caching.FileCacher;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class addproduct extends AppCompatActivity {
    private EditText name;
    private Spinner category;
    private Spinner subcategory;
    private ImageView productimage;
    private ImageButton prodimagebutton;
    private EditText Price;
    private EditText description;
    private Button addbutton;
    private Spinner Location;
    private String categorydata="";
    private String subcategorydata="";
    private String locationdata="";
    private String cnty="";
    private ArrayAdapter<String> cateadapter;
    private ArrayAdapter<String> subcateadapter;
    private ArrayAdapter<String> locoeadapter;
    private ProgressDialog progressDialog;
    private Mycommand mycommand;
    private ArrayList<String> title;
    private ArrayList<String> titletwo;
    private ArrayList<String> titlethree;
    private GalleryPhoto galleryphoto;
    final String Tag = this.getClass().getName();
    private FileCacher<String> stringcacher;
    private SharedPreferences preferences;
    private String phn;
    private String selectedphoto = "";
    final int GALLERY_REQUEST = 12768;
    ImageLoader imageLoader=ImageLoader.getInstance();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);

        name=(EditText)findViewById(R.id.pnamep);
        category=(Spinner)findViewById(R.id.pcategoryp);
        subcategory=(Spinner)findViewById(R.id.psubcategoryp);
        productimage=(ImageView)findViewById(R.id.pivp);
        prodimagebutton=(ImageButton)findViewById(R.id.pimgbtnp);
        Price=(EditText)findViewById(R.id.ppricep);
        Location=(Spinner) findViewById(R.id.plocatiop);
        description=(EditText)findViewById(R.id.pdescp);
        addbutton=(Button)findViewById(R.id.pbuttonp);
        stringcacher=new FileCacher<>(getApplicationContext(),"nav.txt");
        preferences = addproduct.this.getSharedPreferences("logininfo.conf", MODE_PRIVATE);
        phn = preferences.getString("phone", "");
        try {
            String t = stringcacher.readCache();
            ArrayList<farmerdetails> navlist = new JsonConverter<farmerdetails>().toArrayList(t, farmerdetails.class);
            ArrayList<String> title = new ArrayList<String>();
            for (farmerdetails value : navlist) {
                title.add(value.location);
            }
            Log.d(Tag, "DAta:"+title.get(0));
            cnty=String.valueOf(title.get(0));

        }catch (IOException e) {
            e.printStackTrace();
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("processing..");
        mycommand = new Mycommand(this);

        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        getcategory();
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object categoryitem = parent.getItemAtPosition(position);
                categorydata = (String) categoryitem;
                getsubcategory(categorydata);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        subcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object categoryitem = parent.getItemAtPosition(position);
                subcategorydata = (String) categoryitem;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object categoryitem = parent.getItemAtPosition(position);
                locationdata = (String) categoryitem;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getmarket(cnty);
        prodimagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryphoto = new GalleryPhoto(addproduct.this);
                startActivityForResult(galleryphoto.openGalleryIntent(), GALLERY_REQUEST);
            }
        });
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String nm=name.getText().toString().trim();
            String priz=Price.getText().toString().trim();
            String desc=description.getText().toString().trim();
            savedata(nm,categorydata,subcategorydata,selectedphoto,priz,desc,locationdata);
            }
        });
    }
    private void getcategory() {
        String url = "http://www.kilicom.mabnets.com/android/categories.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Tag, response);
                progressDialog.dismiss();
                if(response.contains("None")){
                    Toast.makeText(addproduct.this, response, Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<Category> categorylist = new JsonConverter<Category>().toArrayList(response, Category.class);
                    title = new ArrayList<String>();
                    for (Category value : categorylist) {
                        title.add(value.category);
                    }
                    cateadapter = new ArrayAdapter<String>(addproduct.this, R.layout.spinnerlayout, title);
                    category.setAdapter(cateadapter);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    progressDialog.dismiss();
                    Toast.makeText(addproduct.this, "error time out ", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
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
                    Toast.makeText(addproduct.this, "error no connection", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
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
                    Toast.makeText(addproduct.this, "error network error", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
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
                    Toast.makeText(addproduct.this, "errorin Authentication", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
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
                    Toast.makeText(addproduct.this, "error while parsing", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
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
                    Toast.makeText(addproduct.this, "error  in server", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
                    alert.setMessage("please check your internet connectivity");
                    alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alert.show();
                } else if (error instanceof ClientError) {
                    progressDialog.dismiss();
                    Toast.makeText(addproduct.this, "error with Client", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
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
                    Toast.makeText(addproduct.this, "error while loading", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
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
    private void getsubcategory(String s) {
        String url = "http://www.kilicom.mabnets.com/android/subcategories.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Tag, response);
                progressDialog.dismiss();
                if(response.contains("None")){
                    Toast.makeText(addproduct.this, response, Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<Category> scategorylist = new JsonConverter<Category>().toArrayList(response, Category.class);
                    titletwo = new ArrayList<String>();
                    for (Category value : scategorylist) {
                        titletwo.add(value.subcategory);
                    }
                    subcateadapter = new ArrayAdapter<String>(addproduct.this, R.layout.spinnerlayout, titletwo);
                    subcategory.setAdapter(subcateadapter);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    progressDialog.dismiss();
                    Toast.makeText(addproduct.this, "error time out ", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
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
                    Toast.makeText(addproduct.this, "error no connection", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
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
                    Toast.makeText(addproduct.this, "error network error", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
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
                    Toast.makeText(addproduct.this, "errorin Authentication", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
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
                    Toast.makeText(addproduct.this, "error while parsing", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
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
                    Toast.makeText(addproduct.this, "error  in server", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
                    alert.setMessage("please check your internet connectivity");
                    alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alert.show();
                } else if (error instanceof ClientError) {
                    progressDialog.dismiss();
                    Toast.makeText(addproduct.this, "error with Client", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
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
                    Toast.makeText(addproduct.this, "error while loading", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("category", s);
                return params;
            }
        };
        mycommand.add(stringRequest);
        progressDialog.show();
        mycommand.execute();
        mycommand.remove(stringRequest);

    }
    private void getmarket(String x) {
        String url = "http://www.kilicom.mabnets.com/android/getmarkets.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Tag, response);
                progressDialog.dismiss();
                if(response.contains("None")){
                    Toast.makeText(addproduct.this, response, Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<county> scategorylist = new JsonConverter<county>().toArrayList(response, county.class);
                    titlethree = new ArrayList<String>();
                    for (county value : scategorylist) {
                        titlethree.add(value.market);
                    }
                    locoeadapter = new ArrayAdapter<String>(addproduct.this, R.layout.spinnerlayout, titlethree);
                    Location.setAdapter(locoeadapter);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    progressDialog.dismiss();
                    Toast.makeText(addproduct.this, "error time out ", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
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
                    Toast.makeText(addproduct.this, "error no connection", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
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
                    Toast.makeText(addproduct.this, "error network error", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
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
                    Toast.makeText(addproduct.this, "errorin Authentication", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
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
                    Toast.makeText(addproduct.this, "error while parsing", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
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
                    Toast.makeText(addproduct.this, "error  in server", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
                    alert.setMessage("please check your internet connectivity");
                    alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alert.show();
                } else if (error instanceof ClientError) {
                    progressDialog.dismiss();
                    Toast.makeText(addproduct.this, "error with Client", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
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
                    Toast.makeText(addproduct.this, "error while loading", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("county", x);
                return params;
            }
        };
        mycommand.add(stringRequest);
        progressDialog.show();
        mycommand.execute();
        mycommand.remove(stringRequest);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST) {
            Uri uri = data.getData();
            galleryphoto.setPhotoUri(uri);
            String picpath = galleryphoto.getPath();
            Bitmap bitmap=null;
            imageLoader.loadImage(String.valueOf(uri), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    // Do whatever you want with Bitmap
                     if(Build.VERSION.SDK_INT<=27) {
                            productimage.setImageBitmap(loadedImage);

                        }else{
                            productimage.setImageURI(Uri.parse(imageUri));
                        }

                }
            });
            try {
                bitmap = com.kosalgeek.android.photoutil.ImageLoader.init().from(picpath).requestSize(512, 512).getBitmap();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            selectedphoto = ImageBase64.encode(bitmap);
            /*if(Build.VERSION.SDK_INT<=27) {
                productimage.setImageBitmap(bitmap);

            }else{
                productimage.setImageURI(uri);
            }*/
            Log.d(Tag, selectedphoto);
        }
    }


    private  void savedata(String pn,String ct,String subct,String slpic,String pr,String d,String mkt){
        if (pn.isEmpty()) {
            name.setError("product name is invalid");
            name.requestFocus();
            return;
        }   else if (pr.isEmpty()) {
            Price.setError("product price  is invalid");
            Price.requestFocus();
            return;
        }  else if (d.isEmpty()) {
            description.setError("product description  is invalid");
            description.requestFocus();
            return;
        } else if(slpic.isEmpty()) {
            Toast.makeText(addproduct.this, "please add a product picture to continue", Toast.LENGTH_SHORT).show();

        }else{
            String url = "http://www.kilicom.mabnets.com/android/productsaver.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(Tag, response);
                    progressDialog.dismiss();
                    if(response.contains("success")){
                        androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
                        alert.setMessage(response);
                        alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alert.show();
                    } else {
                        Toast.makeText(addproduct.this, response, Toast.LENGTH_SHORT).show();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof TimeoutError) {
                        progressDialog.dismiss();
                        Toast.makeText(addproduct.this, "error time out ", Toast.LENGTH_SHORT).show();
                        androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
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
                        Toast.makeText(addproduct.this, "error no connection", Toast.LENGTH_SHORT).show();
                        androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
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
                        Toast.makeText(addproduct.this, "error network error", Toast.LENGTH_SHORT).show();
                        androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
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
                        Toast.makeText(addproduct.this, "errorin Authentication", Toast.LENGTH_SHORT).show();
                        androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
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
                        Toast.makeText(addproduct.this, "error while parsing", Toast.LENGTH_SHORT).show();
                        androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
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
                        Toast.makeText(addproduct.this, "error  in server", Toast.LENGTH_SHORT).show();
                        androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
                        alert.setMessage("please check your internet connectivity");
                        alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alert.show();
                    } else if (error instanceof ClientError) {
                        progressDialog.dismiss();
                        Toast.makeText(addproduct.this, "error with Client", Toast.LENGTH_SHORT).show();
                        androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
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
                        Toast.makeText(addproduct.this, "error while loading", Toast.LENGTH_SHORT).show();
                        androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(addproduct.this);
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
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("product", pn);
                    params.put("desc", d);
                    params.put("category", ct);
                    params.put("price", pr);
                    params.put("phone",phn);
                    params.put("photo", selectedphoto);
                    params.put("market", mkt);
                    params.put("subcategory", subct);

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
