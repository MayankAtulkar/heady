package com.heady.bean;

import java.util.ArrayList;

/**
 * Created by DELL on 12/4/2017.
 */

public class Product {
    int id;
    int view_count;
    int order_count;
    int shares;
    String name;
    String date_added;
    ArrayList<Variant> variants;
    Tax tax;

    public Product() {
    }

    public Product(int id, int view_count, int order_count, int shares, String name, String date_added, ArrayList<Variant> variants, Tax tax) {
        this.id = id;
        this.view_count = view_count;
        this.order_count = order_count;
        this.shares = shares;
        this.name = name;
        this.date_added = date_added;
        this.variants = variants;
        this.tax = tax;
    }

    public Product(int id, String name, Tax tax, ArrayList<Variant> variants) {
        this.id = id;
        this.view_count = view_count;
        this.name = name;
        this.variants = variants;
        this.tax = tax;
    }

    public ArrayList<Variant> getVariants() {
        return variants;
    }

    public void setVariants(ArrayList<Variant> variants) {
        this.variants = variants;
    }

    public Tax getTax() {
        return tax;
    }

    public void setTax(Tax tax) {
        this.tax = tax;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getView_count() {
        return view_count;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public int getOrder_count() {
        return order_count;
    }

    public void setOrder_count(int order_count) {
        this.order_count = order_count;
    }

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

}
