package com.heady.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.heady.bean.EcommResponse;
import com.heady.network.RetrofitInterface;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = findViewById(R.id.target);
        Button btn_target = findViewById(R.id.btn_target);
        ((MyApplication) getApplication()).getEcommComponent().inject(this);

        btn_target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Call<EcommResponse> call = mRetrofitInterface.getData();
                call.enqueue(new Callback<EcommResponse>() {
                    @Override
                    public void onResponse(Call<EcommResponse> call, Response<EcommResponse> response) {
                        //                if (response.isSuccess()) {
                        ecomm = new Gson().toJson(response);
                        Log.i("DEBUG", ecomm);
//                        Toast.makeText(MainActivity.this, new Gson().toJson(response), Toast.LENGTH_SHORT).show();
                        textView.setText("Response: " + ecomm);

//                } else {
//                    Log.i("ERROR", String.valueOf(response.code()));
//                }
                    }

                    @Override
                    public void onFailure(Call<EcommResponse> call, Throwable t) {

                    }
                });
            }
        });
    }
}
