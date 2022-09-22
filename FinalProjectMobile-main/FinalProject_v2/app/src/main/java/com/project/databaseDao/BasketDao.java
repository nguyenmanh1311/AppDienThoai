package com.project.databaseDao;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.models.Basket;
import com.project.models.ItemProduct;

public class BasketDao {

    private FirebaseDatabase db;
    private DatabaseReference dbRef;

    public BasketDao(){
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("data/basket/");
    }

    /**
     * Create new basket
     * @param basket not have a any item
     * @return a key of new basket
     */
    public String createNewBasket(Basket basket, ItemProduct itemProduct){
        String key = dbRef.push().getKey();
        dbRef.child(key).setValue(basket);
        dbRef.child(key+"/productList/0/").push().setValue(itemProduct);
        return key;
    }

    /**
     * add new product to basket
     * @param key key of basket
     * @param product new item product to add
     */
    public void addNewProductToBasket(String key, ItemProduct product){
        dbRef.child(key+"/productList/0/").push().setValue(product);
    }

    /**
     * delete item out of basket
     * @param keyBasket basket key
     * @param keyItem key of item to delete
     */
    public void deleteItemOnBasket(String keyBasket, String keyItem){
        dbRef.child(keyBasket + "/productList/0/"+keyItem).removeValue();
    }

    /**
     * Update quantity of item product
     * @param keyBasket  basket key
     * @param keyItem item product key
     * @param item item to update
     */
    public void updateQuantityItemOnBasket(String keyBasket, String keyItem, ItemProduct item){
        dbRef.child(keyBasket+"/productList/0/"+keyItem).setValue(item);
    }


}
