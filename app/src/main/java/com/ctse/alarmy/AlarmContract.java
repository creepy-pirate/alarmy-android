package com.ctse.alarmy;

public final class AlarmContract {

    private AlarmContract() {}

    public static class AlarmTable {

        public static final String _ID = "_ID";
        public static final String TABLE_NAME = "alarm_times";
        public static final String COLUMN_ALARM_TIME = "alarm_time";
        public static final String COLUMN_ALARM_IN_MILLIES = "alarm_in_millis";
        public static final String COLUMN_ALARM_STATUS = "alarm_status";
        public static final String COLUMN_RINGTONE_NAME = "ringtone";
        public static final String COLUMN_RINGTONE_URI = "ringtone_uri";
        public static final String COLUMN_LABEL = "label";
        public static final String COLUMN_NAME_FLAG = "flag";
    }

}
