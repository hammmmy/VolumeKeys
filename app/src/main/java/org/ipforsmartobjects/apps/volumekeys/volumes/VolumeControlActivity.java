package org.ipforsmartobjects.apps.volumekeys.volumes;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.widget.SeekBar;
import android.widget.Toast;

import org.ipforsmartobjects.apps.volumekeys.R;
import org.ipforsmartobjects.apps.volumekeys.databinding.VolumeControlBinding;

public class VolumeControlActivity extends AppCompatActivity {

    private AudioManager mAudioManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VolumeControlBinding binding = DataBindingUtil.setContentView(this, R.layout.volume_control);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        setTitle(R.string.all_volume_title);
        setUI(binding.music, AudioManager.STREAM_MUSIC);
        setUI(binding.ring, AudioManager.STREAM_RING);
        setUI(binding.system, AudioManager.STREAM_SYSTEM);
        setUI(binding.notification, AudioManager.STREAM_NOTIFICATION);
        setUI(binding.voice, AudioManager.STREAM_VOICE_CALL);


    }

    private void setUI(final AppCompatSeekBar seekBar, final int stream) {
        int max = mAudioManager.getStreamMaxVolume(stream);
        int progress = mAudioManager.getStreamVolume(stream);
        seekBar.setMax(max);
        seekBar.setProgress(progress);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                try {
                    mAudioManager.setStreamVolume(stream, progress, AudioManager.FLAG_PLAY_SOUND);
                } catch (SecurityException e) {
                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                            && !notificationManager.isNotificationPolicyAccessGranted()) {

                        Toast.makeText(VolumeControlActivity.this, R.string.notification_access, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(
                                android.provider.Settings
                                        .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

                        startActivity(intent);
                    } else {
                        Toast.makeText(VolumeControlActivity.this, R.string.notification_access_do_not_disturb, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
}
