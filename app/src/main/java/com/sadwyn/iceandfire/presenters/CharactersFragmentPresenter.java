package com.sadwyn.iceandfire.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.sadwyn.iceandfire.CharactersView;
import com.sadwyn.iceandfire.Constants;
import com.sadwyn.iceandfire.models.Character;
import com.sadwyn.iceandfire.models.CharacterModelImpl;
import com.sadwyn.iceandfire.utils.ParcelableCopyOnWriteArrayList;
import com.sadwyn.iceandfire.views.widgets.CharacterWidget;

import org.parceler.Parcels;
import org.reactivestreams.Subscription;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

public class CharactersFragmentPresenter extends BasePresenter  {

    public static final String PAGE_KEY = "PAGE_KEY";
    public static final String LIST_KEY = "LIST_KEY";

    private List<Character> list;
    private Set<Character> set;

    private final int size = 50;
    private int page;

    private Context context;
    private CharacterModelImpl characterModel;

    private CharactersView characterFragmentView;
    private CompositeDisposable disposables;
    private SharedPreferences sp;

    public CharactersFragmentPresenter(Context context, CharactersView characterFragmentView) {
        initializeData();
        this.context = context;
        this.characterFragmentView = characterFragmentView;
        this.characterModel = CharacterModelImpl.getInstance();
        disposables = new CompositeDisposable();

    }

    public List<Character> getList() {
        return list;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
         sp = PreferenceManager.getDefaultSharedPreferences(context);
        if (getList().isEmpty()) {
            if (bundle == null) {
                disposables.add(characterModel.getCharactersList(page, size, view.getContext())
                        .doOnNext(this::handleResponse)
                        .doOnError(throwable -> characterFragmentView.showCharactersList(true))
                        .subscribe(characters -> {
                            // do nothing
                        }, Throwable::printStackTrace));
            }
            else
                restoreData(bundle);
        }
    }

    @Override
    public void onDestroyView() {
        if (disposables != null && disposables.isDisposed())
            disposables.clear();

    }

    @Override
    public void onPause() {
        CharacterWidget.updateWidget(context);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt(PAGE_KEY, page);
        bundle.putParcelable(LIST_KEY, Parcels.wrap(list));
    }

    private void restoreData(@Nullable Bundle savedInstanceState) {
        this.page = savedInstanceState.getInt(PAGE_KEY);
        this.list.addAll((Collection<? extends Character>) Parcels.unwrap(savedInstanceState.getParcelable(LIST_KEY)));
    }

    public void initializeData() {
        page = 1;
        list = new ParcelableCopyOnWriteArrayList();
        set = new LinkedHashSet<>();

    }

    public void addNewData() {
        page++;
        disposables.add(characterModel.getCharactersList(page, size, context).doOnNext(this::handleResponse).subscribe());
    }

    public void handleResponse(List<Character> characters) {
        for (Character person : characters) {
            if (person != null && !person.getName().equals(""))
                set.add(person);
        }
        list.addAll(set);
        set.clear();
        characterFragmentView.showCharactersList(false);
        if(sp.getBoolean(Constants.IS_PERMANENT_SAVE_CHECKED, false))
        characterModel.saveListCharactersToDB(list,context);
    }
}
