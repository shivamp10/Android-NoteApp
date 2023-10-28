package com.example.noteapp.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.noteapp.dao.NoteDao
import com.example.noteapp.entities.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {


    companion object {
        fun getDatabase(context: Context): NoteDatabase {
                return Room.databaseBuilder(
                    context,
                    NoteDatabase::class.java,
                    "notes_db"
                ).build()
        }
    }

    public abstract fun noteDao():NoteDao

}