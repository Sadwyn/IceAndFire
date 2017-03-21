package com.sadwyn.iceandfire.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HeroesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "game_of_thrones.db";
    private static final int DATABASE_VERSION = 1;

    public HeroesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_HEROES_TABLE = "CREATE TABLE " + HeroesDataContract.TableStructure.TABLE_NAME + " ("
                + HeroesDataContract.TableStructure._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + HeroesDataContract.TableStructure.COLUMN_NAME + " TEXT NOT NULL, "
                + HeroesDataContract.TableStructure.COLUMN_BORN + " TEXT NOT NULL, "
                + HeroesDataContract.TableStructure.COLUMN_KINGDOM + " TEXT NOT NULL, "
                + HeroesDataContract.TableStructure.COLUMN_GENDER + " TEXT NOT NULL, "
                + HeroesDataContract.TableStructure.COLUMN_FATHER + " TEXT NOT NULL, "
                + HeroesDataContract.TableStructure.COLUMN_MOTHER + " TEXT NOT NULL, "
                + HeroesDataContract.TableStructure.COLUMN_DEAD + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_HEROES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
