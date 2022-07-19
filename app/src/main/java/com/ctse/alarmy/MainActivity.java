package com.ctse.alarmy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // variable declarations
    private FloatingActionButton addAlarmButton;

    private AlarmDbHelper dbHelper;
    private Alarm alarm;

    // list to save database alarm data
    private ArrayList<String> mAlarmTimes = new ArrayList<>();
    private ArrayList<Long> mAlarmTimesInMillis = new ArrayList<>();
    private ArrayList<Boolean> mAlarmStatuses = new ArrayList<>();
    private ArrayList<String> mRingtoneNames = new ArrayList<>();
    private ArrayList<String> mRingtoneUris = new ArrayList<>();
    private ArrayList<String> mLabels = new ArrayList<>();
    private ArrayList<Integer> mFlags = new ArrayList<>();

    private RecylcerViewAdapter adapter;

    private Intent alarmIntent;

    private static final int SET_ALARM_INTENET_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // database helper
        dbHelper = AlarmDbHelper.getInstance(this);
        alarm = new Alarm();

        // get all alarms from db
        getAlarms();

        // intent to pass to broadcast receiver
        alarmIntent = new Intent(this, AlarmReceiver.class);

        addAlarmButton = findViewById(R.id.addAlarmButton);

        // add new alarm
        addAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setAlarmIntent = new Intent(MainActivity.this, SetAlarmActivity.class);
                MainActivity.this.startActivityForResult(setAlarmIntent, SET_ALARM_INTENET_REQUEST_CODE);
            }
        });

    }

    // get result from new alarm activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        adapter.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SET_ALARM_INTENET_REQUEST_CODE) {
                String time = data.getStringExtra("timeString");
                long millis = data.getLongExtra("timeInMillis", 0);
                String label = data.getStringExtra("label");
                String ringtoneUri = data.getStringExtra("ringtoneUri");
                String ringtoneName = data.getStringExtra("ringtoneName");
                int flag = data.getIntExtra("flags", 0);

                Log.d("intent result", time);
                Log.d("intent result", String.valueOf(millis));
                Log.d("intent result", label);
                Log.d("intent result", ringtoneUri);
                Log.d("intent result", ringtoneName);
                Log.d("intent result", String.valueOf(flag));

                // create alarm
                setAlarm(time, millis, label,ringtoneUri,ringtoneName, flag);

            }
        }
    }


    // create new alarm
    private void setAlarm(String time, long timeInMillis, String label, String ringtoneUri, String ringtoneName, int flag) {
        alarm.setAlarmTime(time);
        alarm.setAlarmTimeInMillis(timeInMillis);
        alarm.setLabel(label);
        alarm.setRingtoneUri(Uri.parse(ringtoneUri));
        alarm.setRingtoneName(ringtoneName);
        alarm.setAlarmStatus(true);
        alarm.setFlag(flag);

        saveAlarm();
        getAlarms();
    }


    // save new alarm to db
    public void saveAlarm() {
        dbHelper.addAlarm(alarm);
    }

    // get all alarms
    public void getAlarms() {
        List<Alarm> alarmList = dbHelper.getAllAlarms();

        mAlarmTimes.clear();
        mAlarmTimesInMillis.clear();
        mAlarmStatuses.clear();
        mRingtoneNames.clear();
        mRingtoneUris.clear();
        mLabels.clear();
        mFlags.clear();

        for (Alarm alarm : alarmList) {
            mAlarmTimes.add(alarm.getAlarmTime());
            mAlarmTimesInMillis.add(alarm.getAlarmTimeInMillis());
            mAlarmStatuses.add(alarm.isAlarmStatus());
            mRingtoneNames.add(alarm.getRingtoneName());
            mRingtoneUris.add(alarm.getRingtoneUri().toString());
            mLabels.add(alarm.getLabel());
            mFlags.add(alarm.getFlag());
        }

        initRecyclerView();
    }

    // initialize recycler view
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new RecylcerViewAdapter(mAlarmTimes,
                mAlarmTimesInMillis,
                mAlarmStatuses,
                mRingtoneNames,
                mRingtoneUris,
                mLabels,
                mFlags,
                this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // delete alarm
    public void deleteAlarm(String timeInMillis, int position) {
        Log.d("main", "before if");
        if (dbHelper.deleteAlarm(timeInMillis)) {
            Log.d("main", "row deleted");
//            cancelAlarm(mFlags.get(position));
            getAlarms();
        }
    }

//    public void cancelAlarm(int flag) {
//
//    }
}
