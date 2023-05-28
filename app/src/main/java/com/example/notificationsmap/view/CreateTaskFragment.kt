package com.example.notificationsmap.view

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.notificationsmap.CreateTaskViewModel
import com.example.notificationsmap.R
import com.example.notificationsmap.SharedViewModel
import com.example.notificationsmap.data.entities.ActiveTaskEntity
import com.example.notificationsmap.data.entities.MarkerEntity
import com.example.notificationsmap.databinding.FragmentCreateTaskBinding
import com.yandex.mapkit.geometry.Point
import kotlinx.coroutines.launch
import java.util.*


class CreateTaskFragment : Fragment() {
    private lateinit var binding : FragmentCreateTaskBinding
    private val createTaskViewModel: CreateTaskViewModel by viewModels( { requireActivity() })
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var lat :String
    private lateinit var lng: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateTaskBinding.inflate(inflater)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        sharedViewModel.latCoord.observe(viewLifecycleOwner){latCoord ->
//            lat =latCoord
//        }
//        sharedViewModel.lngCoord.observe(viewLifecycleOwner){lngCoord ->
//            lng = lngCoord
//        }
        lat = sharedViewModel.latCoord.value.toString()
        lng = sharedViewModel.lngCoord.value.toString()
        binding.address.text = "(${lat},${lng})"


    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch{
            binding.acceptBtn.setOnClickListener {
                val marker = MarkerEntity.from(
                    time = "",
                    date = "",
                    address = binding.address.text.toString(),
                    desc = binding.description.text.toString(),
                    lat = lat.toDouble(),
                    lng = lng.toDouble()
                )
                val task = ActiveTaskEntity.add(
                    name = binding.name.text.toString(),
                    isActive = true,
                    marker = marker
                )
                createTaskViewModel.insertMarkerPos(task)
                findNavController().navigate(R.id.action_createTaskFragment_to_mapFragment)
            }
            binding.cancelBtn.setOnClickListener {
                findNavController().navigate(R.id.action_createTaskFragment_to_mapFragment)
            }
        }

    }


}