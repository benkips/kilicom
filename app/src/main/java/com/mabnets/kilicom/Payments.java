package com.mabnets.kilicom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.model.AccessToken;
import com.androidstudy.daraja.model.LNMExpress;
import com.androidstudy.daraja.model.LNMResult;
import com.androidstudy.daraja.util.TransactionType;


public class Payments extends AppCompatActivity {
    private SharedPreferences preferences;
    private String phn;
    private ProgressDialog pd;
    private Button proceedbtn;
    private String amount="";
    private  Daraja daraja;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        proceedbtn=(Button)findViewById(R.id.paynow);
        pd=new ProgressDialog(Payments.this);
        pd.setMessage("processing...Transaction");

        preferences = getSharedPreferences("logininfo.conf", MODE_PRIVATE);
        phn = preferences.getString("phone", "");

        Intent intent = getIntent();
        amount= intent.getStringExtra("amount");


        daraja = Daraja.with("bnfcSXLOwiiqUcGATYLDDbZe9fgySG1M", "TOs57DZodcOtCJYm", new DarajaListener<AccessToken>() {
            @Override
            public void onResult(@NonNull AccessToken accessToken) {

                Log.i(Payments.this.getClass().getSimpleName(), accessToken.getAccess_token());
                /*Toast.makeText(payment.this, "TOKEN : " + accessToken.getAccess_token(), Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onError(String error) {
                Log.e(Payments.this.getClass().getSimpleName(), error);
            }
        });

        proceedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                LNMExpress lnmExpress = new LNMExpress(
                        "174379",
                        "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",  //https://developer.safaricom.co.ke/test_credentials
                        TransactionType.CustomerPayBillOnline, //CustomerBuyGoodsOnline TransactionType.CustomerPayBillOnline  <- Apply any of these two
                        amount,
                        "254708374149",
                        "174379",
                        phn,
                        "http://kilicom.mabnets.com/android/payment.php?QtRS619",
                        "001ABC",
                        "Goods Payment"
                );

                daraja.requestMPESAExpress(lnmExpress, new DarajaListener<LNMResult>() {
                    @Override
                    public void onResult(@NonNull LNMResult lnmResult) {
                        pd.dismiss();
                        if(lnmResult.ResponseDescription.contains("sucess")) {
                            Toast.makeText(Payments.this, lnmResult.ResponseDescription, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Payments.this,MainActivity.class));
                        }
                    }

                    @Override
                    public void onError(String error) {
                        pd.dismiss();
                        AlertDialog.Builder alert = new AlertDialog.Builder(Payments.this);
                        alert.setMessage("Something went wrong  please try again later");
                        alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Payments.this,MainActivity.class));
                            }
                        });
                        alert.show();
                        Log.i(Payments.this.getClass().getSimpleName(), error);
                    }
                });

            }
        });

    }
}
