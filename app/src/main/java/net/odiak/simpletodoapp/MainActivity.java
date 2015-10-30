package net.odiak.simpletodoapp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private EditText mEditText;
    private ListView mListView;
    private TasksAdapter mTaskAdapter;
    private TodoContract.TodoDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEditText = (EditText) findViewById(R.id.editText);
        mListView = (ListView) findViewById(R.id.listView);

        mTaskAdapter = new TasksAdapter(this);
        mListView.setAdapter(mTaskAdapter);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onListItemClick(position);
            }
        });

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClick();
            }
        });

        Button clearButton = (Button) findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClearButtonClick();
            }
        });

        mDbHelper = new TodoContract.TodoDbHelper(this);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String[] projection = {
                TodoContract.Tasks._ID,
                TodoContract.Tasks.COLUMN_TEXT,
                TodoContract.Tasks.COLUMN_DONE,
        };
        Cursor c = db.query(
                TodoContract.Tasks.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                TodoContract.Tasks._ID
        );

        TasksAdapter adapter = (TasksAdapter) mListView.getAdapter();
        while (c.moveToNext()) {
            String text = c.getString(c.getColumnIndexOrThrow(TodoContract.Tasks.COLUMN_TEXT));
            long id = c.getLong(c.getColumnIndexOrThrow(TodoContract.Tasks._ID));
            boolean done = c.getInt(c.getColumnIndexOrThrow(TodoContract.Tasks.COLUMN_DONE)) != 0;
            Task task = new Task()
                    .setId(id)
                    .setText(text)
                    .setDone(done);
            adapter.insert(task, 0);
        }
        c.close();
        db.close();
    }

    private void onButtonClick () {
        String text = mEditText.getText().toString();
        if (!text.isEmpty()) {
            mEditText.setText("");
            Task task = new Task().setText(text);
            mTaskAdapter.insert(task, 0);

            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(TodoContract.Tasks.COLUMN_TEXT, task.getText());
            values.put(TodoContract.Tasks.COLUMN_DONE, task.getDone());
            long taskId = db.insert(TodoContract.Tasks.TABLE_NAME, null, values);
            task.setId(taskId);
            db.close();
        }
    }

    private void onListItemClick (int position) {
        TasksAdapter adapter = (TasksAdapter) mListView.getAdapter();
        Task task = adapter.getItem(position);
        task.toggleDone();
        adapter.notifyDataSetChanged();

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TodoContract.Tasks.COLUMN_DONE, task.getDone());

        db.update(
                TodoContract.Tasks.TABLE_NAME,
                values,
                String.format("%s = ?", TodoContract.Tasks._ID),
                new String[]{"" + task.getId()}
        );
        db.close();
    }

    private void onClearButtonClick() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("Are you sure to clear all completed tasks?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeCompletedTasks();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void removeCompletedTasks() {
        ArrayList<Long> removedTaskIds = new ArrayList<>();
        for (int i = 0, count = mTaskAdapter.getCount(); i < count; ) {
            Task task = mTaskAdapter.getItem(i);
            if (task.getDone()) {
                removedTaskIds.add(task.getId());
                mTaskAdapter.remove(task);
                count--;
            } else {
                i++;
            }
        }
        if (removedTaskIds.isEmpty()) return;

        boolean first = true;
        String idsStr = "";
        for (long id: removedTaskIds) {
            if (first) {
                first = false;
            } else {
                idsStr += ",";
            }
            idsStr += "" + id;
        }
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(
                TodoContract.Tasks.TABLE_NAME,
                String.format("%s IN (%s)", TodoContract.Tasks._ID, idsStr),
                new String[]{}
        );
        db.close();
    }
}
