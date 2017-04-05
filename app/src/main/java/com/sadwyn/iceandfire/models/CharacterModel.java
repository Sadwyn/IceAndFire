package com.sadwyn.iceandfire.models;

import android.content.Context;

import java.util.List;

import retrofit2.Call;

public interface CharacterModel {
    void getCharactersList(int page, int size, Context context, ResultListCallback listRequestCallback,
                           FailureRequestCallback failureRequestCallback);

    void saveCharacterToDB(Character character, Context context);

    List<Character> getCharacters(Context context);

    void deleteCharacterBySwipe(Context context, String name);

    Call<List<Character>> getCall();
}
