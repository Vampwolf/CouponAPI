package com.example.CouponAPI.dto;

import java.util.List;

public class CouponDTO {

    private Double cartTotal;  // Total cart value for cart-wise coupons
    private List<String> productList;  // List of products in the cart for product-wise coupons
    private String product;  // Product to apply the discount for product-wise coupons
    private Double productPrice;  // Price of the product to calculate the discount
    private List<String> buyList;  // Products being bought in the Buy X Get Y logic
    private List<String> getList;  // Products to get for free in the Buy X Get Y logic
    private int maxRepetitions;  // Maximum number of repetitions for BxGy (e.g., b2g1)

    // Getters and Setters

    public Double getCartTotal() {
        return cartTotal;
    }

    public void setCartTotal(Double cartTotal) {
        this.cartTotal = cartTotal;
    }

    public List<String> getProductList() {
        return productList;
    }

    public void setProductList(List<String> productList) {
        this.productList = productList;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public List<String> getBuyList() {
        return buyList;
    }

    public void setBuyList(List<String> buyList) {
        this.buyList = buyList;
    }

    public List<String> getGetList() {
        return getList;
    }

    public void setGetList(List<String> getList) {
        this.getList = getList;
    }

    public int getMaxRepetitions() {
        return maxRepetitions;
    }

    public void setMaxRepetitions(int maxRepetitions) {
        this.maxRepetitions = maxRepetitions;
    }
}