package com.mabnets.kilicom;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Categoryadapter extends RecyclerView.Adapter<Categoryadapter.Categoryholder> {
    private Context context;
    private ArrayList<Category> catelist;
    private int p;
    private Fragment fragment;
    private String status;


    public Categoryadapter(Context context, ArrayList<Category> catelist, Fragment fragment, String status) {
        this.context = context;
        this.catelist = catelist;
        this.fragment = fragment;
        this.status = status;
    }

    @NonNull
    @Override
    public Categoryholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoryinf, parent, false);
        return new Categoryholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Categoryholder holder, final int position) {
    final Category categori=(Category)catelist.get(position);

    holder.categobtn.setText(categori.category);


        if (p==position) {
            holder.categobtn.setBackgroundResource(R.drawable.selectedcategoryshaper);
            holder.categobtn.setTextColor(Color.parseColor("#ffffff"));
            if(status.equals("0")){((home)fragment).loadsubcategory(categori.category);}else
                {((hometwo)fragment).loadsubcategory(categori.category);}

        }else {
            holder.categobtn.setBackgroundResource(R.drawable.categoriesshaper);
            holder.categobtn.setTextColor(Color.parseColor("#4eb748"));
        }
    holder.categobtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
             p=position;
            notifyDataSetChanged();

        }
    });
    }

    @Override
    public int getItemCount() {
        if(catelist!=null){
            return catelist.size();
        }
        return 0;
    }

    public  static  class Categoryholder extends RecyclerView.ViewHolder{
        private TextView  categobtn;
        public Categoryholder(@NonNull View itemView) {
            super(itemView);
            categobtn=(TextView)itemView.findViewById(R.id.catebtn);

        }

    }
}
