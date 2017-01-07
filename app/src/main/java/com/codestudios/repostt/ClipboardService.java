package com.codestudios.repostt;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;

public class ClipboardService extends Service  {
    public ClipboardService() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SharedPreferences preferences = getSharedPreferences("preferences",MODE_PRIVATE);
        boolean bService = preferences.getBoolean("bService", false);
        if(bService) {
            ClipboardManager clipBoard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            clipBoard.addPrimaryClipChangedListener(new ClipboardListener(getApplicationContext()));
        }
        else{
            stopSelf();
        }



        return START_STICKY_COMPATIBILITY;

    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
