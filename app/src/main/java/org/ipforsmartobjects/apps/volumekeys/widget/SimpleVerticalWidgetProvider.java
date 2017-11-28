package org.ipforsmartobjects.apps.volumekeys.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import org.ipforsmartobjects.apps.volumekeys.R;
import org.ipforsmartobjects.apps.volumekeys.configuration.SimpleWidgetConfigActivity;
import org.ipforsmartobjects.apps.volumekeys.volumes.VolumeControlActivity;

public class SimpleVerticalWidgetProvider extends SimpleWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int widgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.simple_widget_vertical);

            initWidget(context, appWidgetManager, widgetId, remoteViews);
        }
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int widgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.simple_widget_vertical);

        initWidget(context, appWidgetManager, widgetId, remoteViews);
    }
}
