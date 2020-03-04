package com.mabnets.kilicom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.navigation.NavigationView;
import com.kosalgeek.android.caching.FileCacher;
import com.kosalgeek.android.json.JsonConverter;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Customerinterface extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static TextView carttext;
    private ImageLoaderConfiguration config;
    private File cacheDir;
    private DisplayImageOptions options;
    FragmentManager fm = getSupportFragmentManager();
    private final int CAMERA_REQUEST = 13323;
    public static int cart_count=0;
    private int noti_count=0;
    public static ArrayList cartitems=new ArrayList();
    private TextView notitext;
    private TextView pnamee;
    private TextView ppemail;
    private FileCacher<String> cartcacher;
    private ProgressDialog progressDialog;
    final String Tag=this.getClass().getName();
    private SharedPreferences preferences;
    private SharedPreferences preferencestwo;
    private Mycommand mycommand;
    private  SharedPreferences.Editor editor;
    private String mv="";
    private Thread upp;
    private Handler handler;
    private String phnx="none";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customerinterface);
        Toolbar toolbar = findViewById(R.id.toolbartwo);
        setSupportActionBar(toolbar);
        permission();
        DrawerLayout drawer = findViewById(R.id.drawer_layouttwo);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        cartcacher=new FileCacher<>(Customerinterface.this,"cart.txt");
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_viewtwo);
        navigationView.setNavigationItemSelectedListener(Customerinterface.this);
        View NavHeader=navigationView.getHeaderView(0);
        pnamee=(TextView)NavHeader.findViewById(R.id.unamep);
        ppemail=(TextView)NavHeader.findViewById(R.id.uemailp);
        preferences = getSharedPreferences("customer.info", MODE_PRIVATE);
        handler=new Handler();



        editor = preferences.edit();
        editor.clear();
        editor.commit();

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("loading..");
        mycommand=new Mycommand(this);
        if(cartcacher.hasCache()){
            try {
                String t=cartcacher.readCache();
                /*cartcacher.clearCache();*/

                ArrayList<details> locodetails = new JsonConverter<details>().toArrayList(t, details.class);
                ArrayList title = new ArrayList<String>();
                for (details value : locodetails) {
                    title.add(value.getUsername());
                    title.add(value.getPhone());
                    title.add(value.getEmail());
                }
                ppemail.setText(String.valueOf(title.get(2)));
                pnamee.setText(String.valueOf(title.get(0)));
                refresh(String.valueOf(title.get(1)));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }




        Fragment fragmentmain=new hometwo();
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayouttwo,fragmentmain).commit();

        Intent intent = getIntent();

        String mv= intent.getStringExtra("move");
        if(mv!=null){
            String name = intent.getStringExtra("product");
            String details = intent.getStringExtra("details");
            String photo = intent.getStringExtra("photo");
            String price = intent.getStringExtra("price");
            String location = intent.getStringExtra("location");
            Fragment fragmentx=new productview();
            Bundle bundle = new Bundle();
            bundle.putString("product", name);
            bundle.putString("photo", photo);
            bundle.putString("price", price);
            bundle.putString("location", location);
            bundle.putString("details", details);
            bundle.putString("status", mv);
            fragmentx.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayouttwo,fragmentx).addToBackStack(null).commit();
        }
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(0)
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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cartview, menu);
        final MenuItem menuItem=menu.findItem(R.id.cart_twoaction);
        final MenuItem menuItemtwo=menu.findItem(R.id.ntfction);

        View actionView= MenuItemCompat.getActionView(menuItem);
        View actionViewtwo= MenuItemCompat.getActionView(menuItemtwo);
        carttext=(TextView)actionView.findViewById(R.id.cart_badge);
        notitext=(TextView)actionViewtwo.findViewById(R.id.noti_badge);

        setupbadge(cart_count);
        setupbadgetwo(noti_count);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);

            }
        });
        actionViewtwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItemtwo);

            }
        });

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

      if(id==R.id.cart_twoaction){
            if (cartitems.size()!=0) {
                Fragment fragmentmain=new cashout();
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayouttwo,fragmentmain).addToBackStack(null).commit();
            }else{
                Toast.makeText(this, "You have to add product to cart", Toast.LENGTH_SHORT).show();
            }
            return true;

        }else if(id==R.id.search){
            Fragment fragmentmain=new Search();
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayouttwo,fragmentmain).addToBackStack(null).commit();
        }else if(id==R.id.ntfction){


          if(cartcacher.hasCache()){
              try {
                  String t=cartcacher.readCache();
                  /*cartcacher.clearCache();*/

                  ArrayList<details> locodetails = new JsonConverter<details>().toArrayList(t, details.class);
                  ArrayList title = new ArrayList<String>();
                  for (details value : locodetails) {
                      title.add(value.getUsername());
                      title.add(value.getPhone());
                      title.add(value.getEmail());
                  }
                  ppemail.setText(String.valueOf(title.get(2)));
                  Bundle bundle=new Bundle();
                  bundle.putString("customerphone", String.valueOf(title.get(1)));
                  Fragment fragmentmain=new notification();
                  fragmentmain.setArguments(bundle);
                  getSupportFragmentManager().beginTransaction().replace(R.id.framelayouttwo,fragmentmain).addToBackStack(null).commit();


              } catch (IOException e) {
                  e.printStackTrace();
              }

          }

      }

        return super.onOptionsItemSelected(item);
    }

    public static void setupbadge(int cart){
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
    private  void setupbadgetwo(int cart){
        /*carttext.setText(String.valueOf(cart));*/
        if(cart==0){
            if(notitext.getVisibility()!=View.GONE){
                notitext.setVisibility(View.GONE);
            }
        }else{
            notitext.setText(String.valueOf(Math.min(cart,99)));
            notitext.setText(String.valueOf(cart));
            if(notitext.getVisibility()!=View.VISIBLE){
                notitext.setVisibility(View.VISIBLE);
            }
        }
    }
    private void notificationcounter(String x){
        String url = "http://www.kilicom.mabnets.com/android/notifications.php";
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
                    noti_count=Integer.valueOf(String.valueOf(title.get(0)));
                   setupbadgetwo(Integer.valueOf(String.valueOf(title.get(0))));


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
                params.put("phone",x);

                return params;
            }
        };
        mycommand.add(stringRequest);
        /*progressDialog.show();*/
        mycommand.execute();
        mycommand.remove(stringRequest);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.tpmarkets){

            editor = preferences.edit();
            editor.putString("customer","data");
            editor.apply();
            startActivity(new Intent(Customerinterface.this,Market.class));
            Customerinterface.this.finish();
        }else if(id==R.id.customerhelp){
            if(cartcacher.hasCache()){
                try {
                    String t=cartcacher.readCache();
                    /*cartcacher.clearCache();*/

                    ArrayList<details> locodetails = new JsonConverter<details>().toArrayList(t, details.class);
                    ArrayList title = new ArrayList<String>();
                    for (details value : locodetails) {
                        title.add(value.getUsername());
                        title.add(value.getPhone());
                        title.add(value.getAdress());
                    }
                    Bundle bundle=new Bundle();
                    bundle.putString("customerphone", String.valueOf(title.get(1)));
                    Fragment fragmentmain=new helpfarmer();
                    fragmentmain.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayouttwo,fragmentmain).addToBackStack(null).commit();


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else{
                Bundle bundle=new Bundle();
                bundle.putString("customerphone", phnx);
                Fragment fragmentmain=new helpfarmer();
                fragmentmain.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayouttwo,fragmentmain).addToBackStack(null).commit();
            }

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layouttwo);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layouttwo);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        if (fm.getBackStackEntryCount() !=1){
            fm.beginTransaction().addToBackStack(null);
        }else {
            Log.i("customerActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }
    private void permission() {
        if (ContextCompat.checkSelfPermission(Customerinterface.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Customerinterface.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {


        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO}, CAMERA_REQUEST);
            }
        }
    }
    private  void refresh(String s) {

        upp = new Thread() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        notificationcounter(s);
                        handler.postDelayed(this, 10000);
                    }
                });
            }

        };
        upp.setDaemon(true);
        upp.start();
    }

}
