package com.hera.noteapp2.ui.addeditnote

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hera.noteapp2.R
import com.hera.noteapp2.data.inner.Note
import com.hera.noteapp2.databinding.FragmentAddEditNoteBinding
import com.hera.noteapp2.ui.NoteActivity
import com.hera.noteapp2.ui.notes.observerState
import com.hera.noteapp2.utils.AddEditStatus
import com.hera.noteapp2.utils.ObserverState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddEditNoteFragment : Fragment(R.layout.fragment_add_edit_note) {

    private val args: AddEditNoteFragmentArgs by navArgs()

    private val viewModel: AddEditNoteViewModel by viewModels()

    private lateinit var binding: FragmentAddEditNoteBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if (args.note != null) viewModel.addEditStatus = AddEditStatus.EDIT
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAddEditNoteBinding.bind(view)

        if (viewModel.addEditStatus == AddEditStatus.ADD) {
            (activity as AppCompatActivity).supportActionBar?.title = "Add"
        } else {
            (activity as AppCompatActivity).supportActionBar?.title = "Edit"

            binding.apply {
                val note = args.note!!
                etTitle.setText(note.title)
                etContent.setText(note.content)
                spinnerPriority.setSelection(note.priorityLevel)
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (viewModel.addEditStatus == AddEditStatus.ADD) {
            inflater.inflate(R.menu.menu_addnote_fragment, menu)
        } else {
            inflater.inflate(R.menu.menu_editnote_fragment, menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_add -> {
            binding.apply {
                val title = etTitle.text.toString()
                val content = etContent.text.toString()
                val priorityLevel = spinnerPriority.selectedItemId.toInt()

                if (!validate(title, content)) return@apply

                val note = Note(title, content, priorityLevel)
                observerState = ObserverState.INSERT
                viewModel.insert(note)
                hideKeyboard(activity as NoteActivity)
                findNavController().navigateUp()
            }
            true
        }
        R.id.action_edit -> {
            binding.apply {
                val title = etTitle.text.toString()
                val content = etContent.text.toString()
                val priorityLevel = spinnerPriority.selectedItemId.toInt()

                if (!validate(title, content)) return@apply

                observerState = ObserverState.UPDATE
                viewModel.update(args.note!!.copy(
                    title = title,
                    content = content,
                    priorityLevel = priorityLevel
                ))
                hideKeyboard(activity as NoteActivity)
                findNavController().navigateUp()
            }
            true
        }
        R.id.action_delete -> {
            observerState = ObserverState.DELETE
            viewModel.delete(args.note!!)
            hideKeyboard(activity as NoteActivity)
            findNavController().navigateUp()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }


    private fun validate(title: String, content: String): Boolean {
        var isValid = true

        if (title.isEmpty()) {
            isValid = false
            binding.etTitle.error = "empty"
        } else {
            binding.etTitle.error = null
        }

        if (content.isEmpty()) {
            isValid = false
            binding.etContent.error = "empty"
        } else {
            binding.etContent.error = null
        }

        return isValid
    }


    fun hideKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusedView = activity.currentFocus
        currentFocusedView?.let {
            inputMethodManager.hideSoftInputFromWindow(
                currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}