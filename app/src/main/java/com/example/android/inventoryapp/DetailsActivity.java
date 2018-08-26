package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.inventoryapp.data.InventoryContract;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private DetailsAdapter detailsAdapter;

    private Long productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);

        productId = getIntent().getLongExtra(InventoryContract.SuppliersEntry.COLUMN_NAME_PRODUCT_ID, -1);

        ((EditText) findViewById(R.id.product_name)).setText(getIntent().getStringExtra(InventoryContract.ProductsEntry.COLUMN_NAME_PRODUCT_NAME));
        ((EditText) findViewById(R.id.price)).setText(String.valueOf(getIntent().getLongExtra(InventoryContract.ProductsEntry.COLUMN_NAME_PRICE, -1)));

        detailsAdapter = new DetailsAdapter(this, null);

        ListView suppliersListView = findViewById(R.id.suppliers_list);

        suppliersListView.setAdapter(detailsAdapter);

        suppliersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(DetailsActivity.this, EditSupplierActivity.class);

                intent.putExtra(InventoryContract.SuppliersEntry._ID, (long) view.getTag());

                intent.putExtra(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_NAME, ((TextView) view.findViewById(R.id.supplier_name)).getText());
                intent.putExtra(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_COUNTRY_CODE, ((TextView) view.findViewById(R.id.supplier_country_code)).getText());
                intent.putExtra(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_PHONE, ((TextView) view.findViewById(R.id.supplier_phone)).getText());
                intent.putExtra(InventoryContract.SuppliersEntry.COLUMN_NAME_QUANTITY, Long.parseLong(((TextView) view.findViewById(R.id.quantity)).getText().toString()));

                intent.putExtra(InventoryContract.SuppliersEntry.COLUMN_NAME_PRODUCT_ID, productId);

                startActivity(intent);
            }

        });

        getSupportLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this,
                ContentUris.withAppendedId(InventoryContract.CONTENT_URI_SUPPLIERS, productId),
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        detailsAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        detailsAdapter.swapCursor(null);
    }

    public void closeScreen(View view) {
        finish();
    }

}
