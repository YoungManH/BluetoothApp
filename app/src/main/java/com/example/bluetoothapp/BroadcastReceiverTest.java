package com.example.bluetoothapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class BroadcastReceiverTest extends BroadcastReceiver {
    public BroadcastReceiverTest() {
        super();
    }

    private final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_BOOT.equals(intent.getAction())) {
            Toast.makeText(context, "开机完毕~", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        return super.peekService(myContext, service);
    }
}
