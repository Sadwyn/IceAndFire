package com.sadwyn.iceandfire.content_providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sadwyn.iceandfire.data.HeroesDataContract;
import com.sadwyn.iceandfire.data.HeroesDataContract.AliasesStructure;
import com.sadwyn.iceandfire.data.HeroesDataContract.MainDataStructure;
import com.sadwyn.iceandfire.data.HeroesDbHelper;

public class CharactersContentProvider extends ContentProvider {

    private static final int CHARACTERS = 1;
    private static final int CHARACTERS_ID = 2;
    private static final int ALIASES = 3;
    private static final int ALIASES_ID = 4;


    public final static UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(HeroesDataContract.AUTHORITY, "characters", CHARACTERS);
        uriMatcher.addURI(HeroesDataContract.AUTHORITY, "characters/#", CHARACTERS_ID);
        uriMatcher.addURI(HeroesDataContract.AUTHORITY, "aliases", ALIASES);
        uriMatcher.addURI(HeroesDataContract.AUTHORITY, "aliases/#", ALIASES_ID);
    }

    private HeroesDbHelper heroesDbHelper;

    @Override
    public boolean onCreate() {
        heroesDbHelper = new HeroesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = heroesDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = uriMatcher.match(uri);

        switch (match) {
            case CHARACTERS : {
                cursor = database.query
                        (MainDataStructure.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case CHARACTERS_ID : {
                selection = MainDataStructure._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(MainDataStructure.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case ALIASES : {
                cursor = database.query
                        (AliasesStructure.TABLE_NAME, projection, selection, selectionArgs, null, null,sortOrder);
                break;
            }
            case ALIASES_ID : {
                selection = AliasesStructure._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(AliasesStructure.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            }
            default:
                throw new IllegalArgumentException("Cannot process request with this uri" + uri);

        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match){
            case CHARACTERS : return MainDataStructure.CONTENT_TYPE;
            case CHARACTERS_ID : return MainDataStructure.CONTENT_ITEM_TYPE;
            case ALIASES : return  AliasesStructure.CONTENT_TYPE;
            case ALIASES_ID : return AliasesStructure.CONTENT_ITEM_TYPE;
            default: throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
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
