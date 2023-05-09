package com.example.notificationsmap.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener

import com.example.notificationsmap.R
import com.example.notificationsmap.databinding.FragmentCreateTaskBinding
import java.util.Calendar


class CreateTaskFragment : Fragment() {
    private lateinit var binding : FragmentCreateTaskBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateTaskBinding.inflate(inflater)

        setFragmentResultListener("mapPoint"){ _, bundle ->
            val latitude = bundle.getDouble("latitude")
            val longitude = bundle.getDouble("longitude")
                //binding.address.text = latitude.toString()
        }


        binding.timePicker.setOnClickListener {
            showTimePickerDialog()
        }
        binding.datePicker.setOnClickListener {
            showDatePickerDialog()
        }
        return binding.root
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            {_,hourOfDay, minute ->
                val selectedTime = "%02d:%02d".format(hourOfDay,minute)
                binding.timePicker.setText(selectedTime)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
        true)
        timePickerDialog.show()




    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            {_,year,month,dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                binding.datePicker.setText(selectedDate)
            },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

}