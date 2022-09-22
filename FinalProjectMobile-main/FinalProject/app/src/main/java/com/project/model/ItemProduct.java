package com.project.model;

public class ItemProduct {
    private Product product;
    private int quantity;
    private int total;

    public ItemProduct() {
    }

    public ItemProduct(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.total = this.product.getProductPrice() * this.quantity;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void updateTotal(){
        this.total = quantity * product.getProductPrice();
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
