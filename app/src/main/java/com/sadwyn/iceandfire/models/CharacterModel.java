package com.sadwyn.iceandfire.models;

import android.content.Context;

public interface CharacterModel {
    void getCharactersList(int page, int size, Context context, ResultListCallback listRequestCallback);
    void saveCharacterToDB(Character character, Context context);
}
