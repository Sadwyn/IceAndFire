package com.sadwyn.iceandfire.data;

import android.net.Uri;
import android.provider.BaseColumns;


public final class HeroesDataContract {
    public static final String AUTHORITY = "com.sadwyn.iceandfire.provider.contract";
    private static final String SCHEME = "content://";

    private HeroesDataContract(){}

    public static final class MainDataStructure implements BaseColumns{
        public final static String TABLE_NAME = "heroes";

        private static final String PATH_CHARACTERS = "/characters";
        private static final String PATH_CHARACTERS_ID = "/characters/";

        public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + PATH_CHARACTERS);
        public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_CHARACTERS_ID);
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.characters";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.characters";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_BORN = "born";
        public final static String COLUMN_KINGDOM = "kingdom";
        public final static String COLUMN_GENDER = "gender";
        public final static String COLUMN_FATHER = "father";
        public final static String COLUMN_MOTHER = "mother";
        public final static String COLUMN_DEAD = "dead";

        public static final String[] DEFAULT_PROJECTION = new String[] {
                MainDataStructure._ID,
                MainDataStructure.COLUMN_NAME,
                MainDataStructure.COLUMN_BORN,
                MainDataStructure.COLUMN_KINGDOM,
                MainDataStructure.COLUMN_GENDER,
                MainDataStructure.COLUMN_FATHER,
                MainDataStructure.COLUMN_MOTHER,
                MainDataStructure.COLUMN_DEAD
        };


    }
    public static final class AliasesStructure implements BaseColumns{

        public final static String TABLE_NAME = "aliases";

        private static final String PATH_ALIASES = "/aliases";
        private static final String PATH_ALIASES_ID = "/aliases/";



        public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + PATH_ALIASES);
        public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_ALIASES_ID);
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.aliases";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.aliases";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NICKNAME="nickname";
        public final static String OUTER_KEY= "outer";

        public static final String[] DEFAULT_PROJECTION = new String[] {
                AliasesStructure._ID,
                AliasesStructure.COLUMN_NICKNAME,
                AliasesStructure.OUTER_KEY
        };
    }
}
