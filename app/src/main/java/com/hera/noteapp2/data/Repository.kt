package com.hera.noteapp2.data

import com.hera.noteapp2.data.inner.Note
import com.hera.noteapp2.data.inner.NoteDao
import javax.inject.Inject

class Repository(private val noteDao: NoteDao) {

    suspend fun insert(note: Note) = noteDao.insert(note)

    suspend fun update(note: Note) = noteDao.update(note)

    suspend fun delete(note: Note) = noteDao.delete(note)

    fun getAllNotes() = noteDao.getAllNotes()
}