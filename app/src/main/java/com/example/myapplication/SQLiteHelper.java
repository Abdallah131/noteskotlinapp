package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper {
    // Database info
    private static final String DB_NAME = "Notes";
    private static final int DB_VERSION = 1;
    // Table info
    private static final String TABLE_NAME = "NotesList";
    // Columns info
    private static final String noteId = "noteId";
    private static final String dueDate = "dueDate";
    private static final String noteTitle = "noteTitle";
    private static final String noteMessage = "noteMessage";

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Query for creating the table
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                noteId + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                dueDate + " TEXT," +
                noteTitle + " TEXT," +
                noteMessage + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If the table is upgraded, drop it and recreate it with new upgrades (info)
        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public long insertUser(notesModel note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dueDate, note.getDueDate());
        values.put(noteTitle, note.getNoteTitle());
        values.put(noteMessage, note.getNoteMessage());
        long inserted = db.insert(TABLE_NAME, null, values);
        db.close();
        return inserted;
    }

    public boolean deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int success = db.delete(TABLE_NAME, noteId + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return success != -1;
    }

    public boolean updateUser(notesModel note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dueDate, note.getDueDate());
        values.put(noteTitle, note.getNoteTitle());
        values.put(noteMessage, note.getNoteMessage());
        int success = db.update(TABLE_NAME, values, noteId + " = ?", new String[]{String.valueOf(note.getNoteId())});
        db.close();
        return success != -1;
    }

    @SuppressLint("Range")
    public ArrayList<notesModel> getAllNotes() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<notesModel> notesList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor;

        try {
            cursor = db.rawQuery(query, null);
        } catch (Exception e) {
            e.printStackTrace();
            db.execSQL(query);
            return new ArrayList<>();
        }

        if (cursor.moveToFirst()) {
            do {
                notesModel note = new notesModel();
                note.setNoteId(cursor.getInt(cursor.getColumnIndex(noteId)));
                note.setDueDate(cursor.getString(cursor.getColumnIndex(dueDate)));
                note.setNoteTitle(cursor.getString(cursor.getColumnIndex(noteTitle)));
                note.setNoteMessage(cursor.getString(cursor.getColumnIndex(noteMessage)));
                notesList.add(note);
            } while (cursor.moveToNext());
        }

        return notesList;
    }
}
