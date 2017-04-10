package com.sadwyn.iceandfire.presenters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sadwyn.iceandfire.CharacterView;
import com.sadwyn.iceandfire.ContextAnnotation;
import com.sadwyn.iceandfire.InstanceDagger;
import com.sadwyn.iceandfire.models.FailureRequestCallback;
import com.sadwyn.iceandfire.models.Character;
import com.sadwyn.iceandfire.models.CharacterModel;
import com.sadwyn.iceandfire.models.CharacterModelImpl;
import com.sadwyn.iceandfire.models.ResultListCallback;

import org.parceler.Parcels;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.SingleScheduler;
import io.reactivex.observers.DisposableObserver;

@Module
public class CharactersListPresenter extends BasePresenter implements FailureRequestCallback {

    public static final String PAGE_KEY = "PAGE_KEY";
    public static final String LIST_KEY = "LIST_KEY";

    private List<Character> list;
    private Set<Character> set;

    private final int size = 50;
    private int page;

    private Context context;
    private CharacterModelImpl characterModel;

    private CharacterView characterFragmentView;
    private CompositeDisposable disposables;

    @Provides
    public CharactersListPresenter provideCharacterPresenter(Context context, CharacterView characterFragmentView){
        return new CharactersListPresenter(context, characterFragmentView);
    }


    public CharactersListPresenter(Context context, CharacterView characterFragmentView) {
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
               disposables.add(characterModel.getCharactersList(page, size, view.getContext(), this).subscribeWith(getObserver()));
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
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        };
    }

    @Override
    public void onDestroyView() {
        if(disposables != null && disposables.isDisposed())
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
        list = new ArrayList<>();
        set = new LinkedHashSet<>();

    }


    public void addNewData() {
        page++;
        disposables.add(characterModel.getCharactersList(page, size, context, this).subscribeWith(getObserver()));
    }


    @Override
    public void onFailureRequest() {
        characterFragmentView.showCharactersList(true);
    }
}
