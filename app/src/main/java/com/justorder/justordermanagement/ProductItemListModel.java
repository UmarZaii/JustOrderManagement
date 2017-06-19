package com.justorder.justordermanagement;

public class ProductItemListModel {

    private String productImg;
    private String productName;
    private String productDesc;
    private String productPrice;
    private String productHotPrice;
    private String productColdPrice;

    public ProductItemListModel() {

    }

    public ProductItemListModel(String productImg, String productName, String productDesc, String productPrice,
                                String productHotPrice, String productColdPrice) {
        this.productImg = productImg;
        this.productName = productName;
        this.productDesc = productDesc;
        this.productPrice = productPrice;
        this.productHotPrice = productHotPrice;
        this.productColdPrice = productColdPrice;
    }

    public String getProductImg() {
        return productImg;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductHotPrice() {
        return productHotPrice;
    }

    public String getProductColdPrice() {
        return productColdPrice;
    }
}
