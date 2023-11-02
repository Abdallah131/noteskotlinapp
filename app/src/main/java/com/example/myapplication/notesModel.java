package com.example.myapplication;

public class notesModel {
    private int noteId = 0;
    private String dueDate = "";
    private String noteTitle = "";
    private String noteMessage = "";

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteMessage() {
        return noteMessage;
    }

    public void setNoteMessage(String noteMessage) {
        this.noteMessage = noteMessage;
    }
}
