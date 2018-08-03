package com.example.android.inventoryapp;

import android.provider.BaseColumns;

public final class InventoryContract {

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
        public static final String COLUMN_NAME_SUPPLIER_PHONE = "supplier_phone";
        public static final String COLUMN_NAME_QUANTITY = "quantity";
        public static final String COLUMN_NAME_PRODUCT_ID = "product_id";

    }

}
