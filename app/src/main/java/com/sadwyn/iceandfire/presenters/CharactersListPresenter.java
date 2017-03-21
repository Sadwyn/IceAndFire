package com.sadwyn.iceandfire.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.sadwyn.iceandfire.CharacterView;
import com.sadwyn.iceandfire.Constants;
import com.sadwyn.iceandfire.MainActivity;
import com.sadwyn.iceandfire.fragments.SourceChangeCallBack;
import com.sadwyn.iceandfire.models.Character;
import com.sadwyn.iceandfire.models.CharacterModelImpl;
import com.sadwyn.iceandfire.models.RemoteListCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CharactersListPresenter extends BasePresenter implements RemoteListCallback{

    public static final String PAGE_KEY = "PAGE_KEY";
    public static final String LIST_KEY = "LIST_KEY";

    private List<Character> list;
    private Set<Character> set;

    private final int size = 50;
    private int page;

    private Call<List<Character>> call;

    private CharacterModelImpl characterModel;

    private CharacterView characterFragmentView;

    public CharactersListPresenter(Context context, CharacterView characterFragmentView) {
        this.characterFragmentView = characterFragmentView;
        this.characterModel = new CharacterModelImpl(context, this);
        initializeData();
    }

    public List<Character> getList() {
        return list;
    }


    @Override
    public void onViewCreated(View view, Bundle bundle) {
        if(getList().isEmpty()) {
            if (bundle == null)
                characterModel.getCharactersList(page, size);
            else restoreData(bundle);
        }
    }

    @Override
    public void onDestroyView() {
        if(call != null){
            call.cancel();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt(PAGE_KEY,page);
        bundle.putParcelable(LIST_KEY, Parcels.wrap(list));
    }

    private void restoreData(@Nullable Bundle savedInstanceState) {
        this.page = savedInstanceState.getInt(PAGE_KEY);
        this.list.addAll((Collection<? extends Character>) Parcels.unwrap(savedInstanceState.getParcelable(LIST_KEY)));
    }

    public void initializeData() {
        page = 1;
        list = new ArrayList<>();
        set = new LinkedHashSet<>();
    }

    private void setInfoToView(List<Character> characters, boolean isError){
            for (Character person : characters) {
                if (person != null && !person.getName().equals(""))
                    set.add(person);
            }
            list.addAll(set);
            set.clear();
            characterFragmentView.showCharactersList(isError);
    }

    public void addNewData() {
        page++;
        characterModel.getCharactersList(page, size);
    }


    @Override
    public void onRemoteRequest(List<Character> characters) {
        setInfoToView(characters, false);
    }


}
