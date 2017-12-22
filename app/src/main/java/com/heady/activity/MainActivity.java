package com.heady.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.heady.adapter.CategoriesAdapter;
import com.heady.adapter.NavigationAdapter;
import com.heady.bean.Category;
import com.heady.bean.EcommResponse;
import com.heady.db.DatabaseHandler;
import com.heady.network.RetrofitInterface;
import com.heady.utils.DividerItemDecoration;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    @Inject
    Retrofit mRetrofit;

    @Inject
    RetrofitInterface mRetrofitInterface;
    private String ecomm;
    private DatabaseHandler dbHandler;

    private AVLoadingIndicatorView avi;
    private RecyclerView recyclerView, navList;
    private ArrayList<Category> categories = new ArrayList<>();
    private CategoriesAdapter categoriesAdapter;
    private NavigationAdapter navigationAdapter;
    private ImageView iv_most_viewed, iv_most_ordered, iv_most_shared;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private CategoriesAdapter.MyClickListener cardClickHandler = new CategoriesAdapter.MyClickListener() {

        @Override
        public void onCategoryClick(int position, int id, String name) {
            Intent intent = new Intent(MainActivity.this, ActivityProducts.class);
            Bundle bundle = new Bundle();
            bundle.putInt("categoryId", id);
            bundle.putString("name", name);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };
    private NavigationAdapter.MyClickListener navClickHandler = new NavigationAdapter.MyClickListener() {

        @Override
        public void onNavClick(int position, int id, String name) {
            Intent intent = new Intent(MainActivity.this, ActivityProducts.class);
            Bundle bundle = new Bundle();
            bundle.putInt("categoryId", id);
            bundle.putString("name", name);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHandler = DatabaseHandler.getInstance(MyApplication.getInstance());
        avi = findViewById(R.id.avi);
        recyclerView = findViewById(R.id.all_recycler_view);
        navList = findViewById(R.id.navList);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        iv_most_viewed = findViewById(R.id.iv_most_viewed);
        iv_most_ordered = findViewById(R.id.iv_most_ordered);
        iv_most_shared = findViewById(R.id.iv_most_shared);

        ((MyApplication) getApplication()).getEcommComponent().inject(this);

        fillDatabase();

        iv_most_viewed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentProduct(1);
            }
        });

        iv_most_ordered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentProduct(2);
            }
        });

        iv_most_shared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentProduct(3);
            }
        });
    }

    private void fillDatabase() {
        avi.smoothToShow();
        Call<EcommResponse> call = mRetrofitInterface.getData();
        call.enqueue(new Callback<EcommResponse>() {
            @Override
            public void onResponse(Call<EcommResponse> call, Response<EcommResponse> response) {
                //                if (response.isSuccess()) {
                ecomm = new Gson().toJson(response);
//                dbHandler.deleteall();
                dbHandler.insert_into_product(response.body().getCategories());
                dbHandler.update_most_viewed(response.body().getRankings().get(0).getProducts());
                dbHandler.update_most_ordered(response.body().getRankings().get(1).getProducts());
                dbHandler.update_most_shared(response.body().getRankings().get(2).getProducts());
                Log.i("DEBUG", ecomm);
//                Toast.makeText(MainActivity.this, new Gson().toJson(response), Toast.LENGTH_SHORT).show();
//                textView.setText("Response: " + ecomm);
                avi.smoothToHide();
                showCategories();
                setNavigationDrawer();

//                navDrawer();
            }

            @Override
            public void onFailure(Call<EcommResponse> call, Throwable t) {

            }
        });
    }

    private void intentProduct(int id) {
        Intent intent = new Intent(MainActivity.this, ActivityProducts.class);
        Bundle bundle = new Bundle();
        switch (id) {
            case 1:
                bundle.putInt("id", 1);
                break;
            case 2:
                bundle.putInt("id", 2);
                break;
            case 3:
                bundle.putInt("id", 3);
                break;
            default:
                break;
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void navDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
//                Log.d(TAG, "onDrawerClosed: " + getTitle());

                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void showCategories() {
        categories = dbHandler.getAllCategories();
        for (int i = 0; i < categories.size(); i++) {
            Log.i("TAG", categories.get(i).getName() + "");
        }

        categoriesAdapter = new CategoriesAdapter(MainActivity.this, categories);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, R.drawable.divider));
        recyclerView.setAdapter(categoriesAdapter);
        categoriesAdapter.setOnItemClickListener(cardClickHandler);
    }

    private void setNavigationDrawer() {
        navigationAdapter = new NavigationAdapter(MainActivity.this, categories);
        navList.setHasFixedSize(true);
        navList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        navList.setAdapter(navigationAdapter);
        navigationAdapter.setOnItemClickListener(navClickHandler);
    }

//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        mDrawerToggle.syncState();
//    }

}
