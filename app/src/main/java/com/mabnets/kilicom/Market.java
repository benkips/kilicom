package com.mabnets.kilicom;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
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

public class Market extends AppCompatActivity {
    private RecyclerView rvmrkmainx;
    private ProgressDialog progressDialog;
    private Mycommand mycommand;
    final String Tag = this.getClass().getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        
        rvmrkmainx=(RecyclerView)findViewById(R.id.rvmrktmain);
        rvmrkmainx.setLayoutManager(new LinearLayoutManager(this));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading..");
        mycommand = new Mycommand(this);
        
        getcounties();
    }
    private void getcounties() {
        String url = "http://www.kilicom.co.ke/android/getlocations.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Tag, response);
                progressDialog.dismiss();
                if(response.contains("None")){
                    Toast.makeText(Market.this, response, Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<county> codetails = new JsonConverter<county>().toArrayList(response, county.class);
                    countyadapterone adapter=new countyadapterone(codetails,Market.this);
                    rvmrkmainx.setAdapter(adapter);

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    progressDialog.dismiss();
                    Toast.makeText(Market.this, "error time out ", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(Market.this);
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
                    Toast.makeText(Market.this, "error no connection", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(Market.this);
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
                    Toast.makeText(Market.this, "error network error", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(Market.this);
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
                    Toast.makeText(Market.this, "errorin Authentication", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(Market.this);
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
                    Toast.makeText(Market.this, "error while parsing", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(Market.this);
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
                    Toast.makeText(Market.this, "error  in server", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(Market.this);
                    alert.setMessage("please check your internet connectivity");
                    alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alert.show();
                } else if (error instanceof ClientError) {
                    progressDialog.dismiss();
                    Toast.makeText(Market.this, "error with Client", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(Market.this);
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
                    Toast.makeText(Market.this, "error while loading", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(Market.this);
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
                /*params.put("category", );*/
                return params;
            }
        };
        mycommand.add(stringRequest);
        progressDialog.show();
        mycommand.execute();
        mycommand.remove(stringRequest);

    }
}
