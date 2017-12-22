package com.heady.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.heady.activity.MyApplication;
import com.heady.bean.Category;
import com.heady.bean.Product;
import com.heady.bean.Tax;
import com.heady.utils.Constants;

import java.util.ArrayList;


public class DatabaseHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String HEADY_PRODUCT_TABLE = "HEADYPRODUCTTABLE";
    public static final String HEADY_VARIENT_TABLE = "HEADYVARIENTTABLE";
    public static final String HEADY_CATEGORY_TABLE = "HEADYCATEGORYTABLE";

    public static final String HEADY_ID = "_id";
    public static final String HEADY_VIEW_COUNT = "view_count";
    public static final String HEADY_ORDER_COUNT = "order_count";
    public static final String HEADY_SHARES_COUNT = "shares";
    public static final String HEADY_RANKING_ID = "ranking_id";
    public static final String HEADY_NAME = "name";
    public static final String HEADY_DATE_ADDED = "date_added";
    public static final String HEADY_TAX_NAME = "tax_name";
    public static final String HEADY_TAX_VALUE = "tax_value";
    public static final String HEADY_VARIENT_ID = "varient_id";
    public static final String HEADY_CATEGORY_ID = "category_id";
    public static final String HEADY_COLOR = "color";
    public static final String HEADY_SIZE = "size";
    public static final String HEADY_PRICE = "price";
    public static final String CREATE_PRODUCT = "create table if not exists " + HEADY_PRODUCT_TABLE + " ("
            + "_id INTEGER PRIMARY KEY,"
            + "view_count" + " INTEGER,"
            + "order_count" + " INTEGER,"
            + "shares" + " INTEGER,"
            + "ranking_id" + " INTEGER,"
            + "name" + " TEXT ,"
            + "date_added" + " TEXT ,"
            + "tax_name" + " TEXT ,"
            + "tax_value" + " REAL,"
//            + "varient_id" + " INTEGER,"
            + "category_id" + " INTEGER,"
//            + " FOREIGN KEY (" + "varient_id" + ") REFERENCES " + HEADY_VARIENT_TABLE + "(" + "_id),"
            + " FOREIGN KEY (" + "category_id" + ") REFERENCES " + HEADY_CATEGORY_TABLE + "(" + "_id)"
            + "  );";
    public static final String CREATE_CATEGORY = "create table if not exists " + HEADY_CATEGORY_TABLE + " ("
            + "_id INTEGER PRIMARY KEY, "
            + "name" + " TEXT "
            + " );";
    public static final String CREATE_VARIENT = "create table if not exists " + HEADY_VARIENT_TABLE + " ("
            + "_id INTEGER PRIMARY KEY, "
            + "product_id" + " INTEGER,"
            + "color" + " TEXT,"
            + "size" + " INTEGER,"
            + "price" + " INTEGER,"
            + " FOREIGN KEY (" + "product_id" + ") REFERENCES " + HEADY_PRODUCT_TABLE + "(" + "_id)"
            + " );";
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
        db.execSQL(CREATE_PRODUCT);
        db.execSQL(CREATE_VARIENT);
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
            int categoryId = responseData.get(i).getId();
            categoryValues.put("_id", categoryId);
            categoryValues.put("name", responseData.get(i).getName());

            for (int j = 0; j < responseData.get(i).getProducts().size(); j++) {

                ContentValues productValues = new ContentValues();
                int productId = responseData.get(i).getProducts().get(j).getId();
                productValues.put("category_id", categoryId);
                productValues.put("_id", productId);
                productValues.put("name", responseData.get(i).getProducts().get(j).getName());

                productValues.put("view_count", 0);
                productValues.put("order_count", 0);
                productValues.put("shares", 0);

                productValues.put("date_added", responseData.get(i).getProducts().get(j).getDate_added());

                productValues.put("tax_name", responseData.get(i).getProducts().get(j).getTax().getName());
                productValues.put("tax_value", responseData.get(i).getProducts().get(j).getTax().getValue());

                for (int k = 0; k < responseData.get(i).getProducts().get(j).getVariants().size(); k++) {
                    ContentValues variantValues = new ContentValues();
                    variantValues.put("_id", responseData.get(i).getProducts().get(j).getVariants().get(k).getId());
                    variantValues.put("color", responseData.get(i).getProducts().get(j).getVariants().get(k).getColor());
                    variantValues.put("size", responseData.get(i).getProducts().get(j).getVariants().get(k).getSize());
                    variantValues.put("price", responseData.get(i).getProducts().get(j).getVariants().get(k).getPrice());
                    variantValues.put("product_id", productId);
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

    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        Category model;
        String numQuery = "SELECT *  FROM " + HEADY_CATEGORY_TABLE /*+ " WHERE " + GROUP_STATUS + " = 1;"*/ + ";";
        Cursor cursor = db.rawQuery(numQuery, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    model = new Category();
                    model.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(HEADY_ID))));
                    model.setName(cursor.getString(cursor.getColumnIndex(HEADY_NAME)));
                    categories.add(model);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        return categories;
    }

    public ArrayList<Product> getAllProducts(int categoryId) {
        ArrayList<Product> products = new ArrayList<>();
        Product model;
        String numQuery = "SELECT *  FROM " + HEADY_PRODUCT_TABLE + " WHERE " + HEADY_CATEGORY_ID + " = " + categoryId + ";";
        Log.i("TAG query ", numQuery + "");
        Cursor cursor = db.rawQuery(numQuery, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    model = new Product();
                    model.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(HEADY_ID))));
                    model.setName(cursor.getString(cursor.getColumnIndex(HEADY_NAME)));
//                    Log.i("TAG cat id ", Integer.valueOf(cursor.getString(cursor.getColumnIndex(HEADY_CATEGORY_ID)))+"");
                    Tax tax = new Tax();
                    tax.setName(cursor.getString(cursor.getColumnIndex(HEADY_TAX_NAME)));
                    tax.setValue(cursor.getDouble(cursor.getColumnIndex(HEADY_TAX_VALUE)));
//                    Log.i("TAG name q ", model.getName()+"");
                    model.setTax(tax);
                    products.add(model);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        return products;
    }

    public ArrayList<Product> getMostViewed() {
        ArrayList<Product> products = new ArrayList<>();
        Product model;
        String numQuery = "SELECT *  FROM " + HEADY_PRODUCT_TABLE + " WHERE " + HEADY_VIEW_COUNT + " >= " + "12000" + ";";
        Log.i("TAG query ", numQuery + "");
        Cursor cursor = db.rawQuery(numQuery, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    model = new Product();
                    model.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(HEADY_ID))));
                    model.setName(cursor.getString(cursor.getColumnIndex(HEADY_NAME)));
//                    Log.i("TAG cat id ", Integer.valueOf(cursor.getString(cursor.getColumnIndex(HEADY_CATEGORY_ID)))+"");
                    Tax tax = new Tax();
                    tax.setName(cursor.getString(cursor.getColumnIndex(HEADY_TAX_NAME)));
                    tax.setValue(cursor.getDouble(cursor.getColumnIndex(HEADY_TAX_VALUE)));
//                    Log.i("TAG name q ", model.getName()+"");
                    model.setTax(tax);
                    products.add(model);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        return products;
    }

    public ArrayList<Product> getMostOrdered() {
        ArrayList<Product> products = new ArrayList<>();
        Product model;
        String numQuery = "SELECT *  FROM " + HEADY_PRODUCT_TABLE + " WHERE " + HEADY_ORDER_COUNT + " > " + "1890" + ";";
        Log.i("TAG query ", numQuery + "");
        Cursor cursor = db.rawQuery(numQuery, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    model = new Product();
                    model.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(HEADY_ID))));
                    model.setName(cursor.getString(cursor.getColumnIndex(HEADY_NAME)));
//                    Log.i("TAG cat id ", Integer.valueOf(cursor.getString(cursor.getColumnIndex(HEADY_CATEGORY_ID)))+"");
                    Tax tax = new Tax();
                    tax.setName(cursor.getString(cursor.getColumnIndex(HEADY_TAX_NAME)));
                    tax.setValue(cursor.getDouble(cursor.getColumnIndex(HEADY_TAX_VALUE)));
//                    Log.i("TAG name q ", model.getName()+"");
                    model.setTax(tax);
                    products.add(model);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        return products;
    }

    public ArrayList<Product> getMostShared() {
        ArrayList<Product> products = new ArrayList<>();
        Product model;
        String numQuery = "SELECT *  FROM " + HEADY_PRODUCT_TABLE + " WHERE " + HEADY_SHARES_COUNT + " >= " + "1800" + ";";
        Log.i("TAG query ", numQuery + "");
        Cursor cursor = db.rawQuery(numQuery, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    model = new Product();
                    model.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(HEADY_ID))));
                    model.setName(cursor.getString(cursor.getColumnIndex(HEADY_NAME)));
//                    Log.i("TAG cat id ", Integer.valueOf(cursor.getString(cursor.getColumnIndex(HEADY_CATEGORY_ID)))+"");
                    Tax tax = new Tax();
                    tax.setName(cursor.getString(cursor.getColumnIndex(HEADY_TAX_NAME)));
                    tax.setValue(cursor.getDouble(cursor.getColumnIndex(HEADY_TAX_VALUE)));
//                    Log.i("TAG name q ", model.getName()+"");
                    model.setTax(tax);
                    products.add(model);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        return products;
    }

    public void update_most_viewed(ArrayList<Product> responseData) {
        for (int i = 0; i < responseData.size(); i++) {
            ContentValues categoryValues = new ContentValues();
            categoryValues.put("view_count", responseData.get(i).getView_count());

            Log.e("TAG pro ", new Gson().toJson(categoryValues));
            Log.e("TAG id ", responseData.get(i).getView_count() + responseData.get(i).getId() + "");
            long c = db.updateWithOnConflict(HEADY_PRODUCT_TABLE, categoryValues, "_id" + " = " + responseData.get(i).getId(), null, SQLiteDatabase.CONFLICT_IGNORE);
        }
    }

    public void update_most_ordered(ArrayList<Product> responseData) {
        for (int i = 0; i < responseData.size(); i++) {
            ContentValues categoryValues = new ContentValues();
            categoryValues.put("order_count", responseData.get(i).getOrder_count());
            long c = db.updateWithOnConflict(HEADY_PRODUCT_TABLE, categoryValues, "_id" + " = " + responseData.get(i).getId(), null, SQLiteDatabase.CONFLICT_IGNORE);
        }
    }

    public void update_most_shared(ArrayList<Product> responseData) {
        for (int i = 0; i < responseData.size(); i++) {
            ContentValues categoryValues = new ContentValues();
            categoryValues.put("shares", responseData.get(i).getShares());
            long c = db.updateWithOnConflict(HEADY_PRODUCT_TABLE, categoryValues, "_id" + " = " + responseData.get(i).getId(), null, SQLiteDatabase.CONFLICT_IGNORE);
        }
    }

    public void deleteTable(String table) {
        db.execSQL("delete from " + table);
    }
}
