package com.example.myapplication;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder> {
    private SQLiteHelper sqlliteHelper;
    private ArrayList<notesModel> notesList = new ArrayList<>();

    public void addUser(ArrayList<notesModel> noteItem) {
        this.notesList = noteItem;
        notifyDataSetChanged();
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View noteItem = layoutInflater.inflate(R.layout.note_item, parent, false);
        return new NotesViewHolder(noteItem);
    }

    @Override
    public void onBindViewHolder(NotesViewHolder holder, @SuppressLint("RecyclerView") int position) {
        notesModel note = notesList.get(position);
        holder.bind(note);
        sqlliteHelper = new SQLiteHelper(holder.view.getContext());

        holder.delbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean status = sqlliteHelper.deleteUser(note.getNoteId());
                if (status) {
                    sqlliteHelper.deleteUser(note.getNoteId());
                    Toast.makeText(holder.view.getContext(), "Note Deleted", Toast.LENGTH_SHORT).show();
                    notesList.remove(note);
                    notifyItemRemoved(position);
                    sqlliteHelper.getAllNotes();
                } else {
                    Toast.makeText(holder.view.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}

class NotesViewHolder extends RecyclerView.ViewHolder {
    View view;
    TextView dueDate;
    TextView noteTitle;
    TextView noteMessage;
    ImageView delbutton;

    public NotesViewHolder(View view) {
        super(view);
        this.view = view;
        dueDate = view.findViewById(R.id.dueDateRec);
        noteTitle = view.findViewById(R.id.notesTitleRec);
        noteMessage = view.findViewById(R.id.notesMessageRec);
        delbutton = view.findViewById(R.id.notesDelButton);
    }

    public void bind(notesModel note) {
        dueDate.setText(note.getDueDate());
        noteTitle.setText(note.getNoteTitle());
        noteMessage.setText(String.valueOf(note.getNoteMessage()));
    }
}
