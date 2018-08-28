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

import com.example.android.inventoryapp.R;

public class InventoryProvider extends ContentProvider {

    public static final int MAIN_VIEW = 100;
    public static final int SUPPLIERS = 101;
    public static final int SELL = 102;
    public static final int ADD_PRODUCT = 103;
    public static final int UPDATE_PRODUCT = 104;
    public static final int ADD_SUPPLIER = 105;
    public static final int UPDATE_SUPPLIER = 106;
    public static final int DELETE_ALL = 107;
    public static final int PRODUCTS = 108;
    public static final int DELETE_SUPPLIER = 109;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_MAIN_VIEW, MAIN_VIEW);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_SUPPLIERS + "/#", SUPPLIERS);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_SELL + "/#", SELL);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_ADD_PRODUCT, ADD_PRODUCT);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_UPDATE_PRODUCT + "/#", UPDATE_PRODUCT);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_ADD_SUPPLIER, ADD_SUPPLIER);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_UPDATE_SUPPLIER + "/#", UPDATE_SUPPLIER);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_DELETE_ALL, DELETE_ALL);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_PRODUCTS + "/#", PRODUCTS);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_DELETE_SUPPLIER + "/#", DELETE_SUPPLIER);
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

        switch (sUriMatcher.match(uri)) {
            case MAIN_VIEW:
                cursor = database.rawQuery("SELECT a." + InventoryContract.ProductsEntry._ID +
                        ", a." + InventoryContract.ProductsEntry.COLUMN_NAME_PRODUCT_NAME +
                        ", a." + InventoryContract.ProductsEntry.COLUMN_NAME_PRICE +
                        ", SUM(b." + InventoryContract.SuppliersEntry.COLUMN_NAME_QUANTITY +
                        ") AS " + InventoryContract.SuppliersEntry.COLUMN_NAME_TOTAL_QUANTITY +
                        " FROM " + InventoryContract.ProductsEntry.TABLE_NAME +
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
                throw new IllegalArgumentException(getContext().getResources().getString(R.string.error_unknown_uri, uri));
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
                return InventoryContract.CONTENT_ADD_SUPPLIER_TYPE;
            case UPDATE_SUPPLIER:
                return InventoryContract.CONTENT_UPDATE_SUPPLIER_ITEM_TYPE;
            case DELETE_ALL:
                return InventoryContract.CONTENT_DELETE_ALL_TYPE;
            case PRODUCTS:
                return InventoryContract.CONTENT_PRODUCTS_ITEM_TYPE;
            case DELETE_SUPPLIER:
                return InventoryContract.CONTENT_DELETE_SUPPLIER_ITEM_TYPE;
            default:
                throw new IllegalArgumentException(getContext().getResources().getString(R.string.error_unknown_uri_with_match, uri, match));
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        switch (sUriMatcher.match(uri)) {
            case ADD_PRODUCT:
                return addProduct(uri, contentValues);
            case ADD_SUPPLIER:
                return addSupplier(uri, contentValues);
            default:
                throw new IllegalArgumentException(getContext().getResources().getString(R.string.error_insertion_not_supported_for_uri, uri));
        }
    }

    private Uri addSupplier(Uri uri, ContentValues values) {
        ensureValidSupplierData(values, true);

        SQLiteDatabase database = inventoryDbHelper.getWritableDatabase();

        long id = database.insert(InventoryContract.SuppliersEntry.TABLE_NAME, null, values);

        if (id == -1) {
            throw new RuntimeException(getContext().getResources().getString(R.string.error_failed_to_insert_row_for_uri, uri));
        }

        return ContentUris.withAppendedId(uri, id);
    }

    private void ensureValidSupplierData(ContentValues values, boolean strictCheck) {
        ensureNonemptyNameColumn(values, InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_NAME, R.string.error_supplier_requires_a_name, strictCheck);

        ensureCountryCodeExists(values, strictCheck);

        ensureValidPhoneNumber(values, strictCheck);

        ensureValidQuantity(values, strictCheck);

        ensureValidProductId(values, strictCheck);
    }

    private void ensureValidProductId(ContentValues values, boolean strictCheck) {
        if (values.containsKey(InventoryContract.SuppliersEntry.COLUMN_NAME_PRODUCT_ID)) {
            Integer productId = values.getAsInteger(InventoryContract.SuppliersEntry.COLUMN_NAME_PRODUCT_ID);

            if (productId == null || productId < 1) {
                throw new IllegalArgumentException(getContext().getResources().getString(R.string.error_product_id_should_be_at_least_one));
            }
        } else if (strictCheck) {
            throw new IllegalArgumentException(getContext().getResources().getString(R.string.error_product_id_should_be_at_least_one));
        }
    }

    private void ensureValidQuantity(ContentValues values, boolean strictCheck) {
        if (values.containsKey(InventoryContract.SuppliersEntry.COLUMN_NAME_QUANTITY)) {
            Integer quantity = values.getAsInteger(InventoryContract.SuppliersEntry.COLUMN_NAME_QUANTITY);

            if (quantity == null || quantity < 0) {
                throw new IllegalArgumentException(getContext().getResources().getString(R.string.error_quantity_should_be_at_least_zero));
            }
        } else if (strictCheck) {
            throw new IllegalArgumentException(getContext().getResources().getString(R.string.error_quantity_should_be_at_least_zero));
        }
    }

    private void ensureValidPhoneNumber(ContentValues values, boolean strictCheck) {
        if (values.containsKey(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_PHONE)) {
            String phone = values.getAsString(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_PHONE);

            if (phone == null || phone.trim().length() == 0) {
                throw new IllegalArgumentException(getContext().getResources().getString(R.string.error_phone_number_missing));
            }
        } else if (strictCheck) {
            throw new IllegalArgumentException(getContext().getResources().getString(R.string.error_phone_number_missing));
        }
    }

    @NonNull
    private void ensureCountryCodeExists(ContentValues values, boolean strictCheck) {
        if (values.containsKey(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_COUNTRY_CODE)) {
            String countryCode = values.getAsString(InventoryContract.SuppliersEntry.COLUMN_NAME_SUPPLIER_COUNTRY_CODE);

            if (countryCode == null) {
                throw new IllegalArgumentException(getContext().getResources().getString(R.string.error_country_code_missing));
            }

            countryCode = countryCode.trim();

            if (!(countryCode.length() == 2 || countryCode.length() == 3)) {
                throw new IllegalArgumentException(getContext().getResources().getString(R.string.error_country_code_missing));
            }
        } else if (strictCheck) {
            throw new IllegalArgumentException(getContext().getResources().getString(R.string.error_country_code_missing));
        }
    }

    private void ensureNonemptyNameColumn(ContentValues values, String columnName, int errorMessage, boolean strictCheck) {
        if (values.containsKey(columnName)) {
            String name = values.getAsString(columnName);

            if (name == null || name.trim().length() == 0) {
                throw new IllegalArgumentException(getContext().getResources().getString(errorMessage));
            }
        } else if (strictCheck) {
            throw new IllegalArgumentException(getContext().getResources().getString(errorMessage));
        }
    }

    private Uri addProduct(Uri uri, ContentValues values) {
        ensureNonemptyNameColumn(values, InventoryContract.ProductsEntry.COLUMN_NAME_PRODUCT_NAME, R.string.error_product_requires_a_name, true);

        ensureValidPrice(values, true);

        SQLiteDatabase database = inventoryDbHelper.getWritableDatabase();

        long id = database.insert(InventoryContract.ProductsEntry.TABLE_NAME, null, values);

        if (id == -1) {
            throw new RuntimeException(getContext().getResources().getString(R.string.error_failed_to_insert_row_for_uri, uri));
        }

        return ContentUris.withAppendedId(uri, id);
    }

    private void ensureValidPrice(ContentValues values, boolean strictCheck) {
        if (values.containsKey(InventoryContract.ProductsEntry.COLUMN_NAME_PRICE)) {
            Integer price = values.getAsInteger(InventoryContract.ProductsEntry.COLUMN_NAME_PRICE);

            if (price == null || price < 1) {
                throw new IllegalArgumentException(getContext().getResources().getString(R.string.error_price_should_be_at_least_one));
            }
        } else if (strictCheck) {
            throw new IllegalArgumentException(getContext().getResources().getString(R.string.error_price_should_be_at_least_one));
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = inventoryDbHelper.getWritableDatabase();

        int rowsDeleted;

        switch (sUriMatcher.match(uri)) {
            case DELETE_ALL:
                rowsDeleted = database.delete(InventoryContract.SuppliersEntry.TABLE_NAME, null, null);
                rowsDeleted += database.delete(InventoryContract.ProductsEntry.TABLE_NAME, null, null);

                break;
            case PRODUCTS:
                rowsDeleted = database.delete(InventoryContract.SuppliersEntry.TABLE_NAME,
                        InventoryContract.SuppliersEntry.COLUMN_NAME_PRODUCT_ID + "=?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))}
                );

                rowsDeleted += database.delete(InventoryContract.ProductsEntry.TABLE_NAME,
                        InventoryContract.ProductsEntry._ID + "=?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))}
                );

                break;
            case DELETE_SUPPLIER:
                rowsDeleted = database.delete(InventoryContract.SuppliersEntry.TABLE_NAME,
                        InventoryContract.SuppliersEntry._ID + "=?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))}
                );

                break;
            default:
                throw new IllegalArgumentException(getContext().getResources().getString(R.string.error_deletion_is_not_supported_for_uri, uri));
        }

        if (rowsDeleted <= 0) {
            throw new IllegalArgumentException(getContext().getResources().getString(R.string.error_nothing_was_deleted));
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = inventoryDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case SELL:
                return sellOneProductItem(uri, database);
            case UPDATE_PRODUCT:
                return updateProduct(uri, values, database);
            case UPDATE_SUPPLIER:
                ensureValidSupplierData(values, false);

                if (values.size() == 0) {
                    return 0;
                }

                int rowsUpdated = database.update(InventoryContract.SuppliersEntry.TABLE_NAME, values,
                        InventoryContract.SuppliersEntry._ID + "=?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});

                if (rowsUpdated <= 0) {
                    throw new IllegalArgumentException(getContext().getResources().getString(R.string.error_nothing_updated));
                }

                return rowsUpdated;
            default:
                throw new IllegalArgumentException(getContext().getResources().getString(R.string.error_update_is_not_supported_for_uri, uri));
        }
    }

    private int updateProduct(@NonNull Uri uri, @Nullable ContentValues values, SQLiteDatabase database) {
        ensureNonemptyNameColumn(values, InventoryContract.ProductsEntry.COLUMN_NAME_PRODUCT_NAME, R.string.error_product_requires_a_name, false);

        ensureValidPrice(values, false);

        if (values.size() == 0) {
            return 0;
        }

        int rowsUpdated = database.update(InventoryContract.ProductsEntry.TABLE_NAME, values, InventoryContract.ProductsEntry._ID + "=?", new String[]{String.valueOf(ContentUris.parseId(uri))});

        if (rowsUpdated <= 0) {
            throw new IllegalArgumentException(getContext().getResources().getString(R.string.error_nothing_updated));
        }

        return rowsUpdated;
    }

    private int sellOneProductItem(@NonNull Uri uri, SQLiteDatabase database) {
        Cursor cursor = database.rawQuery(
                "SELECT " + InventoryContract.SuppliersEntry._ID + ", " + InventoryContract.SuppliersEntry.COLUMN_NAME_QUANTITY +
                        " FROM " + InventoryContract.SuppliersEntry.TABLE_NAME +
                        " WHERE " + InventoryContract.SuppliersEntry.COLUMN_NAME_PRODUCT_ID + "=? AND " +
                        InventoryContract.SuppliersEntry.COLUMN_NAME_QUANTITY + " > 0 LIMIT 1",
                new String[]{String.valueOf(ContentUris.parseId(uri))});

        int rowsUpdated = 0;

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();

            ContentValues values = new ContentValues();

            values.put(InventoryContract.SuppliersEntry.COLUMN_NAME_QUANTITY,
                    cursor.getLong(cursor.getColumnIndexOrThrow(InventoryContract.SuppliersEntry.COLUMN_NAME_QUANTITY)) - 1);

            rowsUpdated = database.update(InventoryContract.SuppliersEntry.TABLE_NAME, values,
                    InventoryContract.SuppliersEntry._ID + "=?",
                    new String[]{String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(InventoryContract.SuppliersEntry._ID)))});

            cursor.close();
        } else {
            cursor.close();

            throw new IllegalArgumentException(getContext().getResources().getString(R.string.error_suppliers_dont_have_product));
        }

        if (rowsUpdated == 1) {
            getContext().getContentResolver().notifyChange(InventoryContract.CONTENT_URI_MAIN_VIEW, null);
        } else {
            throw new RuntimeException(getContext().getResources().getString(R.string.error_unable_to_sell_one_product_item_for_uri, uri));
        }

        return rowsUpdated;
    }

}
