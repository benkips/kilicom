package com.mabnets.kilicom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class subscriptionadapter extends RecyclerView.Adapter<subscriptionadapter.subscriptionholder> {
    private Context context;
    private ArrayList<subdata> sublists;

    public subscriptionadapter(Context context, ArrayList<subdata> sublists) {
        this.context = context;
        this.sublists = sublists;
    }

    @NonNull
    @Override
    public subscriptionholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subdatainf, parent, false);
        return new subscriptionholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull subscriptionholder holder, int position) {
        final subdata Subdata=(subdata)sublists.get(position);
        holder.period.setText(Subdata.month);
        holder.amount.setText(Subdata.amount);
    }

    @Override
    public int getItemCount() {
        if(sublists!=null){
            return  sublists.size();
        }
        return 0;
    }

    public  static class subscriptionholder extends RecyclerView.ViewHolder {
        private TextView period;
        private TextView  amount;
        public subscriptionholder(@NonNull View itemView) {
            super(itemView);
            period=(TextView)itemView.findViewById(R.id.speriod);
            amount=(TextView)itemView.findViewById(R.id.smoney);
        }
    }
}
