package com.project.models;

import com.project.util.Const;

import java.util.ArrayList;
import java.util.List;

public class Basket {
    private String userId;
    private List<ItemProduct> productList;
    private int status;

    public Basket() {
    }

    public Basket(String id) {
        productList = new ArrayList<>();
        this.status = Const.IN_USE;
        this.userId = id;

    }

    public Basket(String id, List<ItemProduct> productList) {
        this.userId = id;
        this.productList = productList;
        this.status = Const.IN_USE;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ItemProduct> getProductList() {
        return productList;
    }

    public void setProductList(List<ItemProduct> productList) {
        this.productList = productList;
    }

    public ItemProduct getItemProduct(int pos){
        return productList.get(pos);
    }

    public void addItemProduct(ItemProduct item){
        this.productList.add(item);
    }

    public void updateItemProduct(ItemProduct item, int pos){
        this.productList.set(pos, item);
    }

    public void deleteItemProduct(int pos){
        this.productList.remove(pos);
    }


}
