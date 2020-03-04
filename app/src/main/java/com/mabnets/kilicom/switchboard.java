package com.mabnets.kilicom;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class switchboard extends AppCompatActivity {
    private Button afarmer;
    private  Button abuyer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switchboard);
        afarmer=(Button)findViewById(R.id.amfarmer);
        abuyer=(Button)findViewById(R.id.ambuyer);

        afarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(switchboard.this,Login.class));
            }
        });
        abuyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(switchboard.this,Customerinterface.class));
            }
        });
    }
}
