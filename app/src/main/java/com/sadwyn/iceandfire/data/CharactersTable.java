package com.sadwyn.iceandfire.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sadwyn.iceandfire.models.Character;

import java.util.ArrayList;
import java.util.List;

public class CharactersTable {

    private HeroesDbHelper dbHelper;

    public CharactersTable(Context context) {
        dbHelper = new HeroesDbHelper(context);
    }

    public void saveCharacterToDB(Character character){
        if(!isHeroAlreadySaved(character)) {
        Thread saveCharacter = new Thread(new SaveToDbThread(character, dbHelper));
        saveCharacter.start();
        }
    }

    private boolean isHeroAlreadySaved(Character character) {
        String name = "";
        SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM heroes where name = '"+character.getName()+"'";
        Cursor c = readableDatabase.rawQuery(selectQuery, null);
        if(c.moveToFirst())
            name = c.getString(c.getColumnIndex("name"));
        c.close();
        readableDatabase.close();
        return character.getName().equals(name);
    }

    public List<Character> getCharactersFromDB() {
        List<Character> result = new ArrayList<>();
        SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();

        String[] projection = {
                HeroesDataContract.TableStructure.COLUMN_NAME,
                HeroesDataContract.TableStructure.COLUMN_BORN,
                HeroesDataContract.TableStructure.COLUMN_KINGDOM,
                HeroesDataContract.TableStructure.COLUMN_GENDER,
                HeroesDataContract.TableStructure.COLUMN_FATHER,
                HeroesDataContract.TableStructure.COLUMN_MOTHER,
                HeroesDataContract.TableStructure.COLUMN_DEAD,
        };

        Cursor cursor = readableDatabase.query(
                HeroesDataContract.TableStructure.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        int nameColumnIndex = cursor.getColumnIndex(HeroesDataContract.TableStructure.COLUMN_NAME);
        int bornColumnIndex = cursor.getColumnIndex(HeroesDataContract.TableStructure.COLUMN_BORN);
        int kingdomColumnIndex = cursor.getColumnIndex(HeroesDataContract.TableStructure.COLUMN_KINGDOM);
        int genderColumnIndex = cursor.getColumnIndex(HeroesDataContract.TableStructure.COLUMN_GENDER);
        int fatherColumnIndex = cursor.getColumnIndex(HeroesDataContract.TableStructure.COLUMN_FATHER);
        int motherColumnIndex = cursor.getColumnIndex(HeroesDataContract.TableStructure.COLUMN_MOTHER);
        int deadColumnIndex = cursor.getColumnIndex(HeroesDataContract.TableStructure.COLUMN_DEAD);

        try {
            while (cursor.moveToNext()) {
                String name = cursor.getString(nameColumnIndex);
                String born = cursor.getString(bornColumnIndex);
                String kingdom = cursor.getString(kingdomColumnIndex);
                String gender = cursor.getString(genderColumnIndex);
                String father = cursor.getString(fatherColumnIndex);
                String mother = cursor.getString(motherColumnIndex);
                String dead = cursor.getString(deadColumnIndex);

                Character character = new Character();
                character.setName(name);
                character.setBorn(born);
                character.setCulture(kingdom);
                character.setGender(gender);
                character.setFather(father);
                character.setMother(mother);
                character.setDied(dead);
                result.add(character);
                Log.i("TAG", "name " + character.getName());
            }
        }
        finally {
            cursor.close();
            readableDatabase.close();
        }
        return result;
    }
}
