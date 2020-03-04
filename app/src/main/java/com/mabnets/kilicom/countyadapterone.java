package com.mabnets.kilicom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

public class countyadapterone extends RecyclerView.Adapter<countyadapterone.countyholder> {

    private ArrayList countylist;
    private Context context;

    public countyadapterone(ArrayList countylist, Context context) {
        this.countylist = countylist;
        this.context = context;
    }

    @NonNull
    @Override
    public countyholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.marktinf, parent, false);
        return new countyholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull countyholder holder, int position) {
        county county=(county)countylist.get(position);
        holder.tv.setText(county.county);
        holder.cvx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("county",county.county);
                Intent intent=new Intent(context,categoryanddatafromcounties.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
                CustomIntent.customType(context,"right-to-left");;
            }
        });
    }

    @Override
    public int getItemCount() {
        if(countylist !=null){
            return countylist.size();
        }
        return 0;
    }

    public  static class countyholder extends RecyclerView.ViewHolder {
        private TextView tv;
        private CardView cvx;
        public countyholder(@NonNull View itemView) {
            super(itemView);
            tv=(TextView) itemView.findViewById(R.id.countyx);
            cvx=(CardView) itemView.findViewById(R.id.cvcntmrkt);

        }
    }
}
