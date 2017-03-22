package com.sadwyn.iceandfire.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HeroesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "game_of_thrones.db";
    private static final int DATABASE_VERSION = 2;

    public HeroesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDB(db);
        updateDBtoSecondVersion(db);
    }

    private void createDB(SQLiteDatabase db) {
        String SQL_CREATE_HEROES_TABLE = "CREATE TABLE " + HeroesDataContract.MainDataStructure.TABLE_NAME + " ("
                + HeroesDataContract.MainDataStructure._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + HeroesDataContract.MainDataStructure.COLUMN_NAME + " TEXT NOT NULL, "
                + HeroesDataContract.MainDataStructure.COLUMN_BORN + " TEXT NOT NULL, "
                + HeroesDataContract.MainDataStructure.COLUMN_KINGDOM + " TEXT NOT NULL, "
                + HeroesDataContract.MainDataStructure.COLUMN_GENDER + " TEXT NOT NULL, "
                + HeroesDataContract.MainDataStructure.COLUMN_FATHER + " TEXT NOT NULL, "
                + HeroesDataContract.MainDataStructure.COLUMN_MOTHER + " TEXT NOT NULL, "
                + HeroesDataContract.MainDataStructure.COLUMN_DEAD + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_HEROES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion == 2){
            updateDBtoSecondVersion(db);
        }
    }

    private void updateDBtoSecondVersion(SQLiteDatabase db) {
        String SQL_CREATE_ALIASES_TABLE = "CREATE TABLE " + HeroesDataContract.AliasesStructure.TABLE_NAME + " ("
                + HeroesDataContract.AliasesStructure._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + HeroesDataContract.AliasesStructure.OUTER_KEY + " INTEGER, "
                + HeroesDataContract.AliasesStructure.COLUMN_NICKNAME + " TEXT);";

        db.execSQL(SQL_CREATE_ALIASES_TABLE);
    }
}
