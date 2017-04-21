package com.sadwyn.iceandfire.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.preference.PreferenceManager;

import com.sadwyn.iceandfire.App;
import com.sadwyn.iceandfire.Constants;
import com.sadwyn.iceandfire.R;
import com.sadwyn.iceandfire.data.CharactersTable;
import com.sadwyn.iceandfire.network.Api;
import com.sadwyn.iceandfire.views.widgets.CharacterWidget;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.internal.schedulers.IoScheduler;
import io.reactivex.internal.schedulers.RxThreadFactory;
import io.reactivex.internal.schedulers.SchedulerPoolFactory;
import io.reactivex.internal.schedulers.SingleScheduler;
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
    public Observable<List<Character>> getCharactersList(int page, int size, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String dataSource = preferences.getString(Constants.DATA_SOURCE_PREF, "remote");
        if (dataSource.equals("remote"))
            return getObservable(page, size).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
         else
             return getObservableCharactersList(context).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<Character>> getObservable(int page, int size) {
        return api.getData(page, size);
    }

    @Override
    public void saveCharacterToDB(Character character, Context context) {
            CharactersTable table = new CharactersTable();
            Observable<Integer> oneHeroObservable = table.getInsertObservable(character);
            oneHeroObservable
                    .subscribeOn(Schedulers.io())
                    .subscribe();
    }

    @Override
    public Observable<List<Character>> getObservableCharactersList(Context context) {
        CharactersTable charactersTable = new CharactersTable();
        return Observable.just(charactersTable.getCharactersFromDB());
    }

    public Observable<Character> getCharacter(String url){
        return api.getCharacter(url).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void deleteCharacterBySwipe(Context context, String name) {
        CharactersTable charactersTable = new CharactersTable();
        charactersTable.deleteCharacterByName(name);
    }

    @Override
    public void saveListCharactersToDB(List<Character> list, Context context) {
            CharactersTable table = new CharactersTable();
            Observable<Integer> listObservable = table.getInsertListObservable(list);
            listObservable
                    .subscribeOn(Schedulers.io())
                    .subscribe();
    }
}
