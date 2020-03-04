package com.mabnets.kilicom;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
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

;import com.android.volley.AuthFailureError;
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
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static com.mabnets.kilicom.Customerinterface.cartitems;

/**
 * A simple {@link Fragment} subclass.
 */
public class productview extends Fragment {
    private ImageView  iv;
    private TextView tvprodname;
    private TextView tvlocation;
    private TextView tvprice;
    private  TextView tvdetails;
    private Button rmv;
    private Mycommand mycommand;
    private ProgressDialog pd;
    final String Tag=this.getClass().getName();
    private SharedPreferences preferences;
    private String phn;


    public productview() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View pv=inflater.inflate(R.layout.fragment_productview, container, false);
        iv=(ImageView)pv.findViewById(R.id.pvpivv);
        tvprodname=(TextView)pv.findViewById(R.id.pvprod);
        tvlocation=(TextView)pv.findViewById(R.id.pvplocation);
        tvprice=(TextView)pv.findViewById(R.id.pvpprice);
        tvdetails=(TextView)pv.findViewById(R.id.pvpdetails);
        rmv=(Button) pv.findViewById(R.id.pvpremove);


        pd=new ProgressDialog(getContext());
        pd.setMessage("Deleting");
        mycommand=new Mycommand(getContext());
        preferences = getActivity().getSharedPreferences("logininfo.conf", MODE_PRIVATE);
        phn = preferences.getString("phone", "");



        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.getString("product");
            String details = bundle.getString("details");
            String photo = bundle.getString("photo");
            String price = bundle.getString("price");
            String location = bundle.getString("location");
            String status = bundle.getString("status");
            if(status.equals("1")){
                rmv.setVisibility(getView().VISIBLE);
            }else if(status.equals("0")){
                rmv.setVisibility(getView().GONE);
            }else if(status .equals("5")){
                rmv.setVisibility(getView().VISIBLE);
                rmv.setText("Add product to cart");
                rmv.setBackgroundResource(R.drawable.button_shaper);
            }else if(status .equals("6")){
                rmv.setVisibility(getView().VISIBLE);
                rmv.setText("Add product to cart");
                rmv.setBackgroundResource(R.drawable.button_shaper);
            }

            ImageLoader.getInstance().displayImage("http://www.kilicom.mabnets.com/photos/" + photo, iv);
            tvprodname.setText(name);
            tvlocation.setText(location);
            tvprice.setText("KES "+price);
            tvdetails.setText(details);
            rmv.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    if(status.equals("1")) {
                        deletepic(photo);
                    }else if(status.equals("5") || status.equals("6")){
                        String arr[]=price.split(",");
                        String ans="";
                        for(String t:arr){
                            ans+=t;
                        }
                        int pricex=Integer.valueOf(ans);
                        cartitems.add(new cartz(name,photo,pricex));
                        Log.d(Tag, String.valueOf(cartitems));
                        int ww=0;
                        ww=Customerinterface.cart_count;
                        int crt=cartitems.size();
                        ww=crt;
                        ((Customerinterface)getActivity()).setupbadge(crt);
                        Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }




        return  pv;
    }
    private void deletepic(String d){

        String url="http://www.kilicom.mabnets.com/android/deletions.php";
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
                                Fragment fragmentback=new productview();
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
                params.put("products",phn);
                return params;
            }
        };
        mycommand.add(request);
        pd.show();
        mycommand.execute();
        mycommand.remove(request);
    }
}
