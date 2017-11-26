package org.ipforsmartobjects.apps.volumekeys.data.local;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Hamid on 5/4/2017.
 */

public class WidgetColorsProvider extends ContentProvider {

    private static final String TAG = WidgetColorsProvider.class.getSimpleName();

    private static final int COLORS = 200; // can be used later if we are providing all colors
    private static final int COLORS_WITH_ID = 201;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(WidgetColorsPersistenceContract.CONTENT_AUTHORITY,
                WidgetColorsPersistenceContract.TABLE_COLORS,
                COLORS);

        sUriMatcher.addURI(WidgetColorsPersistenceContract.CONTENT_AUTHORITY,
                WidgetColorsPersistenceContract.TABLE_COLORS + "/#",
                COLORS_WITH_ID);
    }

    private WidgetColorsDBHelper mWidgetColorsDBHelper;

    @Override
    public boolean onCreate() {
        mWidgetColorsDBHelper = new WidgetColorsDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // get readable database access
        final SQLiteDatabase db = mWidgetColorsDBHelper.getReadableDatabase();

        // Match URI
        int match = sUriMatcher.match(uri);

        // query

        Cursor rtCursor;

        switch (match) {
            case COLORS:
                rtCursor = db.query(WidgetColorsPersistenceContract.TABLE_COLORS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;
            case COLORS_WITH_ID:

                // selection and args
                // uri

                final int INDEX_OF_ID_TOKEN = 1;
                String id = uri.getPathSegments().get(INDEX_OF_ID_TOKEN);
                String _selection = "_id=?";
                String[] _selectionArgs = new String[]{id};

                rtCursor = db.query(WidgetColorsPersistenceContract.TABLE_COLORS,
                        projection,
                        _selection,
                        _selectionArgs,
                        null,
                        null,
                        null);

                break;
            default:
                throw new UnsupportedOperationException("Unknown URI : " + uri);
        }

        // notification
        ContentResolver resolver = getContext().getContentResolver();
        if (rtCursor != null && resolver != null) {
            rtCursor.setNotificationUri(resolver, uri);
        }

        return rtCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        // get database access
        final SQLiteDatabase db = mWidgetColorsDBHelper.getWritableDatabase();

        // Match URI
        int match = sUriMatcher.match(uri);

        Uri returnUri;
        switch (match) {
            case COLORS:
                long id = db.insert(WidgetColorsPersistenceContract.TABLE_COLORS, null, values);
                if (id > 0) {
                    // insert successful
                    returnUri = ContentUris.withAppendedId(WidgetColorsPersistenceContract.CONTENT_URI, id);
                } else {
                    // failed
                    throw new SQLException("Failed to insert into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown URI : " + uri);
        }

        // notify change
        ContentResolver resolver = getContext().getContentResolver();
        if (resolver != null) {
            resolver.notifyChange(uri, null);
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // get database access
        final SQLiteDatabase db = mWidgetColorsDBHelper.getWritableDatabase();

        // Match URI
        int match = sUriMatcher.match(uri);

        int noOfDeletedRows;
        switch (match) {
            case COLORS:
                noOfDeletedRows = db.delete(WidgetColorsPersistenceContract.TABLE_COLORS, selection, selectionArgs);
                if (noOfDeletedRows == 0) {
                    throw new SQLException("Failed to delete " + uri);
                }
                break;
            case COLORS_WITH_ID:
                final int INDEX_OF_ID_TOKEN = 1;
                String id = uri.getPathSegments().get(INDEX_OF_ID_TOKEN);
                String _selection = "_id=?";
                String[] _selectionArgs = new String[]{id};

                noOfDeletedRows = db.delete(WidgetColorsPersistenceContract.TABLE_COLORS, _selection, _selectionArgs);
                if (noOfDeletedRows == 0) {
                    throw new SQLException("Failed to delete " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown URI : " + uri);
        }

        // notify change on id
        ContentResolver resolver = getContext().getContentResolver();
        if (resolver != null) {
            resolver.notifyChange(uri, null);
//            if (match == COLORS_WITH_ID) {
//                // in case of uri with id, notifying the main uri too
//                resolver.notifyChange(WidgetColorsPersistenceContract.CONTENT_URI, null);
//            }
        }
        return noOfDeletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("This provider does not support update");
    }
}
