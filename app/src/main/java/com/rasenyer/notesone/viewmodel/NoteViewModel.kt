package com.rasenyer.notesone.viewmodel

//--------------------------------------------------------------------------------------------------

import androidx.lifecycle.*
import com.rasenyer.notesone.db.NoteDao
import com.rasenyer.notesone.model.Note
import kotlinx.coroutines.launch

//--------------------------------------------------------------------------------------------------

class NoteViewModel(private val noteDao: NoteDao) : ViewModel() {

//--------------------------------------------------------------------------------------------------

    fun insert(title: String, description: String) {
        val note = getNewNote(title, description)
        insert(note)
    }

//--------------------------------------------------------------------------------------------------

    private fun getNewNote(title: String, description: String): Note {
        return Note(title = title, description = description)
    }

//--------------------------------------------------------------------------------------------------

    private fun insert(note: Note) {
        viewModelScope.launch { noteDao.insert(note) }
    }

//--------------------------------------------------------------------------------------------------

    fun update(id: Int, title: String, description: String) {
        val note = getUpdatedNote(id, title, description)
        update(note)
    }

//--------------------------------------------------------------------------------------------------

    private fun getUpdatedNote(id: Int, title: String, description: String): Note {
        return Note(id = id, title = title, description = description)
    }

//--------------------------------------------------------------------------------------------------

    private fun update(note: Note) {
        viewModelScope.launch { noteDao.update(note) }
    }

//--------------------------------------------------------------------------------------------------

    fun delete(note: Note) {
        viewModelScope.launch { noteDao.delete(note) }
    }

//--------------------------------------------------------------------------------------------------

    fun getNote(id: Int): LiveData<Note> { return noteDao.getNote(id).asLiveData() }

//--------------------------------------------------------------------------------------------------

    val getNotes: LiveData<List<Note>> = noteDao.getNotes().asLiveData()

//--------------------------------------------------------------------------------------------------

    fun isEntryValid(title: String, description: String): Boolean {
        if (title.isBlank() || description.isBlank()) { return false }
        return true
    }

//--------------------------------------------------------------------------------------------------

}

//--------------------------------------------------------------------------------------------------

class NoteViewModelFactory(private val noteDao: NoteDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(noteDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

//--------------------------------------------------------------------------------------------------