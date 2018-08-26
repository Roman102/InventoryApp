package com.example.android.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

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
    public void bindView(View view, Context context, Cursor cursor) {
        view.setTag(cursor.getLong(cursor.getColumnIndex(InventoryContract.SuppliersEntry._ID)));

        ((TextView) view.findViewById(R.id.supplier_name)).setText(cursor.getString(cursor.getColumnIndex(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_NAME)));
        ((TextView) view.findViewById(R.id.supplier_country_code)).setText(cursor.getString(cursor.getColumnIndex(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_COUNTRY_CODE)));
        ((TextView) view.findViewById(R.id.supplier_phone)).setText(cursor.getString(cursor.getColumnIndex(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_PHONE)));
        ((TextView) view.findViewById(R.id.quantity)).setText(cursor.getString(cursor.getColumnIndex(InventoryContract.SuppliersEntry.COLUMN_NAME_QUANTITY)));
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
