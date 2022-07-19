package com.ctse.alarmy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class SetAlarmActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    // declare variables
    private TextView textViewTimePicker;
    private Button ringtonePickerButton;
    private EditText label;
    private Button setAlarmButton;

    private Calendar calendar;

    private Uri ringtoneUri;
    private String ringtoneName;

    private Intent alarmIntent;
    private PendingIntent pendingIntent;

    private static final int RINGTONE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        // initialize variables
        textViewTimePicker = findViewById(R.id.text_view_time_picker);
        ringtonePickerButton = findViewById(R.id.button_rigtone_picker);
        label = findViewById(R.id.textbox_label);
        setAlarmButton = findViewById(R.id.button_start_alarm);

        // calendar instance
        calendar = Calendar.getInstance();

        // alarmIntent
        alarmIntent = new Intent(this, AlarmReceiver.class);

        // ringtone
        ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        setRingtone(ringtoneUri);

        // time picker
        textViewTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        // ringtone picker
        ringtonePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
                startActivityForResult(intent, RINGTONE_REQUEST_CODE);
            }
        });


        // set alarm
        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String time = (String) textViewTimePicker.getText();
                String labelText = label.getText().toString();

                // alarm manager
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

                Intent intent = new Intent();
                intent.putExtra("timeString", time);
                intent.putExtra("timeInMillis", calendar.getTimeInMillis());
                intent.putExtra("label", labelText);
                intent.putExtra("ringtoneUri", ringtoneUri.toString());
                intent.putExtra("ringtoneName", ringtoneName);
                intent.putExtra("flags", (int) calendar.getTimeInMillis());

                setResult(RESULT_OK, intent);

                Toast.makeText(SetAlarmActivity.this, "Alarm is set for " + time, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    // time set callback
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        textViewTimePicker.setText(hourOfDay + ":" + minute);

        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        alarmIntent.putExtra("alarmTime", textViewTimePicker.getText());
    }

    // result from activity for ringtone picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RINGTONE_REQUEST_CODE) {
                ringtoneUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                setRingtone(ringtoneUri);
            }
        }
    }

    // set ringtone
    private void setRingtone(Uri uri) {
        Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
        ringtoneName = ringtone.getTitle(this);

        ringtonePickerButton.setText(ringtoneName);

        alarmIntent.putExtra("ringtoneUri", ringtoneUri.toString());

        setPendingIntent();
    }

    // create pending intent
    private void setPendingIntent() {
        final int _id = (int) calendar.getTimeInMillis();
        pendingIntent = PendingIntent.getBroadcast(this, _id, alarmIntent, 0);
    }

//    public void cancelAlarm(int flag) {
//        alarmIntent = new Intent(this, AlarmReceiver.class);
//        pendingIntent = PendingIntent.getBroadcast(this, flag, alarmIntent, 0);
//
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        alarmManager.cancel(pendingIntent);
//
//        finish();
//    }

}
