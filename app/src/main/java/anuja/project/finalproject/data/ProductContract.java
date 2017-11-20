package anuja.project.finalproject.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by USER on 19-10-2017.
 */

public class ProductContract {

    public static final String AUTHORITY = "com.anuja.finalproject";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_TASKS = "sale";

    public static final class ProductEntry implements BaseColumns {

        public static final String TABLE_NAME = "sale";


        public static Uri getUriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

        public static Uri buildSaleUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_NAME;


        public static final String COLUMN_UUID = "uuid";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_SITE = "site";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_LASTCHANGE = "lastchange";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_CURRENCY = "currency";
        public static final String COLUMN_OFFER = "offer";
        public static final String COLUMN_IMAGE = "image";

        public static final String COLUMN_GLOBAL_SALE = "global";
        public static final String COLUMN_LOCAL_SALE = "local";
        public static final String COLUMN_FAV = "fav";

        public static final String[] SALE_COLUMNS = {
                _ID,
                COLUMN_UUID,
                COLUMN_URL,
                COLUMN_SITE,
                COLUMN_TITLE,
                COLUMN_NAME,
                COLUMN_DESCRIPTION,
                COLUMN_LASTCHANGE,
                COLUMN_PRICE,
                COLUMN_CURRENCY,
                COLUMN_OFFER,
                COLUMN_IMAGE,
                COLUMN_GLOBAL_SALE,
                COLUMN_LOCAL_SALE,
                COLUMN_FAV
        };

        public static final String[] WIDGET_COLUMNS = {
                _ID,
                COLUMN_TITLE
        };

    }

    public static final String SQL_CREATE_SALE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            ProductEntry.TABLE_NAME + "(" +
            ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            ProductEntry.COLUMN_UUID + " TEXT NOT NULL UNIQUE, " +
            ProductEntry.COLUMN_URL + " TEXT NOT NULL," +
            ProductEntry.COLUMN_SITE + " TEXT NOT NULL," +
            ProductEntry.COLUMN_TITLE + " TEXT NOT NULL," +
            ProductEntry.COLUMN_NAME + " TEXT NOT NULL," +
            ProductEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL," +
            ProductEntry.COLUMN_LASTCHANGE + " TEXT NOT NULL," +
            ProductEntry.COLUMN_PRICE + " TEXT NOT NULL," +
            ProductEntry.COLUMN_CURRENCY + " TEXT NOT NULL," +
            ProductEntry.COLUMN_OFFER + " TEXT NOT NULL," +
            ProductEntry.COLUMN_IMAGE + " TEXT NULL, " +
            ProductEntry.COLUMN_GLOBAL_SALE + " INTEGER DEFAULT 0, " +
            ProductEntry.COLUMN_LOCAL_SALE + " INTEGER DEFAULT 0, " +
            ProductEntry.COLUMN_FAV + " INTEGER DEFAULT 0" +
            ");";

}
