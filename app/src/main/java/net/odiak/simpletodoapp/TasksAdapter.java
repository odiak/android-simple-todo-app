package net.odiak.simpletodoapp;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class TasksAdapter extends ArrayAdapter<Task> {

    private LayoutInflater mLayoutInflater;

    public TasksAdapter (Context context) {
        super(context, 0);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TaskViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.taskrow, parent, false);
            holder = new TaskViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.textView);
            holder.icon = (ImageView) convertView.findViewById(R.id.taskIcon);
            convertView.setTag(holder);
        } else {
            holder = (TaskViewHolder) convertView.getTag();
        }

        Task task = getItem(position);
        holder.textView.setText(task.getText());

        int imgSrc, textColor;
        Resources res = convertView.getResources();
        if (task.getDone()) {
            imgSrc = android.R.drawable.checkbox_on_background;
            textColor = res.getColor(R.color.doneTaskTextColor);
        } else {
            imgSrc = android.R.drawable.checkbox_off_background;
            textColor = res.getColor(R.color.taskTextColor);
        }
        holder.icon.setImageResource(imgSrc);
        holder.textView.setTextColor(textColor);

        return convertView;
    }

    static class TaskViewHolder {
        TextView textView;
        ImageView icon;
    }
}
