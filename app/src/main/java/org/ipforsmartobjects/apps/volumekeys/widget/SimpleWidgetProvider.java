package org.ipforsmartobjects.apps.volumekeys.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.ipforsmartobjects.apps.volumekeys.R;
import org.ipforsmartobjects.apps.volumekeys.configuration.SimpleWidgetConfigActivity;
import org.ipforsmartobjects.apps.volumekeys.volumes.VolumeControlActivity;

public class SimpleWidgetProvider extends AppWidgetProvider {

    final static String MyOnClick_volume_up = "volume_up";
    final static String MyOnClick_volume_down = "volume_down";
    final static String MyOnClick_mute = "mute";

    // yeah I know we need to avoid enums :)
    private enum VolumeEvent{
        UP,
        DOWN,
        MUTE
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int widgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, widgetId);
        }
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int widgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.simple_widget);

        initWidget(context, appWidgetManager, widgetId, remoteViews);
    }

    protected static void initWidget(Context context, AppWidgetManager appWidgetManager, int widgetId, RemoteViews remoteViews) {
        int bgColor = SimpleWidgetConfigActivity.loadBackgrooundColorPref(context, widgetId);
        int iconBackgroundColor = SimpleWidgetConfigActivity.loadIconBackgrooundColorPref(context, widgetId);
        boolean isBlack = SimpleWidgetConfigActivity.loadIsBlackColorPref(context, widgetId);

        remoteViews.setInt(R.id.layout_container, "setBackgroundColor", bgColor);
        remoteViews.setInt(R.id.mute, "setBackgroundColor", iconBackgroundColor);
        remoteViews.setInt(R.id.mute, "setImageResource", isBlack ? R.drawable.ic_volume_off_black : R.drawable.ic_volume_off_white);

        remoteViews.setInt(R.id.volume_down, "setBackgroundColor", iconBackgroundColor);
        remoteViews.setInt(R.id.volume_down, "setImageResource", isBlack ? R.drawable.ic_remove_circle_outline_black : R.drawable.ic_remove_circle_outline_white);

        remoteViews.setInt(R.id.volume_up, "setBackgroundColor", iconBackgroundColor);
        remoteViews.setInt(R.id.volume_up, "setImageResource", isBlack ? R.drawable.ic_add_circle_outline_black : R.drawable.ic_add_circle_outline_white);

        remoteViews.setInt(R.id.action_button_settings, "setBackgroundColor", iconBackgroundColor);
        remoteViews.setInt(R.id.action_button_settings, "setImageResource", isBlack ? R.drawable.ic_more_black : R.drawable.ic_more_white);

        remoteViews.setOnClickPendingIntent(R.id.volume_up, getPendingSelfIntent(context, MyOnClick_volume_up));
        remoteViews.setOnClickPendingIntent(R.id.volume_down, getPendingSelfIntent(context, MyOnClick_volume_down));
        remoteViews.setOnClickPendingIntent(R.id.mute, getPendingSelfIntent(context, MyOnClick_mute));

        Intent launchIntent = new Intent(context, VolumeControlActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.action_button_settings, pendingIntent);

        appWidgetManager.updateAppWidget(widgetId, remoteViews);
    }

    static PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, SimpleWidgetProvider.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private static final String TAG = "SimpleWidgetProvider";
    private void adjustBar(Context context, AudioManager manager, final int stream, VolumeEvent event) {

        int currentVolume = manager.getStreamVolume(stream);
        int maxVolume = manager.getStreamMaxVolume(stream);

        Log.d(TAG, "initBar: currentVolume = " + currentVolume + " maxVolume = "+ maxVolume);
        switch (event) {
            case UP:
                manager.adjustSuggestedStreamVolume(AudioManager.ADJUST_RAISE, stream, AudioManager.FLAG_SHOW_UI);
                break;
            case DOWN:
                manager.adjustSuggestedStreamVolume(AudioManager.ADJUST_LOWER, stream, AudioManager.FLAG_SHOW_UI);
                break;
            case MUTE:
                try {
                    manager.setStreamVolume(stream, 0, AudioManager.FLAG_SHOW_UI);
                    manager.setStreamVolume(AudioManager.STREAM_RING, 0, AudioManager.FLAG_VIBRATE);
                } catch (SecurityException e) {
                    Toast.makeText(context.getApplicationContext(), R.string.notification_access_do_not_disturb, Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        int stream = AudioManager.STREAM_MUSIC;

        if (MyOnClick_volume_up.equals(intent.getAction())) {
            adjustBar(context, audioManager, stream, VolumeEvent.UP);
        } else if (MyOnClick_volume_down.equals(intent.getAction())) {
            adjustBar(context, audioManager, stream, VolumeEvent.DOWN);
        } else if (MyOnClick_mute.equals(intent.getAction())) {
            adjustBar(context, audioManager, stream, VolumeEvent.MUTE);
        }
    }
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            SimpleWidgetConfigActivity.deleteColorPref(context, appWidgetId);
        }
    }
}
