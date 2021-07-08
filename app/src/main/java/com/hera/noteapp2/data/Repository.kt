package com.hera.noteapp2.data

import com.hera.noteapp2.data.inner.Note
import com.hera.noteapp2.data.room.NoteDao

class Repository(private val noteDao: NoteDao) {

    suspend fun insert(note: Note) = noteDao.insert(note)


    suspend fun update(note: Note) = noteDao.update(note)


    suspend fun delete(note: Note) = noteDao.delete(note)


    fun getAllNotes(query: String, show: Int, sort: Int) = noteDao.getAllNotes(query, show, sort)
}