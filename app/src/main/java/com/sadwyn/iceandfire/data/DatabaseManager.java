package com.sadwyn.iceandfire.data;

import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {

    private int openCounter;
    private static DatabaseManager instance;
    private static HeroesDbHelper dbHelper;
    private SQLiteDatabase database;

    public static synchronized void initializeInstance(HeroesDbHelper helper) {
        if (instance == null) {
            instance = new DatabaseManager();
            dbHelper = helper;
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        openCounter++;
        if (openCounter == 1)
            database = dbHelper.getWritableDatabase();
        return database;
    }

    public synchronized void closeDatabase() {
        openCounter--;
        if (openCounter == 0)
            database.close();
    }
}
