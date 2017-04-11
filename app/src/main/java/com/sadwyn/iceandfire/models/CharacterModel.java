package com.sadwyn.iceandfire.models;

import android.content.Context;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;

public interface CharacterModel {
    Observable<List<Character>> getCharactersList(int page, int size, Context context,
                           FailureRequestCallback failureRequestCallback);

    void saveCharacterToDB(Character character, Context context);

    Observable<List<Character>> getObservableCharactersList(Context context);

    List<Character> getCharactersList(Context context);

    void deleteCharacterBySwipe(Context context, String name);

    void saveListCharactersToDB(List<Character> list, Context context);

}
