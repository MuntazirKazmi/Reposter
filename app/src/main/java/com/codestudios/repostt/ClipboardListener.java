package com.codestudios.repostt;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.MODE_PRIVATE;


class ClipboardListener implements ClipboardManager.OnPrimaryClipChangedListener {
    private Context context;

    ClipboardListener(Context context) {
        this.context = context;
    }

    @Override
    public void onPrimaryClipChanged() {

        try {
            ClipboardManager clipBoard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData abc = clipBoard.getPrimaryClip();
            ClipData.Item item = abc.getItemAt(0);
            String text = item.getText().toString();

            SharedPreferences preferences = context.getSharedPreferences("preferences", MODE_PRIVATE);
            Pattern pattern = Pattern.compile("https://www.instagram.com/p/([^/]+)");
            Matcher matcher = pattern.matcher(text);
            if (matcher.find() && preferences.getBoolean("bService", false)) {
                Intent intent = new Intent(context.getApplicationContext(), DownloadActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
        catch (Exception e){
            Log.e("ERROR", "onPrimaryClipChanged: ",e );
        }
    }
}
