package com.mabnets.kilicom;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.kosalgeek.android.caching.FileCacher;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.EachExceptionsHandler;
import com.kosalgeek.asynctask.PostResponseAsyncTask;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private  TextView carttext;
    private AppBarConfiguration mAppBarConfiguration;
    BottomNavigationView bottomNavigationView;
    FragmentManager fm = getSupportFragmentManager();
    private final int CAMERA_REQUEST = 13323;
    private TextView pnamee;
    private TextView ppemail;
    private ImageView ppppic;
    private Handler naviupdater;
    private SharedPreferences preferences;
    private FileCacher<String> stringcacher;
    String phn;
    private ImageLoaderConfiguration config;
    private File cacheDir;
    private DisplayImageOptions options;
    private ProgressDialog progressDialog;
    private Mycommand mycommand;
    public int cart_count=0;
    final String Tag=this.getClass().getName();
    private SharedPreferences.Editor editor;
    private Thread upp;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation);

        setSupportActionBar(toolbar);
        permission();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);

        Fragment fragmentmain=new home();
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragmentmain).commit();


        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("loading..");
        mycommand=new Mycommand(this);

        naviupdater=new Handler();
        handler=new Handler();
        View NavHeader=navigationView.getHeaderView(0);
        pnamee=(TextView)NavHeader.findViewById(R.id.pname);
        ppemail=(TextView)NavHeader.findViewById(R.id.pemail);
        ppppic=(ImageView)NavHeader.findViewById(R.id.pppic);
        stringcacher=new FileCacher<>(getApplicationContext(),"nav.txt");
        preferences=getSharedPreferences("logininfo.conf",MODE_PRIVATE);
        phn=preferences.getString("phone","");

        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(500)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .build();
        cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
        config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(options)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .defaultDisplayImageOptions(options) // default
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);

        updateprofilebay();
        refresh();
        deactivation();

       bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch (item.getItemId()) {
                   case R.id.action_home:
                       Fragment fragmenthome=new home();
                      fm.beginTransaction().replace(R.id.framelayout,fragmenthome).addToBackStack(null).commit();
                       break;
                   case R.id.action_activity:
                       Fragment fragmenta=new uactivity();
                       fm.beginTransaction().replace(R.id.framelayout,fragmenta).addToBackStack(null).commit();
                       break;
                   case R.id.action_person:
                       Fragment fragmentproff=new profile();
                       fm.beginTransaction().replace(R.id.framelayout,fragmentproff).addToBackStack(null).commit();
                       break;
               }
               return true;
           }
       });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        final MenuItem menuItem=menu.findItem(R.id.cart_action);

        View actionView= MenuItemCompat.getActionView(menuItem);
        carttext=(TextView)actionView.findViewById(R.id.noti_badge);

        setupbadge(cart_count);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);

            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            editor = preferences.edit();
            editor.clear();
            editor.commit();
            startActivity(new Intent(MainActivity.this, Login.class));
            MainActivity.this.finish();
            return true;
        }else if(id==R.id.cart_action){

                Fragment fragmentmain = new notification();
                getSupportFragmentManager().beginTransaction().remove(fragmentmain);
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragmentmain).addToBackStack(null).commit();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.nav_prof){
            Fragment fragment=new profile();
            fm.beginTransaction().replace(R.id.framelayout,fragment).addToBackStack(null).commit();
        }else if(id==R.id.nav_sub){
            Fragment fragment=new subscriptionview();
            fm.beginTransaction().replace(R.id.framelayout,fragment).addToBackStack(null).commit();
        }else if(id==R.id.nav_activity){
            Fragment fragment=new uactivity();
            fm.beginTransaction().replace(R.id.framelayout,fragment).addToBackStack(null).commit();
        }else if(id==R.id.nav_market){
            startActivity(new Intent(MainActivity.this,Market.class));
        }else if(id==R.id.nav_help){
            Fragment fragment=new helpfarmer();
            fm.beginTransaction().replace(R.id.framelayout,fragment).addToBackStack(null).commit();
        } if(id==R.id.nav_update){

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        if (fm.getBackStackEntryCount() !=1){
            fm.beginTransaction().addToBackStack(null);
        }else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

/*    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }*/
private void permission() {
    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {


    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO}, CAMERA_REQUEST);
        }
    }
}
public void  updateprofilebay(){

    String url = "http://www.kilicom.co.ke/android/farmerdetails.php";
    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            progressDialog.dismiss();
            Log.d(Tag, response);
            String s=response;
            if(!s.isEmpty()) {
                /* Toast.makeText(index.this, s, Toast.LENGTH_LONG).show();*/
                Log.d(Tag, s);
                ArrayList<farmerdetails> navdetails = new JsonConverter<farmerdetails>().toArrayList(s, farmerdetails.class);
                ArrayList<String> title = new ArrayList<String>();
                for (farmerdetails value : navdetails) {
                    title.add(value.name);
                    title.add(value.email);
                    title.add(value.photo);
                    title.add(value.location);
                    title.add(value.bio);
                    title.add(value.phone);
                }
                Log.d(Tag,title.get(2) );
                pnamee.setText(title.get(0));
                ppemail.setText(title.get(1));
                ImageLoader.getInstance().displayImage("http://www.kilicom.mabnets.com/photos/" + title.get(2), ppppic,options);

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
                Toast.makeText(MainActivity.this, "error time out ", Toast.LENGTH_SHORT).show();
                offlineprofilepic();
            } else if (error instanceof NoConnectionError) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "error no connection", Toast.LENGTH_SHORT).show();
                offlineprofilepic();
            } else if (error instanceof NetworkError) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "error network error", Toast.LENGTH_SHORT).show();
            } else if (error instanceof AuthFailureError) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "errorin Authentication", Toast.LENGTH_SHORT).show();
            } else if (error instanceof ParseError) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "error while parsing", Toast.LENGTH_SHORT).show();
            } else if (error instanceof ServerError) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "error  in server", Toast.LENGTH_SHORT).show();
            } else if (error instanceof ClientError) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "error with Client", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "error while loading", Toast.LENGTH_SHORT).show();
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

 private void offlineprofilepic(){
     if(stringcacher.hasCache()){
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
             }
             pnamee.setText(title.get(0));
             ppemail.setText(title.get(1));
             String encodedstring=title.get(2);
             Log.d(Tag, encodedstring);
             ImageLoader.getInstance().displayImage("http://www.kilicom.co.ke/photos/" + title.get(2), ppppic,options);

         }catch (IOException e){
             e.printStackTrace();
         }
         }
     }
    private   void setupbadge(int cart){
        /*carttext.setText(String.valueOf(cart));*/
        if(cart==0){
            if(carttext.getVisibility()!=View.GONE){
                carttext.setVisibility(View.GONE);
            }
        }else{
            carttext.setText(String.valueOf(Math.min(cart,99)));
            carttext.setText(String.valueOf(cart));
            if(carttext.getVisibility()!=View.VISIBLE){
                carttext.setVisibility(View.VISIBLE);
            }
        }
    }
    private void notificationcounter(){
        String url = "http://www.kilicom.co.ke/android/notifications.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Tag, response);
                String s=response;
                if(!s.isEmpty()) {
                    ArrayList<total> tdetails = new JsonConverter<total>().toArrayList(s, total.class);
                    ArrayList title = new ArrayList<String>();
                    for (total value : tdetails) {
                        title.add(value.total);
                    }
                        cart_count=Integer.valueOf(String.valueOf(title.get(0)));
                        setupbadge(Integer.valueOf(String.valueOf(title.get(0))));


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


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
    private  void refresh() {

        upp = new Thread() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        notificationcounter();
                        handler.postDelayed(this, 10000);
                    }
                });
            }

        };
        upp.setDaemon(true);
        upp.start();
    }
    private void deactivation(){
        String url = "http://www.kilicom.co.ke/android/login.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d(Tag, response);
                String s=response;
                if(!s.isEmpty()) {
                    if(s.equals("deactivated")){
                        editor = preferences.edit();
                        editor.clear();
                        editor.commit();
                        startActivity(new Intent(MainActivity.this, Login.class));
                        MainActivity.this.finish();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                if (error instanceof TimeoutError) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "error time out ", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "error no connection", Toast.LENGTH_SHORT).show();
                    offlineprofilepic();
                } else if (error instanceof NetworkError) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "error network error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "errorin Authentication", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "error while parsing", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "error  in server", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ClientError) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "error with Client", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "error while loading", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phonen",phn);

                return params;
            }
        };
        mycommand.add(stringRequest);
        /*progressDialog.show();*/
        mycommand.execute();
        mycommand.remove(stringRequest);
    }
}
