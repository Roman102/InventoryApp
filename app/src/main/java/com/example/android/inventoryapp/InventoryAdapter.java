package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.InventoryContract;

public class InventoryAdapter extends CursorAdapter {

    InventoryAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.mainview_list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final long productId = Long.parseLong(cursor.getString(cursor.getColumnIndex(InventoryContract.ProductsEntry._ID)));

        view.setTag(productId);

        view.findViewById(R.id.sell).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    context.getContentResolver().update(
                            ContentUris.withAppendedId(InventoryContract.CONTENT_URI_SELL, productId),
                            null, null, null
                    );
                } catch (RuntimeException e) {
                    (Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT)).show();
                }
            }

        });

        view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            try {
                                context.getContentResolver().delete(
                                        ContentUris.withAppendedId(InventoryContract.CONTENT_PRODUCTS, productId),
                                        null, null
                                );

                                context.getContentResolver().notifyChange(InventoryContract.CONTENT_URI_MAIN_VIEW, null);
                            } catch (RuntimeException e) {
                                (Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT)).show();
                            }

                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }

            };

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setMessage(R.string.should_product_be_deleted)
                        .setPositiveButton(R.string.yes, dialogClickListener)
                        .setNegativeButton(R.string.no, dialogClickListener)
                        .show();
            }

        });

        ((TextView) view.findViewById(R.id.product_name)).setText(cursor.getString(cursor.getColumnIndex(InventoryContract.ProductsEntry.COLUMN_NAME_PRODUCT_NAME)));
        ((TextView) view.findViewById(R.id.price)).setText(cursor.getString(cursor.getColumnIndex(InventoryContract.ProductsEntry.COLUMN_NAME_PRICE)));
        ((TextView) view.findViewById(R.id.quantity)).setText(cursor.getString(cursor.getColumnIndex(InventoryContract.SuppliersEntry.COLUMN_NAME_TOTAL_QUANTITY)));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        if (position % 2 == 1) {
            view.setBackgroundColor(Color.WHITE);
        } else {
            view.setBackgroundColor(Color.LTGRAY);
        }

        return view;
    }

}
