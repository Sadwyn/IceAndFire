package com.sadwyn.iceandfire.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sadwyn.iceandfire.models.Character;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;

import static com.sadwyn.iceandfire.data.HeroesDataContract.MainDataStructure.COLUMN_BORN;
import static com.sadwyn.iceandfire.data.HeroesDataContract.MainDataStructure.COLUMN_DEAD;
import static com.sadwyn.iceandfire.data.HeroesDataContract.MainDataStructure.COLUMN_FATHER;
import static com.sadwyn.iceandfire.data.HeroesDataContract.MainDataStructure.COLUMN_GENDER;
import static com.sadwyn.iceandfire.data.HeroesDataContract.MainDataStructure.COLUMN_KINGDOM;
import static com.sadwyn.iceandfire.data.HeroesDataContract.MainDataStructure.COLUMN_MOTHER;
import static com.sadwyn.iceandfire.data.HeroesDataContract.MainDataStructure.COLUMN_NAME;

public class CharactersTable {


    private int saveCharacterToDb(Character character) {
        makeTransaction(character);
        return 0;
    }

    public Observable<Integer> getInsertObservable(Character character) {
        return Observable.defer(() -> Observable.just(saveCharacterToDb(character)));
    }

    public synchronized Observable<Integer> getInsertListObservable(List<Character> list) {
        return Observable.defer(() -> Observable.just(saveCharactersListToDb(list)));
    }

    public void makeTransaction(Character value) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, value.getName());
        values.put(COLUMN_BORN, value.getBorn());
        values.put(COLUMN_KINGDOM, value.getCulture());
        values.put(COLUMN_GENDER, value.getGender());
        values.put(COLUMN_FATHER, value.getFather());
        values.put(COLUMN_MOTHER, value.getMother());
        values.put(COLUMN_DEAD, value.getDied());

        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        database.insert(HeroesDataContract.MainDataStructure.TABLE_NAME, null, values);

        ContentValues aliases = new ContentValues();
        for (String alias : value.getAliases()) {
            aliases.put(HeroesDataContract.AliasesStructure.COLUMN_NICKNAME, alias);
            aliases.put(HeroesDataContract.AliasesStructure.OUTER_KEY, value.getName());
            database.insert(HeroesDataContract.AliasesStructure.TABLE_NAME, null, aliases);
        }
        DatabaseManager.getInstance().closeDatabase();
    }

    public int saveCharactersListToDb(List<Character> list) {
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        database.beginTransaction();
        Iterator<Character> characterIterator = list.iterator();

        while (characterIterator.hasNext()) {
            Character character = characterIterator.next();
            if (!isHeroAlreadySaved(character)) {
                makeTransaction(character);
            }
        }

        database.setTransactionSuccessful();
        database.endTransaction();
        DatabaseManager.getInstance().closeDatabase();
        return 0;
    }

    public boolean deleteCharacterByName(String name) {
        SQLiteDatabase writableDatabase = DatabaseManager.getInstance().openDatabase();
        writableDatabase.delete("heroes", "name" + "='" + name + "'", null);
        writableDatabase.delete("aliases", "outer" + "='" + name + "'", null);
        DatabaseManager.getInstance().closeDatabase();
        return true;
    }

    public boolean isHeroAlreadySaved(Character character) {
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        String name = "";
        String selectQuery = "SELECT * FROM heroes WHERE name = ?";
        Cursor c = database.rawQuery(selectQuery, new String[]{character.getName()});
        if (c.moveToFirst())
            name = c.getString(c.getColumnIndex("name"));
        c.close();
        DatabaseManager.getInstance().closeDatabase();
        return character.getName().equals(name);
    }

    public List<Character> getCharactersFromDB() {
        List<Character> listResult = new ArrayList<>();
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();

        Cursor cursor = database.query(
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
                String selectQuery = "SELECT * FROM aliases WHERE outer = ?";
                Cursor c = database.rawQuery(selectQuery, new String[]{name});
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

                listResult.add(character);
            }
        } finally {
            cursor.close();
            DatabaseManager.getInstance().closeDatabase();
        }

        return listResult;
    }
}
