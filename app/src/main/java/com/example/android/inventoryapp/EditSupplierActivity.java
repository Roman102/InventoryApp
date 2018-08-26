package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.inventoryapp.data.InventoryContract;

public class EditSupplierActivity extends AppCompatActivity {

    private long supplierId;
    private long productId;

    private EditText quantity;
    private EditText supplierName;
    private EditText supplierCountryCode;
    private EditText supplierPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit);

        supplierId = getIntent().getLongExtra(InventoryContract.SuppliersEntry._ID, -1);
        productId = getIntent().getLongExtra(InventoryContract.SuppliersEntry.COLUMN_NAME_PRODUCT_ID, -1);

        supplierName = findViewById(R.id.supplier_name);
        supplierCountryCode = findViewById(R.id.supplier_country_code);
        supplierPhone = findViewById(R.id.supplier_phone);
        quantity = findViewById(R.id.quantity);

        supplierName.setText(getIntent().getStringExtra(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_NAME));
        supplierCountryCode.setText(getIntent().getStringExtra(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_COUNTRY_CODE));
        supplierPhone.setText(getIntent().getStringExtra(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_PHONE));
        quantity.setText(String.valueOf(getIntent().getLongExtra(InventoryContract.SuppliersEntry.COLUMN_NAME_QUANTITY, -1)));
    }

    public void decreaseQuantity(View view) {
        try {
            long newQuantity = Long.parseLong(quantity.getText().toString());

            if (newQuantity > 0) {
                quantity.setText(String.valueOf(newQuantity - 1));
            } else {
                quantity.setText("0");
            }
        } catch (NumberFormatException e) {
            quantity.setText("0");
        }
    }

    public void increaseQuantity(View view) {
        try {
            long newQuantity = Long.parseLong(quantity.getText().toString());

            if (newQuantity >= 0 && newQuantity < Long.MAX_VALUE) {
                quantity.setText(String.valueOf(newQuantity + 1));
            } else {
                quantity.setText("0");
            }
        } catch (NumberFormatException e) {
            quantity.setText("0");
        }
    }

    public void closeScreen(View view) {
        finish();
    }

    public void saveSupplier(View view) {
        ContentValues values = new ContentValues();

        values.put(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_NAME, supplierName.getText().toString());
        values.put(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_COUNTRY_CODE, supplierCountryCode.getText().toString());
        values.put(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_PHONE, supplierPhone.getText().toString());

        try {
            values.put(InventoryContract.SuppliersEntry.COLUMN_NAME_QUANTITY, Integer.parseInt(quantity.getText().toString()));

            getContentResolver().update(ContentUris.withAppendedId(InventoryContract.CONTENT_UPDATE_SUPPLIER, supplierId), values, null, null);

            getContentResolver().notifyChange(ContentUris.withAppendedId(InventoryContract.CONTENT_URI_SUPPLIERS, productId), null);
            getContentResolver().notifyChange(InventoryContract.CONTENT_URI_MAIN_VIEW, null);

            finish();
        } catch (NumberFormatException e) {
            (Toast.makeText(this, R.string.error_quantity_should_be_at_least_zero, Toast.LENGTH_SHORT)).show();
        } catch (RuntimeException e) {
            (Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT)).show();
        }
    }

}
