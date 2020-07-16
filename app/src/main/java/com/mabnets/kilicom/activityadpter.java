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

public class activityadpter extends RecyclerView.Adapter<activityadpter.activityholder> {

    private ArrayList<activityz> alist;
    private Context context;
    private String status;

    public activityadpter(ArrayList<activityz> alist, Context context, String status) {
        this.alist = alist;
        this.context = context;
        this.status = status;
    }

    @NonNull
    @Override
    public activityholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activityinf, parent, false);
        return new activityholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull activityholder holder, int position) {
    final activityz activityz=(activityz)alist.get(position);
    holder.tvc.setText(activityz.crop);
    holder.tvda.setText(activityz.details);
    if (activityz.affected.equals("0")){
        String affected="";
        holder.aastatus.setText("");
        holder.tvaa.setText(affected);
    }else{
        String affected=activityz.affected;
        holder.tvaa.setText(affected+"%");
    }

    if(activityz.photo.contains(".mp4")){
        holder.iva.setImageResource(R.drawable.videoicon);
    }else {
        ImageLoader.getInstance().displayImage("http://www.kilicom.co.ke/photos/temp/A" + activityz.photo, holder.iva);
    }
    holder.cva.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(activityz.photo.contains(".mp4")){
            FragmentManager fragmentManager=((AppCompatActivity)context).getSupportFragmentManager();
            Bundle bundle=new Bundle();
            bundle.putString("crop",activityz.crop);
            bundle.putString("photo",activityz.photo);
            bundle.putString("details",activityz.details);
            bundle.putString("solution",activityz.reply);
                if(status.equals("1")) {
                    bundle.putString("status", status);
                }else{
                    status="0";
                    bundle.putString("status", status);
                }
            Fragment fragmentc=new videosolution();
            fragmentc.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.framelayout,fragmentc).addToBackStack(null).commit();
            }else {
                FragmentManager fragmentManager=((AppCompatActivity)context).getSupportFragmentManager();
                Bundle bundle=new Bundle();
                bundle.putString("crop",activityz.crop);
                bundle.putString("photo",activityz.photo);
                bundle.putString("details",activityz.details);
                bundle.putString("solution",activityz.reply);
                if(status.equals("1")) {
                    bundle.putString("status", status);
                }else{
                    status="0";
                    bundle.putString("status", status);
                }
                Fragment fragmentc=new solution();
                fragmentc.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.framelayout,fragmentc).addToBackStack(null).commit();
            }
        }
    });



    }

    @Override
    public int getItemCount() {
        if(alist!=null){
            return  alist.size();
        }
        return 0;
    }

    public static  class  activityholder extends RecyclerView.ViewHolder{
        private ImageView iva;
        private TextView tvc;
        private TextView tvda;
        private  TextView tvaa;
        private  TextView aastatus;
        private CardView cva;
        
        public activityholder(@NonNull View itemView) {
            super(itemView);
            iva=(ImageView)itemView.findViewById(R.id.ivac);
            tvc=(TextView)itemView.findViewById(R.id.tvcropac);
            tvaa=(TextView)itemView.findViewById(R.id.tvacaf);
            tvda=(TextView)itemView.findViewById(R.id.tvacdetails);
            cva=(CardView)itemView.findViewById(R.id.cvactivitx);
            aastatus=(TextView)itemView.findViewById(R.id.astatus);
        }
    }
}
