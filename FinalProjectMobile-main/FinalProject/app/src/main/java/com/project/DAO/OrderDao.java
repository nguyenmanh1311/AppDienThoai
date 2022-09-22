package com.project.DAO;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.model.Order;

public class OrderDao {
    private FirebaseDatabase db;
    private DatabaseReference dbRef;

    public OrderDao(){
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("data/orders/");
    }

    public void createNewOrder(Order orders){
        dbRef.push().setValue(orders);
    }

    public void updateOrder(String orderKey, Order order){
        dbRef.child(orderKey).setValue(order);
    }
}
