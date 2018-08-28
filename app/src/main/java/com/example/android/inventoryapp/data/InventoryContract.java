package com.example.android.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class InventoryContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.inventoryapp";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MAIN_VIEW = "mainview";
    public static final String PATH_SUPPLIERS = "suppliers";
    public static final String PATH_SELL = "sell";
    public static final String PATH_ADD_PRODUCT = "addproduct";
    public static final String PATH_UPDATE_PRODUCT = "updateproduct";
    public static final String PATH_ADD_SUPPLIER = "addsupplier";
    public static final String PATH_UPDATE_SUPPLIER = "updatesupplier";
    public static final String PATH_DELETE_ALL = "delete-all";
    public static final String PATH_PRODUCTS = "products";
    public static final String PATH_DELETE_SUPPLIER = "deletesupplier";

    public static final Uri CONTENT_URI_MAIN_VIEW = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MAIN_VIEW);
    public static final Uri CONTENT_URI_SUPPLIERS = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SUPPLIERS);
    public static final Uri CONTENT_URI_SELL = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SELL);
    public static final Uri CONTENT_ADD_PRODUCT = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ADD_PRODUCT);
    public static final Uri CONTENT_UPDATE_PRODUCT = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_UPDATE_PRODUCT);
    public static final Uri CONTENT_ADD_SUPPLIER = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ADD_SUPPLIER);
    public static final Uri CONTENT_UPDATE_SUPPLIER = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_UPDATE_SUPPLIER);
    public static final Uri CONTENT_DELETE_ALL = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_DELETE_ALL);
    public static final Uri CONTENT_PRODUCTS = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);
    public static final Uri CONTENT_DELETE_SUPPLIER = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_DELETE_SUPPLIER);

    public static final String CONTENT_MAIN_VIEW_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MAIN_VIEW;
    public static final String CONTENT_SUPPLIERS_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SUPPLIERS;
    public static final String CONTENT_SELL_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SELL;
    public static final String CONTENT_ADD_PRODUCT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ADD_PRODUCT;
    public static final String CONTENT_UPDATE_PRODUCT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_UPDATE_PRODUCT;
    public static final String CONTENT_ADD_SUPPLIER_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ADD_SUPPLIER;
    public static final String CONTENT_UPDATE_SUPPLIER_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_UPDATE_SUPPLIER;
    public static final String CONTENT_DELETE_ALL_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DELETE_ALL;
    public static final String CONTENT_PRODUCTS_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;
    public static final String CONTENT_DELETE_SUPPLIER_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DELETE_SUPPLIER;

    private InventoryContract() {
    }

    public static class ProductsEntry implements BaseColumns {

        public static final String TABLE_NAME = "products";

        public static final String COLUMN_NAME_PRODUCT_NAME = "product_name";
        public static final String COLUMN_NAME_PRICE = "price";

    }

    public static class SuppliersEntry implements BaseColumns {

        public static final String TABLE_NAME = "suppliers";

        public static final String COLUMN_NAME_SUPPLIER_NAME = "supplier_name";
        public static final String COLUMN_NAME_SUPPLIER_COUNTRY_CODE = "supplier_country_code";
        public static final String COLUMN_NAME_SUPPLIER_PHONE = "supplier_phone";
        public static final String COLUMN_NAME_QUANTITY = "quantity";
        public static final String COLUMN_NAME_PRODUCT_ID = "product_id";

        public static final String COLUMN_NAME_TOTAL_QUANTITY = "total_quantity";

    }

}
