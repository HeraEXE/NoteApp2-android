package com.hera.noteapp2.ui.notes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.hera.noteapp2.data.Repository
import com.hera.noteapp2.util.Sort
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val show = MutableStateFlow(3)
    val sort = MutableStateFlow(0)

    val notes = combine(show, sort) { show, sort ->
        Pair(show, sort)
    }.flatMapLatest { (show, sort) ->
        repository.getAllNotes(show, sort)
    }
}