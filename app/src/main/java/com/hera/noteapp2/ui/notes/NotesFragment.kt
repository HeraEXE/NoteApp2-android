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
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.hera.noteapp2.R
import com.hera.noteapp2.data.inner.Note
import com.hera.noteapp2.databinding.FragmentNotesBinding
import com.hera.noteapp2.util.Constants.KEY_SHOW
import com.hera.noteapp2.util.Constants.KEY_SORT
import com.hera.noteapp2.util.Constants.SHARED_PREFERENCES_NAME
import com.hera.noteapp2.util.Constants.SHOW_ALL
import com.hera.noteapp2.util.Constants.SHOW_HIGH
import com.hera.noteapp2.util.Constants.SHOW_LOW
import com.hera.noteapp2.util.Constants.SHOW_MEDIUM
import com.hera.noteapp2.util.Constants.SORT_BY_DATE
import com.hera.noteapp2.util.Constants.SORT_BY_DATE_DESC
import com.hera.noteapp2.util.Constants.SORT_BY_PRIORITY
import com.hera.noteapp2.util.Constants.SORT_BY_PRIORITY_DESC
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class NotesFragment : Fragment(R.layout.fragment_notes), NotesAdapter.Listener {

    private val viewModel: NotesViewModel by viewModels()
    private lateinit var adapter: NotesAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        sharedPreferences = (activity as AppCompatActivity)
            .getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.show.value = sharedPreferences.getInt(KEY_SHOW, SHOW_ALL)
        viewModel.sort.value = sharedPreferences.getInt(KEY_SORT, SORT_BY_DATE_DESC)
        adapter = NotesAdapter(requireContext(), this)
        _binding = FragmentNotesBinding.bind(view)
        binding.apply {
            recycler.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            recycler.adapter = adapter

            fabAddNote.setOnClickListener {
                val action = NotesFragmentDirections.actionNotesFragmentToAddEditNoteFragment(title = "Add")
                findNavController().navigate(action)
            }
        }
        lifecycleScope.launch {
            viewModel.notes.collect { notes ->
                adapter.differ.submitList(notes)
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_notes_fragment, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = requireContext().getText(R.string.search_query_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null)
                    viewModel.query.value = newText
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_only_low_level -> {
            viewModel.show.value = SHOW_LOW
            saveInSharedPreferences(KEY_SHOW, SHOW_LOW)
            true
        }
        R.id.action_only_medium_level -> {
            viewModel.show.value = SHOW_MEDIUM
            saveInSharedPreferences(KEY_SHOW, SHOW_MEDIUM)
            true
        }
        R.id.action_only_high_level -> {
            viewModel.show.value = SHOW_HIGH
            saveInSharedPreferences(KEY_SHOW, SHOW_HIGH)
            true
        }
        R.id.action_all_levels -> {
            viewModel.show.value = SHOW_ALL
            saveInSharedPreferences(KEY_SHOW, SHOW_ALL)
            true
        }
        R.id.action_sort_by_date_new -> {
            viewModel.sort.value = SORT_BY_DATE_DESC
            saveInSharedPreferences(KEY_SORT, SORT_BY_DATE_DESC)
            true
        }
        R.id.action_sort_by_date_old -> {
            viewModel.sort.value = SORT_BY_DATE
            saveInSharedPreferences(KEY_SORT, SORT_BY_DATE)
            true
        }
        R.id.action_sort_by_low_level -> {
            viewModel.sort.value = SORT_BY_PRIORITY
            saveInSharedPreferences(KEY_SORT, SORT_BY_PRIORITY)
            true
        }
        R.id.action_sort_by_high_level -> {
            viewModel.sort.value = SORT_BY_PRIORITY_DESC
            saveInSharedPreferences(KEY_SORT, SORT_BY_PRIORITY_DESC)
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
        val action = NotesFragmentDirections.actionNotesFragmentToAddEditNoteFragment(note = note, title = "Edit")
        findNavController().navigate(action)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}