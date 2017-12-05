package com.heady.bean;

import java.util.ArrayList;

/**
 * Created by DELL on 12/4/2017.
 */

public class Rankings {
    String ranking;
    int rankingId;
    ArrayList<Product> products;

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public int getRankingId() {
        return rankingId;
    }

    public void setRankingId(int rankingId) {
        this.rankingId = rankingId;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
