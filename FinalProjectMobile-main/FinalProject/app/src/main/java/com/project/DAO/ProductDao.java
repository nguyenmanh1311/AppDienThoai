package com.project.DAO;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.model.Product;


public class ProductDao {
    private final FirebaseStorage storage;
    private final StorageReference storageReference;
    private final FirebaseDatabase db;
    private final DatabaseReference ref;
    public ProductDao(){
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        db = FirebaseDatabase.getInstance();
        ref = db.getReference("data/products");
    }

    /**
     * Insert New Product to firebase
     * @param context content of application
     * @param imgUri Uri of image
     * @param products -> new product
     */
    public void insertNewProduct(Context context, Uri imgUri, Product product) {

        //defining the child of storageReference
        StorageReference storageRef = storageReference.child("images/" + imgUri);

        //adding listener on upload
        storageRef.putFile(imgUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(context, "Image Uploaded!", Toast.LENGTH_SHORT).show();
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // update success
                        String url = uri.toString();
                        product.setProductImageUrl(url);
                        ref.push().setValue(product);
                        Toast.makeText(context, "Successfully!", Toast.LENGTH_SHORT).show();
                    });

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();

                });
    }

    /**
     * Update data but not change image
     * @param context  - application
     * @param keys - key of product
     * @param product - data product update
     */
    public void updateProductWithoutImage(Context context, String keys, Product product){
            ref.child(keys).setValue(product);
            Toast.makeText(context, "Update successfully", Toast.LENGTH_SHORT).show();
    }

    /**
     * Update product have image
     * @param context application
     * @param keys  - key of product
     * @param product - info product will update
     * @param uri - url new img
     */
    public void updateProductHaveImage(Context context, String keys, Product product, Uri uri){
        //TODO: remove old image

        StorageReference ref = storageReference.child("images/");

        ref.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(context, "Changed Image", Toast.LENGTH_SHORT).show();
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            product.setProductImageUrl(url);
                            DatabaseReference ref = db.getReference("data/products");
                            ref.child(keys).setValue(product);
                            Toast.makeText(context, "Update successfully", Toast.LENGTH_SHORT).show();

                        }
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();

                });

    }

    /**
     * Delete product
     * @param key - key of product
     */
    public void deleteProduct(Context context, String key){
        ref.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Remove product",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Can't remove product",Toast.LENGTH_SHORT).show();
            }
        });

    }

}
