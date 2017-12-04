package com.heady.bean;

import java.util.ArrayList;

/**
 * Created by DELL on 12/4/2017.
 */

public class Category {
    int id;
    String name;
    ArrayList<Product> products;
    ArrayList<Integer> child_categories;
}
