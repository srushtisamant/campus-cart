package edu.uncc.evaluation05.models;

import java.io.Serializable;

public class Product implements Serializable {
    String pid, name, img_url;
    double price_per_item;
    int quantity = 0;

      /*
    {
            "pid": "cdcab5ed-f4ad-43f8-8551-c6efac39427d",
            "name": "Charlotte 49ers 11.5'' Suntime Premium Glass Face Football Helmet Wall Clock",
            "img_url": "https://www.theappsdr.com/items-imgs/charlotte-49ers-wall-clock.jpeg",
            "price_per_item": 35.99
        },
     */

    public Product() {
    }

    public Product(String pid, String name, String img_url, double price_per_item, int quantity) {
        this.pid = pid;
        this.name = name;
        this.img_url = img_url;
        this.price_per_item = price_per_item;
        this.quantity = quantity;
    }

    public Product(String pid, String name, String img_url, double price_per_item) {
        this.pid = pid;
        this.name = name;
        this.img_url = img_url;
        this.price_per_item = price_per_item;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public double getPrice_per_item() {
        return price_per_item;
    }

    public void setPrice_per_item(double price_per_item) {
        this.price_per_item = price_per_item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void incrementQuantity() {
        quantity++;
    }

    public void decrementQuantity() {
        if(quantity > 0) {
            quantity--;
        }
    }

}
