package com.mabnets.kilicom;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.kosalgeek.android.json.JsonConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class viewcountymarkets extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private Mycommand mycommand;
    final String Tag = this.getClass().getName();
    private RecyclerView rv;
    private String vc;
    private String mrkts;
    private String cnty;
    private SharedPreferences preferences;
    private String customersdata="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewcountymarkets);
        rv=(RecyclerView)findViewById(R.id.rvcvm);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading..");
        mycommand = new Mycommand(this);
        LinearLayoutManager manager = new GridLayoutManager(viewcountymarkets.this, 2);
        rv.setLayoutManager(manager);
        preferences=getSharedPreferences("customer.info",MODE_PRIVATE);
        customersdata=preferences.getString("customer","");

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            vc=bundle.getString("subcategory");
            mrkts=bundle.getString("markets");
            cnty=bundle.getString("county");
            Log.d(Tag, vc);
            if(customersdata.equals("data")){
                getproducts(vc,"6");
            }else if(customersdata.equals("")){
                getproducts(vc,"7");
            }


        }
        
        
        
    }
    private void getproducts(String x,String st) {
        String url = "http://www.kilicom.co.ke/android/markets.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Tag, response);
                progressDialog.dismiss();
                if(response.contains("None")){
                    AlertDialog.Builder alert = new AlertDialog.Builder(viewcountymarkets.this);
                    alert.setMessage("There are no "+x+" products from farmers");
                    alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            /*startActivity(new Intent(getContext(),MainActivity.class));*/
                        }
                    });
                    alert.show();
                } else {
                    Log.d(Tag, response);
                    ArrayList<viewdata> vlist= new JsonConverter<viewdata>().toArrayList(response, viewdata.class);
                    categoryviewadapter categoryviewadapter=new categoryviewadapter(viewcountymarkets.this,vlist,st);
                    rv.setAdapter(categoryviewadapter);

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    progressDialog.dismiss();
                    Toast.makeText(viewcountymarkets.this, "error time out ", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(viewcountymarkets.this);
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
                    Toast.makeText(viewcountymarkets.this, "error no connection", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(viewcountymarkets.this);
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
                    Toast.makeText(viewcountymarkets.this, "error network error", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(viewcountymarkets.this);
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
                    Toast.makeText(viewcountymarkets.this, "errorin Authentication", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(viewcountymarkets.this);
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
                    Toast.makeText(viewcountymarkets.this, "error while parsing", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(viewcountymarkets.this);
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
                    Toast.makeText(viewcountymarkets.this, "error  in server", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(viewcountymarkets.this);
                    alert.setMessage("please check your internet connectivity");
                    alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alert.show();
                } else if (error instanceof ClientError) {
                    progressDialog.dismiss();
                    Toast.makeText(viewcountymarkets.this, "error with Client", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(viewcountymarkets.this);
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
                    Toast.makeText(viewcountymarkets.this, "error while loading", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(viewcountymarkets.this);
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
                params.put("subcat", x);
                params.put("mks", mrkts);
                params.put("county", cnty);
                return params;
            }
        };
        mycommand.add(stringRequest);
        progressDialog.show();
        mycommand.execute();
        mycommand.remove(stringRequest);

    }
}
