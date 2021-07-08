package com.hera.noteapp2.data.room

import androidx.room.*
import com.hera.noteapp2.data.inner.Note
import com.hera.noteapp2.util.Constants.SORT_BY_DATE
import com.hera.noteapp2.util.Constants.SORT_BY_DATE_DESC
import com.hera.noteapp2.util.Constants.SORT_BY_PRIORITY
import com.hera.noteapp2.util.Constants.SORT_BY_PRIORITY_DESC
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)


    @Update
    suspend fun update(note: Note)


    @Delete
    suspend fun delete(note: Note)


    @Query("SELECT * FROM note_table WHERE CASE WHEN :show = 3 THEN priority_level = 0 OR priority_level = 1 OR priority_level = 2 ELSE priority_level = :show END AND title LIKE '%' || :query || '%' ORDER BY date")
    fun orderedByDate(query: String, show: Int): Flow<List<Note>>


    @Query("SELECT * FROM note_table WHERE CASE WHEN :show = 3 THEN priority_level = 0 OR priority_level = 1 OR priority_level = 2 ELSE priority_level = :show END AND title LIKE '%' || :query || '%' ORDER BY date DESC")
    fun orderedByDateDesc(query: String, show: Int): Flow<List<Note>>


    @Query("SELECT * FROM note_table WHERE CASE WHEN :show = 3 THEN priority_level = 0 OR priority_level = 1 OR priority_level = 2 ELSE priority_level = :show END AND title LIKE '%' || :query || '%' ORDER BY priority_level")
    fun orderedByPriority(query: String, show: Int): Flow<List<Note>>


    @Query("SELECT * FROM note_table WHERE CASE WHEN :show = 3 THEN priority_level = 0 OR priority_level = 1 OR priority_level = 2 ELSE priority_level = :show END AND title LIKE '%' || :query || '%' ORDER BY priority_level DESC")
    fun orderedByPriorityDesc(query: String, show: Int): Flow<List<Note>>


    fun getAllNotes(query: String, show: Int, sort: Int) = when (sort) {
        SORT_BY_DATE_DESC -> orderedByDateDesc(query, show)
        SORT_BY_DATE -> orderedByDate(query, show)
        SORT_BY_PRIORITY -> orderedByPriority(query, show)
        SORT_BY_PRIORITY_DESC -> orderedByPriorityDesc(query, show)
        else -> orderedByDateDesc(query, show)
    }
}