package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private InventoryDbHelper mDbHelper;

    private TextView debugOutput;

    private Random randomText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        debugOutput = findViewById(R.id.debugOutput);

        mDbHelper = new InventoryDbHelper(this);

        randomText = new Random();
    }

    public void insertDataClick(View view) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues productRow = new ContentValues();
        ContentValues suppliersRow = new ContentValues();

        productRow.put(InventoryContract.ProductsEntry.COLUMN_NAME_PRODUCT_NAME, "Lorem Ipsum " + randomText.nextLong());
        productRow.put(InventoryContract.ProductsEntry.COLUMN_NAME_PRICE, 123);

        suppliersRow.put(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_NAME, "Lorem Ipsum " + randomText.nextLong());
        suppliersRow.put(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_PHONE, "+123456789");
        suppliersRow.put(InventoryContract.SuppliersEntry.COLUMN_NAME_QUANTITY, 321);
        suppliersRow.put(InventoryContract.SuppliersEntry.COLUMN_NAME_PRODUCT_ID,
                db.insert(InventoryContract.ProductsEntry.TABLE_NAME, null, productRow)
        );

        debugOutput.setText(String.valueOf(db.insert(InventoryContract.SuppliersEntry.TABLE_NAME, null, suppliersRow)));
    }

    public void queryDataClick(View view) {
        StringBuilder result = new StringBuilder();

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + InventoryContract.ProductsEntry.TABLE_NAME +
                " a INNER JOIN " + InventoryContract.SuppliersEntry.TABLE_NAME +
                " b ON a." + InventoryContract.ProductsEntry._ID + "=b." +
                InventoryContract.SuppliersEntry.COLUMN_NAME_PRODUCT_ID, null);

        while (cursor.moveToNext()) {
            result.append(cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.ProductsEntry.COLUMN_NAME_PRODUCT_NAME))).append(", ")
                    .append(cursor.getLong(cursor.getColumnIndexOrThrow(InventoryContract.ProductsEntry.COLUMN_NAME_PRICE))).append(", ")
                    .append(cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_NAME))).append(", ")
                    .append(cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_PHONE))).append(", ")
                    .append(cursor.getLong(cursor.getColumnIndexOrThrow(InventoryContract.SuppliersEntry.COLUMN_NAME_QUANTITY))).append(", ")
                    .append(cursor.getLong(cursor.getColumnIndexOrThrow(InventoryContract.SuppliersEntry.COLUMN_NAME_PRODUCT_ID))).append(", ")
                    .append(cursor.getLong(cursor.getColumnIndexOrThrow(InventoryContract.ProductsEntry._ID))).append("\n");
        }

        cursor.close();

        debugOutput.setText(result);
    }

}
