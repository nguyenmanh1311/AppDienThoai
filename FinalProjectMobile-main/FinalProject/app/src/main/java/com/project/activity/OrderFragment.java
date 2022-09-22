package com.project.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.project.adapter.ShowItemOrderAdapter;
import com.project.model.Order;
import com.project.util.Const;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderFragment extends Fragment {

    private ListView listViewPicking, listViewShipping, listViewDone;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    public OrderFragment(){
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("data/orders/");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_order, container, false);
        listViewPicking = view.findViewById(R.id.listViewPicking);
        listViewShipping = view.findViewById(R.id.listViewShipping);
        listViewDone = view.findViewById(R.id.listViewDone);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<Map<String, Order>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Order>>() {};
                Map<String, Order> orderMap = snapshot.getValue(genericTypeIndicator);

                Map<String, Order> map0 = new HashMap<>();
                Map<String, Order> map1 = new HashMap<>();
                Map<String, Order> map2 = new HashMap<>();

                if(orderMap != null){
                    for(Map.Entry<String, Order> ele: orderMap.entrySet()){
                        if(ele.getValue().getStatus() == Const.PACKING){
                            map0.put(ele.getKey(), ele.getValue());
                        }else if(ele.getValue().getStatus() == Const.SHIPPING){
                            map1.put(ele.getKey(), ele.getValue());
                        }else{
                            map2.put(ele.getKey(), ele.getValue());
                        }
                    }
                }

                ShowItemOrderAdapter adapter0 =  new ShowItemOrderAdapter(getContext(), map0);
                ShowItemOrderAdapter adapter1 =  new ShowItemOrderAdapter(getContext(), map1);
                ShowItemOrderAdapter adapter2 =  new ShowItemOrderAdapter(getContext(), map2);

                listViewPicking.setAdapter(adapter0);
                listViewShipping.setAdapter(adapter1);
                listViewDone.setAdapter(adapter2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something wrong", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
}