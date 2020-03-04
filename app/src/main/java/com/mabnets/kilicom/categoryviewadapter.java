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
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class categoryviewadapter extends RecyclerView.Adapter<categoryviewadapter.categoryviewholder> {
    private Context context;
    private ArrayList<viewdata> viewlist;
    private String status;

    public categoryviewadapter(Context context, ArrayList<viewdata> viewlist, String status) {
        this.context = context;
        this.viewlist = viewlist;
        this.status = status;
    }

    @NonNull
    @Override
    public categoryviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoryviewinf, parent, false);
        return new categoryviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull categoryviewholder holder, int position) {
        viewdata viewdataa=(viewdata)viewlist.get(position);
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
                    if(status.equals("1")) {
                        bundle.putString("status", status);
                        Fragment fragmentc = new productview();
                        fragmentc.setArguments(bundle);
                        fragmentManager.beginTransaction().replace(R.id.framelayout, fragmentc).addToBackStack(null).commit();
                    }else if(status.equals("0")){
                        status="0";
                        bundle.putString("status", status);
                        Fragment fragmentc = new productview();
                        fragmentc.setArguments(bundle);
                        fragmentManager.beginTransaction().replace(R.id.framelayout, fragmentc).addToBackStack(null).commit();
                    }else if(status.equals("5")){
                        status="5";
                        bundle.putString("status", status);
                        Fragment fragmentc = new productview();
                        fragmentc.setArguments(bundle);
                        fragmentManager.beginTransaction().replace(R.id.framelayouttwo, fragmentc).addToBackStack(null).commit();
                    }else if(status.equals("6")){
                        Intent intent = new Intent(context, Customerinterface.class);
                        intent.putExtra("move",status );
                        intent.putExtra("product", viewdataa.product);
                        intent.putExtra("photo", viewdataa.photo);
                        intent.putExtra("price", viewdataa.price);
                        intent.putExtra("location", viewdataa.location);
                        intent.putExtra("details", viewdataa.details);
                        context.startActivity(intent);
                    }if(status.equals("7")){

                }


            }
        });

    }

    @Override
    public int getItemCount() {
        if(viewlist!=null){
            return viewlist.size();
        }
        return 0;
    }

    public  static  class categoryviewholder extends RecyclerView.ViewHolder{
        private ImageView ivp;
        private TextView tvprod;
        private  TextView tvprice;
        private  TextView tvplace;
        private CardView cv;

        public categoryviewholder(@NonNull View itemView) {
            super(itemView);
            ivp=(ImageView)itemView.findViewById(R.id.ivproduc);
            tvprod=(TextView)itemView.findViewById(R.id.productname);
            tvprice=(TextView)itemView.findViewById(R.id.prodprice);
            tvplace=(TextView)itemView.findViewById(R.id.prodplace);
            cv=(CardView)itemView.findViewById(R.id.cvcateviewcv);
        }
    }
}
