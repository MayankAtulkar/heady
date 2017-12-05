package com.heady.bean;

import java.util.ArrayList;

/**
 * Created by DELL on 12/4/2017.
 */

public class EcommResponse {
    ArrayList<Category> categories;
    ArrayList<Rankings> rankings;

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public ArrayList<Rankings> getRankings() {
        return rankings;
    }

    public void setRankings(ArrayList<Rankings> rankings) {
        this.rankings = rankings;
    }
}
