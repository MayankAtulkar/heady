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
import com.heady.bean.Category;

import java.util.ArrayList;

/**
 * Created by DELL on 12/5/2017.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.DataObjectHolder> {

    private static CategoriesAdapter.MyClickListener myClickListener;
    private ArrayList<Category> mDataset;
    private Context context;

    public CategoriesAdapter(Context context, ArrayList<Category> myDataset) {
        mDataset = new ArrayList<>();
        this.context = context;
        for (Category category : myDataset) {
            mDataset.add(new Category(category.getId(), category.getName(), category.getProducts(), category.getChild_categories()));

        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        CategoriesAdapter.myClickListener = myClickListener;
    }

    @Override
    public CategoriesAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_category, parent, false);
        return new CategoriesAdapter.DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(final CategoriesAdapter.DataObjectHolder holder, final int position) {

        holder.tv_category_name.setText(mDataset.get(position).getName());

        Log.i("TAG Adap ", mDataset.get(position).getName());

        int id = context.getResources().getIdentifier("ic_launcher_background", "drawable", context.getPackageName());
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

        holder.img_category.setImageResource(id);

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onCategoryClick(position, mDataset.get(position).getId(), mDataset.get(position).getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void replaceItem(Category Category, int position) {
        mDataset.set(position, Category);
        notifyItemChanged(position);
    }

    public interface MyClickListener {
        void onCategoryClick(int position, int id, String name);
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView tv_category_name;
        ImageView img_category;
        CardView card_view;

        public DataObjectHolder(View itemView) {
            super(itemView);
            tv_category_name = itemView.findViewById(R.id.tv_category_name);
            img_category = itemView.findViewById(R.id.img_category);
            card_view = itemView.findViewById(R.id.card_view);
        }
    }

}