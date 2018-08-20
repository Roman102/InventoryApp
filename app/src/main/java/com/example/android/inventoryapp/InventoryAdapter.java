package com.example.android.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.inventoryapp.data.InventoryContract;

public class InventoryAdapter extends CursorAdapter {

    public InventoryAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.mainview_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ((TextView) view.findViewById(R.id.product_name)).setText(cursor.getString(cursor.getColumnIndex(InventoryContract.ProductsEntry.COLUMN_NAME_PRODUCT_NAME)));
        ((TextView) view.findViewById(R.id.price)).setText(cursor.getString(cursor.getColumnIndex(InventoryContract.ProductsEntry.COLUMN_NAME_PRICE)));
        ((TextView) view.findViewById(R.id.quantity)).setText(cursor.getString(cursor.getColumnIndex(InventoryContract.SuppliersEntry.COLUMN_NAME_TOTAL_QUANTITY)));
    }

}
