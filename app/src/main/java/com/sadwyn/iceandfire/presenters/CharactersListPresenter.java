package com.sadwyn.iceandfire.presenters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.sadwyn.iceandfire.CharacterView;
import com.sadwyn.iceandfire.models.FailureRequestCallback;
import com.sadwyn.iceandfire.models.Character;
import com.sadwyn.iceandfire.models.CharacterModel;
import com.sadwyn.iceandfire.models.CharacterModelImpl;
import com.sadwyn.iceandfire.models.ResultListCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;

public class CharactersListPresenter extends BasePresenter implements ResultListCallback, FailureRequestCallback {

    public static final String PAGE_KEY = "PAGE_KEY";
    public static final String LIST_KEY = "LIST_KEY";

    private List<Character> list;
    private Set<Character> set;

    private final int size = 50;
    private int page;

    private Context context;
    private CharacterModel characterModel;


    private CharacterView characterFragmentView;

    public CharactersListPresenter(Context context, CharacterView characterFragmentView) {
        this.context = context;
        this.characterFragmentView = characterFragmentView;
        this.characterModel = CharacterModelImpl.getInstance();
        initializeData();
    }

    public List<Character> getList() {
        return list;
    }


    @Override
    public void onViewCreated(View view, Bundle bundle) {
        if(getList().isEmpty()) {
            if (bundle == null)
                characterModel.getCharactersList(page, size, view.getContext(), this, this);
            else restoreData(bundle);
        }
    }

    @Override
    public void onDestroyView() {
        if(characterModel.getCall()!=null) characterModel.getCall().cancel();
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

    private void setInfoToView(List<Character> characters){
            for (Character person : characters) {
                if (person != null && !person.getName().equals(""))
                    set.add(person);
            }
            list.addAll(set);
            set.clear();
            characterFragmentView.showCharactersList(false);
    }

    public void addNewData() {
        page++;
        characterModel.getCharactersList(page, size, context, this, this);
    }

    @Override
    public void onListRequest(List<Character> characters) {
        setInfoToView(characters);
    }

    @Override
    public void onFailureRequest() {
      characterFragmentView.showCharactersList(true);
    }
}
