package com.heady.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.heady.bean.Variant;
import com.heady.db.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public class ActivityDetails extends AppCompatActivity {

    private int productId;
    private int drawable;
    private ArrayList<Variant> variants;
    private DatabaseHandler dbHandler;
    private ImageView iv_product;
    private TextView tv_header;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        iv_product = findViewById(R.id.iv_product);
        tv_header = findViewById(R.id.tv_header);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            productId = bundle.getInt("productId");
            drawable = bundle.getInt("drawable");
            name = bundle.getString("name");
            tv_header.setText(name);
            Log.i("TAG rec drawable ", drawable + "pro" + productId);
            variants = new ArrayList<>();
            dbHandler = DatabaseHandler.getInstance(MyApplication.getInstance());
            if (productId > 0) {
                variants = dbHandler.getAllVariants(productId);
                iv_product.setImageResource(drawable);
                List<String> list = new ArrayList<>();
                for (int i = 0; i < variants.size(); i++) {
                    list.add("Size: " + variants.get(i).getSize() + "\tColor: " + variants.get(i).getColor()
                            + "\tPrice: " + variants.get(i).getPrice());
                }

                String[] varient = new String[list.size()];
                list.toArray(varient);

                ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, varient);

                ListView listView = findViewById(R.id.size_list);
                listView.setAdapter(adapter);


                /*tv_product_color.setText(variants.get(0).getColor());
//                tv_price.setText(variants.get(0).getPrice());

                List<String> listColor = new ArrayList<>();
                List<Integer> listSize = new ArrayList<>();
                for (int i = 0; i < variants.size(); i++){
                    listColor.add(variants.get(i).getColor());
                    listSize.add(variants.get(i).getSize());
                }
                Integer [] varient_size = new Integer[listSize.size()];
                listSize.toArray(varient_size);

                String [] varient_color = new String[listColor.size()];
                listColor.toArray(varient_color);

                ArrayAdapter adapter = new ArrayAdapter<Integer>(this, R.layout.activity_listview, varient_size);
                ArrayAdapter adapter2 = new ArrayAdapter<String>(this, R.layout.activity_listview, varient_color);

                ListView listView = (ListView) findViewById(R.id.size_list);
                listView.setAdapter(adapter);

                ListView listView1 = (ListView) findViewById(R.id.color_list);
                listView1.setAdapter(adapter2);*/
            }
        }
    }
}