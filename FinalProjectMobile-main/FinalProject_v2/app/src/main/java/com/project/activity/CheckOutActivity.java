package com.project.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.project.adapter.CheckOutAdapter;
import com.project.databaseDao.OrderDao;
import com.project.models.ItemProduct;
import com.project.models.Order;
import com.project.models.User;
import com.project.util.Const;

import java.util.Map;

public class CheckOutActivity extends AppCompatActivity {
    private ListView listView;
    private TextView txtUserName, txtUserAddress, txtUserPhone, txtTotal;
    private Button btnCheckOut;

    private int totalCost = 0;
    private String basketKey, userKey;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        initialize();
        getData();
        setEvent();
    }

    private void initialize(){

        // get data user from SharedPreferences;
        Gson gson = new Gson();
        sharedPreferences = getApplicationContext().getSharedPreferences(Const.SHARED_PREFERENCE, MODE_PRIVATE);

        basketKey = sharedPreferences.getString(Const.BASKET_KEY,"");
        userKey = sharedPreferences.getString(Const.USER_KEY,"");

        String userData = sharedPreferences.getString(Const.USER_DATA, "");
        User user = gson.fromJson(userData, User.class);

        listView = findViewById(R.id.listViewCheckOut);
        txtUserName = findViewById(R.id.txtUsername);
        txtUserAddress = findViewById(R.id.txtUserAddress);
        txtUserPhone = findViewById(R.id.txtUserPhone);
        txtTotal = findViewById(R.id.txtTotal);
        btnCheckOut = findViewById(R.id.btnCheckOut);

        txtUserName.setText("Username: "+ user.getUsername());
        txtUserPhone.setText("Phone Number: " + user.getPhoneNumber());
        txtUserAddress.setText("Address: " + user.getAddress());
    }

    /**
     * SET EVENT FOR BUTTON {btnCheckOut}
     */
    private void setEvent(){
        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order orders = new Order(basketKey, userKey, totalCost);
                OrderDao orderDao = new OrderDao();

                orderDao.createNewOrder(orders);

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Const.SHARED_PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(Const.BASKET_KEY);
                editor.commit();

                Toast.makeText(getApplicationContext() , "Ordered !", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }


    /**
     * GET DATA FROM FIREBASE AND SET ADAPTER FOR LISTVIEW
     */
    private void getData(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("data/basket/");

        dbRef.child(basketKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<Map<String, ItemProduct>> genericTypeIndicator = new GenericTypeIndicator<Map<String, ItemProduct>>() {};
                Map<String, ItemProduct> list = snapshot.child("/productList/0/").getValue(genericTypeIndicator);

                for(Map.Entry<String, ItemProduct> ele: list.entrySet()){
                    totalCost = totalCost + ele.getValue().getTotal();
                }

                String s = "Total : " + totalCost;
                txtTotal.setText(s);

                CheckOutAdapter adapter = new CheckOutAdapter(getApplicationContext(), list);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}