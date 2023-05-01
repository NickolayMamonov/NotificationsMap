
package com.example.notificationsmap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
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
        binding.navbar.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.map -> {
                    childFragmentManager.commit {
                        replace(R.id.content, mapGraphFragment)
                    }
                    return@setOnItemSelectedListener true
                }
                R.id.tasks -> {
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