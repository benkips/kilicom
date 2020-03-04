package com.mabnets.kilicom;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


public class searchAdapter extends RecyclerView.Adapter<searchAdapter.searchHolder>  {
    private Context context;
    private List<viewdata> searchlist;



    public searchAdapter(Context context, List<viewdata> searchlist){
        this.context=context;
        this.searchlist=searchlist;

    }
    @Override
    public searchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflator=LayoutInflater.from(parent.getContext());
        View view=inflator.inflate(R.layout.categoryviewinf,parent,false);
        searchHolder srch=new searchHolder(view);
        return srch;
    }

    @Override
    public void onBindViewHolder(searchHolder holder, int position) {
        viewdata viewdataa=(viewdata)searchlist.get(position);
        ImageLoader.getInstance().displayImage("http://www.kilicom.mabnets.com/photos/" + viewdataa.photo, holder.ivp);
        holder.tvprod.setText(viewdataa.product);
        holder.tvprice.setText("KES "+viewdataa.price);
        holder.tvplace.setText((viewdataa.location));
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString("product", viewdataa.product);
                bundle.putString("photo", viewdataa.photo);
                bundle.putString("price", viewdataa.price);
                bundle.putString("location", viewdataa.location);
                bundle.putString("details", viewdataa.details);
                String status="5";
                bundle.putString("status", status);
                Fragment fragmentc = new productview();
                fragmentc.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.framelayouttwo, fragmentc).addToBackStack(null).commit();


            }
        });

    }

    @Override
    public int getItemCount() {
        if(searchlist!=null){
            return searchlist.size();
        }
        return 0;
    }


    public static class searchHolder extends RecyclerView.ViewHolder{
        private ImageView ivp;
        private TextView tvprod;
        private  TextView tvprice;
        private  TextView tvplace;
        private CardView cv;

        public searchHolder(@NonNull View itemView) {
            super(itemView);
            ivp=(ImageView)itemView.findViewById(R.id.ivproduc);
            tvprod=(TextView)itemView.findViewById(R.id.productname);
            tvprice=(TextView)itemView.findViewById(R.id.prodprice);
            tvplace=(TextView)itemView.findViewById(R.id.prodplace);
            cv=(CardView)itemView.findViewById(R.id.cvcateviewcv);
        }
    }
}

