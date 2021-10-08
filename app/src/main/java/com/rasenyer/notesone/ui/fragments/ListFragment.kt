package com.rasenyer.notesone.ui.fragments

//--------------------------------------------------------------------------------------------------

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rasenyer.notesone.R
import com.rasenyer.notesone.adapter.NoteAdapter
import com.rasenyer.notesone.databinding.FragmentListBinding
import com.rasenyer.notesone.utils.NoteApplication
import com.rasenyer.notesone.viewmodel.NoteViewModel
import com.rasenyer.notesone.viewmodel.NoteViewModelFactory

//--------------------------------------------------------------------------------------------------

class ListFragment : Fragment() {

//--------------------------------------------------------------------------------------------------

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

//--------------------------------------------------------------------------------------------------

    private val noteViewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory((activity?.application as NoteApplication).noteDatabase.noteDao())
    }

//--------------------------------------------------------------------------------------------------

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

//--------------------------------------------------------------------------------------------------

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//--------------------------------------------------------------------------------------------------

        val noteAdapter = NoteAdapter {
            val action = ListFragmentDirections.actionListFragmentToDetailsFragment(it.id)
            this.findNavController().navigate(action)
        }

//--------------------------------------------------------------------------------------------------

        binding.mRecyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.mRecyclerView.adapter = noteAdapter

//--------------------------------------------------------------------------------------------------

        noteViewModel.getNotes.observe(this.viewLifecycleOwner) { items ->
            items.let { noteAdapter.submitList(it) }
        }

//--------------------------------------------------------------------------------------------------

        binding.mFloatingActionButton.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToAddFragment(getString(R.string.add_note))
            this.findNavController().navigate(action)
        }

//--------------------------------------------------------------------------------------------------

    }

//--------------------------------------------------------------------------------------------------

}