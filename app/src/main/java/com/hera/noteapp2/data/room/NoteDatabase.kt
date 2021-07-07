package com.hera.noteapp2.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hera.noteapp2.data.inner.Note

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
}