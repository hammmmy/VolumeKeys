package org.ipforsmartobjects.apps.volumekeys.volumes;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.Toast;


public class FirebaseMessageIntentService extends IntentService {
    public static final String EXTRA_MESSAGE = "extra_message";

    public FirebaseMessageIntentService() {
        super("FirebaseMessageIntentService");

    }
    public FirebaseMessageIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Gets data from the incoming Intent
        String msg = intent.getStringExtra(EXTRA_MESSAGE);
        Toast.makeText(FirebaseMessageIntentService.this, msg, Toast.LENGTH_SHORT).show();
    }
}
