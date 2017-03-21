package com.sadwyn.iceandfire.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.sadwyn.iceandfire.models.Character;

import static com.sadwyn.iceandfire.data.HeroesDataContract.MainDataStructure.COLUMN_BORN;
import static com.sadwyn.iceandfire.data.HeroesDataContract.MainDataStructure.COLUMN_DEAD;
import static com.sadwyn.iceandfire.data.HeroesDataContract.MainDataStructure.COLUMN_FATHER;
import static com.sadwyn.iceandfire.data.HeroesDataContract.MainDataStructure.COLUMN_GENDER;
import static com.sadwyn.iceandfire.data.HeroesDataContract.MainDataStructure.COLUMN_KINGDOM;
import static com.sadwyn.iceandfire.data.HeroesDataContract.MainDataStructure.COLUMN_MOTHER;
import static com.sadwyn.iceandfire.data.HeroesDataContract.MainDataStructure.COLUMN_NAME;

public class SaveToDbThread implements Runnable {

    private Character character;
    private HeroesDbHelper dbHelper;

    public SaveToDbThread(Character character, HeroesDbHelper dbHelper) {
        this.character = character;
        this.dbHelper = dbHelper;
    }

    @Override
    public void run() {
        insertCharacter(character);
    }

    private void insertCharacter(Character character) {
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, character.getName());
        values.put(COLUMN_BORN, character.getBorn());
        values.put(COLUMN_KINGDOM, character.getCulture());
        values.put(COLUMN_GENDER, character.getGender());
        values.put(COLUMN_FATHER, character.getFather());
        values.put(COLUMN_MOTHER, character.getMother());
        values.put(COLUMN_DEAD, character.getDied());

        int id = (int) writableDatabase.insert(HeroesDataContract.MainDataStructure.TABLE_NAME, null, values);

        ContentValues aliases = new ContentValues();
        for (String alias : character.getAliases()) {
            aliases.put(HeroesDataContract.AliasesStructure.COLUMN_NICKNAME, alias);
            aliases.put(HeroesDataContract.AliasesStructure.OUTER_KEY, id);
            writableDatabase.insert(HeroesDataContract.AliasesStructure.TABLE_NAME, null, aliases);
        }
        writableDatabase.close();
    }
}
