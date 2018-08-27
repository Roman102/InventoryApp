package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.inventoryapp.data.InventoryContract;

public class AddSupplierActivity extends AppCompatActivity {

    private EditText quantity;
    private EditText supplierName;
    private EditText supplierCountryCode;
    private EditText supplierPhone;

    private DialogInterface.OnClickListener dialogClickListener;

    private long productId = -1;
    private boolean gotProductIdFromIntent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_supplier);

        if (savedInstanceState != null && savedInstanceState.containsKey(InventoryContract.SuppliersEntry.COLUMN_NAME_PRODUCT_ID)) {
            productId = savedInstanceState.getLong(InventoryContract.SuppliersEntry.COLUMN_NAME_PRODUCT_ID);
        }

        dialogClickListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        supplierName.setText("");
                        supplierCountryCode.setText("");
                        supplierPhone.setText("");
                        quantity.setText("0");

                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        if (gotProductIdFromIntent) {
                            // Called from DetailsActivity in order to add another supplier for a product
                            // so return there.

                            finish();
                        } else {
                            startActivity(new Intent(AddSupplierActivity.this, MainActivity.class));
                        }

                        break;
                }
            }

        };

        supplierName = findViewById(R.id.supplier_name);
        supplierCountryCode = findViewById(R.id.supplier_country_code);
        supplierPhone = findViewById(R.id.supplier_phone);
        quantity = findViewById(R.id.quantity);

        quantity.setText("0");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(InventoryContract.SuppliersEntry.COLUMN_NAME_PRODUCT_ID, productId);

        super.onSaveInstanceState(outState);
    }

    public void closeScreen(View view) {
        finish();
    }

    public void saveSupplier(View view) {
        try {
            ContentValues values;

            if (productId == -1) {
                gotProductIdFromIntent = getIntent().hasExtra(InventoryContract.SuppliersEntry.COLUMN_NAME_PRODUCT_ID);

                if (gotProductIdFromIntent) {
                    // Called from DetailsActivity in order to add another supplier for a product.

                    productId = getIntent().getLongExtra(InventoryContract.SuppliersEntry.COLUMN_NAME_PRODUCT_ID, -1);
                } else {
                    // Called from AddProductActivity in order to create a new product with at least one supplier.

                    values = new ContentValues();

                    values.put(InventoryContract.ProductsEntry.COLUMN_NAME_PRODUCT_NAME, getIntent().getStringExtra(InventoryContract.ProductsEntry.COLUMN_NAME_PRODUCT_NAME));
                    values.put(InventoryContract.ProductsEntry.COLUMN_NAME_PRICE, getIntent().getLongExtra(InventoryContract.ProductsEntry.COLUMN_NAME_PRICE, -1));

                    productId = ContentUris.parseId(getContentResolver().insert(InventoryContract.CONTENT_ADD_PRODUCT, values));
                }
            }

            values = new ContentValues();

            values.put(InventoryContract.SuppliersEntry.COLUMN_NAME_PRODUCT_ID, productId);

            values.put(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_NAME, supplierName.getText().toString());
            values.put(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_COUNTRY_CODE, supplierCountryCode.getText().toString());
            values.put(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_PHONE, supplierPhone.getText().toString());
            values.put(InventoryContract.SuppliersEntry.COLUMN_NAME_QUANTITY, Long.parseLong(quantity.getText().toString()));

            getContentResolver().insert(InventoryContract.CONTENT_ADD_SUPPLIER, values);

            getContentResolver().notifyChange(ContentUris.withAppendedId(InventoryContract.CONTENT_URI_SUPPLIERS, productId), null);
            getContentResolver().notifyChange(InventoryContract.CONTENT_URI_MAIN_VIEW, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(R.string.add_another_supplier)
                    .setPositiveButton(R.string.yes, dialogClickListener)
                    .setNegativeButton(R.string.no, dialogClickListener)
                    .show();
        } catch (NumberFormatException e) {
            (Toast.makeText(this, R.string.error_quantity_should_be_at_least_zero, Toast.LENGTH_SHORT)).show();
        } catch (RuntimeException e) {
            (Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT)).show();
        }
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

}
