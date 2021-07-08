package com.hera.noteapp2.ui.notes

import androidx.lifecycle.ViewModel
import com.hera.noteapp2.data.Repository
import com.hera.noteapp2.util.Constants.EMPTY_QUERY
import com.hera.noteapp2.util.Constants.SHOW_ALL
import com.hera.noteapp2.util.Constants.SORT_BY_DATE_DESC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val query = MutableStateFlow(EMPTY_QUERY)
    val show = MutableStateFlow(SHOW_ALL)
    val sort = MutableStateFlow(SORT_BY_DATE_DESC)
    val notes = combine(query, show, sort) {query, show, sort ->
        Triple(query, show, sort)
    }.flatMapLatest { (query, show, sort) ->
        repository.getAllNotes(query, show, sort)
    }
}