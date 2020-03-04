package com.mabnets.kilicom;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class notification extends Fragment {
    private Mycommand mycommand;
    private ProgressDialog pd;
    final String Tag=this.getClass().getName();
    private RecyclerView rvnotification;
    private SharedPreferences preferences;
    private  String phn="";
    private String customerphn;
    public notification() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View nf=inflater.inflate(R.layout.fragment_notification, container, false);
        rvnotification=(RecyclerView)nf.findViewById(R.id.rvnotif);
        preferences = getActivity().getSharedPreferences("logininfo.conf", MODE_PRIVATE);
        phn = preferences.getString("phone", "");
        pd=new ProgressDialog(getContext());
        pd.setMessage("Loading");
        mycommand=new Mycommand(getContext());


        Bundle bundle = getArguments();
        if (bundle != null) {
            customerphn = bundle.getString("customerphone");
            loadnotification(customerphn,customerphn);
            messagechecked(customerphn);
        }else{
            loadnotification(phn,"1");
            messagechecked(phn);
        }
        rvnotification.setLayoutManager(new LinearLayoutManager(getContext()));



        return  nf;
    }
    public void loadnotification(final String phnn,String sttus){

        String url="http://www.kilicom.mabnets.com/android/notifications.php";
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d(Tag,response);
                if(!response.isEmpty()){

                        Log.d(Tag, response);
                        ArrayList<notif> vlist= new JsonConverter<notif>().toArrayList(response, notif.class);
                        notificationadapter adapter=new notificationadapter(vlist,getContext(),sttus);
                        rvnotification.setAdapter(adapter);


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error!=null) {
                    Log.d(TAG, error.toString());
                    if (error instanceof TimeoutError) {
                        pd.dismiss();
                        Toast.makeText(getContext(), "error time out No internet", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof NoConnectionError) {
                        pd.dismiss();
                        Toast.makeText(getContext(), "error no connection", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof NetworkError) {
                        pd.dismiss();
                        Toast.makeText(getContext(), "error network error", Toast.LENGTH_SHORT).show();
                    }else if (error instanceof AuthFailureError) {
                        pd.dismiss();
                        Toast.makeText(getContext(), "errorin Authentication", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        pd.dismiss();
                        Toast.makeText(getContext(), "error while parsing", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                        pd.dismiss();
                        Toast.makeText(getContext(), "error  in server", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ClientError) {
                        pd.dismiss();
                        Toast.makeText(getContext(), "error with Client", Toast.LENGTH_SHORT).show();
                    } else {
                        pd.dismiss();
                        Toast.makeText(getContext(), "error while loading"+ error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fetch",phnn);
                return params;
            }
        };
        mycommand.add(request);
        pd.show();
        mycommand.execute();
        mycommand.remove(request);
    }
    private  void messagechecked(String p){
        String url="http://www.kilicom.mabnets.com/android/getprodcategory.php";
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d(Tag,response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error!=null) {
                    Log.d(TAG, error.toString());
                    if (error instanceof TimeoutError) {
                        pd.dismiss();
                        Toast.makeText(getContext(), "error time out No internet", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof NoConnectionError) {
                        pd.dismiss();
                        Toast.makeText(getContext(), "error no connection", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof NetworkError) {
                        pd.dismiss();
                        Toast.makeText(getContext(), "error network error", Toast.LENGTH_SHORT).show();
                    }else if (error instanceof AuthFailureError) {
                        pd.dismiss();
                        Toast.makeText(getContext(), "errorin Authentication", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        pd.dismiss();
                        Toast.makeText(getContext(), "error while parsing", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                        pd.dismiss();
                        Toast.makeText(getContext(), "error  in server", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ClientError) {
                        pd.dismiss();
                        Toast.makeText(getContext(), "error with Client", Toast.LENGTH_SHORT).show();
                    } else {
                        pd.dismiss();
                        Toast.makeText(getContext(), "error while loading"+ error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("p",phn);
                return params;
            }
        };
        mycommand.add(request);
        pd.show();
        mycommand.execute();
        mycommand.remove(request);
    }
}
