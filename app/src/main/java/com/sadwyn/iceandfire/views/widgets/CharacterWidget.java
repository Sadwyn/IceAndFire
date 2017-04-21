package com.sadwyn.iceandfire.views.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;

import com.sadwyn.iceandfire.R;
import com.sadwyn.iceandfire.activities.MainActivity;
import com.sadwyn.iceandfire.receivers.WidgetIntentReceiver;

import java.util.ArrayList;
import java.util.Random;

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
    public static int currentListId = 1;
    public static ArrayList<Integer> charactersIds;
    public static int listSize;


    public static void updateWidget(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        context.sendBroadcast(intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        WidgetHelper widgetHelper = new WidgetHelper();
        charactersIds = widgetHelper.getCharactersIds(context);
        if (charactersIds.size() != 0) {
            refreshWidgetWithInfo(context, appWidgetManager, appWidgetIds);
        } else {
            refreshWidgetWithoutInfo(context, appWidgetManager, appWidgetIds);
        }
    }

    public void refreshWidgetWithoutInfo(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.character_widget);
        views.setTextViewText(R.id.characterName, "No Heroes in database");
        Intent intent = new Intent(context, MainActivity.class);
        views.setOnClickPendingIntent(R.id.characterName, PendingIntent.getActivity(context, 103, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }

    public void refreshWidgetWithInfo(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        listSize = charactersIds.size();
        Intent nameRequest = new Intent(context, WidgetIntentReceiver.class);
        Intent prevRequest = new Intent(context, WidgetIntentReceiver.class);
        Intent nextRequest = new Intent(context, WidgetIntentReceiver.class);

        Intent instantIdRequest = new Intent(context, WidgetIntentReceiver.class);
        instantIdRequest.putExtra(INCOMING_INTENT, INSTANT_ID_REQUEST);

        instantIdRequest.putExtra(HERO_START_ID, charactersIds.get(currentListId - 1));


        nameRequest.putExtra(INCOMING_INTENT, HERO_DETAIL_REQUESTED);
        prevRequest.putExtra(INCOMING_INTENT, PREV_HERO_SWITCH);
        nextRequest.putExtra(INCOMING_INTENT, NEXT_HERO_SWITCH);

        nameRequest.setAction(WIDGET_INFO_GOTH);
        prevRequest.setAction(WIDGET_INFO_GOTH);
        nextRequest.setAction(WIDGET_INFO_GOTH);

        PendingIntent namePending = PendingIntent.getBroadcast(context.getApplicationContext(), 100, nameRequest, 0);
        PendingIntent prevPending = PendingIntent.getBroadcast(context.getApplicationContext(), 101, prevRequest, 0);
        PendingIntent nextPending = PendingIntent.getBroadcast(context.getApplicationContext(), 102, nextRequest, 0);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.character_widget);
        views.setOnClickPendingIntent(R.id.characterName, namePending);
        views.setOnClickPendingIntent(R.id.prevCharacter, prevPending);
        views.setOnClickPendingIntent(R.id.nextCharacter, nextPending);

        context.sendBroadcast(instantIdRequest);
        appWidgetManager.updateAppWidget(appWidgetIds, views);
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

        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), 0, rnd.nextInt(256));

        Intent nameRequest = new Intent(context, WidgetIntentReceiver.class);
        nameRequest.putExtra(INCOMING_INTENT, HERO_DETAIL_REQUESTED);
        nameRequest.setAction(WIDGET_INFO_GOTH);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.character_widget);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        ComponentName thisWidget = new ComponentName(context.getApplicationContext(), CharacterWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        if (appWidgetIds != null && appWidgetIds.length > 0) {
            if (intent.hasExtra(PREV_HERO_ID)) {
                nameRequest.putExtra(CURRENT_HERO_ID, intent.getIntExtra(PREV_HERO_ID, 1));
                views.setTextViewText(R.id.characterName, intent.getStringExtra(PREV_HERO_NAME));
                views.setTextColor(R.id.characterName, color);
            } else if (intent.hasExtra(NEXT_HERO_ID)) {
                nameRequest.putExtra(CURRENT_HERO_ID, intent.getIntExtra(NEXT_HERO_ID, 1));
                views.setTextViewText(R.id.characterName, intent.getStringExtra(NEXT_HERO_NAME));
                views.setTextColor(R.id.characterName, color);
            } else if (intent.hasExtra(INSTANT_HERO_ID)) {
                nameRequest.putExtra(CURRENT_HERO_ID, intent.getIntExtra(INSTANT_HERO_ID, 1));
                views.setTextViewText(R.id.characterName, intent.getStringExtra(INSTANT_HERO_NAME));
                views.setTextColor(R.id.characterName, color);
            }

            PendingIntent namePending = PendingIntent.getBroadcast(context.getApplicationContext(), 100, nameRequest, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.characterName, namePending);
            appWidgetManager.updateAppWidget(appWidgetIds, views);

            if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE))
                onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }
}

