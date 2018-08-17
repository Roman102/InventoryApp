package com.example.android.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class InventoryProvider extends ContentProvider {

    public static final int MAIN_VIEW = 100;
    public static final int SUPPLIERS = 101;
    public static final int SELL = 102;
    public static final int ADD_PRODUCT = 103;
    public static final int UPDATE_PRODUCT = 104;
    public static final int ADD_SUPPLIER = 105;
    public static final int UPDATE_SUPPLIER = 106;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_MAIN_VIEW, MAIN_VIEW);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_SUPPLIERS + "/#", SUPPLIERS);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_SELL + "/#", SELL);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_ADD_PRODUCT, ADD_PRODUCT);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_UPDATE_PRODUCT + "/#", UPDATE_PRODUCT);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_ADD_SUPPLIER + "/#", ADD_SUPPLIER);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_UPDATE_SUPPLIER + "/#", UPDATE_SUPPLIER);
    }

    private InventoryDbHelper inventoryDbHelper;

    @Override
    public boolean onCreate() {
        inventoryDbHelper = new InventoryDbHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = inventoryDbHelper.getReadableDatabase();

        Cursor cursor = null;

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MAIN_VIEW:
                cursor = database.rawQuery("SELECT a." + InventoryContract.ProductsEntry._ID +
                        ", a." + InventoryContract.ProductsEntry.COLUMN_NAME_PRODUCT_NAME +
                        ", a." + InventoryContract.ProductsEntry.COLUMN_NAME_PRICE +
                        ", SUM(b." + InventoryContract.SuppliersEntry.COLUMN_NAME_QUANTITY +
                        ") FROM " + InventoryContract.ProductsEntry.TABLE_NAME +
                        " a INNER JOIN " + InventoryContract.SuppliersEntry.TABLE_NAME +
                        " b ON a." + InventoryContract.ProductsEntry._ID + "=b." +
                        InventoryContract.SuppliersEntry.COLUMN_NAME_PRODUCT_ID +
                        " GROUP BY a." + InventoryContract.ProductsEntry._ID, null);
                break;
            case SUPPLIERS:
                cursor = database.query(InventoryContract.SuppliersEntry.TABLE_NAME, projection,
                        InventoryContract.SuppliersEntry.COLUMN_NAME_PRODUCT_ID + "=?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MAIN_VIEW:
                return InventoryContract.CONTENT_MAIN_VIEW_TYPE;
            case SUPPLIERS:
                return InventoryContract.CONTENT_SUPPLIERS_ITEM_TYPE;
            case SELL:
                return InventoryContract.CONTENT_SELL_ITEM_TYPE;
            case ADD_PRODUCT:
                return InventoryContract.CONTENT_ADD_PRODUCT_TYPE;
            case UPDATE_PRODUCT:
                return InventoryContract.CONTENT_UPDATE_PRODUCT_ITEM_TYPE;
            case ADD_SUPPLIER:
                return InventoryContract.CONTENT_ADD_SUPPLIER_ITEM_TYPE;
            case UPDATE_SUPPLIER:
                return InventoryContract.CONTENT_UPDATE_SUPPLIER_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

}
