package com.mabnets.kilicom;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class categoryanddatafromcounties extends AppCompatActivity {
    private RecyclerView rvtotal;
    private MultiSnapRecyclerView rcnty;
    private ProgressDialog progressDialog;
    private Mycommand mycommand;
    final String Tag = this.getClass().getName();
    private String cty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoryanddatafromcounties);
        rvtotal=(RecyclerView)findViewById(R.id.rvtotalproducts);
        rcnty=(MultiSnapRecyclerView)findViewById(R.id.rvcategoriesfromcounties);

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            cty=bundle.getString("county");
            Log.d(Tag, cty);
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading..");
        mycommand = new Mycommand(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(categoryanddatafromcounties.this, LinearLayoutManager.HORIZONTAL, false);
        rcnty.setLayoutManager(layoutManager);
        rvtotal.setLayoutManager(new LinearLayoutManager(this));

        getmarkert(cty);


    }
    private void getmarkert(String x) {
        String url = "http://www.kilicom.mabnets.com/android/getmarkets.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Tag, response);
                progressDialog.dismiss();
                if(response.contains("None")){
                    Toast.makeText(categoryanddatafromcounties.this, response, Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<county> smarkets= new JsonConverter<county>().toArrayList(response, county.class);
                    categoryfromcountiesadapter adapter=new categoryfromcountiesadapter(smarkets,categoryanddatafromcounties.this);
                    rcnty.setAdapter(adapter);

                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    progressDialog.dismiss();
                    Toast.makeText(categoryanddatafromcounties.this, "error time out ", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(categoryanddatafromcounties.this);
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
                    Toast.makeText(categoryanddatafromcounties.this, "error no connection", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(categoryanddatafromcounties.this);
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
                    Toast.makeText(categoryanddatafromcounties.this, "error network error", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(categoryanddatafromcounties.this);
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
                    Toast.makeText(categoryanddatafromcounties.this, "errorin Authentication", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(categoryanddatafromcounties.this);
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
                    Toast.makeText(categoryanddatafromcounties.this, "error while parsing", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(categoryanddatafromcounties.this);
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
                    Toast.makeText(categoryanddatafromcounties.this, "error  in server", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(categoryanddatafromcounties.this);
                    alert.setMessage("please check your internet connectivity");
                    alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alert.show();
                } else if (error instanceof ClientError) {
                    progressDialog.dismiss();
                    Toast.makeText(categoryanddatafromcounties.this, "error with Client", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(categoryanddatafromcounties.this);
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
                    Toast.makeText(categoryanddatafromcounties.this, "error while loading", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(categoryanddatafromcounties.this);
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
    public  void gettotalproducts(String x) {
        if (x.contains("No Markets")) {
            androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(categoryanddatafromcounties.this);
            alert.setMessage("No products exits for this market");
            alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert.setCancelable(false);
            alert.show();
        } else {
            rvtotal.setAdapter(null);
            String url = "http://www.kilicom.mabnets.com/android/markets.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(Tag, response);
                    progressDialog.dismiss();
                    if (response.contains("none")) {
                        Toast.makeText(categoryanddatafromcounties.this, "no products yet from "+ x, Toast.LENGTH_SHORT).show();

                    } else {
                        ArrayList<total> markets = new JsonConverter<total>().toArrayList(response, total.class);
                        Totalproductsadpter adapter = new Totalproductsadpter(markets, categoryanddatafromcounties.this);
                        rvtotal.setAdapter(adapter);

                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof TimeoutError) {
                        progressDialog.dismiss();
                        Toast.makeText(categoryanddatafromcounties.this, "error time out ", Toast.LENGTH_SHORT).show();
                        androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(categoryanddatafromcounties.this);
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
                        Toast.makeText(categoryanddatafromcounties.this, "error no connection", Toast.LENGTH_SHORT).show();
                        androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(categoryanddatafromcounties.this);
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
                        Toast.makeText(categoryanddatafromcounties.this, "error network error", Toast.LENGTH_SHORT).show();
                        androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(categoryanddatafromcounties.this);
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
                        Toast.makeText(categoryanddatafromcounties.this, "errorin Authentication", Toast.LENGTH_SHORT).show();
                        androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(categoryanddatafromcounties.this);
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
                        Toast.makeText(categoryanddatafromcounties.this, "error while parsing", Toast.LENGTH_SHORT).show();
                        androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(categoryanddatafromcounties.this);
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
                        Toast.makeText(categoryanddatafromcounties.this, "error  in server", Toast.LENGTH_SHORT).show();
                        androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(categoryanddatafromcounties.this);
                        alert.setMessage("please check your internet connectivity");
                        alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alert.show();
                    } else if (error instanceof ClientError) {
                        progressDialog.dismiss();
                        Toast.makeText(categoryanddatafromcounties.this, "error with Client", Toast.LENGTH_SHORT).show();
                        androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(categoryanddatafromcounties.this);
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
                        Toast.makeText(categoryanddatafromcounties.this, "error while loading", Toast.LENGTH_SHORT).show();
                        androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(categoryanddatafromcounties.this);
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
                    params.put("market", x);
                    params.put("county", cty);
                    return params;
                }
            };
            mycommand.add(stringRequest);
            progressDialog.show();
            mycommand.execute();
            mycommand.remove(stringRequest);
        }
    }
}
