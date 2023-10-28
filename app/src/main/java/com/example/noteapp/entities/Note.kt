package com.example.noteapp.entities

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
class Note(
    @PrimaryKey(autoGenerate = true)
     var id: Int,

    @ColumnInfo(name = "title")
     var title: String,

    @ColumnInfo(name = "date_time")
     var dateTime: String,

    @ColumnInfo(name = "subtitle")
     var subtitle: String,

    @ColumnInfo(name = "note_text")
     var noteText: String,

    @ColumnInfo(name = "color")
     var color: String?

) {
    @NonNull
     override fun toString(): String {
        return "$title : $dateTime"
    }
}

