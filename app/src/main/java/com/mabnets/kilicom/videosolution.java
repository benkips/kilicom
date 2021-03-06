package com.mabnets.kilicom;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.potyvideo.library.AndExoPlayerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class videosolution extends Fragment {
    private AndExoPlayerView andExoPlayerView;
    private TextView tvcd;
    private TextView tvcx;
    private TextView tvs;
    private Button vsbtn;
    private Mycommand mycommand;
    private ProgressDialog pd;
    final String Tag=this.getClass().getName();
    private SharedPreferences preferences;
    private String phn;
    public videosolution() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View vissol= inflater.inflate(R.layout.fragment_videosolution, container, false);
        andExoPlayerView = vissol.findViewById(R.id.andExoPlayerView);
        tvcd=(TextView) vissol.findViewById(R.id.tvvadetails);
        tvcx=(TextView)vissol.findViewById(R.id.tvvac);
        tvs=(TextView)vissol.findViewById(R.id.tvvasl);
        vsbtn=(Button)vissol.findViewById(R.id.vsremove);

        pd=new ProgressDialog(getContext());
        pd.setMessage("Deleting");
        mycommand=new Mycommand(getContext());
        preferences = getActivity().getSharedPreferences("logininfo.conf", MODE_PRIVATE);
        phn = preferences.getString("phone", "");

        Bundle bundle = getArguments();
        if (bundle != null) {
            String video = bundle.getString("photo");
            String details = bundle.getString("details");
            String crop = bundle.getString("crop");
            String solution = bundle.getString("solution");
            String status = bundle.getString("status");

            andExoPlayerView.setSource("http://www.kilicom.co.ke/photos/"+video);
            tvcd.setText(details);
            tvcx.setText(crop);
            tvs.setText(solution);

            if(status.equals("1")){
                vsbtn.setVisibility(getView().VISIBLE);
            }else{
                vsbtn.setVisibility(getView().GONE);
            }
            vsbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deletevideo(video);
                }
            });
        }

       return vissol;
    }
    private void deletevideo(String d){

        String url="http://www.kilicom.co.ke/android/deletevideo.php";
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d(Tag,response);
                if(!response.isEmpty()){
                    if(response.equals("success")){
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setMessage("Deleted succesfully");
                        alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Fragment fragmentmain=new History();
                                Fragment fragmentback=new videosolution();
                                getFragmentManager().beginTransaction().remove(fragmentback);
                                getFragmentManager().popBackStack();
                                getFragmentManager().beginTransaction().replace(R.id.framelayout,fragmentmain).addToBackStack(null).commit();
                            }
                        });
                        alert.show();
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
                params.put("photo",d);
                params.put("activity",phn);
                return params;
            }
        };
        mycommand.add(request);
        pd.show();
        mycommand.execute();
        mycommand.remove(request);
    }
}
