package com.hera.noteapp2.ui.notes

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hera.noteapp2.R
import com.hera.noteapp2.data.inner.Note
import com.hera.noteapp2.databinding.ItemNoteBinding

class NotesAdapter(
    private val context: Context,
    private val listener: Listener
) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private var notes = listener.getAllNotes()


    inner class ViewHolder(val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        val priorityLevels: Array<String> = context.resources.getStringArray(R.array.priority_array)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNoteBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]

        holder.binding.apply {
            tvTitle.text = note.title
            tvContent.text = note.content
            tvPriorityLevel.text = holder.priorityLevels[note.priorityLevel]
            tvDate.text = note.dateFormatted

            layoutNote.setOnClickListener {
                listener.onLayoutNoteClick(note, position)
            }
        }
    }


    override fun getItemCount() = notes.size


    fun updateListOfNotes() {
        notes = listener.getAllNotes()
    }


    fun getLastPosition() = notes.size



    interface Listener {


        fun getAllNotes(): List<Note>


        fun onLayoutNoteClick(note: Note, position: Int)
    }
}