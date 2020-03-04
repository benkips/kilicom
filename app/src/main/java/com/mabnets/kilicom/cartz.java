package com.mabnets.kilicom;

public class cartz {
    private String product ;
    private String photo;
    private  Integer price;

    public cartz(String product, String photo, Integer price) {
        this.product = product;
        this.photo = photo;
        this.price = price;
    }

    public String getProduct() {
        return product;
    }

    public String getPhoto() {
        return photo;
    }

    public Integer getPrice() {
        return price;
    }
}
