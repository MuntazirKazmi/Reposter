package com.codestudios.repostt;

import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by Murtaz on 12/1/2016.
 */

public class CopyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ClipboardManager clipBoard = (ClipboardManager)context.getSystemService(CLIPBOARD_SERVICE);
        clipBoard.addPrimaryClipChangedListener( new ClipboardListener(context) );
    }
}
