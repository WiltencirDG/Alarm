package com.wiltbr.despertador;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private TextView TextAlarme;
    private RadioGroup rgBtn;
    private Calendar cal = Calendar.getInstance();
    private String date;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent s = new Intent(MainActivity.this, Settings.class);
                s.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(s);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button BotaoAlarme;
        ImageView BotaoDeletar;

        TextAlarme = (TextView) findViewById(R.id.alarme);
        BotaoAlarme = (Button) findViewById(R.id.BtnDeitei);
        BotaoDeletar = (ImageView) findViewById(R.id.excBtn);
        Boolean snoozed = getIntent().getBooleanExtra("snoozed",false);

        if(snoozed){
            int i = getIntent().getIntExtra("snoozetime",1);

            cal.setTime(new Date());
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            cal.add(Calendar.MINUTE,i);
            date = (sdf.format(cal.getTime()));

            Intent O = new Intent(MainActivity.this, Alarm.class);
            PendingIntent pend = PendingIntent.getBroadcast(getApplicationContext(),0,O,0);
            AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarm.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),pend);

            TextAlarme.setText(String.valueOf(date));
            Toast.makeText(MainActivity.this, "Alarme tocará em "+ i + " minutos.", Toast.LENGTH_SHORT).show();

        }

        BotaoAlarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rgBtn = (RadioGroup) findViewById(R.id.rg);
                int op = rgBtn.getCheckedRadioButtonId();
                if (op == R.id.radioButtonPouco){
                    cal.setTime(new Date());
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    cal.add(Calendar.HOUR,6);
                    cal.add(Calendar.MINUTE,10);
                    date = (sdf.format(cal.getTime()));
                    Toast.makeText(MainActivity.this, "Alarme tocará em 6 horas e 10 minutos.", Toast.LENGTH_SHORT).show();
                }else if (op == R.id.radioButtonMuito){
                    cal.setTime(new Date());
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//                    cal.add(Calendar.HOUR,9);
//                    cal.add(Calendar.MINUTE,7);
                    cal.add(Calendar.SECOND, 5);
                    date = (sdf.format(cal.getTime()));
//                    Toast.makeText(MainActivity.this, "Alarme tocará em 9 horas e 7 minutos.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "Alarme tocará em 5 segundos.", Toast.LENGTH_SHORT).show();
                }else{
                    cal.setTime(new Date());
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    cal.add(Calendar.HOUR,7);
                    cal.add(Calendar.MINUTE,30);
                    cal.add(Calendar.MINUTE,7);
                    date = (sdf.format(cal.getTime()));
                    Toast.makeText(MainActivity.this, "Alarme tocará em 7 horas e 37 minutos.", Toast.LENGTH_SHORT).show();
                }

                TextAlarme.setText(String.valueOf(date));

                Intent i = new Intent(MainActivity.this, Alarm.class);
                i.putExtra("date",date);
                PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),0,i,0);
                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                am.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),pi);
            }
        });

        BotaoDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextAlarme.setText("--:--");
                Toast.makeText(MainActivity.this, "Alarme desligado.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, Alarm.class);
                PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),0,i,0);
                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                am.cancel(pi);
            }
        });
    }
}
