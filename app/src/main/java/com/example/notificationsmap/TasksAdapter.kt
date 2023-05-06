package com.example.notificationsmap

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notificationsmap.data.database.MarkerDao
import com.example.notificationsmap.data.entities.MarkerEntity

class TasksAdapter(private var markerList: List<MarkerEntity>): RecyclerView.Adapter<TasksAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_task_item,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TasksAdapter.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val markers = markerList[position]
        holder.address.text =markers.lat.toString()

    }

    override fun getItemCount(): Int {
        return markerList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newTask: List<MarkerEntity>) {
        markerList = newTask
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var address : TextView = itemView.findViewById(R.id.address)

    }
}