package com.hera.noteapp2.ui.notes

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.hera.noteapp2.R
import com.hera.noteapp2.data.inner.Note
import com.hera.noteapp2.databinding.FragmentNotesBinding
import com.hera.noteapp2.utils.ObserverState
import com.hera.noteapp2.utils.PriorityLevel
import com.hera.noteapp2.utils.Sort
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay


var observerState = ObserverState.SORT


@AndroidEntryPoint
class NotesFragment : Fragment(R.layout.fragment_notes), NotesAdapter.Listener {

    private val viewModel: NotesViewModel by viewModels()
    private lateinit var binding: FragmentNotesBinding
    private lateinit var adapter: NotesAdapter

    private var notePosition = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Notes"

        adapter = NotesAdapter(requireContext(), this)

        binding = FragmentNotesBinding.bind(view)
        binding.apply {
            recycler.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            recycler.adapter = adapter

            fabAddNote.setOnClickListener {
                notePosition = adapter.getLastPosition()
                val action = NotesFragmentDirections.actionNotesFragmentToAddEditNoteFragment()
                findNavController().navigate(action)
            }
        }

        viewModel.notes.observe(viewLifecycleOwner) {
            adapter.updateListOfNotes()
            when (observerState) {
                ObserverState.INSERT -> {
                    adapter.notifyItemInserted(notePosition)
                }
                ObserverState.UPDATE -> {
                    adapter.notifyItemChanged(notePosition)
                }
                ObserverState.DELETE -> {
                    adapter.notifyItemRemoved(notePosition)
                }
                ObserverState.SORT -> {
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_notes_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_only_high_level -> {
            observerState = ObserverState.SORT
            viewModel.databaseSort.value = Sort.HIGH_ONLY
            true
        }
        R.id.action_only_medium_level -> {
            observerState = ObserverState.SORT
            viewModel.databaseSort.value = Sort.MEDIUM_ONLY
            true
        }
        R.id.action_only_low_level -> {
            observerState = ObserverState.SORT
            viewModel.databaseSort.value = Sort.LOW_ONLY
            true
        }
        R.id.action_sort_by_date_new -> {
            observerState = ObserverState.SORT
            viewModel.databaseSort.value = Sort.BY_DATE_DESC
            true
        }
        R.id.action_sort_by_date_old -> {
            observerState = ObserverState.SORT
            viewModel.databaseSort.value = Sort.BY_DATE
            true
        }
        R.id.action_sort_by_high_level -> {
            observerState = ObserverState.SORT
            viewModel.databaseSort.value = Sort.BY_PRIORITY
            true
        }
        R.id.action_sort_by_low_level -> {
            observerState = ObserverState.SORT
            viewModel.databaseSort.value = Sort.BY_PRIORITY_DESC
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }


    override fun getAllNotes() = viewModel.notes.value ?: listOf()


    override fun onLayoutNoteClick(note: Note, position: Int) {
        notePosition = position
        val action = NotesFragmentDirections.actionNotesFragmentToAddEditNoteFragment(note)
        findNavController().navigate(action)
    }
}