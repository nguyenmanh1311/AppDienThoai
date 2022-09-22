package com.project.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.project.adapter.ManagerProductsAdapter;
import com.project.model.Product;

import java.util.HashMap;
import java.util.Map;

public class ManagerProductsFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    GridView gridView;
    Button btnAdd;
    Map<String, Product> products ;

    public ManagerProductsFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_manager_products, container, false);
        gridView =  view.findViewById(R.id.gridView);
        // get data
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("data/products");
        products = new HashMap<>();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<Map<String, Product>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Product>>() {};
                Map<String, Product> productsList = snapshot.getValue(genericTypeIndicator);
                ManagerProductsAdapter managerProductsAdapter;
                if(productsList != null){
                    managerProductsAdapter  = new ManagerProductsAdapter(getContext(), productsList);

                }else{
                    Toast.makeText(getContext(), "No Have Data", Toast.LENGTH_LONG).show();
                    managerProductsAdapter = new ManagerProductsAdapter(getContext(), null);
                }

                gridView.setAdapter(managerProductsAdapter);
                gridView.setOnItemClickListener(ManagerProductsFragment.this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error Loading Image", Toast.LENGTH_SHORT).show();

            }
        });

        btnAdd =  view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        return view;

    }

    private View showData(){
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Toast.makeText(this.getContext(), "You Click! " + position, Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onClick(View view) {
        Intent addProduct = new Intent(getContext(), AddNewProductActivity.class);
        startActivity(addProduct);
    }
}