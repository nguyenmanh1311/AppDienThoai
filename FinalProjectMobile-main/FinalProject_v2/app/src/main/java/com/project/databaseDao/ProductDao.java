package com.project.databaseDao;

import android.content.Context;
import android.widget.GridView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.project.activity.MainActivity;
import com.project.adapter.ShowProductAdapter;
import com.project.models.Product;

import java.util.Map;

public class ProductDao {
    private FirebaseDatabase db;
    private DatabaseReference dbRef;

    public ProductDao(){
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("data/products/");
    }

}
