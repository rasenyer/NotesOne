package com.rasenyer.notesone.ui.fragments

//--------------------------------------------------------------------------------------------------

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rasenyer.notesone.model.Note
import com.rasenyer.notesone.databinding.FragmentAddBinding
import com.rasenyer.notesone.utils.NoteApplication
import com.rasenyer.notesone.viewmodel.NoteViewModel
import com.rasenyer.notesone.viewmodel.NoteViewModelFactory

//--------------------------------------------------------------------------------------------------

class AddFragment : Fragment() {

//--------------------------------------------------------------------------------------------------

    private var _binding: FragmentAddBinding? = null
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
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

//--------------------------------------------------------------------------------------------------

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = detailsFragmentArgs.id

        if (id > 0) {

            noteViewModel.getNote(id).observe(this.viewLifecycleOwner) { selectedItem ->
                note = selectedItem
                bind(note)
            }

        } else { binding.mButtonSave.setOnClickListener { insert() } }

    }

//--------------------------------------------------------------------------------------------------

    override fun onDestroyView() {
        super.onDestroyView()

        val hideKeyboard = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        hideKeyboard.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null

    }

//--------------------------------------------------------------------------------------------------

    private fun bind(item: Note) {

        binding.apply {
            mEditTextTitle.setText(item.title, TextView.BufferType.SPANNABLE)
            mEditTextDescription.setText(item.description, TextView.BufferType.SPANNABLE)
            mButtonSave.setOnClickListener { update() }
        }

    }

//--------------------------------------------------------------------------------------------------

    private fun insert() {

        if (isEntryValid()) {

            noteViewModel.insert(
                binding.mEditTextTitle.text.toString(),
                binding.mEditTextDescription.text.toString()
            )

            val action = AddFragmentDirections.actionAddFragmentToListFragment()
            findNavController().navigate(action)

        } else {
            Toast.makeText(context, "All fields are required.", Toast.LENGTH_SHORT).show()
        }

    }

//--------------------------------------------------------------------------------------------------

    private fun update() {

        if (isEntryValid()) {

            noteViewModel.update(
                this.detailsFragmentArgs.id,
                this.binding.mEditTextTitle.text.toString(),
                this.binding.mEditTextDescription.text.toString()
            )

            val action = AddFragmentDirections.actionAddFragmentToListFragment()
            findNavController().navigate(action)

        } else {
            Toast.makeText(context, "All fields are required.", Toast.LENGTH_SHORT).show()
        }

    }

//--------------------------------------------------------------------------------------------------

    private fun isEntryValid(): Boolean {
        return noteViewModel.isEntryValid(
            binding.mEditTextTitle.text.toString(),
            binding.mEditTextDescription.text.toString()
        )
    }

//--------------------------------------------------------------------------------------------------

}