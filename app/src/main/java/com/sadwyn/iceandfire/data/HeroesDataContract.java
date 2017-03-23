package com.sadwyn.iceandfire.data;

import android.provider.BaseColumns;


public final class HeroesDataContract {
    private HeroesDataContract(){
    };

    public static final class MainDataStructure implements BaseColumns{
        public final static String TABLE_NAME = "heroes";
        // TODO: 23.03.2017  
        private static final String SCHEME = "content://";
        private static final String PATH_HEROES = "/students";
        private static final String PATH_HEROES_ID = "/students/";
        public static final int HEROES_ID_PATH_POSITION = 1;

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
