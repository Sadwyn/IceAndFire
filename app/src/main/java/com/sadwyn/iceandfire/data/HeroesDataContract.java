package com.sadwyn.iceandfire.data;

import android.provider.BaseColumns;


public final class HeroesDataContract {
    private HeroesDataContract(){
    };

    public static final class MainDataStructure implements BaseColumns{
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
    public static final class AliasesStructure implements BaseColumns{
        public final static String TABLE_NAME = "aliases";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NICKNAME="nickname";
        public final static String OUTER_KEY= "outer";
    }
}
