package com.hera.noteapp2.ui.addeditnote

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hera.noteapp2.R
import com.hera.noteapp2.data.inner.Note
import com.hera.noteapp2.databinding.FragmentAddEditNoteBinding
import com.hera.noteapp2.util.AddEditStatus
import com.hera.noteapp2.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddEditNoteFragment : Fragment(R.layout.fragment_add_edit_note) {

    private val args: AddEditNoteFragmentArgs by navArgs()
    private val viewModel: AddEditNoteViewModel by viewModels()
    private var _binding: FragmentAddEditNoteBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if (args.note != null) viewModel.addEditStatus = AddEditStatus.EDIT
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddEditNoteBinding.bind(view)
        if (viewModel.addEditStatus == AddEditStatus.EDIT) {
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
                (activity as AppCompatActivity).hideKeyboard()
                AlertDialog
                    .Builder(requireContext())
                    .setCancelable(false)
                    .setTitle(requireContext().getText(R.string.alert_add_note_title))
                    .setMessage(requireContext().getText(R.string.alert_add_note_message))
                    .setPositiveButton(requireContext().getText(R.string.alert_add_note_positive)) { dialog, _ ->
                        val note = Note(title, content, priorityLevel)
                        viewModel.insert(note)
                        findNavController().navigateUp()
                        dialog.dismiss()
                    }
                    .setNegativeButton(requireContext().getText(R.string.alert_cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
            true
        }
        R.id.action_edit -> {
            binding.apply {
                val title = etTitle.text.toString()
                val content = etContent.text.toString()
                val priorityLevel = spinnerPriority.selectedItemId.toInt()
                if (!validate(title, content)) return@apply
                (activity as AppCompatActivity).hideKeyboard()
                AlertDialog
                    .Builder(requireContext())
                    .setCancelable(false)
                    .setTitle(requireContext().getText(R.string.alert_edit_note_title))
                    .setMessage(requireContext().getText(R.string.alert_edit_note_message))
                    .setPositiveButton(requireContext().getText(R.string.alert_edit_note_positive)) { dialog, _ ->
                        viewModel.update(args.note!!.copy(
                            title = title,
                            content = content,
                            priorityLevel = priorityLevel
                        ))
                        findNavController().navigateUp()
                        dialog.dismiss()
                    }
                    .setNegativeButton(requireContext().getText(R.string.alert_cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
            true
        }
        R.id.action_delete -> {
            (activity as AppCompatActivity).hideKeyboard()
            AlertDialog
                .Builder(requireContext())
                .setCancelable(false)
                .setTitle(requireContext().getText(R.string.alert_delete_note_title))
                .setMessage(requireContext().getText(R.string.alert_delete_note_message))
                .setPositiveButton(requireContext().getText(R.string.alert_delete_note_positive)) { dialog, _ ->
                    viewModel.delete(args.note!!)
                    findNavController().navigateUp()
                    dialog.dismiss()
                }
                .setNegativeButton(requireContext().getText(R.string.alert_cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
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
            binding.etTitle.error = requireContext().getText(R.string.et_error_empty)
        } else {
            binding.etTitle.error = null
        }
        if (content.isEmpty()) {
            isValid = false
            binding.etContent.error = requireContext().getText(R.string.et_error_empty)
        } else {
            binding.etContent.error = null
        }
        return isValid
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}