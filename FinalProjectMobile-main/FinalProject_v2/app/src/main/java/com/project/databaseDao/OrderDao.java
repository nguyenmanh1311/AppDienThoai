package com.project.databaseDao;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.models.Order;

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
}
