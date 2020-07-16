package com.mabnets.kilicom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

public class Totalproductsadpter extends RecyclerView.Adapter<Totalproductsadpter.Totalproductholder> {

    private ArrayList<total> tlist;
    private Context context;

    public Totalproductsadpter(ArrayList<total> tlist, Context context) {
        this.tlist = tlist;
        this.context = context;
    }

    @NonNull
    @Override
    public Totalproductholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.countyinfocategory, parent, false);
        return new Totalproductholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Totalproductholder holder, int position) {
    total total=(total)tlist.get(position);
    holder.tv1.setText(total.subcategory);
    holder.tv2.setText(total.totalproducts);

    ImageLoader.getInstance().displayImage("http://www.kilicom.co.ke/photos/" + total.photo, holder.iv);
    holder.cv.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle bundle=new Bundle();
            bundle.putString("subcategory",total.subcategory);
            bundle.putString("markets",total.market);
            bundle.putString("county",total.county);
            Intent intent=new Intent(context,viewcountymarkets.class);
            intent.putExtras(bundle);
            context.startActivity(intent);
            CustomIntent.customType(context,"right-to-left");;
        }
    });

    }

    @Override
    public int getItemCount() {
        if(tlist!=null){
            return  tlist.size();
        }
        return 0;
    }

    public static  class Totalproductholder extends RecyclerView.ViewHolder {
        private CardView cv;
        private ImageView iv;
        private TextView tv1;
        private TextView tv2;
        public Totalproductholder(@NonNull View itemView) {
            super(itemView);
            cv=(CardView)itemView.findViewById(R.id.ciccv);
            iv=(ImageView)itemView.findViewById(R.id.ciciv);
            tv1=(TextView)itemView.findViewById(R.id.cicprod);
            tv2=(TextView)itemView.findViewById(R.id.cictotal);

        }
    }
    public void clear() {
        int size = tlist.size();
        tlist.clear();
        notifyItemRangeRemoved(0, size);
    }
}
