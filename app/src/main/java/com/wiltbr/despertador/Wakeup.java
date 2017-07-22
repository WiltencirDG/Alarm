package com.wiltbr.despertador;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by wilte on 13/07/2017.
 */

public class Wakeup extends AppCompatActivity {


    private MediaPlayer mPlayer;
    Boolean vibrar = true;
    int musica = 0;
    int snooze = 0;
    Boolean AlarmeoffPress = false;
    Boolean SnoozePress = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wakeup_screen);
        Calendar c = Calendar.getInstance();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON +
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD +
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED +
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        TextView Alarmeatual;

        Button Alarmeoff;
        Button Snooze;
        SharedPreferences sharedPref;

        Alarmeoff = (Button) findViewById(R.id.btnOff);
        Alarmeatual = (TextView) findViewById(R.id.horaatt);
        Snooze = (Button) findViewById(R.id.btnSnooze);

        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        SnoozePress=false;
        AlarmeoffPress=false;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String date = (sdf.format(c.getTime()));
        Alarmeatual.setText(date);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        musica = sharedPref.getInt("musicaSpin",1);
        snooze = sharedPref.getInt("snoozeSpin",1);
        vibrar = sharedPref.getBoolean("vibra",true);

        if(vibrar) {
            long[] pattern = {0, 700, 1000};
            vibe.vibrate(pattern, 0);
        }

        switch (musica) {
            case 0:
                mPlayer = MediaPlayer.create(this,R.raw.aperture);
                break;
            case 1:
                mPlayer = MediaPlayer.create(this,R.raw.morning);
                break;
            case 2:
                mPlayer = MediaPlayer.create(this,R.raw.sandstorm);
                break;
            case 3:
                mPlayer = MediaPlayer.create(this,R.raw.symphony40);
                break;
            case 4:
                mPlayer = MediaPlayer.create(this,R.raw.whistle);
                break;
        }

        mPlayer.setLooping(true);
        mPlayer.start();

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(vibratereceiver, filter);

        Snooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 0;
                switch (snooze) {
                    case 0:
                        i = 0;
                        break;
                    case 1:
                        i = 5;
                        break;
                    case 2:
                        i = 10;
                        break;
                    case 3:
                        i = 15;
                        break;
                }

                SnoozePress = true;

                if (i != 0) {

                    vibe.cancel();
                    mPlayer.stop();
                    mPlayer.release();
                    mPlayer = null;
                    unregisterReceiver(vibratereceiver);

                    Intent it = new Intent(Wakeup.this, MainActivity.class);
                    it.putExtra("snoozed",SnoozePress);
                    it.putExtra("snoozetime",i);
                    startActivity(it);
                    finish();

                }else{
                    Toast.makeText(Wakeup.this, "Sem soneca!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Alarmeoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmeoffPress = true;
                vibe.cancel();
                mPlayer.stop();
                mPlayer.release();
                mPlayer = null;
                unregisterReceiver(vibratereceiver);
                Intent it = new Intent(Wakeup.this, MainActivity.class);
                startActivity(it);
                finish();
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(vibrar) {
            final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            long[] pattern = {0, 700, 1000};
            vibe.vibrate(pattern, 0);
        }
    }

    public BroadcastReceiver vibratereceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                long[] pattern = {0, 700, 1000};
                vibe.vibrate(pattern, 0);
            }
        }
    };

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();

        if(!AlarmeoffPress && !SnoozePress){
            final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.cancel();
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            finish();
        }}

    @Override
    public void onBackPressed() {
    }



}
