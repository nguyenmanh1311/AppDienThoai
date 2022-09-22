package com.project.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.project.DAO.ProductDao;
import com.project.model.Product;
import com.project.util.Const;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class UpdateProductActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText productName, productType, productDescription,productPrice;
    private Button btnUpdate, btnDelete;
    private String key;
    private Uri uri;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        init();
        setEvent();
    }

    /**
     * Initialize for ImageView, EditText, Button, String
     * get data from intent and set it into field
     * set event for button
     */
    private void init(){
        product = new Product();
        // init for element
        imageView = findViewById(R.id.productImage);
        productName = findViewById(R.id.txtProductName);
        productPrice =  findViewById(R.id.txtProductPrice);
        productType = findViewById(R.id.txtProductType);
        productDescription = findViewById(R.id.txtProductDescription);
        btnUpdate =  findViewById(R.id.btnUpdateProduct);
        btnDelete = findViewById(R.id.btnDeleteProduct);

        // get intent and data from bundle
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            key = bundle.getString("key");
            Picasso.get().load(bundle.getString("link")).into(imageView);
            product.setProductImageUrl(bundle.getString("link"));
            productType.setText(bundle.getString("productType"));
            productPrice.setText(String.valueOf(bundle.getInt("productPrice")));
            productDescription.setText(bundle.getString("productDescription"));
            productName.setText(bundle.getString("productName"));
        }else{
            Toast.makeText(this, "Can resolve any data", Toast.LENGTH_SHORT).show();
        }

    }

    private void forward(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }
    /**
     * set event for object
     */
    private void setEvent(){
        // set Event for button
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkAndUpdateData()){
                    ProductDao dao = new ProductDao();
                    if(uri == null){
                        dao.updateProductWithoutImage(getApplicationContext(), key, product);
                    }else{
                        dao.updateProductHaveImage(getApplicationContext(),key, product, uri);
                    }
                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductDao dao = new ProductDao();
                dao.deleteProduct(getApplicationContext(), key);
                forward();
            }


        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // open gallery for change picture
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, Const.PICK_IMAGE);
            }
        });
    }

    /**
     * result of open Gallery and load image to ImageView
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == Const.PICK_IMAGE){
            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * check data of user input. Check any change and update data for product variable
     */
    private boolean checkAndUpdateData(){
        String name =productName.getText().toString();
        String type = productType.getText().toString();
        String description = productDescription.getText().toString();
        int price = 0;

        // try convert and check price
        try{
            price = Integer.parseInt(productPrice.getText().toString());
            product.setProductPrice(price);
        }catch(Exception e){
            Toast.makeText(this, "Invalid Price", Toast.LENGTH_SHORT).show();
            return false;
        }

        // check EditText field
        if(name.compareTo("")==0 || type.compareTo("")==0 || description.compareTo("")==0){
            Toast.makeText(this, "All field are require!", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            product.setProductName(name);
            product.setProductDescription(description);
            product.setProductType(type);
        }
        return true;
    }
}