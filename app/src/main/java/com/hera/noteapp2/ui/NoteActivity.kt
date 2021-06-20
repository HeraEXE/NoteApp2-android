package com.hera.noteapp2.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hera.noteapp2.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
    }
}