package com.project.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.databaseDao.BasketDao;
import com.project.models.Basket;
import com.project.models.ItemProduct;
import com.project.models.Product;
import com.project.util.Const;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;

/**
 * VIEW DETAIL WHEN CLICK ON PRODUCT
 */
public class ViewDetailsActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView txtName, txtPrice,txtType, txtDescription, txtQuantity, txtSumCost;
    private Button btnAdd, btnForwardCheckout, btnBack;
    private String key;
    private Product product;
    private int cost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);

        initialize();
        setEvent();
    }

    private void initialize(){

        product = new Product();

        imageView = findViewById(R.id.productImage);
        txtDescription = findViewById(R.id.txtDescription);
        txtName = findViewById(R.id.txtProductName);
        txtPrice = findViewById(R.id.txtPrice);
        txtType = findViewById(R.id.txtType);
        txtQuantity = findViewById(R.id.txtQuantity);
        txtSumCost = findViewById(R.id.txtSumCost);
        btnAdd = findViewById(R.id.btnAddProduct);
        btnForwardCheckout = findViewById(R.id.btnForwardCheckOut);
        btnBack = findViewById(R.id.btnBack);

        txtSumCost.setText("Total: 0 vnd");
        Intent intent   = getIntent();
        Bundle data = intent.getExtras();

        if(data != null){
            key = data.getString("key");
            Picasso.get().load(data.getString("link")).into(imageView);
            product.setProductImageUrl(data.getString("link"));

            txtType.setText("Type: " + data.getString("productType"));
            product.setProductType(data.getString("productType"));

            cost = data.getInt("productPrice");
            txtPrice.setText( "Price: " + String.valueOf(cost));
            product.setProductPrice(cost);

            txtDescription.setText("Description: " + data.getString("productDescription"));
            product.setProductDescription(data.getString("productDescription"));

            txtName.setText("Product: " + data.getString("productName"));
            product.setProductName(data.getString("productName"));
        }else{
            Toast.makeText(this, "Can resolve any data", Toast.LENGTH_SHORT).show();
        }

    }

    private void forward(Class c){
        Intent a = new Intent(getApplicationContext(), c);
        startActivity(a);
    }

    private void setEvent(){
        txtQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    int num = Integer.parseInt(txtQuantity.getText().toString());
                    txtSumCost.setText("Total: " + num * cost + " vnđ");
                }catch(NumberFormatException e){
                    Toast.makeText(getApplicationContext(), "Quantity must be a positive number", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forward(MainActivity.class);
            }
        });

        btnForwardCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BasketDao basketDao  = new BasketDao();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Const.SHARED_PREFERENCE, MODE_PRIVATE);
                String isExitKey = sharedPreferences.getString(Const.BASKET_KEY, "");

                if(isExitKey.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Your Basket Is Empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    forward(ShowBasketActivity.class);
                }
            }
        });

        // ADD THIS PRODUCT TO BASKET
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!txtQuantity.getText().toString().isEmpty()){


                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Const.SHARED_PREFERENCE,MODE_PRIVATE);
                    String isExitKey = sharedPreferences.getString(Const.BASKET_KEY, "");
                    ItemProduct item = new ItemProduct(product, Integer.parseInt(txtQuantity.getText().toString()));
                    BasketDao basketDao = new BasketDao();

                    // IF NOT HAVE BASKET, CREATE ONE AND ADD PRODUCT
                    if(isExitKey.isEmpty()){
                        String userId = sharedPreferences.getString(Const.USER_KEY, "");
                        Basket basket = new Basket(userId);

                        String key = basketDao.createNewBasket(basket, item);
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putString(Const.BASKET_KEY, key);
                        edit.commit();

                        Toast.makeText(getApplicationContext(), "Add new item to new basket", Toast.LENGTH_SHORT).show();
                    }else{
                        // BASKET EXIST, JUST ADD NEW PRODUCT
                        basketDao.addNewProductToBasket(isExitKey,item);
                        Toast.makeText(getApplicationContext(), "Add new item to basket", Toast.LENGTH_SHORT).show();

                    }
                    forward(MainActivity.class);
                }else{
                    Toast.makeText(getApplicationContext(), "Quantity equal = 0", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}