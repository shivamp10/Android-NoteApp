package com.example.noteapp.adapters

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.NotesListener
import com.example.noteapp.R
import com.example.noteapp.entities.Note
import kotlinx.android.synthetic.main.container_note_item.view.*
import java.sql.Time
import java.util.*
import kotlin.collections.ArrayList

class NotesAdapter(var notes: List<Note>,var notesListener: NotesListener) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {
    private var timer : Timer? = null
    private  var noteSource : List<Note> = notes

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setNote(note: Note){
            itemView.textTitle.text = note.title
            if(note.subtitle.trim().isEmpty()){
                itemView.textSubtitle.visibility = View.GONE
            }
            else{
                itemView.textSubtitle.text = note.subtitle
            }
            itemView.textDateTime.text = note.dateTime
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.container_note_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
            holder.setNote(notes[position]);
            holder.itemView.layoutnote.setOnClickListener {
                notesListener.onNoteClicked(notes[position],position)
            }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return notes.size
    }

     fun searchNotes(searchQurery: String ){
        timer = Timer()
        timer!!.schedule(object :TimerTask(){
            @SuppressLint("NotifyDataSetChanged")
            override fun run() {
                if (searchQurery.trim().isEmpty()){
                    notes = noteSource
                }
                else {
                  var temp:  ArrayList<Note> =  ArrayList()
                    for (note :Note in noteSource){
                        if(note.title.toLowerCase().contains(searchQurery.toLowerCase())
                            || note.subtitle.toLowerCase().contains(searchQurery.toLowerCase())
                            || note.noteText.toLowerCase().contains(searchQurery.toLowerCase())){
                            temp.add(note)

                        }
                    }
                    notes = temp;
                }
                Handler(Looper.getMainLooper()).post {
                    notifyDataSetChanged()
                }
            }

        },500)

    }
    fun cancelTimer(){
        if (timer != null){
            timer!!.cancel()
        }
    }
}