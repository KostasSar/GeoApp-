package com.example.android.croudsourcelocations;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by kostas on 10/2/2018.
 */


public class LocationsContentProvider extends ContentProvider {

    private LocationsHelper locHelp;
    private static final int ALL_LOCATIONS = 1;
    private static final int SINGLE_LOCATION = 2;

    private static final String AUTHORITY = "com.example.android.croudsourcelocations.contentprovider";
    private static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/location_table");

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH); // NO-MATCH value = -1
        uriMatcher.addURI(AUTHORITY, "location_table", ALL_LOCATIONS);
        uriMatcher.addURI(AUTHORITY, "location_table/#", SINGLE_LOCATION);
    }


    @Override
    public boolean onCreate() {
        // Gets access to the LocationsHelper
        locHelp = new LocationsHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = locHelp.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(LocationsHelper.TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case ALL_LOCATIONS:
                //do nothing
                break;

            case SINGLE_LOCATION:
                String id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(LocationsHelper.COL_1 + "=" + id);
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ALL_LOCATIONS:
                return "vnd.android.cursor.dir/vnd.com.example.android.croudsourcelocations.contentprovider.location_table";

            case SINGLE_LOCATION:
                return "vnd.android.cursor.item/vnd.com.example.android.croudsourcelocations.contentprovider.location_table";

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = locHelp.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_LOCATIONS:
                //do nothing
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        long id = db.insert(LocationsHelper.TABLE_NAME, null, contentValues);
        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.parse(CONTENT_URI + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = locHelp.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_LOCATIONS:
                //do nothing
                break;

            case SINGLE_LOCATION:
                String id = uri.getPathSegments().get(1);
                selection = LocationsHelper.COL_1 + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        "AND (" + selection + ')' : "");
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        int deleteCount = db.delete(LocationsHelper.TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = locHelp.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_LOCATIONS:
                //do nothing
                break;

            case SINGLE_LOCATION:
                String id = uri.getPathSegments().get(1);
                selection = LocationsHelper.COL_1 + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        "AND (" + selection + ')' : "");
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int updateCount = db.update(LocationsHelper.TABLE_NAME, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }

}
