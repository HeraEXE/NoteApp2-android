package com.hera.noteapp2.ui

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.hera.noteapp2.R
import com.hera.noteapp2.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteActivity : AppCompatActivity() {

    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        navController = findNavController(R.id.fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        hideKeyboard()
        return navController.navigateUp()
    }
}