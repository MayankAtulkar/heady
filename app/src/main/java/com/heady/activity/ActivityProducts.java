package com.heady.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.heady.adapter.ProductsAdapter;
import com.heady.bean.Product;
import com.heady.db.DatabaseHandler;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class ActivityProducts extends AppCompatActivity {

    private AVLoadingIndicatorView avi;
    private RecyclerView recyclerView;
    private ArrayList<Product> products = new ArrayList<>();
    private ProductsAdapter productsAdapter;
    private int categoryId;
    private DatabaseHandler dbHandler;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        avi = findViewById(R.id.avi);
        recyclerView = findViewById(R.id.all_recycler_view);
        dbHandler = DatabaseHandler.getInstance(MyApplication.getInstance());

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            categoryId = bundle.getInt("categoryId");
            id = bundle.getInt("id");
//            avi.smoothToShow();
            if (categoryId > 0) {
//                showProducts(categoryId);
                products = dbHandler.getAllProducts(categoryId);
            } else {
                switch (id) {
                    case 1:
                        products = dbHandler.getMostViewed();
                        break;
                    case 2:
                        products = dbHandler.getMostOrdered();
                        break;
                    case 3:
                        products = dbHandler.getMostShared();
                        break;
                    default:
                        break;
                }
            }
            showProduct();
        }
    }

    private void showProduct() {
        productsAdapter = new ProductsAdapter(ActivityProducts.this, products);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(ActivityProducts.this, 2));
        recyclerView.setAdapter(productsAdapter);
    }

    private void showProducts(int categoryId) {
        products = dbHandler.getAllProducts(categoryId);
        /*for (int i = 0; i < products.size(); i++) {
            Log.i("TAG", products.get(i).getName()+"");
            Log.i("TAG", products.get(i).getTax().getValue()+"");
        }*/
        productsAdapter = new ProductsAdapter(ActivityProducts.this, products);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(ActivityProducts.this, 2));
        recyclerView.setAdapter(productsAdapter);
//        productsAdapter.setOnItemClickListener(cardClickHandler);
    }

}
