package com.hera.noteapp2.ui.notes

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.hera.noteapp2.R
import com.hera.noteapp2.data.inner.Note
import com.hera.noteapp2.databinding.FragmentNotesBinding
import com.hera.noteapp2.ui.NoteActivity
import com.hera.noteapp2.util.Sort
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class NotesFragment : Fragment(R.layout.fragment_notes), NotesAdapter.Listener {

    private val viewModel: NotesViewModel by viewModels()
    private lateinit var binding: FragmentNotesBinding
    private lateinit var adapter: NotesAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        sharedPreferences = (activity as AppCompatActivity).getSharedPreferences("settings", Context.MODE_PRIVATE)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.show.value = sharedPreferences.getInt("show", 3)
        viewModel.sort.value = sharedPreferences.getInt("sort", 0)

        adapter = NotesAdapter(requireContext(), this)

        binding = FragmentNotesBinding.bind(view)
        binding.apply {
            recycler.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            recycler.adapter = adapter

            fabAddNote.setOnClickListener {
                val action = NotesFragmentDirections.actionNotesFragmentToAddEditNoteFragment()
                findNavController().navigate(action)
            }
        }

        lifecycleScope.launch {
            viewModel.notes.collect { notes ->
                adapter.differ.submitList(notes)
            }
        }
    }


    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.title = "Notes"
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_notes_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_only_high_level -> {
            viewModel.show.value = 0
            saveInSharedPreferences("show", 0)
            true
        }
        R.id.action_only_medium_level -> {
            viewModel.show.value = 1
            saveInSharedPreferences("show", 1)
            true
        }
        R.id.action_only_low_level -> {
            viewModel.show.value = 2
            saveInSharedPreferences("show", 2)
            true
        }
        R.id.action_all_levels -> {
            viewModel.show.value = 3
            saveInSharedPreferences("show", 3)
            true
        }
        R.id.action_sort_by_date_new -> {
            viewModel.sort.value = 0
            saveInSharedPreferences("sort", 0)
            true
        }
        R.id.action_sort_by_date_old -> {
            viewModel.sort.value = 1
            saveInSharedPreferences("sort", 1)
            true
        }
        R.id.action_sort_by_high_level -> {
            viewModel.sort.value = 2
            saveInSharedPreferences("sort", 2)
            true
        }
        R.id.action_sort_by_low_level -> {
            viewModel.sort.value = 3
            saveInSharedPreferences("sort", 3)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }


    private fun saveInSharedPreferences(key: String, value: Int) {
        sharedPreferences.edit().apply {
            putInt(key, value)
            apply()
        }
    }



    override fun onNoteClick(note: Note, position: Int) {
        val action = NotesFragmentDirections.actionNotesFragmentToAddEditNoteFragment(note)
        findNavController().navigate(action)
    }
}