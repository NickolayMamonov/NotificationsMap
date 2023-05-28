package com.example.notificationsmap.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notificationsmap.R
import com.example.notificationsmap.TasksViewModel
import com.example.notificationsmap.data.TaskRepo
import com.example.notificationsmap.data.database.MarkerDao
import com.example.notificationsmap.data.database.MarkerDatabase
import com.example.notificationsmap.data.entities.ActiveTaskEntity
import com.example.notificationsmap.data.entities.MarkerEntity
import com.example.notificationsmap.databinding.FragmentDetailsTaskBinding
import kotlinx.coroutines.launch

class DetailsTaskFragment : Fragment() {
    private val args by navArgs<DetailsTaskFragmentArgs>()
    private val userId by lazy { args.userId }
    private lateinit var binding : FragmentDetailsTaskBinding
    private lateinit var viewModel: TasksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsTaskBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[TasksViewModel::class.java]


//        arguments?.let { taskId = it.getDouble("id")
//        }
        val getTask = viewModel.getTaskById(taskId.toLong())
        binding.name.text =
            return binding.root
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch{
//            val getTask = viewModel.getTaskById(taskId.toLong())
//            binding.acceptBtn.setOnClickListener {
//                val marker = MarkerEntity.from(
//                    time = "",
//                    date = "",
//                    address = binding.address.text.toString(),
//                    desc = binding.description.text.toString(),
//                    lat = 3.3,
//                    lng = 3.3
//                )
//                val task = ActiveTaskEntity.add(
//                    name = binding.name.text.toString(),
//                    isActive = true,
//                    marker = marker
//                )
//                viewModel.updateMarker(task)
//                findNavController().navigate(R.id.action_createTaskFragment_to_mapFragment)
//            }
            binding.cancelBtn.setOnClickListener {
                findNavController().navigate(R.id.action_detailsTaskFragment_to_main_fragment)
            }
        }

    }

}