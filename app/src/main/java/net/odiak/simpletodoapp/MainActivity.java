package net.odiak.simpletodoapp;

import android.content.ContentValues;
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

        mDbHelper = new TodoContract.TodoDbHelper(this);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String[] projection = {
                TodoContract.Tasks._ID,
                TodoContract.Tasks.COLUMN_TEXT,
                TodoContract.Tasks.COLUMN_DONE,
        };
        String order = String.format("%s DESC", TodoContract.Tasks._ID);
        Cursor c = db.query(
                TodoContract.Tasks.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                order
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
}
