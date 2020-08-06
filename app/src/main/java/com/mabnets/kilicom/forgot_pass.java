package com.mabnets.kilicom;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.mabnets.kilicom.R;

import java.util.HashMap;
import java.util.Map;

public class forgot_pass extends AppCompatActivity {
    private EditText etmailrc;
    private Button rcbtn;
    private ProgressDialog progressDialog;
    private Mycommand mycommand;
    private Dialog mydialog;
    final String Tag=this.getClass().getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        etmailrc=(EditText)findViewById(R.id.etmailrcc);
        rcbtn=(Button) findViewById(R.id.recievebtn);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("loading..");
        mycommand=new Mycommand(this);
        mydialog=new Dialog(this);

        rcbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String em=etmailrc.getText().toString().trim();
                Validatedetails(em);
            }
        });

    }
    private void Validatedetails(final String e){
        if (e.isEmpty()) {
            etmailrc.setError("email is  is invalid");
            etmailrc.requestFocus();
            return;
        }else if (!isValidEmail(e)) {
            etmailrc.setError("email is invalid");
            etmailrc.requestFocus();
            return;
        } else{
            String url="http://www.kilicom.co.ke/emailpass.php";
            StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    Log.d(Tag,response);
                    if(!response.isEmpty()){
                        if(response.contains("Success")){
                            Toast.makeText(forgot_pass.this, response, Toast.LENGTH_SHORT).show();
                            androidx.appcompat.app.AlertDialog.Builder alert=new AlertDialog.Builder(forgot_pass.this);
                            alert.setMessage(response);
                            alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(forgot_pass.this,Login.class));
                                }
                            });
                            alert.setCancelable(false);
                            alert.show();
                        } else {
                            Toast.makeText(forgot_pass.this, response, Toast.LENGTH_SHORT).show();
                            Log.d(Tag, response);

                        }

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(error!=null) {
                        Log.d(Tag, error.toString());
                        if (error instanceof TimeoutError) {
                            progressDialog.dismiss();
                            Toast.makeText(forgot_pass.this, "error time out ", Toast.LENGTH_SHORT).show();

                        } else if (error instanceof NoConnectionError) {
                            progressDialog.dismiss();
                            Toast.makeText(forgot_pass.this, "error no connection", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            progressDialog.dismiss();
                            Toast.makeText(forgot_pass.this, "error network error", Toast.LENGTH_SHORT).show();

                        }else if (error instanceof AuthFailureError) {
                            progressDialog.dismiss();
                            Toast.makeText(forgot_pass.this, "errorin Authentication", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            progressDialog.dismiss();
                            Toast.makeText(forgot_pass.this, "error while parsing", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            progressDialog.dismiss();
                            Toast.makeText(forgot_pass.this, "error  in server", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ClientError) {
                            progressDialog.dismiss();
                            Toast.makeText(forgot_pass.this, "error with Client", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(forgot_pass.this, "error while loading", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> params=new HashMap<>();
                    params.put("email", e);
                    return params;
                }
            };
            mycommand.add(request);
            progressDialog.show();
            mycommand.execute();
            mycommand.remove(request);
        }
    }
    public final static boolean isValidEmail(String target) {

        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}