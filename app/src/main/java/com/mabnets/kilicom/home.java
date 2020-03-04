package com.mabnets.kilicom;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;
import com.takusemba.multisnaprecyclerview.OnSnapListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class home extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private CardView tkphoto;
    private CardView history;
    private CardView subscription;
    private MultiSnapRecyclerView rcategories;
    private MultiSnapRecyclerView subrcategories;
    private MultiSnapRecyclerView rfmarkets;
    private Mycommand mycommand;
    private ProgressDialog pd;
    final String Tag=this.getClass().getName();
    private String f="2";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View homefrag=inflater.inflate(R.layout.fragment_home, container, false);
        tkphoto=(CardView)homefrag.findViewById(R.id.takephoto);
        history=(CardView)homefrag.findViewById(R.id.history);
        subscription=(CardView)homefrag.findViewById(R.id.subscr);
        rcategories=(MultiSnapRecyclerView)homefrag.findViewById(R.id.rvcategories);
        subrcategories=(MultiSnapRecyclerView)homefrag.findViewById(R.id.rvsubcategoriz);
        rfmarkets=(MultiSnapRecyclerView)homefrag.findViewById(R.id.rvfeaturedmarkets);



        pd=new ProgressDialog(getContext());
        pd.setMessage("Loading");
        mycommand=new Mycommand(getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagertwo = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerthree = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rcategories.setLayoutManager(layoutManager);
        subrcategories.setLayoutManager(layoutManagertwo);
        rfmarkets.setLayoutManager(layoutManagerthree);



        loadcategory();
        loadfmarkets();


        rcategories.setOnSnapListener(new OnSnapListener() {
            @Override
            public void snapped(int position) {
                Log.d(Tag,String.valueOf(position));
            }
        });
        tkphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Camerashooting.class);
                startActivity(intent);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentmain=new History();
                getFragmentManager().beginTransaction().replace(R.id.framelayout,fragmentmain).addToBackStack(null).commit();
            }
        });
         subscription.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Fragment fragmentmain=new subscriptionview();
                 getFragmentManager().beginTransaction().replace(R.id.framelayout,fragmentmain).addToBackStack(null).commit();
             }
         });
        return  homefrag;
    }
    private void loadfmarkets(){

        String url="http://www.kilicom.mabnets.com/android/getfeatured.php";
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               /* pd.dismiss();*/
                Log.d(Tag,response);
                if(!response.isEmpty()){
                    if(response.contains("None")){
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(Tag, response);
                        ArrayList<fmarkets> flist= new JsonConverter<fmarkets>().toArrayList(response, fmarkets.class);
                        featuredmarketsadapter featuredmarketsadapte=new featuredmarketsadapter(getContext(),flist,"0");
                        rfmarkets.setAdapter(featuredmarketsadapte);

                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error!=null) {
                    Log.d(TAG, error.toString());
                    if (error instanceof TimeoutError) {
                        /*pd.dismiss();*/
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
                       /* pd.dismiss();*/
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
                       /* pd.dismiss();*/
                        Toast.makeText(getContext(), "errorin Authentication", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        /* pd.dismiss();*/
                        Toast.makeText(getContext(), "error while parsing", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                        /* pd.dismiss();*/
                        Toast.makeText(getContext(), "error  in server", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ClientError) {
                        /* pd.dismiss();*/
                        Toast.makeText(getContext(), "error with Client", Toast.LENGTH_SHORT).show();
                    } else {
                        /* pd.dismiss();*/
                        Toast.makeText(getContext(), "error while loading", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fetch",f);
                return params;
            }
        };
        mycommand.add(request);
        /*pd.show();*/
        mycommand.execute();
        mycommand.remove(request);
    }
    private void loadcategory(){

        String url="http://www.kilicom.mabnets.com/android/categories.php";
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d(Tag,response);
                if(!response.isEmpty()){
                    if(response.contains("None")){
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(Tag, response);
                        ArrayList<Category> categorylist= new JsonConverter<Category>().toArrayList(response, Category.class);
                        Categoryadapter categoryadapter=new Categoryadapter(getContext(),categorylist,home.this,"0");
                        rcategories.setAdapter(categoryadapter);

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
        });
        mycommand.add(request);
        pd.show();
        mycommand.execute();
        mycommand.remove(request);
    }
    public void loadsubcategory(final String ct){

        String url="http://www.kilicom.mabnets.com/android/subcategories.php";
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                /*pd.dismiss();*/
                Log.d(Tag,response);
                if(!response.isEmpty()){
                    if(response.contains("None")){
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(Tag, response);
                        ArrayList<Category> scategorylist= new JsonConverter<Category>().toArrayList(response, Category.class);
                        Subcategoryadapter scategoryadapter=new Subcategoryadapter(getContext(),scategorylist,"0");
                        subrcategories.setAdapter(scategoryadapter);

                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error!=null) {
                    Log.d(TAG, error.toString());
                    if (error instanceof TimeoutError) {
                        /*pd.dismiss();*/
                        Toast.makeText(getContext(), "error time out No internet", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof NoConnectionError) {
                       /* pd.dismiss();*/
                        Toast.makeText(getContext(), "error no connection", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof NetworkError) {
                        /*pd.dismiss();*/
                        Toast.makeText(getContext(), "error network error", Toast.LENGTH_SHORT).show();
                    }else if (error instanceof AuthFailureError) {
                        /*pd.dismiss();*/
                        Toast.makeText(getContext(), "errorin Authentication", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        /*pd.dismiss();*/
                        Toast.makeText(getContext(), "error while parsing", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                       /* pd.dismiss();*/
                        Toast.makeText(getContext(), "error  in server", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ClientError) {
                       /* pd.dismiss();*/
                        Toast.makeText(getContext(), "error with Client", Toast.LENGTH_SHORT).show();
                    } else {
                       /* pd.dismiss();*/
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
        /*pd.show();*/
        mycommand.execute();
        mycommand.remove(request);
    }
}