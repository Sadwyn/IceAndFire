package com.sadwyn.iceandfire.services;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.sadwyn.iceandfire.Constants;
import com.sadwyn.iceandfire.fragments.SettingsFragment;
import com.sadwyn.iceandfire.models.Character;
import com.sadwyn.iceandfire.models.CharacterModelImpl;

import org.parceler.Parcels;

import java.util.List;

import static com.sadwyn.iceandfire.Constants.REQUEST_FOR_WRITE_TO_CSV;

/**
 * Created by Sadwyn on 12.04.2017.
 */

public class ExportDataToService implements Runnable {
    private Intent intent;
    private Context context;
    private Activity activity;

    public ExportDataToService(Context context, Activity activityIntent, Intent intent) {
        this.intent = intent;
        this.context = context;
    }

    @Override
    public void run() {


        CharacterModelImpl model = CharacterModelImpl.getInstance();
        List<Character> characterList = model.getCharactersList(context);
        intent.putExtra(Constants.SEND_TO_SERVICE_KEY, Parcels.wrap(characterList));

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
            context.startService(intent);
        else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_FOR_WRITE_TO_CSV);
        }
    }
}
