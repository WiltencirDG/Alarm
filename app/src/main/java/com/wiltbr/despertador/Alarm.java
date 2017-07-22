package com.wiltbr.despertador;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by wilte on 13/07/2017.
 */

public class Alarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String date = intent.getStringExtra("date");
        Intent i = new Intent(context, Wakeup.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("date", date);
        context.startActivity(i);
    }
}
