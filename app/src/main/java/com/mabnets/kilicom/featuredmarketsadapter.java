package com.mabnets.kilicom;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class featuredmarketsadapter extends RecyclerView.Adapter<featuredmarketsadapter.featuredmarketsholder> {
    private Context context;
    private ArrayList<fmarkets> fmarketlist;
    private  String status;

    public featuredmarketsadapter(Context context, ArrayList<fmarkets> fmarketlist, String status) {
        this.context = context;
        this.fmarketlist = fmarketlist;
        this.status = status;
    }

    @NonNull
    @Override
    public featuredmarketsholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fmarketsinf, parent, false);
        return new featuredmarketsholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull featuredmarketsholder holder, int position) {
       fmarkets fmarket=(fmarkets)fmarketlist.get(position);
       holder.tvfm.setText(fmarket.market);
        ImageLoader.getInstance().displayImage("http://www.kilicom.co.ke/photos/"+ fmarket.photo, holder.ivfm);
        holder.btnvalll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager=((AppCompatActivity)context).getSupportFragmentManager();
                Fragment fragmentc=new fmarkertview();
                if (status.equals("0")){
                    fragmentManager.beginTransaction().replace(R.id.framelayout,fragmentc).addToBackStack(null).commit();
                }else{
                    fragmentManager.beginTransaction().replace(R.id.framelayouttwo,fragmentc).addToBackStack(null).commit();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
       if(fmarketlist!=null){
           return fmarketlist.size();
       }
       return 0;
    }

    public  static class featuredmarketsholder extends RecyclerView.ViewHolder {
        private ImageView ivfm;
        private Button btnvalll;
        private TextView tvfm;
        public featuredmarketsholder(@NonNull View itemView) {
            super(itemView);
            ivfm=(ImageView)itemView.findViewById(R.id.ivfmarket);
            btnvalll=(Button)itemView.findViewById(R.id.vall);
            tvfm=(TextView)itemView.findViewById(R.id.tvfmarketx);
        }
    }
}
