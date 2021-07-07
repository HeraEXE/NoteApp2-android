package com.hera.noteapp2.ui.notes

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hera.noteapp2.R
import com.hera.noteapp2.data.inner.Note
import com.hera.noteapp2.databinding.ItemNoteBinding

class NotesAdapter(
    private val context: Context,
    private val listener: Listener
) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {



    inner class ViewHolder(private val binding: ItemNoteBinding)
        : RecyclerView.ViewHolder(binding.root) {

        private val priorityLevels: Array<String> = context.resources.getStringArray(R.array.priority_array)

        fun bind(position: Int) {
            val note = differ.currentList[position]
            binding.apply {
                tvTitle.text = note.title
                tvContent.text = note.content
                tvPriorityLevel.text = priorityLevels[note.priorityLevel]
                tvDate.text = note.dateFormatted

                layoutNote.setOnClickListener {
                    listener.onNoteClick(note, position)
                }
            }
        }
    }



    private val diffCallback = object: DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Note, newItem: Note) = oldItem == newItem
    }
    val differ = AsyncListDiffer(this, diffCallback)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)


    override fun getItemCount() = differ.currentList.size



    interface Listener {

        fun onNoteClick(note: Note, position: Int)
    }
}