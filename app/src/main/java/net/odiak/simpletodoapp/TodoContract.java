package net.odiak.simpletodoapp;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


public final class TodoContract {
    public TodoContract () {}

    public static class Tasks implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_DONE = "done";
    }

    public static class TodoDbHelper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "todo.sqlite3";

        public TodoDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(String.format(
                    "CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT, %s BOOLEAN)",
                    Tasks.TABLE_NAME,
                    Tasks._ID,
                    Tasks.COLUMN_TEXT,
                    Tasks.COLUMN_DONE));
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
