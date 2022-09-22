package com.project.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.project.adapter.ShowBasketAdapter;
import com.project.models.ItemProduct;
import com.project.util.Const;

import java.util.Map;

public class ShowBasketActivity extends AppCompatActivity {

    private String key;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private ListView listView;
    private Button btnOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_basket);

        listView = findViewById(R.id.listView);
        btnOrder = findViewById(R.id.btnOrder);

        /**
         * CHECK SHARED PREFERENCE IF HAVE A BASKET KEY, GET DATA FROM DATABASE AND SHOW IT.
         */

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Const.SHARED_PREFERENCE, MODE_PRIVATE);
        key = sharedPreferences.getString(Const.BASKET_KEY, "");
        if(key.isEmpty()){
            Toast.makeText(this, "No Item ", Toast.LENGTH_SHORT).show();
        }
        else{
            db = FirebaseDatabase.getInstance();
            dbRef = db.getReference("data/basket/"+key);

            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    GenericTypeIndicator<Map<String, ItemProduct>> genericTypeIndicator = new GenericTypeIndicator<Map<String, ItemProduct>>() {};
                    Map<String, ItemProduct> productMap = snapshot.child("/productList/0/").getValue(genericTypeIndicator);
                    if(productMap == null){
                        Toast.makeText(getApplicationContext(), "No item", Toast.LENGTH_LONG).show();
                    }else{
                        ShowBasketAdapter showBasketAdapter = new ShowBasketAdapter(getApplicationContext(), productMap);
                        listView.setAdapter(showBasketAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Something error when fetch data", Toast.LENGTH_SHORT).show();
                }
            });

            // FORWARD TO CHECKOUT ACTIVITY IF CLICK
            btnOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CheckOutActivity.class);
                    startActivity(intent);
                }
            });

        }

    }

}