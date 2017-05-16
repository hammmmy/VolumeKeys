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
import org.ipforsmartobjects.apps.volumekeys.VolumeControlActivity;

public class SimpleWidgetProvider extends AppWidgetProvider {

    final String MyOnClick_volume_up = "volume_up";
    final String MyOnClick_volume_down = "volume_down";
    final String MyOnClick_mute = "mute";

    // yeah I know we need to avoid enums :)
    private enum VolumeEvent{
        UP,
        DOWN,
        MUTE
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int widgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.simple_widget);

            remoteViews.setOnClickPendingIntent(R.id.volume_up, getPendingSelfIntent(context, MyOnClick_volume_up));
            remoteViews.setOnClickPendingIntent(R.id.volume_down, getPendingSelfIntent(context, MyOnClick_volume_down));
            remoteViews.setOnClickPendingIntent(R.id.mute, getPendingSelfIntent(context, MyOnClick_mute));

            Intent launchIntent = new Intent(context, VolumeControlActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.action_button_settings, pendingIntent);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
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

}
