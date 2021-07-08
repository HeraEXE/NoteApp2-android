package com.hera.noteapp2.ui.notes

import androidx.lifecycle.ViewModel
import com.hera.noteapp2.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val query = MutableStateFlow("")
    val show = MutableStateFlow(3)
    val sort = MutableStateFlow(0)
    val notes = combine(query, show, sort) {query, show, sort ->
        Triple(query, show, sort)
    }.flatMapLatest { (query, show, sort) ->
        repository.getAllNotes(query, show, sort)
    }
}