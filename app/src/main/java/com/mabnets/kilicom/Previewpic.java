package com.mabnets.kilicom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;

public class Previewpic extends AppCompatActivity {
    private ImageView ivprev;
    private Button btnc;
    private Button btns;
    private String img="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previewpic);
        ivprev=(ImageView)findViewById(R.id.ivpreview);
        btnc=(Button)findViewById(R.id.btncancel);
        btns=(Button)findViewById(R.id.btnsubmit);

        try {

            Intent intent = getIntent();
            img= intent.getStringExtra("Image");

            File f=new File(img);
            Uri uri=Uri.fromFile(f);
            ivprev.setImageURI(uri);
            if(Build.VERSION.SDK_INT<=27) {
                try {
                    Bitmap bitmap = ImageLoader.init().from(img).requestSize(512, 512).getBitmap();
                    ivprev.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }


        } catch(Exception e) {
            e.printStackTrace();
        }

        btns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Previewpic.this, Diseasecapture.class);
                intent.putExtra("Imagex",img );
                startActivity(intent);
                Previewpic.this.finish();
            }
        });
        btnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Previewpic.this,MainActivity.class));
                Previewpic.this.finish();
            }
        });

    }


}
