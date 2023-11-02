package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView notesRecycler;
    private NotesAdapter notesAdapter = null;
    private SQLiteHelper sqlliteHelper;
    private ImageView showAddButton;
    private TextView allnotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        allnotes = findViewById(R.id.totalofnotes);
        showAddButton = findViewById(R.id.showaddimage);
        showAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddNote();
            }
        });

        sqlliteHelper = new SQLiteHelper(this);
        notesRecycler = findViewById(R.id.notesrecycler);
        notesAdapter = new NotesAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        notesRecycler.setLayoutManager(layoutManager);
        notesRecycler.setAdapter(notesAdapter);

        getNotes();


    }
    private String formatDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private void getNotes() {
        ArrayList<notesModel> noteList = sqlliteHelper.getAllNotes();
        if (notesAdapter != null) {
            notesAdapter.addUser(noteList);
        }
        allnotes.setText(String.valueOf(noteList.size()));
    }

    private void showAddNote() {
        DialogPlus dialogBuilder = DialogPlus.newDialog(this)
                .setGravity(Gravity.BOTTOM)
                .setContentHolder(new ViewHolder(R.layout.add_note))
                .setCancelable(true)
                .create();

        View dialogView = dialogBuilder.getHolderView();
        EditText noteTitle = dialogView.findViewById(R.id.noteTitle);
        EditText noteMessage = dialogView.findViewById(R.id.noteMessage);
        Button addToDb = dialogView.findViewById(R.id.noteaddbutton);
        Button dueDateButton = dialogView.findViewById(R.id.dueDate);

        dueDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                String formattedDate = formatDate(selectedYear, selectedMonth, selectedDay);
                                dueDateButton.setText(formattedDate);
                            }
                        },
                        year,
                        month,
                        dayOfMonth
                );
                datePickerDialog.show();
            }
        });

        addToDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = noteTitle.getText().toString();
                String message = noteMessage.getText().toString();
                String duedate = dueDateButton.getText().toString();

                if (title.isEmpty() || message.isEmpty() || duedate.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                } else {
                    notesModel note = new notesModel();
                    note.setNoteTitle(title);
                    note.setNoteMessage(message);
                    note.setDueDate(duedate);
                    int status = (int) sqlliteHelper.insertUser(note);

                    if (status > -1) {
                        Toast.makeText(MainActivity.this, "Note Added successfully", Toast.LENGTH_SHORT).show();
                        dialogBuilder.dismiss();
                        getNotes();
                    } else {
                        Toast.makeText(MainActivity.this, "Record not saved", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        dialogBuilder.show();
    }

}
