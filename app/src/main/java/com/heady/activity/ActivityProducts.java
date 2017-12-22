package com.heady.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.heady.adapter.ProductsAdapter;
import com.heady.bean.Product;
import com.heady.db.DatabaseHandler;

import java.util.ArrayList;

public class ActivityProducts extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Product> products = new ArrayList<>();
    private ProductsAdapter productsAdapter;
    private int categoryId;
    private DatabaseHandler dbHandler;
    private int id;
    private String name;
    private TextView tv_header;
    private ProductsAdapter.MyClickListener clickListener = new ProductsAdapter.MyClickListener() {

        @Override
        public void onProductClick(int position, int id, int drawable, String name) {
            Intent intent = new Intent(ActivityProducts.this, ActivityDetails.class);
            Bundle bundle = new Bundle();
            bundle.putInt("productId", id);
            bundle.putInt("drawable", drawable);
            bundle.putString("name", name);
            Log.i("TAG sent drawable ", drawable + "");
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        recyclerView = findViewById(R.id.all_recycler_view);
        dbHandler = DatabaseHandler.getInstance(MyApplication.getInstance());
        tv_header = findViewById(R.id.tv_header);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            categoryId = bundle.getInt("categoryId");
            id = bundle.getInt("id");
            name = bundle.getString("name");
            Log.i("TAG name ", name + "");
            tv_header.setText(name);
            if (categoryId > 0) {
                products = dbHandler.getAllProducts(categoryId);
            } else {
                switch (id) {
                    case 1:
                        tv_header.setText("Most Viewed Products");
                        products = dbHandler.getMostViewed();
                        break;
                    case 2:
                        tv_header.setText("Most Ordered Products");
                        products = dbHandler.getMostOrdered();
                        break;
                    case 3:
                        tv_header.setText("Most Shared Products");
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
        productsAdapter.setOnItemClickListener(clickListener);
    }
}
