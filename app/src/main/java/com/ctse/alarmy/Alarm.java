package com.ctse.alarmy;

import android.net.Uri;

public class Alarm {

    private int _id;
    private String alarmTime;
    private long alarmTimeInMillis;
    private boolean alarmStatus;
    private String ringtoneName;
    private Uri ringtoneUri;
    private String label;
    private int flag;

    public Alarm() {}

    public Alarm(int _id,
                 String alarmTime,
                 long alarmTimeInMillis,
                 boolean alarmStatus,
                 String ringtoneName,
                 Uri ringtoneUri,
                 String label,
                 int flag) {
        this._id = _id;
        this.alarmTime = alarmTime;
        this.alarmTimeInMillis = alarmTimeInMillis;
        this.alarmStatus = alarmStatus;
        this.ringtoneName = ringtoneName;
        this.ringtoneUri = ringtoneUri;
        this.label = label;
        this.flag = flag;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public long getAlarmTimeInMillis() {
        return alarmTimeInMillis;
    }

    public void setAlarmTimeInMillis(long alarmTimeInMillis) {
        this.alarmTimeInMillis = alarmTimeInMillis;
    }

    public boolean isAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(boolean alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public String getRingtoneName() {
        return ringtoneName;
    }

    public void setRingtoneName(String ringtoneName) {
        this.ringtoneName = ringtoneName;
    }

    public Uri getRingtoneUri() {
        return ringtoneUri;
    }

    public void setRingtoneUri(Uri ringtoneUri) {
        this.ringtoneUri = ringtoneUri;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
