package com.rasenyer.notesone.ui.fragments

//--------------------------------------------------------------------------------------------------

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rasenyer.notesone.model.Note
import com.rasenyer.notesone.databinding.FragmentDetailsBinding
import com.rasenyer.notesone.R
import com.rasenyer.notesone.utils.NoteApplication
import com.rasenyer.notesone.viewmodel.NoteViewModel
import com.rasenyer.notesone.viewmodel.NoteViewModelFactory

//--------------------------------------------------------------------------------------------------

class DetailsFragment : Fragment() {

//--------------------------------------------------------------------------------------------------

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

//--------------------------------------------------------------------------------------------------

    private val detailsFragmentArgs: DetailsFragmentArgs by navArgs()
    lateinit var note: Note

//--------------------------------------------------------------------------------------------------

    private val noteViewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory((activity?.application as NoteApplication).noteDatabase.noteDao())
    }

//--------------------------------------------------------------------------------------------------

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

//--------------------------------------------------------------------------------------------------

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = detailsFragmentArgs.id

        noteViewModel.getNote(id).observe(this.viewLifecycleOwner) { selectedItem ->

            try {
                note = selectedItem!!
                bind(note)
            }catch (e: Exception){
                Toast.makeText(context, "Error: $e", Toast.LENGTH_LONG).show()
            }

        }

    }

//--------------------------------------------------------------------------------------------------

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//--------------------------------------------------------------------------------------------------

    private fun bind(item: Note) {

        binding.apply {

            mTextViewTitle.text = item.title
            mTextViewDescription.text = item.description

            mButtonEdit.setOnClickListener { go() }
            mButtonDelete.setOnClickListener { showConfirmationDialog() }

        }

    }

//--------------------------------------------------------------------------------------------------

    private fun go() {
        val action = DetailsFragmentDirections.actionDetailsFragmentToAddFragment(getString(R.string.edit_note), note.id)
        this.findNavController().navigate(action)
    }

//--------------------------------------------------------------------------------------------------

    private fun showConfirmationDialog() {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_note))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ -> deleteItem() }
            .show()

    }

//--------------------------------------------------------------------------------------------------

    private fun deleteItem() {
        noteViewModel.delete(note)
        findNavController().navigateUp()
    }

//--------------------------------------------------------------------------------------------------

}