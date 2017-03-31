package com.sadwyn.iceandfire.models;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import com.sadwyn.iceandfire.App;
import com.sadwyn.iceandfire.Constants;
import com.sadwyn.iceandfire.data.CharactersTable;
import com.sadwyn.iceandfire.presenters.CharactersListPresenter;
import com.sadwyn.iceandfire.views.widgets.CharacterWidget;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

public class CharacterModelImpl implements CharacterModel {
    private static CharacterModelImpl model;

    public static CharacterModelImpl getInstance(){
        if(model == null) model = new CharacterModelImpl();
        return model;
    }

    private CharacterModelImpl(){}

    @Override
    public void getCharactersList(int page, int size, Context context, ResultListCallback listRequestCallback,
                                  FailureRequestCallback failureRequestCallback) {

        if (listRequestCallback instanceof CharactersListPresenter) {

            CharactersTable characterTable = new CharactersTable(context);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String dataSource = preferences.getString(Constants.DATA_SOURCE_PREF, "remote");
            if (dataSource.equals("remote")) {
                App.getApi().getData(page, size).enqueue(new Callback<List<Character>>() {
                    @Override
                    public void onResponse(Call<List<Character>> call, Response<List<Character>> response) {
                        listRequestCallback.onListRequest(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Character>> call, Throwable t) {
                        failureRequestCallback.onFailureRequest();
                    }
                });
            } else listRequestCallback.onListRequest(characterTable.getCharactersFromDB());
        }
    }

    @Override
    public void saveCharacterToDB(Character character, Context context) {
        CharactersTable table = new CharactersTable(context);
        table.saveCharacterToDB(character);
    }

    @Override
    public List<Character> getCharacters(Context context){
        CharactersTable charactersTable = new CharactersTable(context);
        return charactersTable.getCharactersFromDB();
    }

    @Override
    public void deleteCharacterBySwipe(Context context, String name) {
        CharactersTable charactersTable = new CharactersTable(context);
        charactersTable.deleteCharacterByName(name);
    }
}
