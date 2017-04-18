package com.example.ilia.hw1;

import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private ImageView battery;
    private TextView battery_percent;
    private TextView battery_technology;
    private TextView battery_status;

    private BroadcastReceiver battery_receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);



            float batteryPct = ((float) level / (float)scale) * 100f;
            int percent = Math.round(batteryPct);

            String technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
            String status = getResources().getStringArray(R.array.battery_status)[getBatteryStatus(intent)];

            battery.getDrawable().setLevel(percent *100);
            battery_percent.setText(getString(R.string.battery_percent_format, percent));
            battery_technology.setText(getString(R.string.battery_technology_format, technology));
            battery_status.setText(getString(R.string.battery_status_format, status));


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        battery = (ImageView) findViewById(R.id.battery_image);
        battery_percent = (TextView) findViewById(R.id.battery_percent);
        battery_technology = (TextView) findViewById(R.id.battery_technology);
        battery_status = (TextView) findViewById(R.id.battery_status);

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(battery_receiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(battery_receiver);
    }

    private boolean battery_present(Intent intent){
        return  intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, true);
    }

    private int getBatteryStatus(Intent intent){
        int state = 0;
        if (battery_present(intent)){
            state = intent.getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_UNKNOWN);
        }
        return state;
    }
}
