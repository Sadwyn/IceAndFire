package com.sadwyn.iceandfire.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sadwyn.iceandfire.Constants;
import com.sadwyn.iceandfire.content_providers.WidgetHelper;
import com.sadwyn.iceandfire.models.Character;

import static com.sadwyn.iceandfire.Constants.CURRENT_HERO_ID;
import static com.sadwyn.iceandfire.Constants.HERO_DETAIL_REQUESTED;
import static com.sadwyn.iceandfire.Constants.HERO_START_ID;
import static com.sadwyn.iceandfire.Constants.INSTANT_HERO_ID;
import static com.sadwyn.iceandfire.Constants.INSTANT_HERO_NAME;
import static com.sadwyn.iceandfire.Constants.INSTANT_ID_REQUEST;
import static com.sadwyn.iceandfire.Constants.NEXT_HERO_ID;
import static com.sadwyn.iceandfire.Constants.NEXT_HERO_NAME;
import static com.sadwyn.iceandfire.Constants.NEXT_HERO_SWITCH;
import static com.sadwyn.iceandfire.Constants.PREV_HERO_ID;
import static com.sadwyn.iceandfire.Constants.PREV_HERO_NAME;
import static com.sadwyn.iceandfire.Constants.PREV_HERO_SWITCH;


public class WidgetIntentReceiver extends BroadcastReceiver {

        WidgetHelper provider = new WidgetHelper();
        public WidgetIntentReceiver() {}

        @Override
        public void onReceive(Context context, Intent intent) {
            String s = intent.getStringExtra(Constants.INCOMING_INTENT);
            switch (s) {
                case HERO_DETAIL_REQUESTED:
                    int currentId = intent.getIntExtra(CURRENT_HERO_ID, 1);
                    Character character = provider.getCharacterById(context, currentId);
                    provider.showDetailsOfChosenHero(context, character);

                    break;
                case PREV_HERO_SWITCH:
                    int prevId = 1;
                    character =  provider.getCharacterById(context,prevId);
                    Intent prevHeroIntent = new Intent("com.sadwyn.update.widget");
                    prevHeroIntent.putExtra(PREV_HERO_ID, prevId);
                    prevHeroIntent.putExtra(PREV_HERO_NAME, character.getName());
                    context.sendBroadcast(prevHeroIntent);

                    break;
                case NEXT_HERO_SWITCH:
                    int nextId = 1;
                    character =  provider.getCharacterById(context,nextId);
                    Intent nextHeroIntent = new Intent("com.sadwyn.update.widget");
                    nextHeroIntent.putExtra(NEXT_HERO_ID, nextId);
                    nextHeroIntent.putExtra(NEXT_HERO_NAME, character.getName());
                    context.sendBroadcast(nextHeroIntent);
                    break;
                case INSTANT_ID_REQUEST:
                    int startID = intent.getIntExtra(HERO_START_ID,0);
                    character =  provider.getCharacterById(context,startID);
                    Intent instantHeroIntent = new Intent("com.sadwyn.update.widget");
                    instantHeroIntent.putExtra(INSTANT_HERO_ID, startID);
                    instantHeroIntent.putExtra(INSTANT_HERO_NAME, character.getName());
                    context.sendBroadcast(instantHeroIntent);
                    break;

            }
        }
}
