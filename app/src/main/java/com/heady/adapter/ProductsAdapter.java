package com.heady.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.heady.activity.R;
import com.heady.bean.Product;

import java.util.ArrayList;

/**
 * Created by DELL on 12/5/2017.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.DataObjectHolder> {

    private static ProductsAdapter.MyClickListener myClickListener;
    private ArrayList<Product> mDataset;
    private Context context;
    private int id;

    public ProductsAdapter(Context context, ArrayList<Product> myDataset) {
        mDataset = new ArrayList<>();
        this.context = context;
        for (Product product : myDataset) {
            mDataset.add(new Product(product.getId(), product.getName(), product.getTax(), product.getVariants()));

        }
    }

    public void setOnItemClickListener(ProductsAdapter.MyClickListener myClickListener) {
        ProductsAdapter.myClickListener = myClickListener;
    }

    @Override
    public ProductsAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_product, parent, false);
        return new ProductsAdapter.DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductsAdapter.DataObjectHolder holder, final int position) {

        holder.tv_product_name.setText(mDataset.get(position).getName());

        Log.i("TAG Adap ", mDataset.get(position).getName());

        int id = 0;
        switch (mDataset.get(position).getId() % 9) {
            case 0:
                id = context.getResources().getIdentifier("prod1", "drawable", context.getPackageName());
                break;
            case 1:
                id = context.getResources().getIdentifier("prod2", "drawable", context.getPackageName());
                break;
            case 2:
                id = context.getResources().getIdentifier("prod3", "drawable", context.getPackageName());
                break;
            case 3:
                id = context.getResources().getIdentifier("prod4", "drawable", context.getPackageName());
                break;
            case 4:
                id = context.getResources().getIdentifier("prod5", "drawable", context.getPackageName());
                break;
            case 5:
                id = context.getResources().getIdentifier("prod6", "drawable", context.getPackageName());
                break;
            case 6:
                id = context.getResources().getIdentifier("prod7", "drawable", context.getPackageName());
                break;
            case 7:
                id = context.getResources().getIdentifier("prod8", "drawable", context.getPackageName());
                break;
            case 8:
                id = context.getResources().getIdentifier("prod9", "drawable", context.getPackageName());
                break;
            default:
                id = context.getResources().getIdentifier("ic_launcher_background", "drawable", context.getPackageName());
                break;
        }
        holder.img_product.setImageResource(id);

        final int finalId = id;
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG adap ", finalId + "");
                myClickListener.onProductClick(position, mDataset.get(position).getId(), finalId, mDataset.get(position).getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void replaceItem(Product Product, int position) {
        mDataset.set(position, Product);
        notifyItemChanged(position);
    }

    public interface MyClickListener {
        void onProductClick(int position, int id, int drawable, String name);
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView tv_product_name;
        ImageView img_product;
        CardView card_view;

        public DataObjectHolder(View itemView) {
            super(itemView);
            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            img_product = itemView.findViewById(R.id.img_product);
            card_view = itemView.findViewById(R.id.card_view);
        }
    }

}
