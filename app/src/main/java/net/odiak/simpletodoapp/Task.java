package net.odiak.simpletodoapp;

import android.provider.BaseColumns;

public class Task implements BaseColumns {
    public static final String TABLE_NAME = "tasks";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_DONE = "done";

    private long mId;
    private String mText;
    private boolean mDone;

    public Task(String text, boolean done, long id) {
        mId = id;
        mText = text;
        mDone = done;
    }

    public Task() {
        this("", false, 0);
    }

    public long getId() {
        return mId;
    }

    public String getText() {
        return mText;
    }

    public boolean getDone() {
        return mDone;
    }

    public Task setId(long id) {
        mId = id;
        return this;
    }

    public Task setText(String text) {
        mText = text;
        return this;
    }

    public Task setDone(boolean done) {
        mDone = done;
        return this;
    }

    public Task toggleDone() {
        mDone = !mDone;
        return this;
    }
}
