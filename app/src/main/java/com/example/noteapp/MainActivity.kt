package com.example.noteapp

import android.annotation.SuppressLint
import android.content.Entity
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.adapters.NotesAdapter
import com.example.noteapp.database.NoteDatabase
import com.example.noteapp.entities.Note
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable

class MainActivity : AppCompatActivity(),NotesListener {
     val REQUEST_CODE_ADD_NOTE = 1
    val REQUEST_CODE_UPDATE_NOTE = 2
    val REQUEST_CODE_SHOW_NOTES = 3

    private var noteClickedPosition = -1

     var noteList = ArrayList<Note>()
     lateinit var noteAdapter : NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imgAddNote.setOnClickListener {
            val intent =  Intent(applicationContext,CreateNoteActivity::class.java)
            startActivityForResult(intent,REQUEST_CODE_ADD_NOTE)
        }
        notesRecyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        noteAdapter = NotesAdapter(noteList,this)
        notesRecyclerView.adapter = noteAdapter
        getNotes(REQUEST_CODE_SHOW_NOTES)
        inputSearch.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                noteAdapter.cancelTimer()
            }

            override fun afterTextChanged(s: Editable?) {
                if (noteList.size != 0){
                    noteAdapter.searchNotes(s.toString())
                }
            }

        })

    }

    override fun onNoteClicked(note: Note, position: Int) {
        noteClickedPosition = position
        intent = Intent(applicationContext,CreateNoteActivity::class.java)
        intent.putExtra("isViewOrUpdate",true)
        intent.putExtra("noteTitle",note.title)
        intent.putExtra("noteSubtitle",note.subtitle)
        intent.putExtra("noteText",note.noteText)
        intent.putExtra("dateTime",note.dateTime)
        intent.putExtra("id",note.id)
        startActivityForResult(intent,REQUEST_CODE_UPDATE_NOTE)

    }
    private fun getNotes(requestCode: Int){
        class GetNotesTask : AsyncTask<Void, Void, List<Note>>() {
            override fun doInBackground(vararg params: Void?): List<Note> {
                return NoteDatabase.getDatabase(applicationContext).noteDao().getAllNotes()
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onPostExecute(result: List<Note>?) {
                super.onPostExecute(result)
                if(requestCode == REQUEST_CODE_SHOW_NOTES){
                    if (result != null) {
                        noteList.addAll(result)
                        noteAdapter.notifyDataSetChanged()
                    }
                }
                else if(requestCode == REQUEST_CODE_ADD_NOTE){
                    if (result != null) {
                        noteList.add(0,result[0])
                        noteAdapter.notifyItemInserted(0)
                        notesRecyclerView.smoothScrollToPosition(0)
                    }
                }else if(requestCode == REQUEST_CODE_UPDATE_NOTE){
                    if (result != null) {
                        noteList.removeAt(noteClickedPosition)
                        noteList.add(noteClickedPosition,result[noteClickedPosition])
                        noteAdapter.notifyItemChanged(noteClickedPosition)
                    }
                }
            }

        }
        GetNotesTask().execute()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK){
            getNotes(REQUEST_CODE_ADD_NOTE)
        }else if (requestCode == REQUEST_CODE_UPDATE_NOTE){
            if(data != null){
                getNotes(REQUEST_CODE_UPDATE_NOTE)
            }
        }
    }

}
