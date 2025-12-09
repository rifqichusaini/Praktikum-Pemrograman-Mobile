package com.kiki.post8

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kiki.post8.adapter.TodoAdapter
import com.kiki.post8.component.AddOrEditTodoDialog
import com.kiki.post8.databinding.ActivityMainBinding
import com.kiki.post8.model.Todo
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var todosRef: DatabaseReference

    private lateinit var todoAdapter: TodoAdapter
    private val todoList = mutableListOf<Todo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todosRef = FirebaseDatabase.getInstance().getReference("todos")

        todoAdapter = TodoAdapter(
            todoList,
            onCheckChanged = { todo, isChecked ->
                updateTodoStatus(todo, isChecked)
            },
            onDeleteClick = { deleteTodo(it) },
            onItemClick = { editTodo(it) }
        )

        binding.rvTodos.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = todoAdapter
        }

        fetchTodos()
        setupAddButton()
    }

    private fun fetchTodos() {
        todosRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val newTodos = mutableListOf<Todo>()
                for (data in snapshot.children) {
                    val todo = data.getValue(Todo::class.java)
                    todo?.let { newTodos.add(it) }
                }

                val sortedTodos = newTodos.sortedBy { it.completed }

                todoAdapter.updateData(sortedTodos)

                if (sortedTodos.isEmpty()) {
                    binding.emptyState.visibility = View.VISIBLE
                    binding.rvTodos.visibility = View.GONE
                } else {
                    binding.emptyState.visibility = View.GONE
                    binding.rvTodos.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@MainActivity,
                    "Error: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun setupAddButton() {
        binding.fabAddTodo.setOnClickListener {
            AddOrEditTodoDialog(this, todosRef).show()
        }
    }

    private fun updateTodoStatus(todo: Todo, isCompleted: Boolean) {
        todo.id?.let { id ->
            todosRef.child(id)
                .child("completed")
                .setValue(isCompleted)
        }
    }


    private fun deleteTodo(todo: Todo) {
        todo.id?.let { id ->
            todosRef.child(id).removeValue()
        }
    }

    private fun editTodo(todo: Todo) {
        AddOrEditTodoDialog(this, todosRef, todo).show()
    }
}
