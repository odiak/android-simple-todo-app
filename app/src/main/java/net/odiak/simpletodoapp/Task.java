package net.odiak.simpletodoapp;

public class Task {
    private long mId;
    private String mText;
    private boolean mDone;

    public Task (String text, boolean done, long id) {
        mId = id;
        mText = text;
        mDone = done;
    }

    public Task() {
        this("", false, 0);
    }

    public long getId () {
        return System.identityHashCode(this);
    }

    public String getText () {
        return mText;
    }

    public boolean getDone () {
        return mDone;
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
