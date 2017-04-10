package com.sadwyn.iceandfire.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.sadwyn.iceandfire.App;
import com.sadwyn.iceandfire.Constants;
import com.sadwyn.iceandfire.data.CharactersTable;
import com.sadwyn.iceandfire.network.Api;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CharacterModelImpl implements CharacterModel {
    private static CharacterModelImpl model;

    @Inject
    Api api;

    public static CharacterModelImpl getInstance() {
        if (model == null) model = new CharacterModelImpl();
        return model;
    }

    private CharacterModelImpl() {
        App.getComponentDagger().inject(this);
    }

    @Override
    public Observable<List<Character>> getCharactersList(int page, int size, Context context,
                                                         FailureRequestCallback failureRequestCallback) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String dataSource = preferences.getString(Constants.DATA_SOURCE_PREF, "remote");
        if (dataSource.equals("remote"))
            return getObservable(page, size).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
         else return getObservableCharactersList(context);
    }

    public Observable<List<Character>> getObservable(int page, int size) {
        return api.getData(page, size);
    }

    @Override
    public void saveCharacterToDB(Character character, Context context) {
        CharactersTable table = new CharactersTable(context);
        table.saveCharacterToDB(character);
    }

    @Override
    public Observable<List<Character>> getObservableCharactersList(Context context) {
        CharactersTable charactersTable = new CharactersTable(context);
        return Observable.just(charactersTable.getCharactersFromDB());
    }

    @Override
    public List<Character> getCharactersList(Context context) {
        CharactersTable charactersTable = new CharactersTable(context);
        return charactersTable.getCharactersFromDB();
    }

    @Override
    public void deleteCharacterBySwipe(Context context, String name) {
        CharactersTable charactersTable = new CharactersTable(context);
        charactersTable.deleteCharacterByName(name);
    }
}
