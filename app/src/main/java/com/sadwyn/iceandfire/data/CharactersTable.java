package com.sadwyn.iceandfire.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sadwyn.iceandfire.models.Character;
import com.sadwyn.iceandfire.views.widgets.CharacterWidget;

import java.util.ArrayList;
import java.util.List;

public class CharactersTable {

    private HeroesDbHelper dbHelper;
    private Context context;

    public CharactersTable(Context context) {
        dbHelper = new HeroesDbHelper(context);
        this.context = context;
    }

    public void saveCharacterToDB(Character character){
        if(!isHeroAlreadySaved(character)) {
        Thread saveCharacter = new Thread(new SaveToDbThread(character, dbHelper));
        saveCharacter.start();
        CharacterWidget.updateWidget(context.getApplicationContext());
        }
    }

    public boolean deleteCharacterByName(String name){
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        writableDatabase.delete("heroes", "name" + "='" + name + "'", null);
        writableDatabase.delete("aliases", "outer" + "='" + name +"'", null);
        writableDatabase.close();
        return true;
    }

    private boolean isHeroAlreadySaved(Character character) {
        String name = "";
        SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM heroes WHERE name = '"+character.getName()+"'";
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

        Cursor cursor = readableDatabase.query(
                HeroesDataContract.MainDataStructure.TABLE_NAME,
                HeroesDataContract.MainDataStructure.DEFAULT_PROJECTION,
                null,
                null,
                null,
                null,
                null);

        int idColumnIndex = cursor.getColumnIndex(HeroesDataContract.MainDataStructure._ID);
        int nameColumnIndex = cursor.getColumnIndex(HeroesDataContract.MainDataStructure.COLUMN_NAME);
        int bornColumnIndex = cursor.getColumnIndex(HeroesDataContract.MainDataStructure.COLUMN_BORN);
        int kingdomColumnIndex = cursor.getColumnIndex(HeroesDataContract.MainDataStructure.COLUMN_KINGDOM);
        int genderColumnIndex = cursor.getColumnIndex(HeroesDataContract.MainDataStructure.COLUMN_GENDER);
        int fatherColumnIndex = cursor.getColumnIndex(HeroesDataContract.MainDataStructure.COLUMN_FATHER);
        int motherColumnIndex = cursor.getColumnIndex(HeroesDataContract.MainDataStructure.COLUMN_MOTHER);
        int deadColumnIndex = cursor.getColumnIndex(HeroesDataContract.MainDataStructure.COLUMN_DEAD);

        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(idColumnIndex);
                String name = cursor.getString(nameColumnIndex);
                String born = cursor.getString(bornColumnIndex);
                String kingdom = cursor.getString(kingdomColumnIndex);
                String gender = cursor.getString(genderColumnIndex);
                String father = cursor.getString(fatherColumnIndex);
                String mother = cursor.getString(motherColumnIndex);
                String dead = cursor.getString(deadColumnIndex);

                ArrayList<String> aliases = new ArrayList<>();
                String selectQuery = "SELECT * FROM aliases WHERE outer = '"+name+"'";
                Cursor c = readableDatabase.rawQuery(selectQuery, null);
                while (c.moveToNext())
                    aliases.add(c.getString(c.getColumnIndex("nickname")));
                c.close();

                Character character = new Character();

                character.setName(name);
                character.setBorn(born);
                character.setCulture(kingdom);
                character.setGender(gender);
                character.setFather(father);
                character.setMother(mother);
                character.setDied(dead);
                character.setAliases(aliases);

                result.add(character);
            }
        }
        finally {
            cursor.close();
            readableDatabase.close();
        }
        return result;
    }
}
