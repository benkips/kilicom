package com.mabnets.kilicom;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.kosalgeek.android.json.JsonConverter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class cashout extends Fragment {
    private RecyclerView rvcashout;
    private TextView tvdue;
    final String Tag = this.getClass().getName();
    private Button btnorder;
    private String boughtproducts = "";
    public static int totalz = 0;


    public cashout() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vcashout = inflater.inflate(R.layout.fragment_cashout, container, false);
        rvcashout = (RecyclerView) vcashout.findViewById(R.id.rvcashout);
        tvdue = (TextView) vcashout.findViewById(R.id.tvdue);
        btnorder = (Button) vcashout.findViewById(R.id.btnorder);


        rvcashout.setHasFixedSize(true);
        rvcashout.setLayoutManager(new LinearLayoutManager(getContext()));
        cashoutadapter cashoutadapter = new cashoutadapter(getContext(), Customerinterface.cartitems, cashout.this);
        rvcashout.setAdapter(cashoutadapter);


        sendgson();
        btnorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("productbought", boughtproducts);
                Fragment fragment = new ordernow();
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.framelayouttwo, fragment).addToBackStack(null).commit();
            }
        });

        return vcashout;
    }

    public void sendgson() {
        Gson gson = new GsonBuilder().create();
        JsonArray myCustomArray = gson.toJsonTree(Customerinterface.cartitems).getAsJsonArray();
        boughtproducts = String.valueOf(myCustomArray);
        Log.d(Tag, String.valueOf(myCustomArray));
        if (String.valueOf(myCustomArray) != null) {
            ArrayList<cartz> notidata = new JsonConverter<cartz>().toArrayList(String.valueOf(myCustomArray), cartz.class);
            int o = 0;
            for (cartz value : notidata) {
                o += value.getPrice();
            }

            Log.d(Tag, String.valueOf(o));
            totalz = o;
            if (totalz == 0) {
                if (btnorder.getVisibility() != View.GONE) {
                    btnorder.setVisibility(View.GONE);
                   /* Fragment fragmentmain=new hometwo();*/
                    Fragment fragmentback=new cashout();
                    getFragmentManager().beginTransaction().remove(fragmentback);
                    getFragmentManager().popBackStack();
                    /*getFragmentManager().beginTransaction().replace(R.id.framelayout,fragmentmain).addToBackStack(null).commit();*/
                }
            } else {
                if (btnorder.getVisibility() != View.VISIBLE) {
                    btnorder.setVisibility(View.VISIBLE);
                }
            }
            tvdue.setText("Total cost=Ksh."+String.valueOf(totalz));
        }
    }
}