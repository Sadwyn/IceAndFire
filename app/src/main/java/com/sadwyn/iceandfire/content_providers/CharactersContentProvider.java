package com.sadwyn.iceandfire.content_providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class CharactersContentProvider extends ContentProvider{

    private static final int CHARACTERS = 1;
    private static final int CHARACTERS_ID = 2;
    private static final int ALIASES = 3;
    private static final int ALIASES_ID = 4;

    // TODO: 23.03.2017  
    /*public final static UriMatcher uriMatcher;

    static {
            uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
            uriMatcher.addURI(ContractClass.AUTHORITY, "students", STUDENTS);
            uriMatcher.addURI(ContractClass.AUTHORITY, "students/#", STUDENTS_ID);
            uriMatcher.addURI(ContractClass.AUTHORITY, "classes", CLASSES);
            uriMatcher.addURI(ContractClass.AUTHORITY, "classes/#", CLASSES_ID);

    }*/ 

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
