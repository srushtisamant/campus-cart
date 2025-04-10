package edu.uncc.evaluation05.models;

import java.io.Serializable;
import java.util.ArrayList;

public class PurchaseList implements Serializable {
    String plid;
    String name;
    ArrayList<Product> items;
    int totalItems=4;
    double totalCost=2;

    public PurchaseList(String plid, String name, ArrayList<Product> items) {
        this.plid = plid;
        this.name = name;
        this.items = items;
    }


        // Getters and setters for totalItems and totalCost
        public int getTotalItems() {
            return totalItems;
        }

        public void setTotalItems(int totalItems) {
            this.totalItems = totalItems;
        }

        public double getTotalCost() {
            return totalCost;
        }

        public void setTotalCost(double totalCost) {
            this.totalCost = totalCost;
        }


    public String getPlid() {
        return plid;
    }

    public void setPlid(String plid) {
        this.plid = plid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Product> getItems() {
        return items;
    }

    public void setItems(ArrayList<Product> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "PurchaseList{" +
                "plid='" + plid + '\'' +
                ", name='" + name + '\'' +
                ", items=" + items +
                '}';
    }
}
