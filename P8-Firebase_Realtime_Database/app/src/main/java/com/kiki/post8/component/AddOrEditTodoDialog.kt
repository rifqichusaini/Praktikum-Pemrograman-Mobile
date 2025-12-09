package com.kiki.post8.component

import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.kiki.post8.databinding.DialogAddEditTodoBinding
import com.kiki.post8.model.Todo
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DatabaseReference
import java.text.SimpleDateFormat
import java.util.*

class AddOrEditTodoDialog(
    private val context: Context,
    private val todosRef: DatabaseReference,
    private val existingTodo: Todo? = null
) {

    fun show() {
        val dialogBinding = DialogAddEditTodoBinding.inflate(LayoutInflater.from(context))

        existingTodo?.let {
            dialogBinding.editTextTitle.setText(it.title)
            dialogBinding.editTextDescription.setText(it.description)
            dialogBinding.editTextDeadline.setText(it.deadline)
        }

        dialogBinding.editTextDeadline.setOnClickListener {
            showDatePicker(dialogBinding)
        }

        val title = if (existingTodo == null) "Tambah Tugas Baru" else "Edit Tugas"
        val positiveButton = if (existingTodo == null) "Simpan" else "Simpan"

        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setView(dialogBinding.root)
            .setPositiveButton(positiveButton) { dialog, _ ->
                val todoTitle = dialogBinding.editTextTitle.text.toString().trim()
                val description = dialogBinding.editTextDescription.text.toString().trim()
                val deadline = dialogBinding.editTextDeadline.text.toString().trim()

                if (todoTitle.isEmpty() || deadline.isEmpty()) {
                    Toast.makeText(context, "Judul dan deadline wajib diisi!", Toast.LENGTH_SHORT).show()
                } else {
                    if (existingTodo == null) {
                        addTodo(todoTitle, description, deadline)
                    } else {
                        updateTodo(existingTodo, todoTitle, description, deadline)
                    }
                }
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showDatePicker(binding: DialogAddEditTodoBinding) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())

                binding.editTextDeadline.setText(dateFormat.format(selectedCalendar.time))
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun addTodo(title: String, description: String, deadline: String) {
        val id = todosRef.push().key
        val newTodo = Todo(
            id = id,
            title = title,
            description = description,
            deadline = deadline,
            completed = false
        )

        id?.let {
            todosRef.child(it).setValue(newTodo)
                .addOnSuccessListener {
                    Toast.makeText(context, "Tugas berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { error ->
                    Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun updateTodo(todo: Todo, title: String, description: String, deadline: String) {
        val updatedTodo = todo.copy(
            title = title,
            description = description,
            deadline = deadline
        )

        todo.id?.let {
            todosRef.child(it).setValue(updatedTodo)
                .addOnSuccessListener {
                    Toast.makeText(context, "Tugas berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { error ->
                    Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}