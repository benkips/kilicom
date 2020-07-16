package com.mabnets.kilicom;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class History extends Fragment {
    private RecyclerView rvhis;
    private Spinner personhis;
    private Mycommand mycommand;
    private ProgressDialog pd;
    final String Tag=this.getClass().getName();
    private ArrayList<String> Dates;
    private ArrayAdapter<String> dateadapter;
    private SharedPreferences preferences;
    private String phn;
    private RecyclerView rva;

    public History() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vw= inflater.inflate(R.layout.fragment_history, container, false);
        rvhis=(RecyclerView)vw.findViewById(R.id.rvhistx);
        personhis=(Spinner)vw.findViewById(R.id.hisperiod);
        rva=(RecyclerView)vw.findViewById(R.id.rvhistupld);

        preferences = getActivity().getSharedPreferences("logininfo.conf", MODE_PRIVATE);
        phn = preferences.getString("phone", "");

        LinearLayoutManager manager = new GridLayoutManager(getContext(), 2);
        rva.setLayoutManager(manager);
        rva.setHasFixedSize(true);
        rvhis.setLayoutManager(new LinearLayoutManager(getContext()));
        rvhis.setHasFixedSize(true);

        pd=new ProgressDialog(getContext());
        pd.setMessage("Loading");
        mycommand=new Mycommand(getContext());

        Dates=new ArrayList<>();
        Dates.add("This month");
        Dates.add("Last 3 months");
        Dates.add("Last 6 month");
        Dates.add("Last 1 year");
        Dates.add("Whole period");

        dateadapter=new ArrayAdapter<String>(getContext(),R.layout.spinnerlayout,Dates);
        personhis.setAdapter(dateadapter);
        personhis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object duritem = parent.getItemAtPosition(position);
                String datex=(String) duritem;

                if(datex.equals("This month")){
                    loadactivitydata("1");
                    loadactivityhistory("1");
                }else if (datex.equals("Last 3 months")){
                    loadactivitydata("3");
                    loadactivityhistory("3");
                }else if (datex.equals("Last 6 month")){
                    loadactivitydata("6");
                    loadactivityhistory("6");
                }else if (datex.equals("Last 1 year")){
                    loadactivitydata("12");
                    loadactivityhistory("12");
                }else{
                    loadactivitydata("0");
                    loadactivityhistory("0");

                }
            }
            public void onNothingSelected(AdapterView<?> parent) {


                return;
            }
        });

        return vw;
    }

    private void loadactivitydata(String d){

        String url="http://www.kilicom.co.ke/android/history.php";
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d(Tag,response);
                if(!response.isEmpty()){
                    if(response.equals("none")){
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setMessage("No product upload history found for the period ");
                        alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alert.show();
                    }else{
                        Log.d(Tag, response);
                        ArrayList<viewdata> vlist= new JsonConverter<viewdata>().toArrayList(response, viewdata.class);
                        categoryviewadapter categoryviewadapter=new categoryviewadapter(getContext(),vlist,"1");
                        rva.setAdapter(categoryviewadapter);
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
    private void loadactivityhistory(String d){

        String url="http://www.kilicom.co.ke/android/history.php";
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d(Tag,response);
                if(!response.isEmpty()){
                    if(response.equals("none")){
                        Toast.makeText(getContext(), "No activities for the period", Toast.LENGTH_SHORT).show();
                    }else{
                        Log.d(Tag, response);
                        ArrayList<activityz> aclist= new JsonConverter<activityz>().toArrayList(response, activityz.class);
                        activityadpter adapter=new activityadpter(aclist,getContext(),"1");
                        rvhis.setAdapter(adapter);
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
                params.put("dura",d);
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
