package com.example.android.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InventoryDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Inventory.db";

    private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE " + InventoryContract.ProductsEntry.TABLE_NAME + " (" +
            InventoryContract.ProductsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            InventoryContract.ProductsEntry.COLUMN_NAME_PRODUCT_NAME + " TEXT NOT NULL," +
            InventoryContract.ProductsEntry.COLUMN_NAME_PRICE + " INTEGER NOT NULL" +
            ")";

    private static final String CREATE_TABLE_SUPPLIERS = "CREATE TABLE " + InventoryContract.SuppliersEntry.TABLE_NAME + " (" +
            InventoryContract.SuppliersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_NAME + " TEXT NOT NULL," +
            InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_COUNTRY_CODE + " TEXT NOT NULL," +
            InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_PHONE + " TEXT NOT NULL," +
            InventoryContract.SuppliersEntry.COLUMN_NAME_QUANTITY + " INTEGER NOT NULL," +
            InventoryContract.SuppliersEntry.COLUMN_NAME_PRODUCT_ID + " INTEGER NOT NULL," +
            "FOREIGN KEY(" + InventoryContract.SuppliersEntry.COLUMN_NAME_PRODUCT_ID + ") REFERENCES " +
            InventoryContract.ProductsEntry.TABLE_NAME + "(" + InventoryContract.ProductsEntry._ID + ")" +
            ")";

    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_SUPPLIERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
