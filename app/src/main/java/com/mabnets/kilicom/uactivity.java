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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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
public class uactivity extends Fragment {
    private RecyclerView rvactivityx;
    private Mycommand mycommand;
    private ProgressDialog pd;
    final String Tag=this.getClass().getName();
    private Spinner spdate;
    private ArrayList<String> Dates;
    private ArrayAdapter<String> dateadapter;
    private SharedPreferences preferences;
    private String phn;
    public uactivity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        pd=new ProgressDialog(getContext());
        pd.setMessage("Loading");
        mycommand=new Mycommand(getContext());
        // Inflate the layout for this fragment
        View useractivities=inflater.inflate(R.layout.fragment_uactivity, container, false);


        preferences=getActivity().getSharedPreferences("logininfo.conf",MODE_PRIVATE);
        phn=preferences.getString("phone","");


        rvactivityx=(RecyclerView)useractivities.findViewById(R.id.rvactivity);
        spdate=(Spinner)useractivities.findViewById(R.id.filterdate);
        rvactivityx.setLayoutManager(new LinearLayoutManager(getContext()));

        Dates=new ArrayList<>();
        Dates.add("Last 24hrs");
        Dates.add("7 days");
        Dates.add("All");


        dateadapter=new ArrayAdapter<String>(getContext(),R.layout.spinnerlayout,Dates);
        spdate.setAdapter(dateadapter);

        spdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object duritem = parent.getItemAtPosition(position);
                String datex=(String) duritem;

                if(datex.equals("Last 24hrs")){
                    loadactivitydata("24");
                }else if (datex.equals("7 days")){
                    loadactivitydata("7");
                }else{
                    loadactivitydata("0");

                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
                loadactivitydata("0");

                return;
            }
        });


        return useractivities;
    }
    private void loadactivitydata(String d){

        String url="http://www.kilicom.mabnets.com/android/getactivity.php";
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d(Tag,response);
                if(!response.isEmpty()){
                        if(response.equals("none")){
                            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                            alert.setMessage("No activties found for the  period selected");
                            alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            alert.show();
                        }else{
                            Log.d(Tag, response);
                        ArrayList<activityz> aclist= new JsonConverter<activityz>().toArrayList(response, activityz.class);
                        activityadpter adapter=new activityadpter(aclist,getContext(),"0");
                        rvactivityx.setAdapter(adapter);
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
                        AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
                        alert.setMessage("please check your internet connectivity");
                        alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(0);
                            }
                        });
                        alert.show();
                    } else if (error instanceof NoConnectionError) {
                        pd.dismiss();
                        AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
                        alert.setMessage("please check your internet connectivity");
                        alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(0);
                            }
                        });
                        alert.show();
                    } else if (error instanceof NetworkError) {
                        pd.dismiss();
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setMessage("please check your internet connectivity");
                        alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(0);
                            }
                        });
                        alert.show();
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
                        Toast.makeText(getContext(), "error while loading", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("dur",d);
                params.put("phone",phn);
                return params;
            }
        };
        mycommand.add(request);
        pd.show();
        mycommand.execute();
        mycommand.remove(request);
    }

}
