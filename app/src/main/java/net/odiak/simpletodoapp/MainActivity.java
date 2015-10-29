package net.odiak.simpletodoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private EditText mEditText;
    private Button mButton;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEditText = (EditText) findViewById(R.id.editText);
        mButton = (Button) findViewById(R.id.button);
        mListView = (ListView) findViewById(R.id.listView);

        final TasksAdapter adapter = new TasksAdapter(this);
        mListView.setAdapter(adapter);

        adapter.addAll(
                new Task().setText("ほげ"),
                new Task().setText("ふが"),
                new Task().setText("ピヨ"));

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = mEditText.getText().toString();
                if (!text.isEmpty()) {
                    mEditText.setText("");
                    adapter.insert(new Task().setText(text), 0);
                }
            }
        });

        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TasksAdapter adapter = (TasksAdapter) mListView.getAdapter();
        Task task = adapter.getItem(position);
        task.toggleDone();
        adapter.notifyDataSetChanged();
    }
}
