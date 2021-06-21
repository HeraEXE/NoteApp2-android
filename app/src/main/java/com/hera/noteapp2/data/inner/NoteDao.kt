package com.hera.noteapp2.data.inner

import androidx.room.*
import com.hera.noteapp2.utils.Sort
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)


    @Update
    suspend fun update(note: Note)


    @Delete
    suspend fun delete(note: Note)


    @Query("SELECT * FROM note_table ORDER BY date")
    fun orderedByDate(): Flow<List<Note>>


    @Query("SELECT * FROM note_table ORDER BY date DESC")
    fun orderedByDateDesc(): Flow<List<Note>>


    @Query("SELECT * FROM note_table ORDER BY priority_level")
    fun orderedByPriority(): Flow<List<Note>>


    @Query("SELECT * FROM note_table ORDER BY priority_level DESC")
    fun orderedByPriorityDesc(): Flow<List<Note>>


    @Query("SELECT * FROM note_table WHERE priority_level = 0")
    fun onlyHighPriority(): Flow<List<Note>>


    @Query("SELECT * FROM note_table WHERE priority_level = 1")
    fun onlyMediumPriority(): Flow<List<Note>>


    @Query("SELECT * FROM note_table WHERE priority_level = 2")
    fun onlyLowPriority(): Flow<List<Note>>


    fun getAllNotes(sort: Sort) = when (sort) {
        Sort.BY_DATE -> orderedByDate()
        Sort.BY_DATE_DESC -> orderedByDateDesc()
        Sort.BY_PRIORITY -> orderedByPriority()
        Sort.BY_PRIORITY_DESC -> orderedByPriorityDesc()
        Sort.HIGH_ONLY -> onlyHighPriority()
        Sort.MEDIUM_ONLY -> onlyMediumPriority()
        Sort.LOW_ONLY -> onlyLowPriority()
    }
}