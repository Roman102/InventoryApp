package com.example.android.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.inventoryapp.data.InventoryContract;

public class AddProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_product);
    }

    public void closeScreen(View view) {
        finish();
    }

    public void createNewSupplier(View view) {
        Intent intent = new Intent(AddProductActivity.this, AddSupplierActivity.class);

        intent.putExtra(InventoryContract.ProductsEntry.COLUMN_NAME_PRODUCT_NAME, ((EditText) findViewById(R.id.product_name)).getText().toString());

        try {
            intent.putExtra(InventoryContract.ProductsEntry.COLUMN_NAME_PRICE, Long.parseLong(((EditText) findViewById(R.id.price)).getText().toString()));

            startActivity(intent);
        } catch (NumberFormatException e) {
            (Toast.makeText(this, R.string.error_price_too_large_or_invalid_number, Toast.LENGTH_SHORT)).show();
        }
    }

}
