package com.sadwyn.iceandfire.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.sadwyn.iceandfire.models.Character;

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
        values.put(HeroesDataContract.TableStructure.COLUMN_NAME, character.getName());
        values.put(HeroesDataContract.TableStructure.COLUMN_BORN, character.getBorn());
        values.put(HeroesDataContract.TableStructure.COLUMN_KINGDOM, character.getCulture());
        values.put(HeroesDataContract.TableStructure.COLUMN_GENDER, character.getGender());
        values.put(HeroesDataContract.TableStructure.COLUMN_FATHER, character.getFather());
        values.put(HeroesDataContract.TableStructure.COLUMN_MOTHER, character.getMother());
        values.put(HeroesDataContract.TableStructure.COLUMN_DEAD, character.getDied());

        int id = (int) writableDatabase.insert(HeroesDataContract.TableStructure.TABLE_NAME, null, values);
        
        writableDatabase.close();
    }
}
