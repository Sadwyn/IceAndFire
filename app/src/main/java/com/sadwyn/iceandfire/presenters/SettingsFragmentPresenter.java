package com.sadwyn.iceandfire.presenters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sadwyn.iceandfire.Constants;
import com.sadwyn.iceandfire.models.Character;
import com.sadwyn.iceandfire.models.CharacterModelImpl;
import com.sadwyn.iceandfire.services.ExportDataService;
import com.sadwyn.iceandfire.views.notifications.ExportDataNotification;

import org.parceler.Parcels;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;



public class SettingsFragmentPresenter extends BasePresenter {
    private Intent intent;

    public void onExportButtonClick(Context context){
        intent = new Intent(context, ExportDataService.class);
        ExportDataNotification notification = new ExportDataNotification(context);
        notification.showNotification();
       Observer<List<Character>> observer =  new Observer<List<Character>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Character> value) {
                intent.putExtra(Constants.SEND_TO_SERVICE_KEY, Parcels.wrap(value));
                context.startService(intent);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        CharacterModelImpl model = CharacterModelImpl.getInstance();
         Observable.defer(() -> model.getObservableCharactersList(context)).subscribeOn(Schedulers.io()).subscribe(observer);

    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {

    }

    @Override
    public void onDestroyView() {

    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {

    }
}
