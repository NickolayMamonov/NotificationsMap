package com.example.notificationsmap.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notificationsmap.R
import com.example.notificationsmap.TasksViewModel
import com.example.notificationsmap.data.entities.ActiveTaskEntity
import com.example.notificationsmap.databinding.FragmentDetailsTaskBinding
import kotlinx.coroutines.launch

class DetailsTaskFragment : Fragment() {
    private val args by navArgs<DetailsTaskFragmentArgs>()
    private val userId by lazy { args.taskId }
    private lateinit var binding: FragmentDetailsTaskBinding
    private lateinit var viewModel: TasksViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsTaskBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[TasksViewModel::class.java]
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            val task = viewModel.getTaskById(userId)
            binding.name.setText(task.name)
            binding.address.text = task.address
            binding.description.setText(task.desc)
            binding.acceptBtn.setOnClickListener {
                val newTask = ActiveTaskEntity(
                    id = userId,
                    name = binding.name.text.toString(),
                    address = binding.address.text.toString(),
                    desc = binding.description.text.toString(),
                    lat = task.lat,
                    lng = task.lng
                )
                viewModel.updateTask(newTask)
                findNavController().navigate(R.id.action_detailsTaskFragment_to_main_fragment)
            }
            binding.btnDel.setOnClickListener {
                viewModel.deleteTask(task)
                findNavController().navigate(R.id.action_detailsTaskFragment_to_main_fragment)
            }
            binding.cancelBtn.setOnClickListener {
                findNavController().navigate(R.id.action_detailsTaskFragment_to_main_fragment)
            }
        }

    }

}