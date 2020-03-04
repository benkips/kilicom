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

import java.util.ArrayList;

public class notificationadapter extends RecyclerView.Adapter<notificationadapter.notificationholder> {
    private ArrayList<notif> nlist;
    private Context context;
    private String status;


    public notificationadapter(ArrayList<notif> nlist, Context context, String status) {
        this.nlist = nlist;
        this.context = context;
        this.status = status;
    }

    @NonNull
    @Override
    public notificationholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notificationinf, parent, false);
        return new notificationholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull notificationholder holder, int position) {
        final  notif notif=(notif)nlist.get(position);
        holder.tvmsg.setText(notif.message);
        holder.tvtitle.setText(notif.title);
        holder.tvtime.setText(notif.time);

        if(notif.type.equals("chat")){
            holder.ivn.setImageResource(R.drawable.chaticon);
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(status.startsWith("07")){
                        FragmentManager fragmentManager=((AppCompatActivity)context).getSupportFragmentManager();
                        Bundle bundle=new Bundle();
                        bundle.putString("customerphone",status);
                        Fragment fragmentc=new helpfarmer();
                        fragmentc.setArguments(bundle);
                        fragmentManager.beginTransaction().replace(R.id.framelayouttwo,fragmentc).addToBackStack(null).commit();

                    }else {
                        FragmentManager fragmentManager=((AppCompatActivity)context).getSupportFragmentManager();
                        Fragment fragmentc=new helpfarmer();
                        fragmentManager.beginTransaction().replace(R.id.framelayout,fragmentc).addToBackStack(null).commit();
                    }
                }
            });
        }else if(notif.type.equals("payment")){
            holder.ivn.setImageResource(R.drawable.paymenticon);
        }else {
            holder.ivn.setImageResource(R.drawable.bellicon);
        }

    }

    @Override
    public int getItemCount() {
        if(nlist!=null){
            return nlist.size();
        }
        return 0;
    }

    public  static  class notificationholder extends RecyclerView.ViewHolder {
        private TextView tvtitle;
        private TextView tvmsg;
        private CardView cv;
        private ImageView ivn;
        private TextView tvtime;

        public notificationholder(@NonNull View itemView) {
            super(itemView);
            tvtitle=(TextView)itemView.findViewById(R.id.titlen);
            tvmsg=(TextView)itemView.findViewById(R.id.msgn);
            ivn=(ImageView)itemView.findViewById(R.id.imgn);
            cv=(CardView)itemView.findViewById(R.id.cvn);
            tvtime=(TextView)itemView.findViewById(R.id.tme);
        }
    }
}
