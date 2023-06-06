package com.example.notificationsmap.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notificationsmap.R
import com.example.notificationsmap.TasksAdapter
import com.example.notificationsmap.TasksViewModel
import com.example.notificationsmap.databinding.FragmentTasksBinding
import kotlinx.coroutines.launch

class TasksFragment : Fragment() {
    private lateinit var binding: FragmentTasksBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TasksAdapter
    private lateinit var viewModel: TasksViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[TasksViewModel::class.java]
        taskAdapter = TasksAdapter(emptyList()){id ->
            val action = MainFragmentDirections.actionMainFragmentToDetailsTaskFragment(id)
            findNavController().navigate(action)
        }
        recyclerView = binding.recycleview

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            val tasks = viewModel.getAllTasks()
            taskAdapter.updateData(tasks)
        }
        recyclerView.adapter = taskAdapter
        return binding.root
    }

}