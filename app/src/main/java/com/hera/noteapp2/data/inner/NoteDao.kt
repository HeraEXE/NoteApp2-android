package com.hera.noteapp2.data.inner

import androidx.room.*
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


    @Query("SELECT * FROM note_table ORDER BY priority_level")
    fun getAllNotes(): Flow<List<Note>>
}