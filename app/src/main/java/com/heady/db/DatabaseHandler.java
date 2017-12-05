package com.heady.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.heady.activity.MyApplication;
import com.heady.bean.Category;
import com.heady.utils.Constants;

import java.util.ArrayList;


public class DatabaseHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String HEADY_PRODUCT_TABLE = "HEADYPRODUCTTABLE";
    public static final String HEADY_VARIENT_TABLE = "HEADYVARIENTTABLE";
    public static final String HEADY_CATEGORY_TABLE = "HEADYCATEGORYTABLE";
    public static final String CREATE_PRODUCT = "create table if not exists " + HEADY_PRODUCT_TABLE + " (" + "_id INTEGER PRIMARY KEY,"
            + "view_count" + " integer,"
            + "ranking_id" + " integer,"
            + "name" + " text ,"
            + "date_added" + " text ,"
            + "tax_name" + " text ,"
            + "tax_value" + " REAL,"
            + "varient_id" + " integer,"
            + "category_id" + " integer,"
            + " FOREIGN KEY (" + "varient_id" + ") REFERENCES " + HEADY_VARIENT_TABLE + "(" + "_id),"
            + " FOREIGN KEY (" + "category_id" + ") REFERENCES " + HEADY_CATEGORY_TABLE + "(" + "_id)"
            + "  );";
    public static final String CREATE_CATEGORY = "create table if not exists " + HEADY_CATEGORY_TABLE + " (" + "_id INTEGER PRIMARY KEY, "
            + "name" + " text "
            + " );";
    public static final String CREATE_VARIENT = "create table if not exists " + HEADY_VARIENT_TABLE + " (" + "_id INTEGER PRIMARY KEY, "
            + "color" + " text,"
            + "size" + " integer,"
            + "price" + " integer  );";
    private static final String TAG = "DatabaseHandler";
    private static DatabaseHandler mInstance = null;
    private SQLiteDatabase db = null;

    private DatabaseHandler(final Context context, final String name,
                            final int version) {
        super(context, name, null, version);
        db = getWritableDatabase();
    }

    // DB overridden methods
    public static DatabaseHandler getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseHandler(MyApplication.getInstance(), Constants.DATABASE_NAME, DATABASE_VERSION);
        }
        return mInstance;
    }

    @Override
    public synchronized void close() {
        if (mInstance != null) {
            db.close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CATEGORY);
        db.execSQL(CREATE_VARIENT);
        db.execSQL(CREATE_PRODUCT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        Log.i(TAG, "onUpgrade +" + oldVersion + "--->" + newVersion);
        if (oldVersion < 1) {
            db.execSQL("DROP TABLE IF EXISTS " + HEADY_CATEGORY_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + HEADY_VARIENT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + HEADY_PRODUCT_TABLE);
        }
        onCreate(db);
    }

    public void deleteall() {
        db.execSQL("delete from " + HEADY_CATEGORY_TABLE);
        db.execSQL("delete from " + HEADY_VARIENT_TABLE);
        db.execSQL("delete from " + HEADY_PRODUCT_TABLE);
    }

    public void insert_into_product(ArrayList<Category> responseData) {
        for (int i = 0; i < responseData.size(); i++) {
            ContentValues categoryValues = new ContentValues();
            categoryValues.put("_id", responseData.get(i).getId());
            categoryValues.put("name", responseData.get(i).getName());

            for (int j = 0; j < responseData.get(i).getProducts().size(); j++) {

                ContentValues productValues = new ContentValues();
                productValues.put("category_id", responseData.get(i).getId());
                productValues.put("_id", responseData.get(i).getProducts().get(j).getId());

                productValues.put("view_count", responseData.get(i).getProducts().get(j).getView_count());
                productValues.put("date_added", responseData.get(i).getProducts().get(j).getDate_added());
                productValues.put("_id", responseData.get(i).getProducts().get(j).getId());

                productValues.put("tax_name", responseData.get(i).getProducts().get(j).getTax().getName());
                productValues.put("tax_value", responseData.get(i).getProducts().get(j).getTax().getValue());

                for (int k = 0; k < responseData.get(i).getProducts().get(j).getVariants().size(); k++) {
                    ContentValues variantValues = new ContentValues();
                    variantValues.put("_id", responseData.get(i).getProducts().get(j).getVariants().get(k).getId());
                    variantValues.put("color", responseData.get(i).getProducts().get(j).getVariants().get(k).getColor());
                    variantValues.put("size", responseData.get(i).getProducts().get(j).getVariants().get(k).getSize());
                    variantValues.put("price", responseData.get(i).getProducts().get(j).getVariants().get(k).getPrice());
                    Log.e("TAG variant ", new Gson().toJson(variantValues));
                    long a = db.insertWithOnConflict(HEADY_VARIENT_TABLE, null, variantValues, SQLiteDatabase.CONFLICT_IGNORE);
                    if (a == -1) {
                        a = db.updateWithOnConflict(HEADY_VARIENT_TABLE, variantValues, "_id" + " = " + responseData.get(i).getProducts().get(j).getVariants().get(k).getId(), null, SQLiteDatabase.CONFLICT_IGNORE);
                    }
                }

                Log.e("TAG pro ", new Gson().toJson(productValues));
                long b = db.insertWithOnConflict(HEADY_PRODUCT_TABLE, null, productValues, SQLiteDatabase.CONFLICT_IGNORE);
                if (b == -1) {
                    b = db.updateWithOnConflict(HEADY_PRODUCT_TABLE, productValues, "_id" + " = " + responseData.get(i).getProducts().get(j).getId(), null, SQLiteDatabase.CONFLICT_IGNORE);
                }
            }
            Log.e("TAG cate ", new Gson().toJson(categoryValues));
            long c = db.insertWithOnConflict(HEADY_CATEGORY_TABLE, null, categoryValues, SQLiteDatabase.CONFLICT_IGNORE);
            if (c == -1) {
                c = db.updateWithOnConflict(HEADY_CATEGORY_TABLE, categoryValues, "_id" + " = " + responseData.get(i).getId(), null, SQLiteDatabase.CONFLICT_IGNORE);
            }
        }
    }

    public void deleteTable(String table) {
        db.execSQL("delete from " + table);
    }
}
