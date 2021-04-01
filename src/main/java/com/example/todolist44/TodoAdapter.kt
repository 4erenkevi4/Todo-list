package com.example.todolist44

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter(
    private val list: List<Todo>,
    val click: (Long) -> Unit
) : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return ViewHolder(view, click)
    }
    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(list[position])
        holder.oClick(item.id)
    }
    class ViewHolder(
        view: View,
        val onClick: (Long) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val root: View = view.findViewById(R.id.item_todo)
        private val textData: TextView = root.findViewById(R.id.data)
        private val titleTodo: TextView = root.findViewById(R.id.text_todo)

        fun bind(task: Todo) {
            textData.text = task.date
            titleTodo.text = task.title
        }

        fun oClick(position: Long) {
            root.setOnClickListener {
                onClick(position)
            }
        }
    }
}

