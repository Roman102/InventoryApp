package com.example.android.inventoryapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.android.inventoryapp.data.InventoryContract;

public class EditSupplierActivity extends AppCompatActivity {

    private long supplierId;
    private long productId;

    private EditText quantityPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit);

        supplierId = getIntent().getLongExtra(InventoryContract.SuppliersEntry._ID, -1);
        productId = getIntent().getLongExtra(InventoryContract.SuppliersEntry.COLUMN_NAME_PRODUCT_ID, -1);

        ((EditText) findViewById(R.id.supplier_name)).setText(getIntent().getStringExtra(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_NAME));
        ((EditText) findViewById(R.id.supplier_country_code)).setText(getIntent().getStringExtra(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_COUNTRY_CODE));
        ((EditText) findViewById(R.id.supplier_phone)).setText(getIntent().getStringExtra(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_PHONE));

        quantityPicker = findViewById(R.id.quantity);
        quantityPicker.setText(String.valueOf(getIntent().getLongExtra(InventoryContract.SuppliersEntry.COLUMN_NAME_QUANTITY, -1)));
    }

    public void decreaseQuantity(View view) {
        try {
            long newQuantity = Long.parseLong(quantityPicker.getText().toString());

            if (newQuantity > 0) {
                quantityPicker.setText(String.valueOf(newQuantity - 1));
            } else {
                quantityPicker.setText("0");
            }
        } catch (NumberFormatException e) {
            quantityPicker.setText("0");
        }
    }

    public void increaseQuantity(View view) {
        try {
            long newQuantity = Long.parseLong(quantityPicker.getText().toString());

            if (newQuantity >= 0 && newQuantity < Long.MAX_VALUE) {
                quantityPicker.setText(String.valueOf(newQuantity + 1));
            } else {
                quantityPicker.setText("0");
            }
        } catch (NumberFormatException e) {
            quantityPicker.setText("0");
        }
    }

}
