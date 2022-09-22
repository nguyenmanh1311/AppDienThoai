package com.project.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.project.adapter.ManagerProductsAdapter;
import com.project.adapter.ShowItemOrderAdapter;
import com.project.model.Order;
import com.project.model.Product;
import com.project.util.Const;

import java.util.HashMap;
import java.util.Map;

public class ReportFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef;

        View view = inflater.inflate(R.layout.fragment_report, container, false);
        TextView txtShowMoney = view.findViewById(R.id.txtShowMoney);
        TextView txtShowOrder = view.findViewById(R.id.txtShowOrder);
        TextView txtShowProduct = view.findViewById(R.id.txtShowProduct);

        dbRef = db.getReference("data/orders/");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<Map<String, Order>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Order>>() {
                };
                Map<String, Order> orderMap = snapshot.getValue(genericTypeIndicator);

                int num0 = 0, num1 = 0, num2 = 0;
                int money = 0;
                if (orderMap != null) {
                    for (Map.Entry<String, Order> ele : orderMap.entrySet()) {
                        if (ele.getValue().getStatus() == Const.PACKING) {
                            num0++;
                        } else if (ele.getValue().getStatus() == Const.SHIPPING) {
                            num1++;
                        } else {
                            num2++;
                            money += ele.getValue().getTotalMoney();
                        }
                    }
                }

                String s = num0 + " Packing | " + num1 + " Shipping | " + num2 + " Done";
                String s2 = "Achieved: total " + money + " VND";
                txtShowOrder.setText(s);
                txtShowMoney.setText(s2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dbRef = db.getReference("data/products/");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<Map<String, Product>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Product>>() {};
                Map<String, Product> productsList = snapshot.getValue(genericTypeIndicator);
                if(productsList == null){
                    txtShowProduct.setText("Don't have any product");
                }
                else{
                    txtShowProduct.setText("Have " + productsList.size() + " in stock");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error Loading Image", Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }
}
