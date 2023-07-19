package com.cllasify.cllasify.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Activities.RightPanel.Show_Notice;
import com.cllasify.cllasify.ModelClasses.Class_Notice;
import com.cllasify.cllasify.R;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.notesViewHolder> {

    List<Class_Notice> notice;
    private final Context context;

    public NotesAdapter(Context context) {
        this.context = context;
    }

    public void setNotices(List<Class_Notice> mDatalistNew) {
        this.notice = mDatalistNew;
    }

    @Override
    public notesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new notesViewHolder(LayoutInflater.from(context).inflate(R.layout.item_notice, parent, false));
    }

    @Override
    public void onBindViewHolder(notesViewHolder holder, int position) {

        Class_Notice notices = notice.get(position);

        holder.title.setText(notices.title);
        holder.notes.setText(notices.description);
        holder.notesDate.setText(notices.date);

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, Show_Notice.class);
            intent.putExtra("title", notices.title);
            intent.putExtra("notes", notices.description);
            intent.putExtra("date", notices.date);
            intent.putExtra("docs", notices.docs);
            context.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {
        return notice.size();
    }

    static class notesViewHolder extends RecyclerView.ViewHolder {
        TextView title, notes, notesDate;

        public notesViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notesTitle);
            notes = itemView.findViewById(R.id.notesData);
            notesDate = itemView.findViewById(R.id.notesDate);
        }
    }

}
