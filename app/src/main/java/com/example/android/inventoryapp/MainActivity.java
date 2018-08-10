package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.android.inventoryapp.data.InventoryContract;
import com.example.android.inventoryapp.data.InventoryDbHelper;

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

    private long insertData(String tableName, ContentValues row) {
        return mDbHelper.getWritableDatabase().insert(tableName, null, row);
    }

    private Cursor queryData(String query_) {
        return mDbHelper.getReadableDatabase().rawQuery(query_, null);
    }

    public void insertDataClick(View view) {
        ContentValues productRow = new ContentValues();
        ContentValues suppliersRow = new ContentValues();

        productRow.put(InventoryContract.ProductsEntry.COLUMN_NAME_PRODUCT_NAME, "Lorem Ipsum " + randomText.nextLong());
        productRow.put(InventoryContract.ProductsEntry.COLUMN_NAME_PRICE, 123);

        suppliersRow.put(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_NAME, "Lorem Ipsum " + randomText.nextLong());
        suppliersRow.put(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_PHONE, "+123456789");
        suppliersRow.put(InventoryContract.SuppliersEntry.COLUMN_NAME_QUANTITY, 321);
        suppliersRow.put(InventoryContract.SuppliersEntry.COLUMN_NAME_PRODUCT_ID,
                insertData(InventoryContract.ProductsEntry.TABLE_NAME, productRow)
        );

        debugOutput.setText(String.valueOf(insertData(InventoryContract.SuppliersEntry.TABLE_NAME, suppliersRow)));
    }

    public void queryDataClick(View view) {
        Cursor cursor = queryData("SELECT * FROM " + InventoryContract.ProductsEntry.TABLE_NAME +
                " a INNER JOIN " + InventoryContract.SuppliersEntry.TABLE_NAME +
                " b ON a." + InventoryContract.ProductsEntry._ID + "=b." +
                InventoryContract.SuppliersEntry.COLUMN_NAME_PRODUCT_ID);

        StringBuilder result = new StringBuilder();

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
