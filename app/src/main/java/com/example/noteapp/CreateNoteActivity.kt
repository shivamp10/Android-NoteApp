package com.example.noteapp

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast

import com.example.noteapp.database.NoteDatabase
import com.example.noteapp.entities.Note
import kotlinx.android.synthetic.main.activity_create_note.*

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CreateNoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)

        iconBack.setOnClickListener {
            onBackPressed()
        }
        textDateTime.text = SimpleDateFormat("EEEE,dd MMMM yyyy HH:mm a", Locale.getDefault()).format(
            Date()
        )
        iconDone.setOnClickListener {
            saveNote()
        }
        if (intent.getBooleanExtra("isViewOrUpdate",false)){
            inputNoteTitle.setText(intent.getStringExtra("noteTitle"))
            inputNoteSubtitle.setText(intent.getStringExtra("noteSubtitle"))
            inputNote.setText(intent.getStringExtra("noteText"))
            textDateTime.setText(intent.getStringExtra("dateTime"))

        }
        if (intent.getBooleanExtra("isViewOrUpdate",false)){
            layoutDeleteNote.visibility = View.VISIBLE
        }
    }


    private fun saveNote(){
        if(inputNoteTitle.text.toString()=="") {
            Toast.makeText(this, "Note title can't be empty", Toast.LENGTH_SHORT).show()
            return
        }else if(inputNoteSubtitle.text.toString() == "" && inputNote.text.toString() == ""){
            Toast.makeText(this, "Note can't be empty", Toast.LENGTH_SHORT).show()
        }

        val note : Note = Note(
            title = inputNoteTitle.text.toString(),
            dateTime = textDateTime.text.toString(),
            subtitle = inputNoteSubtitle.text.toString(),
            noteText = inputNote.text.toString(),
            color = "sdfsdf",
            id=0
            )

        if (intent.getBooleanExtra("isViewOrUpdate",false)){
            note.id = intent.getIntExtra("id",0)

        }

        class SaveNoteTask : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {
                    NoteDatabase.getDatabase(applicationContext).noteDao().insertNote(note)
                return null;
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                intent = Intent()
                setResult(RESULT_OK,intent)
                finish()
            }
        }
        SaveNoteTask().execute();
    }
}