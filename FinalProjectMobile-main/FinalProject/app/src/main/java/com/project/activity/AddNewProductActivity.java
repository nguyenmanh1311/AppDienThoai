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

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.DAO.ProductDao;
import com.project.model.Product;
import com.project.util.Const;

import java.io.IOException;

public class AddNewProductActivity extends AppCompatActivity {

    private ImageView imageProductView;
    private EditText txtProductName, txtProductPrice, txtProductType, txtProductDescription;
    private Button btnAddNewProduct;
    private Uri imageUri;
    private String productName, productType, productDescription;
    private int productPrice;
    private String url;
    private String key = "";

    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);
        init();
        setEvent();
    }

    /**
     * initialize all field of activity
     * get instance for database
     */
    private void init(){
        imageProductView = findViewById(R.id.productImage);
        txtProductName =  findViewById(R.id.txtProductName);
        txtProductPrice =  findViewById(R.id.txtProductPrice);
        txtProductType = findViewById(R.id.txtProductType);
        txtProductDescription = findViewById(R.id.txtProductDescription);
        btnAddNewProduct =  findViewById(R.id.btnAddNewProduct);

        // get the firebase storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    /**
     * Use for upload have  an error or have any error on processing
     * .Set all field are null like first-time access this activity
     */
    private void destroy(){
        imageProductView.setImageBitmap(null);
        txtProductName.setText(null);
        txtProductPrice.setText(null);
        txtProductType.setText(null);
        txtProductDescription.setText(null);
    }

    /**
     * Set event for object
     */
    private void setEvent(){
        btnAddNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkData()){
                    ProductDao dao = new ProductDao();
                    dao.insertNewProduct(getApplicationContext(), imageUri, new Product(productName, "", productType, productDescription, productPrice ));
                }
                destroy();

            }
        });

        imageProductView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }

    /**
     * Open Gallery: where save image in phone
     */
    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, Const.PICK_IMAGE);
    }

    /**
     * result of open Gallery and load image to ImageView
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == Const.PICK_IMAGE){
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageProductView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getText(EditText t){
        return t.getText().toString().trim();
    }

    /**
     * Check data from user input
     * @return true if all field are correct  else  false
     */
    private boolean checkData(){
        productName = getText(txtProductName);
        productType = getText(txtProductType);
        productDescription = getText(txtProductDescription);
        productPrice = 0;

        try {
            productPrice = Integer.parseInt(getText(txtProductPrice));
        }catch (Exception e){
            Toast.makeText(this, "Invalid Price", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(productName.compareTo("")==0 || productType.compareTo("")==0 || productDescription.compareTo("")==0){
            Toast.makeText(this, "All field are require!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return imageUri != null;
    }



}
