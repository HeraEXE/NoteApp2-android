package com.hera.noteapp2.data.room

import androidx.room.*
import com.hera.noteapp2.data.inner.Note
import com.hera.noteapp2.util.Sort
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)


    @Update
    suspend fun update(note: Note)


    @Delete
    suspend fun delete(note: Note)


    @Query("SELECT * FROM note_table WHERE CASE WHEN :show = 3 THEN priority_level = 0 OR priority_level = 1 OR priority_level = 2 ELSE priority_level = :show END ORDER BY date")
    fun orderedByDate(show: Int): Flow<List<Note>>


    @Query("SELECT * FROM note_table WHERE CASE WHEN :show = 3 THEN priority_level = 0 OR priority_level = 1 OR priority_level = 2 ELSE priority_level = :show END ORDER BY date DESC")
    fun orderedByDateDesc(show: Int): Flow<List<Note>>


    @Query("SELECT * FROM note_table WHERE CASE WHEN :show = 3 THEN priority_level = 0 OR priority_level = 1 OR priority_level = 2 ELSE priority_level = :show END ORDER BY priority_level")
    fun orderedByPriority(show: Int): Flow<List<Note>>


    @Query("SELECT * FROM note_table WHERE CASE WHEN :show = 3 THEN priority_level = 0 OR priority_level = 1 OR priority_level = 2 ELSE priority_level = :show END ORDER BY priority_level DESC")
    fun orderedByPriorityDesc(show: Int): Flow<List<Note>>


    fun getAllNotes(show: Int, sort: Int) = when (sort) {
        0 -> orderedByDate(show)
        1 -> orderedByDateDesc(show)
        2 -> orderedByPriority(show)
        3 -> orderedByPriorityDesc(show)
        else -> orderedByDate(show)
    }
}