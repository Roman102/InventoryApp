package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.InventoryContract;

public class DetailsAdapter extends CursorAdapter {

    DetailsAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.detailsview_supplier_item, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        view.setTag(cursor.getLong(cursor.getColumnIndex(InventoryContract.SuppliersEntry._ID)));

        final TextView supplierPhone = view.findViewById(R.id.supplier_phone);

        ((TextView) view.findViewById(R.id.supplier_name)).setText(cursor.getString(cursor.getColumnIndex(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_NAME)));
        ((TextView) view.findViewById(R.id.supplier_country_code)).setText(cursor.getString(cursor.getColumnIndex(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_COUNTRY_CODE)));
        supplierPhone.setText(cursor.getString(cursor.getColumnIndex(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_PHONE)));
        ((TextView) view.findViewById(R.id.quantity)).setText(cursor.getString(cursor.getColumnIndex(InventoryContract.SuppliersEntry.COLUMN_NAME_QUANTITY)));

        view.findViewById(R.id.delete_supplier).setOnClickListener(new View.OnClickListener() {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            if (cursor.getCount() > 1) {
                                try {
                                    context.getContentResolver().delete(ContentUris.withAppendedId(InventoryContract.CONTENT_DELETE_SUPPLIER,
                                            (long) view.getTag()), null, null);

                                    context.getContentResolver().notifyChange(ContentUris.withAppendedId(InventoryContract.CONTENT_URI_SUPPLIERS,
                                            cursor.getLong(cursor.getColumnIndex(InventoryContract.SuppliersEntry.COLUMN_NAME_PRODUCT_ID))), null);

                                    context.getContentResolver().notifyChange(InventoryContract.CONTENT_URI_MAIN_VIEW, null);
                                } catch (RuntimeException e) {
                                    (Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT)).show();
                                }
                            } else {
                                (Toast.makeText(context, R.string.error_at_least_one_supplier_needed, Toast.LENGTH_SHORT)).show();
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

                builder.setMessage(R.string.should_supplier_be_deleted)
                        .setPositiveButton(R.string.yes, dialogClickListener)
                        .setNegativeButton(R.string.no, dialogClickListener)
                        .show();
            }

        });

        view.findViewById(R.id.order).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(Intent.ACTION_DIAL,
                        Uri.fromParts("tel", supplierPhone.getText().toString(), null)));
            }

        });
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
