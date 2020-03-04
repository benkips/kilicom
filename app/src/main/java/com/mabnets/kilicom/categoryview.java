package com.mabnets.kilicom;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kosalgeek.android.json.JsonConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class categoryview extends Fragment {
    private RecyclerView rvcvv;
    private FloatingActionButton floatingActionButton;
    private Mycommand mycommand;
    private ProgressDialog pd;
    final String Tag=this.getClass().getName();
    private String categori;
    private String switcher="";
    public categoryview() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View cateviewxx=inflater.inflate(R.layout.fragment_categoryview, container, false);
       rvcvv=(RecyclerView)cateviewxx.findViewById(R.id.rvcview);
       floatingActionButton=(FloatingActionButton)cateviewxx.findViewById(R.id.flctegories);
        Bundle bundle = getArguments();
        if (bundle != null) {
            categori = bundle.getString("category");
            switcher= bundle.getString("rmvfl");
            Log.d(Tag, "categorymanze: "+categori+switcher);
        }

        pd=new ProgressDialog(getContext());
        pd.setMessage("Loading");
        mycommand=new Mycommand(getContext());

        rvcvv.setHasFixedSize(true);
        LinearLayoutManager manager = new GridLayoutManager(getContext(), 2);
        rvcvv.setLayoutManager(manager);


        if(switcher == null){
        loadcategorydataas(categori,"0");

        }else{
        floatingActionButton.setVisibility(getView().GONE);
        loadcategorydataas(categori,"5");
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(),addproduct.class));
            }
        });
       return cateviewxx;
    }
    public void loadcategorydataas(final String ct,final String st){

        String url="http://www.kilicom.mabnets.com/android/getprodcategory.php";
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d(Tag,response);
                if(!response.isEmpty()){
                    if(response.contains("None")){
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setMessage("There are no "+ct+" products from farmers");
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
                        categoryviewadapter categoryviewadapter=new categoryviewadapter(getContext(),vlist,st);
                        rvcvv.setAdapter(categoryviewadapter);

                    }

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
                params.put("category",ct);
                return params;
            }
        };
        mycommand.add(request);
        pd.show();
        mycommand.execute();
        mycommand.remove(request);
    }
}
