package com.hera.noteapp2.ui.addeditnote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hera.noteapp2.data.Repository
import com.hera.noteapp2.data.inner.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {


    fun insert(note: Note) = viewModelScope.launch {
        repository.insert(note)
    }


    fun update(note: Note) = viewModelScope.launch {
        repository.update(note)
    }


    fun delete(note: Note) = viewModelScope.launch {
        repository.delete(note)
    }
}