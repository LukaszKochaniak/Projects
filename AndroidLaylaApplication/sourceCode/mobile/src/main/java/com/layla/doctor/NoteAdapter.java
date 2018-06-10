package com.layla.doctor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.layla.R;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {

    private List<Note> notesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date, mood, selfmood, heartrate, comment;

        public MyViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.date);
            mood = view.findViewById(R.id.mood);
            selfmood = view.findViewById(R.id.selfmood);
            heartrate = view.findViewById(R.id.heartrate);
            comment = view.findViewById(R.id.comment);
        }
    }


    public NoteAdapter(List<Note> notesList) {
        this.notesList = notesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Note note = notesList.get(position);
        holder.date.setText(note.getDate());
        holder.mood.setText(note.getMood());
        holder.selfmood.setText(note.getSelfmood());
        holder.heartrate.setText(note.getHeart());
        holder.comment.setText(note.getComment());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}