package com.mabnets.kilicom;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class categoryfromcountiesadapter extends RecyclerView.Adapter<categoryfromcountiesadapter.countydataholder> {


    private ArrayList<county> clist;
    private Context context;
    int p;

    public categoryfromcountiesadapter(ArrayList<county> clist, Context context) {
        this.clist = clist;
        this.context = context;
    }

    @NonNull
    @Override
    public countydataholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoryinf, parent, false);
        return new countydataholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull countydataholder holder, int position) {
        final county cnty=(county)clist.get(position);
        holder.cntygobtn.setText(cnty.market);

        if (p==position) {
            holder.cntygobtn.setBackgroundResource(R.drawable.selectedcategoryshaper);
            holder.cntygobtn.setTextColor(Color.parseColor("#ffffff"));
            if (context instanceof categoryanddatafromcounties) {
                ((categoryanddatafromcounties) context).gettotalproducts(cnty.market);
            }
        }else {
            holder.cntygobtn.setBackgroundResource(R.drawable.categoriesshaper);
            holder.cntygobtn.setTextColor(Color.parseColor("#4eb748"));

        }
        holder.cntygobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p=position;
                /*if (context instanceof categoryanddatafromcounties) {
                    ((categoryanddatafromcounties) context).gettotalproducts(cnty.market);
                }*/
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(clist!=null){
            return  clist.size();
        }
        return 0;
    }

    public static class countydataholder extends RecyclerView.ViewHolder {
        private TextView cntygobtn;
        public countydataholder(@NonNull View itemView) {
            super(itemView);
            cntygobtn=(TextView)itemView.findViewById(R.id.catebtn);
        }
    }
}
