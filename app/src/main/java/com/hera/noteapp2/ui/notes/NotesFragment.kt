package com.hera.noteapp2.ui.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.hera.noteapp2.R
import com.hera.noteapp2.databinding.FragmentNotesBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NotesFragment : Fragment(R.layout.fragment_notes) {

    private lateinit var binding: FragmentNotesBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentNotesBinding.bind(view)

        binding.apply {
            fabAddNote.setOnClickListener {
                val action = NotesFragmentDirections.actionNotesFragmentToAddEditNoteFragment()
                findNavController().navigate(action)
            }
        }
    }

}