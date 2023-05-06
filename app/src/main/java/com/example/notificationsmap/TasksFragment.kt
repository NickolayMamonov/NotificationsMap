package com.example.notificationsmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notificationsmap.data.database.MarkerDatabase
import com.example.notificationsmap.databinding.FragmentTasksBinding
import com.google.android.gms.tasks.Task
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
        binding = FragmentTasksBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this)[TasksViewModel::class.java]
        taskAdapter = TasksAdapter(emptyList())
        recyclerView = binding.recycleview
        recyclerView.adapter = taskAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            val tasks = viewModel.getAllMarkers()
            taskAdapter.updateData(tasks)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val markerDao = MarkerDatabase.getDatabase(requireContext()).markerDao()
//
//        val markerList = markerDao.getAllMarkers()
//        taskAdapter = TasksAdapter(markerList)
//        binding.recycleview.adapter = taskAdapter

    }

}