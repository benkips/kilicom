package com.mabnets.kilicom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class fmarketviewadapter extends RecyclerView.Adapter<fmarketviewadapter.fmarketviewadapterholder> {
    private ArrayList<fmarkets> flist;
    private Context context;

    public fmarketviewadapter(ArrayList<fmarkets> flist, Context context) {
        this.flist = flist;
        this.context = context;
    }

    @NonNull
    @Override
    public fmarketviewadapterholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fmarketviewinf, parent, false);
        return new fmarketviewadapterholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull fmarketviewadapterholder holder, int position) {
        fmarkets fmarket=(fmarkets)flist.get(position);
        holder.nmarket.setText(fmarket.market);
        holder.ncounty.setText(fmarket.county);
        holder.ndetails.setText(fmarket.details);
        ImageLoader.getInstance().displayImage("http://www.kilicom.mabnets.com/photos/"+ fmarket.photo, holder.ivmarkt);
    }

    @Override
    public int getItemCount() {
        if(flist!=null){
            return  flist.size();
        }
        return 0;
    }

    public static  class fmarketviewadapterholder extends RecyclerView.ViewHolder {
        private TextView nmarket;
        private TextView ncounty;
        private TextView ndetails;
        private ImageView ivmarkt;
        public fmarketviewadapterholder(@NonNull View itemView) {
            super(itemView);
            nmarket=(TextView)itemView.findViewById(R.id.fmarketx);
            ncounty=(TextView)itemView.findViewById(R.id.fcountx);
            ndetails=(TextView)itemView.findViewById(R.id.fdetails);
            ivmarkt=(ImageView) itemView.findViewById(R.id.fivmarket);
        }
    }
}
