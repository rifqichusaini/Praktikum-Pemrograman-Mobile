package com.kiki.post8.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kiki.post8.databinding.ItemTodoBinding
import com.kiki.post8.model.Todo

class TodoAdapter(
    private val todos: MutableList<Todo>,
    private val onCheckChanged: (Todo, Boolean) -> Unit,
    private val onDeleteClick: (Todo) -> Unit,
    private val onItemClick: (Todo) -> Unit
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    inner class TodoViewHolder(private val binding: ItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(todo: Todo) {

            binding.cbCompleted.setOnCheckedChangeListener(null)
            binding.cbCompleted.isChecked = todo.completed


            binding.tvTitle.text = todo.title
            binding.tvDescription.text = todo.description
            binding.tvDeadline.text = todo.deadline

            val cardView = binding.root as androidx.cardview.widget.CardView

            if (todo.completed) {
                cardView.setCardBackgroundColor(android.graphics.Color.parseColor("#E0E0E0"))
                binding.tvTitle.paintFlags =
                    binding.tvTitle.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                binding.tvTitle.setTextColor(android.graphics.Color.parseColor("#9E9E9E"))
                binding.tvDescription.setTextColor(android.graphics.Color.parseColor("#BDBDBD"))
            } else {
                cardView.setCardBackgroundColor(android.graphics.Color.parseColor("#F5F5F5"))
                binding.tvTitle.paintFlags =
                    binding.tvTitle.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
                binding.tvTitle.setTextColor(android.graphics.Color.parseColor("#1A1A1A"))
                binding.tvDescription.setTextColor(android.graphics.Color.parseColor("#666666"))
            }

            binding.cbCompleted.setOnCheckedChangeListener { _, isChecked ->
                onCheckChanged(todo, isChecked)
            }

            binding.btnDelete.setOnClickListener {
                onDeleteClick(todo)
            }

            binding.root.setOnClickListener {
                onItemClick(todo)
            }
        }
    }

    fun updateData(newTodos: List<Todo>) {
        todos.clear()
        todos.addAll(newTodos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(todos[position])
    }

    override fun getItemCount() = todos.size
}
