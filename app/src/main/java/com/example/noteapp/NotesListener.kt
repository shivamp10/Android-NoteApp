package com.example.noteapp

import com.example.noteapp.entities.Note

interface NotesListener {
    fun onNoteClicked(note: Note,position: Int){

    }
}