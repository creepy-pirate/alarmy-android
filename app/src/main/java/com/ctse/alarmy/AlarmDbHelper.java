package com.ctse.alarmy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AlarmDbHelper extends SQLiteOpenHelper {
    private static AlarmDbHelper mInstance = null;

    public static final String DATABASE_NAME = "alarm.db";
    public static final int DATABASE_VERSION = 1;

    private Context mCtx;

    private SQLiteDatabase db;

    private AlarmDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mCtx = context;
    }

    public static AlarmDbHelper getInstance(Context ctx) {
        /**
         * use the application context as suggested by CommonsWare.
         * this will ensure that you dont accidentally leak an Activitys
         * context (see this article for more information:
         * http://android-developers.blogspot.nl/2009/01/avoiding-memory-leaks.html)
         */
        if (mInstance == null) {
            mInstance = new AlarmDbHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuizContract.QuestionTable.TABLE_NAME + " ( " +
                QuizContract.QuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuizContract.QuestionTable.COLUMN_QUESTION + " TEXT, " +
                QuizContract.QuestionTable.COLUMN_OPTION1 + " TEXT, " +
                QuizContract.QuestionTable.COLUMN_OPTION2 + " TEXT, " +
                QuizContract.QuestionTable.COLUMN_OPTION3 + " TEXT, " +
                QuizContract.QuestionTable.COLUMN_OPTION4 + " TEXT, " +
                QuizContract.QuestionTable.COLUMN_ANSWER + " INTEGER" +
                ")";

        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);

        final String SQL_CREATE_ALARM_TABLE = "CREATE TABLE " +
                AlarmContract.AlarmTable.TABLE_NAME + " ( " +
                AlarmContract.AlarmTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AlarmContract.AlarmTable.COLUMN_ALARM_TIME + " TEXT, " +
                AlarmContract.AlarmTable.COLUMN_ALARM_IN_MILLIES + " INTEGER, " +
                AlarmContract.AlarmTable.COLUMN_ALARM_STATUS + " INTEGER, " +
                AlarmContract.AlarmTable.COLUMN_RINGTONE_NAME + " TEXT, " +
                AlarmContract.AlarmTable.COLUMN_RINGTONE_URI + " TEXT, " +
                AlarmContract.AlarmTable.COLUMN_LABEL + " TEXT, " +
                AlarmContract.AlarmTable.COLUMN_NAME_FLAG+ " INTEGER" +
                ")";

        db.execSQL(SQL_CREATE_ALARM_TABLE);

        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuizContract.QuestionTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AlarmContract.AlarmTable.TABLE_NAME);
        onCreate(db);
    }

    private void fillQuestionsTable() {
        Question q1 = new Question("1 + 1 = ", "2", "3", "4", "5", 1);
        addQuestion(q1);

        Question q2 = new Question("2 + 2 = ", "2", "3", "4", "5", 3);
        addQuestion(q2);

        Question q3 = new Question("4 * 2 = ", "2", "3", "4", "8", 4);
        addQuestion(q3);

        Question q4 = new Question("7 * 3 = ", "20", "23", "21", "25", 3);
        addQuestion(q4);

        Question q5 = new Question("6 - 1 = ", "2", "3", "4", "5", 4);
        addQuestion(q5);
    }

    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuizContract.QuestionTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuizContract.QuestionTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuizContract.QuestionTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuizContract.QuestionTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuizContract.QuestionTable.COLUMN_OPTION4, question.getOption4());
        cv.put(QuizContract.QuestionTable.COLUMN_ANSWER, question.getAnswer());

        db.insert(QuizContract.QuestionTable.TABLE_NAME, null, cv);
    }

    public Question getAQuestion() {
        Question question = new Question();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuizContract.QuestionTable.TABLE_NAME + " ORDER BY RANDOM() LIMIT 1", null);

        if (c.moveToFirst()) {
            do {
                question.setQuestion(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION4)));
                question.setAnswer(c.getInt(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_ANSWER)));
            } while (c.moveToNext());
        }

        c.close();

        return question;
    }

    public void addAlarm(Alarm alarm) {
        ContentValues cv = new ContentValues();
        cv.put(AlarmContract.AlarmTable.COLUMN_ALARM_TIME, alarm.getAlarmTime());
        cv.put(AlarmContract.AlarmTable.COLUMN_ALARM_IN_MILLIES, alarm.getAlarmTimeInMillis());
        cv.put(AlarmContract.AlarmTable.COLUMN_ALARM_STATUS, alarm.isAlarmStatus() ? 1 : 0);
        cv.put(AlarmContract.AlarmTable.COLUMN_RINGTONE_NAME, alarm.getRingtoneName());
        cv.put(AlarmContract.AlarmTable.COLUMN_RINGTONE_URI, alarm.getRingtoneUri().toString());
        cv.put(AlarmContract.AlarmTable.COLUMN_LABEL, alarm.getLabel());
        cv.put(AlarmContract.AlarmTable.COLUMN_NAME_FLAG, alarm.getFlag());

        db.insert(AlarmContract.AlarmTable.TABLE_NAME, null, cv);
    }

    public void updateAlarm(Alarm alarm) {
        ContentValues cv = new ContentValues();
        cv.put(AlarmContract.AlarmTable.COLUMN_ALARM_TIME, alarm.getAlarmTime());
        cv.put(AlarmContract.AlarmTable.COLUMN_ALARM_IN_MILLIES, alarm.getAlarmTimeInMillis());
        cv.put(AlarmContract.AlarmTable.COLUMN_ALARM_STATUS, alarm.isAlarmStatus() ? 1 : 0);
        cv.put(AlarmContract.AlarmTable.COLUMN_RINGTONE_NAME, alarm.getRingtoneName());
        cv.put(AlarmContract.AlarmTable.COLUMN_RINGTONE_URI, alarm.getRingtoneUri().toString());
        cv.put(AlarmContract.AlarmTable.COLUMN_LABEL, alarm.getLabel());
        cv.put(AlarmContract.AlarmTable.COLUMN_NAME_FLAG, alarm.getFlag());

        String where = "id = ?";
        String[] whereAgs = new String[] {String.valueOf(AlarmContract.AlarmTable._ID)};

        db.update(AlarmContract.AlarmTable.TABLE_NAME, cv, where, whereAgs);
    }

    public void updateAlarmStatus(String alarmTime) {
        ContentValues cv = new ContentValues();
        cv.put(AlarmContract.AlarmTable.COLUMN_ALARM_STATUS, 0);

        String where = AlarmContract.AlarmTable.COLUMN_ALARM_TIME + " = ?";
        String[] whereAgs = new String[] {alarmTime};

        db.update(AlarmContract.AlarmTable.TABLE_NAME, cv, where, whereAgs);
    }

    public List<Alarm> getAllAlarms() {
        List<Alarm> alarmList = new ArrayList<>();

        db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + AlarmContract.AlarmTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Alarm alarm = new Alarm();

                alarm.setAlarmTime(c.getString(c.getColumnIndex(AlarmContract.AlarmTable.COLUMN_ALARM_TIME)));
                alarm.setAlarmTimeInMillis(c.getInt(c.getColumnIndex(AlarmContract.AlarmTable.COLUMN_ALARM_IN_MILLIES)));
                alarm.setAlarmStatus(c.getInt(c.getColumnIndex(AlarmContract.AlarmTable.COLUMN_ALARM_STATUS)) == 1 ? true : false);
                alarm.setRingtoneName(c.getString(c.getColumnIndex(AlarmContract.AlarmTable.COLUMN_RINGTONE_NAME)));
                alarm.setRingtoneUri(Uri.parse(c.getString(c.getColumnIndex(AlarmContract.AlarmTable.COLUMN_RINGTONE_URI))));
                alarm.setLabel(c.getString(c.getColumnIndex(AlarmContract.AlarmTable.COLUMN_LABEL)));
                alarm.setFlag(c.getInt(c.getColumnIndex(AlarmContract.AlarmTable.COLUMN_NAME_FLAG)));

                alarmList.add(alarm);
            } while (c.moveToNext());
        }

        c.close();

        return alarmList;
    }

    public boolean deleteAlarm(String alarmTime) {
        Log.d("dbhelper", "delete method");

        String value = String.valueOf(alarmTime);
        String where = AlarmContract.AlarmTable.COLUMN_ALARM_TIME + " = ?";
        String[] whereAgs = new String[] {alarmTime};

        return db.delete(AlarmContract.AlarmTable.TABLE_NAME, where, whereAgs) > 0;
    }


}
