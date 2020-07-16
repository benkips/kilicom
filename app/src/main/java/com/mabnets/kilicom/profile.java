package com.mabnets.kilicom;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.kosalgeek.android.caching.FileCacher;
import com.kosalgeek.android.json.JsonConverter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class profile extends Fragment {
    private ImageView fpic;
    private TextView  fname;
    private TextView   fphone;
    private TextView  flocation;
    private TextView  fbio;
    private Button editbutton;
    private ProgressDialog progressDialog;
    private Mycommand mycommand;
    final String Tag = this.getClass().getName();
    private FileCacher<String> stringcacher;
    private SharedPreferences preferences;
    private String phn;

    public profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vprofile=inflater.inflate(R.layout.fragment_profile, container, false);
        stringcacher=new FileCacher<>(getContext(),"nav.txt");
        fpic=(ImageView)vprofile.findViewById(R.id.ivimgprof);
        fname=(TextView)vprofile.findViewById(R.id.tvprofname);
        fphone=(TextView)vprofile.findViewById(R.id.tvprofphone);
        flocation=(TextView)vprofile.findViewById(R.id.tvprofloco);
        fbio=(TextView)vprofile.findViewById(R.id.tvprofbio);
        editbutton=(Button)vprofile.findViewById(R.id.btneditprof);

        preferences = getActivity().getSharedPreferences("logininfo.conf", MODE_PRIVATE);
        phn = preferences.getString("phone", "");

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading..");
        mycommand = new Mycommand(getContext());


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
            fname.setText(title.get(0));
            fphone.setText("+254"+title.get(5));
            flocation.setText(title.get(3));
            fbio.setText("\n"+title.get(4));
            String encodedstring=title.get(2);
            /*Log.d(Tag, encodedstring);*/
            ImageLoader.getInstance().displayImage("http://www.kilicom.co.ke/photos/" + title.get(2), fpic);



        } catch (IOException e) {
            e.printStackTrace();
        }
        updateprofilebay();
        editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Edituserdetails.class);
                startActivity(intent);
            }
        });
        return vprofile;
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
                    try{
                        stringcacher.writeCache(s);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
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
                        fname.setText(title.get(0));
                        fphone.setText("+254"+title.get(5));
                        flocation.setText(title.get(3));
                        fbio.setText("\n"+title.get(4));
                        String encodedstring=title.get(2);
                        /*Log.d(Tag, encodedstring);*/
                        ImageLoader.getInstance().displayImage("http://www.kilicom.co.ke/photos/" + title.get(2), fpic);



                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                if (error instanceof TimeoutError) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "error time out ", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "error no connection", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "error network error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "errorin Authentication", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "error while parsing", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "error  in server", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ClientError) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "error with Client", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "error while loading", Toast.LENGTH_SHORT).show();
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
}
