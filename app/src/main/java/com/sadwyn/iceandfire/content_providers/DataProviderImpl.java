package com.sadwyn.iceandfire.content_providers;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.sadwyn.iceandfire.Constants;
import com.sadwyn.iceandfire.activities.WidgetDetailActivity;
import com.sadwyn.iceandfire.data.HeroesDataContract;
import com.sadwyn.iceandfire.models.Character;

import org.parceler.Parcels;

import java.util.ArrayList;

import static com.sadwyn.iceandfire.Constants.START_DETAIL_FROM_WIDGET;


public class DataProviderImpl {

    public int getCharactersCount(Context context){
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(Uri.parse("content://com.sadwyn.iceandfire.provider.contract/characters"),
                null, null, null, null, null);
        if(cursor!=null){
            int count =  cursor.getCount();
            cursor.close();
            return count;
        }
        else return 0;
    }

    public void showDetailsOfChosenHero(Context context, Character character) {
        Intent startDetail = new Intent(context.getApplicationContext(), WidgetDetailActivity.class);
        startDetail.putExtra(START_DETAIL_FROM_WIDGET, true);
        startDetail.putExtra(Constants.WRAPPED_CHARACTER_FROM_RECEIVER, Parcels.wrap(character));
        startDetail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(startDetail);
    }

    public Character getCharacterById(Context context, int targetId) {
        Character character = new Character();
        ContentResolver resolver = context.getContentResolver();
        Cursor mainCursor = resolver.query(Uri.parse("content://com.sadwyn.iceandfire.provider.contract/characters/"+targetId),
                null, null, null, null, null);
        if (mainCursor != null) {
            try {
                if (mainCursor.moveToFirst()) {
                    int id = mainCursor.getInt(mainCursor.getColumnIndex(HeroesDataContract.MainDataStructure._ID));
                    character.setName(mainCursor.getString(mainCursor.getColumnIndex(HeroesDataContract.MainDataStructure.COLUMN_NAME)));
                    character.setBorn(mainCursor.getString(mainCursor.getColumnIndex(HeroesDataContract.MainDataStructure.COLUMN_BORN)));
                    character.setCulture(mainCursor.getString(mainCursor.getColumnIndex(HeroesDataContract.MainDataStructure.COLUMN_KINGDOM)));
                    character.setGender(mainCursor.getString(mainCursor.getColumnIndex(HeroesDataContract.MainDataStructure.COLUMN_GENDER)));
                    character.setFather(mainCursor.getString(mainCursor.getColumnIndex(HeroesDataContract.MainDataStructure.COLUMN_FATHER)));
                    character.setMother(mainCursor.getString(mainCursor.getColumnIndex(HeroesDataContract.MainDataStructure.COLUMN_MOTHER)));
                    character.setDied(mainCursor.getString(mainCursor.getColumnIndex(HeroesDataContract.MainDataStructure.COLUMN_DEAD)));
                }
            } finally {
                mainCursor.close();
            }
        }

        Cursor aliasesCursor = resolver.query(Uri.parse("content://com.sadwyn.iceandfire.provider.contract/aliases/"+targetId),
                null, null, null, null, null);
        ArrayList<String> aliases = new ArrayList<>();
        if (aliasesCursor != null)
            try {
                while (aliasesCursor.moveToNext()) {
                    String alias = aliasesCursor.getString(aliasesCursor.getColumnIndex(HeroesDataContract.AliasesStructure.COLUMN_NICKNAME));
                    aliases.add(alias);
                }
            }
            finally {
                aliasesCursor.close();
            }
        character.setAliases(aliases);
        return character;
    }
}
