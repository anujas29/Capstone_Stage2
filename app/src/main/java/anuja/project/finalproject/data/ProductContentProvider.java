package anuja.project.finalproject.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;

/**
 * Created by USER on 19-10-2017.
 */

public class ProductContentProvider extends ContentProvider {

    private Context mContext;
    private static DbHelper mDbHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static final int SALE = 100;
    public static final int SALE_WITH_ID = 101;

    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ProductContract.AUTHORITY, ProductContract.PATH_TASKS, SALE);
        uriMatcher.addURI(ProductContract.AUTHORITY, ProductContract.PATH_TASKS + "/#", SALE_WITH_ID);

        return uriMatcher;
    }

    /*
     * Always return true, indicating that the
     * provider loaded correctly.
     */
    @Override
    public boolean onCreate() {
        mContext = getContext();
        mDbHelper = new DbHelper(mContext);
        return true;
    }

    /*
     * Return no type for MIME type
     */
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case SALE://(DIR) returns multiple rows
                return ProductContract.ProductEntry.CONTENT_TYPE;
            case SALE_WITH_ID:
                return ProductContract.ProductEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    /*
     * query() always returns no results
     *
     */
    @Override
    public Cursor query(
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor returnedCursor;
        switch (sUriMatcher.match(uri)) {

            case SALE: {
                returnedCursor = db.query(ProductContract.ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case SALE_WITH_ID: {
                long _id = Long.valueOf(uri.getLastPathSegment());
                returnedCursor = db.query(ProductContract.ProductEntry.TABLE_NAME, projection, "" + ProductContract.ProductEntry._ID + "=?", new String[]{String.valueOf(_id)}, null, null, sortOrder);
                //the sent id paremerter is actually the index of the clicked mews in the arrayList the holds the movies + 1
                // since the saved news in the database are sorted the same sort of the arrayList that holds the news
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown Uri " + uri);
            }
        }
        returnedCursor.setNotificationUri(mContext.getContentResolver(), uri);//this causes the cursor to register a content observer to watch for changes that happend to that uri
        //this allow the content provider to easily tell the UI when th cursor changes
        return returnedCursor;
    }

    /*
     * insert() always returns null (no URI)
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Uri returnedUri;
        long _id;
        switch (sUriMatcher.match(uri)) {
            case SALE: {
                _id = db.insert(ProductContract.ProductEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnedUri = ProductContract.ProductEntry.getUriWithId(_id);
                } else {
                    throw new SQLiteException("Failed to insert into database");
                }
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown Uri " + uri);
            }

        }
        mContext.getContentResolver().notifyChange(uri, null);
        return returnedUri;
    }

    /*
     * delete() always returns "no rows affected" (0)
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (sUriMatcher.match(uri)) {
            case SALE: {
                rowsDeleted = db.delete(ProductContract.ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case SALE_WITH_ID: {
                long _id = Long.valueOf(uri.getLastPathSegment());
                rowsDeleted = db.delete(ProductContract.ProductEntry.TABLE_NAME,
                        "" + ProductContract.ProductEntry._ID + "= ?", new String[]{String.valueOf(_id)});
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown Uri " + uri);
            }
        }
        mContext.getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    /*
     * update() always returns "no rows affected" (0)
     */
    public int update(
            Uri uri,
            ContentValues values,
            String selection,
            String[] selectionArgs) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rows_affected;
        switch (sUriMatcher.match(uri)) {
            case SALE_WITH_ID: {
                rows_affected = db.update(ProductContract.ProductEntry.TABLE_NAME
                        , values
                        , ProductContract.ProductEntry._ID + " = ?"
                        , new String[]{uri.getLastPathSegment()});
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown Uri " + uri);
            }
        }
        mContext.getContentResolver().notifyChange(uri, null);
        return rows_affected;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case SALE: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(ProductContract.ProductEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                mContext.getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }
}

