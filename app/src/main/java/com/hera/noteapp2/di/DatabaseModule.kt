package com.hera.noteapp2.di

import android.content.Context
import androidx.room.Room
import com.hera.noteapp2.data.Repository
import com.hera.noteapp2.data.room.NoteDao
import com.hera.noteapp2.data.room.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(
            @ApplicationContext context: Context
    ) = Room
            .databaseBuilder(
                    context,
                    NoteDatabase::class.java,
                    "note_database"
            )
            .build()


    @Provides
    fun provideNoteDao(
            noteDatabase: NoteDatabase
    ) = noteDatabase.noteDao()


    @Provides
    fun provideRepository(
            noteDao: NoteDao
    ) = Repository(noteDao)
}