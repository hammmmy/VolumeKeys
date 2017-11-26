package org.ipforsmartobjects.apps.volumekeys.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import org.ipforsmartobjects.apps.volumekeys.R;
import org.ipforsmartobjects.apps.volumekeys.volumes.VolumeControlActivity;

public class SimpleVerticalWidgetProvider extends SimpleWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int widgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.simple_widget_vertical);

            remoteViews.setOnClickPendingIntent(R.id.volume_up, getPendingSelfIntent(context, MyOnClick_volume_up));
            remoteViews.setOnClickPendingIntent(R.id.volume_down, getPendingSelfIntent(context, MyOnClick_volume_down));
            remoteViews.setOnClickPendingIntent(R.id.mute, getPendingSelfIntent(context, MyOnClick_mute));

            Intent launchIntent = new Intent(context, VolumeControlActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.action_button_settings, pendingIntent);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}
