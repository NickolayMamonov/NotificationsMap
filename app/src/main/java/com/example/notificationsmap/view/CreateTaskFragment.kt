package com.example.notificationsmap.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import kotlinx.coroutines.launch
import java.util.Calendar


class CreateTaskFragment : Fragment() {
    private lateinit var binding : FragmentCreateTaskBinding
    private val createTaskViewModel: CreateTaskViewModel by viewModels( { requireActivity() })
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var lat : String
    private lateinit var lng : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateTaskBinding.inflate(inflater)
        val calendar = Calendar.getInstance()

        binding.timePicker.setOnClickListener {
            showTimePickerDialog(binding.timePicker,calendar)
        }
        binding.datePicker.setOnClickListener {
            showDatePickerDialog(binding.datePicker,calendar)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.latCoord.observe(viewLifecycleOwner){latCoord ->
            lat =latCoord
        }
        sharedViewModel.lngCoord.observe(viewLifecycleOwner){lngCoord ->
            lng = lngCoord
        }

    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch{
            binding.acceptBtn.setOnClickListener {
                val marker = MarkerEntity.from(
                    time = binding.timePicker.text.toString(),
                    date = binding.datePicker.text.toString(),
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


    private fun showTimePickerDialog(
        textView:TextView,
        calendar: Calendar
    ) {
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            {_,hourOfDay, minute ->
                val selectedTime = "%02d:%02d".format(hourOfDay,minute)
                textView.text = selectedTime
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
        true)
        timePickerDialog.show()
    }

    private fun showDatePickerDialog(
        textView:TextView,
        calendar: Calendar
    ) {

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            {_,year,month,dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                textView.text = selectedDate
            },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}