package com.example.android.inventoryapp;

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
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.inventoryapp.data.InventoryContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private InventoryAdapter inventoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ListView productListView = findViewById(R.id.product_list);

        inventoryAdapter = new InventoryAdapter(this, null);

        productListView.setEmptyView(findViewById(R.id.empty_view));
        productListView.setAdapter(inventoryAdapter);

        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);

                intent.putExtra(InventoryContract.SuppliersEntry.COLUMN_NAME_PRODUCT_ID, (long) view.getTag());
                intent.putExtra(InventoryContract.ProductsEntry.COLUMN_NAME_PRODUCT_NAME, ((TextView) view.findViewById(R.id.product_name)).getText());
                intent.putExtra(InventoryContract.ProductsEntry.COLUMN_NAME_PRICE, Long.parseLong(((TextView) view.findViewById(R.id.price)).getText().toString()));

                startActivity(intent);
            }

        });

        getSupportLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this,
                InventoryContract.CONTENT_URI_MAIN_VIEW,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        inventoryAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        inventoryAdapter.swapCursor(null);
    }

    public void addProduct(View view) {
        startActivity(new Intent(MainActivity.this, AddProductActivity.class));
    }

}
