package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
    public void bindView(View view, final Context context, Cursor cursor) {
        Button sellButton = view.findViewById(R.id.sell);

        sellButton.setTag(cursor.getString(cursor.getColumnIndex(InventoryContract.ProductsEntry._ID)));
        sellButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    context.getContentResolver().update(
                            ContentUris.withAppendedId(
                                    InventoryContract.CONTENT_URI_SELL,
                                    Long.parseLong(view.getTag().toString())
                            ),
                            null, null, null
                    );
                } catch (IllegalArgumentException e) {
                    (Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT)).show();
                }
            }

        });

        ((TextView) view.findViewById(R.id.product_name)).setText(cursor.getString(cursor.getColumnIndex(InventoryContract.ProductsEntry.COLUMN_NAME_PRODUCT_NAME)));
        ((TextView) view.findViewById(R.id.price)).setText(cursor.getString(cursor.getColumnIndex(InventoryContract.ProductsEntry.COLUMN_NAME_PRICE)));
        ((TextView) view.findViewById(R.id.quantity)).setText(cursor.getString(cursor.getColumnIndex(InventoryContract.SuppliersEntry.COLUMN_NAME_TOTAL_QUANTITY)));
    }

}
