/*
 * The MIT License (MIT)
 * Copyright (c) 2017. Sergio Leonardo Isasmendi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package leoisasmendi.android.com.suricatepodcast.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import leoisasmendi.android.com.suricatepodcast.MainActivity;
import leoisasmendi.android.com.suricatepodcast.R;
import leoisasmendi.android.com.suricatepodcast.services.MediaPlayerService;


public class PodcastWidgetProvider extends AppWidgetProvider {


    private final static String TAG = "PodcastWidgetProvider";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int appWidgetId : appWidgetIds) {

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.podcast_widget_player);
            Intent intent = new Intent(context, MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);


            Intent actionIntent = new Intent(context, PodcastWidgetProvider.class);

            actionIntent.setAction(MediaPlayerService.ACTION_PLAY);
            views.setOnClickPendingIntent(R.id.widget_play, PendingIntent.getBroadcast(context, 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT));

            actionIntent.setAction(MediaPlayerService.ACTION_PAUSE);
            views.setOnClickPendingIntent(R.id.widget_pause, PendingIntent.getBroadcast(context, 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT));

            actionIntent.setAction(MediaPlayerService.ACTION_NEXT);
            views.setOnClickPendingIntent(R.id.widget_next, PendingIntent.getBroadcast(context, 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT));

            actionIntent.setAction(MediaPlayerService.ACTION_PREVIOUS);
            views.setOnClickPendingIntent(R.id.widget_prev, PendingIntent.getBroadcast(context, 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT));

            appWidgetManager.updateAppWidget(appWidgetId, views);

        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        switch (action) {
            case MediaPlayerService.ACTION_PLAY:
                sendActionToService(context, MediaPlayerService.ACTION_PLAY);
                break;
            case MediaPlayerService.ACTION_PAUSE:
                sendActionToService(context, MediaPlayerService.ACTION_PAUSE);
                break;
            case MediaPlayerService.ACTION_NEXT:
                sendActionToService(context, MediaPlayerService.ACTION_NEXT);
                break;
            case MediaPlayerService.ACTION_PREVIOUS:
                sendActionToService(context, MediaPlayerService.ACTION_PREVIOUS);
                break;
            default:
                break;
        }

        super.onReceive(context, intent);
    }

    private void sendActionToService(Context context, String action) {
        Intent playerIntent = new Intent(context, MediaPlayerService.class);
        playerIntent.setAction(action);
        context.startService(playerIntent);
    }
}
