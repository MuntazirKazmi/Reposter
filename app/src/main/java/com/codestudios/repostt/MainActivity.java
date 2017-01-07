package com.codestudios.repostt;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Switch serviceSwitch;
    boolean serviceStarted;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serviceSwitch= (Switch) findViewById(R.id.switchClipboardService);


        preferences = getSharedPreferences("preferences",MODE_PRIVATE);
        serviceStarted = preferences.getBoolean("bService",false);
        serviceSwitch.setChecked(serviceStarted);
        editor = preferences.edit();
        if(serviceStarted&&!isMyServiceRunning(ClipboardService.class))
            startService(new Intent(MainActivity.this, ClipboardService.class));




        serviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                editor.putBoolean("bService",isChecked);
                editor.commit();
                startService(new Intent(MainActivity.this, ClipboardService.class));
                Toast.makeText(MainActivity.this, ((isMyServiceRunning(ClipboardService.class))?"Service started.":"Service stopped."), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {

                return true;
            }
        }
        return false;
    }
}
