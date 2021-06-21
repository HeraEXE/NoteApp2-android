package com.hera.noteapp2.ui.notes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hera.noteapp2.data.Repository
import com.hera.noteapp2.data.inner.Note
import com.hera.noteapp2.utils.Sort
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    var notePosition = 0

    val databaseSort = MutableStateFlow(Sort.BY_DATE)

    val notes = databaseSort.flatMapLatest { sort ->
        repository.getAllNotes(sort)
    }.asLiveData()


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