package com.sadwyn.iceandfire.data;

import android.provider.BaseColumns;

/**
 * Created by Kipris on 17.03.2017.
 */

public final class HeroesDataContract {
    private HeroesDataContract(){

    };

    public static final class TableStructure implements BaseColumns{
        public final static String TABLE_NAME = "heroes";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_BORN = "born";
        public final static String COLUMN_KINGDOM = "kingdom";
        public final static String COLUMN_GENDER = "gender";
        public final static String COLUMN_FATHER = "father";
        public final static String COLUMN_MOTHER = "mother";
        public final static String COLUMN_DEAD = "dead";

    }
}
