package com.project.models;

import com.project.util.Const;

public class Order {
    private String basketId, userId;
    private int totalMoney;
    private int status;

    public Order() {
    }

    public Order(String basketId, String userId, int totalMoney) {
        this.basketId = basketId;
        this.userId = userId;
        this.totalMoney = totalMoney;
        this.status = Const.PACKING;
    }

    public Order(String basketId, String userId, int totalMoney, int status) {
        this.basketId = basketId;
        this.userId = userId;
        this.totalMoney = totalMoney;
        this.status = status;
    }

    public String getBasketId() {
        return basketId;
    }

    public void setBasketId(String basketId) {
        this.basketId = basketId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(int totalMoney) {
        this.totalMoney = totalMoney;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
