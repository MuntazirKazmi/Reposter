package com.codestudios.repostt;

import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Murtaz on 12/1/2016.
 */

public class CopyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("boot","received");
        SharedPreferences preferences = context.getSharedPreferences("preferences",MODE_PRIVATE);
        if(preferences.getBoolean("bService",false)){
            context.startService(new Intent(context, ClipboardService.class));
        }
    }
}
