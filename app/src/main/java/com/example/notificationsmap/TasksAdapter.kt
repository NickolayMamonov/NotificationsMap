package com.example.notificationsmap

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.notificationsmap.data.database.MarkerDao
import com.example.notificationsmap.data.entities.ActiveTaskEntity
import com.example.notificationsmap.data.entities.MarkerEntity

class TasksAdapter(private var taskList: List<ActiveTaskEntity>, private val mItemClickListener: (Long)-> Unit): RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_task_item,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TasksAdapter.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val task = taskList[position]
        holder.name.text = task.name
        holder.address.text = task.address
        holder.desc.text = task.desc
        holder.itemView.setOnClickListener {
            taskList[position].id?.let { _id -> mItemClickListener(_id) }
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newTask: List<ActiveTaskEntity>) {
        taskList = newTask
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name : TextView = itemView.findViewById(R.id.name)
        var address : TextView = itemView.findViewById(R.id.address)
        var desc: TextView = itemView.findViewById(R.id.desc)
    }
}