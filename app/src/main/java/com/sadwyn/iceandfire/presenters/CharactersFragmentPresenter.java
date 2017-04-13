package com.sadwyn.iceandfire.presenters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.sadwyn.iceandfire.CharactersView;
import com.sadwyn.iceandfire.models.Character;
import com.sadwyn.iceandfire.models.CharacterModelImpl;
import com.sadwyn.iceandfire.utils.ParcelableCopyOnWriteArrayList;

import org.parceler.Parcels;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.disposables.CompositeDisposable;
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
        if (getList().isEmpty()) {
            if (bundle == null)
                disposables.add(characterModel.getCharactersList(page, size, view.getContext()).subscribeWith(getObserver()));
            else
                restoreData(bundle);
        }
    }

    public DisposableObserver<List<Character>> getObserver() {
        return new DisposableObserver<List<Character>>() {
            @Override
            public void onNext(List<Character> value) {
                for (Character person : value) {
                    if (person != null && !person.getName().equals(""))
                        set.add(person);
                }
                list.addAll(set);
                set.clear();
                characterFragmentView.showCharactersList(false);
                characterModel.saveListCharactersToDB(list,context);
            }

            @Override
            public void onError(Throwable e) {
                characterFragmentView.showCharactersList(true);
            }
            @Override
            public void onComplete() {
                Log.i("TAG", "ON_COMPLETE");
            }
        };
    }

    @Override
    public void onDestroyView() {
        if (disposables != null && disposables.isDisposed())
            disposables.clear();
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
        disposables.add(characterModel.getCharactersList(page, size, context).subscribeWith(getObserver()));
    }

}
