package com.rasenyer.notesone.adapter

//--------------------------------------------------------------------------------------------------

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rasenyer.notesone.databinding.ItemNoteBinding
import com.rasenyer.notesone.model.Note

//--------------------------------------------------------------------------------------------------

class NoteAdapter(private val onItemClicked: (Note) -> Unit) : ListAdapter<Note, NoteAdapter.MyViewHolder>(DiffCallback) {

//--------------------------------------------------------------------------------------------------

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemNoteBinding.inflate(LayoutInflater.from(parent.context)))
    }

//--------------------------------------------------------------------------------------------------

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener { onItemClicked(current) }
        holder.bind(current)
    }

//--------------------------------------------------------------------------------------------------

    class MyViewHolder(private var binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {

            binding.mTextViewTitle.text = note.title
            binding.mTextViewDescription.text = note.description

        }

    }

//--------------------------------------------------------------------------------------------------

    companion object {

        private val DiffCallback = object : DiffUtil.ItemCallback<Note>() {

            override fun areItemsTheSame(oldNote: Note, newNote: Note): Boolean {
                return oldNote === newNote
            }

            override fun areContentsTheSame(oldNote: Note, newNote: Note): Boolean {
                return oldNote.title == newNote.title
            }

        }

    }

//--------------------------------------------------------------------------------------------------

}