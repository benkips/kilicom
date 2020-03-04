package com.mabnets.kilicom;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class Subcategoryadapter  extends RecyclerView.Adapter<Subcategoryadapter.Subcategoryholder>{
    private  Context context;
    private  ArrayList<Category> sublist;
    private String status;


    public Subcategoryadapter(Context context, ArrayList<Category> sublist, String status) {
        this.context = context;
        this.sublist = sublist;
        this.status = status;
    }

    @NonNull
    @Override
    public Subcategoryholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategoriesinf, parent, false);
        return new Subcategoryholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Subcategoryholder holder, int position) {
        final Category subcategori=(Category)sublist.get(position);
        holder.tvsubz.setText(subcategori.subcategory);
        ImageLoader.getInstance().displayImage("http://www.kilicom.mabnets.com/photos/" + subcategori.photo, holder.ivsub);
        holder.cvsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager=((AppCompatActivity)context).getSupportFragmentManager();


                if (status.equals("0")){Bundle bundle=new Bundle();
                    bundle.putString("category",subcategori.subcategory);
                    Fragment fragmentc=new categoryview();
                    fragmentc.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.framelayout,fragmentc).addToBackStack(null).commit();
                }else{Bundle bundle=new Bundle();
                    bundle.putString("category",subcategori.subcategory);
                    String hide="hide";
                    bundle.putString("rmvfl",hide);
                    Fragment fragmentc=new categoryview();
                    fragmentc.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.framelayouttwo,fragmentc).addToBackStack(null).commit();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        if(sublist!=null){
            return sublist.size();
        }
        return 0;
    }

    public static class Subcategoryholder extends RecyclerView.ViewHolder{
        private CardView cvsub;
        private TextView tvsubz;
        private ImageView ivsub;
        public Subcategoryholder(@NonNull View itemView) {
            super(itemView);
            cvsub=(CardView)itemView.findViewById(R.id.subcard);
            tvsubz=(TextView)itemView.findViewById(R.id.subcardtv);
            ivsub=(ImageView)itemView.findViewById(R.id.ivsubz);
        }
    }
}