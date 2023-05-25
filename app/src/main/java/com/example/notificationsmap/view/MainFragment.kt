package com.example.notificationsmap.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.notificationsmap.R
import com.example.notificationsmap.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val mapGraphFragment by lazy {
        NavHostFragment.create(R.navigation.map_nav_graph)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        val nhf = NavHostFragment.create(R.navigation.map_nav_graph, null)
        childFragmentManager.commit {
            replace(R.id.content, nhf)
        }
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_createTaskFragment)
        }
        binding.navbar.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.map -> {
                    binding.btnAdd.visibility = View.VISIBLE
                    childFragmentManager.commit {
                        replace(R.id.content, mapGraphFragment)
                    }
                    return@setOnItemSelectedListener true
                }
                R.id.tasks -> {
                    binding.btnAdd.visibility = View.INVISIBLE
                    childFragmentManager.commit {
                        replace(R.id.content, TasksFragment())
                    }
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }
        return binding.root
    }


}