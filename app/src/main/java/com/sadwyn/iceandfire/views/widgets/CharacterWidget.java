package com.sadwyn.iceandfire.views.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.sadwyn.iceandfire.R;
import com.sadwyn.iceandfire.receivers.WidgetIntentReceiver;

import java.util.ArrayList;

import static com.sadwyn.iceandfire.Constants.CURRENT_HERO_ID;
import static com.sadwyn.iceandfire.Constants.HERO_DETAIL_REQUESTED;
import static com.sadwyn.iceandfire.Constants.HERO_START_ID;
import static com.sadwyn.iceandfire.Constants.INCOMING_INTENT;
import static com.sadwyn.iceandfire.Constants.INSTANT_HERO_ID;
import static com.sadwyn.iceandfire.Constants.INSTANT_HERO_NAME;
import static com.sadwyn.iceandfire.Constants.INSTANT_ID_REQUEST;
import static com.sadwyn.iceandfire.Constants.NEXT_HERO_ID;
import static com.sadwyn.iceandfire.Constants.NEXT_HERO_NAME;
import static com.sadwyn.iceandfire.Constants.NEXT_HERO_SWITCH;
import static com.sadwyn.iceandfire.Constants.PREV_HERO_ID;
import static com.sadwyn.iceandfire.Constants.PREV_HERO_NAME;
import static com.sadwyn.iceandfire.Constants.PREV_HERO_SWITCH;
import static com.sadwyn.iceandfire.Constants.WIDGET_INFO_GOTH;

public class CharacterWidget extends AppWidgetProvider {
    public static int currentListId;
    public static ArrayList<Integer> charactersIds;
    public static int listSize;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        WidgetHelper provider = new WidgetHelper();
         charactersIds = provider.getCharactersIds(context);
        if(charactersIds.size()!=0) {
            listSize = charactersIds.size();
            Intent nameRequest = new Intent(context, WidgetIntentReceiver.class);
            Intent prevRequest = new Intent(context, WidgetIntentReceiver.class);
            Intent nextRequest = new Intent(context, WidgetIntentReceiver.class);

            Intent instantIdRequest = new Intent(context, WidgetIntentReceiver.class);
            instantIdRequest.putExtra(INCOMING_INTENT, INSTANT_ID_REQUEST);
            currentListId = (Math.round((charactersIds.size())/2f)) ;
            instantIdRequest.putExtra(HERO_START_ID, charactersIds.get(currentListId - 1));


            nameRequest.putExtra(INCOMING_INTENT, HERO_DETAIL_REQUESTED);
            prevRequest.putExtra(INCOMING_INTENT, PREV_HERO_SWITCH);
            nextRequest.putExtra(INCOMING_INTENT, NEXT_HERO_SWITCH);

            nameRequest.setAction(WIDGET_INFO_GOTH);
            prevRequest.setAction(WIDGET_INFO_GOTH);
            nextRequest.setAction(WIDGET_INFO_GOTH);

            PendingIntent namePending = PendingIntent.getBroadcast(context, 100, nameRequest, 0);
            PendingIntent prevPending = PendingIntent.getBroadcast(context, 101, prevRequest, 0);
            PendingIntent nextPending = PendingIntent.getBroadcast(context, 102, nextRequest, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.character_widget);
            views.setOnClickPendingIntent(R.id.characterName, namePending);
            views.setOnClickPendingIntent(R.id.prevCharacter, prevPending);
            views.setOnClickPendingIntent(R.id.nextCharacter, nextPending);

            context.sendBroadcast(instantIdRequest);
            appWidgetManager.updateAppWidget(appWidgetIds, views);
        }
        else {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.character_widget);
            views.setTextViewText(R.id.characterName, "No Heroes in database");
            appWidgetManager.updateAppWidget(appWidgetIds, views);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.

    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        Intent nameRequest = new Intent(context, WidgetIntentReceiver.class);
        nameRequest.putExtra(INCOMING_INTENT, HERO_DETAIL_REQUESTED);
        nameRequest.setAction(WIDGET_INFO_GOTH);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.character_widget);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        ComponentName thisWidget = new ComponentName(context.getApplicationContext(), CharacterWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        if(intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) onUpdate(context, appWidgetManager,appWidgetIds);

        if (appWidgetIds != null && appWidgetIds.length > 0) {
            if (intent.hasExtra(PREV_HERO_ID)) {
                nameRequest.putExtra(CURRENT_HERO_ID, intent.getIntExtra(PREV_HERO_ID, 1));
                views.setTextViewText(R.id.characterName, intent.getStringExtra(PREV_HERO_NAME));
            } else if (intent.hasExtra(NEXT_HERO_ID)) {
                nameRequest.putExtra(CURRENT_HERO_ID, intent.getIntExtra(NEXT_HERO_ID, 1));
                views.setTextViewText(R.id.characterName, intent.getStringExtra(NEXT_HERO_NAME));
            }
            else if(intent.hasExtra(INSTANT_HERO_ID)){
                nameRequest.putExtra(CURRENT_HERO_ID, intent.getIntExtra(INSTANT_HERO_ID, 0));
                views.setTextViewText(R.id.characterName, intent.getStringExtra(INSTANT_HERO_NAME));
            }

            PendingIntent namePending = PendingIntent.getBroadcast(context, 100, nameRequest, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.characterName, namePending);

            appWidgetManager.updateAppWidget(appWidgetIds, views);
        }
    }
    public static void updateWidget(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        context.sendBroadcast(intent);
    }
}

