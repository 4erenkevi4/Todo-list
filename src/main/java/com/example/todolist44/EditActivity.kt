package com.example.todolist44

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class EditActivity : AppCompatActivity() {
    private lateinit var deleteFab: FloatingActionButton
    private lateinit var doneFab: FloatingActionButton
    private lateinit var todoViewModel: TodoViewModel
    private lateinit var descriptionText: EditText
    private lateinit var todoEditText: EditText
    private lateinit var dateView: TextView
    private var textData: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        deleteFab = findViewById(R.id.deleteFab)
        doneFab = findViewById(R.id.doneFab)
        todoEditText = findViewById(R.id.todoEditText)
        descriptionText = findViewById(R.id.description_text)
        textData?.findViewById<TextView>(R.id.data)
        todoViewModel = ViewModelProviders.of(this).get(TodoViewModel::class.java)
        val id = intent.getLongExtra("id", -1L)
        if (id == -1L) {
            insertMode()
        } else {
            updateMode(id)
        }
        dateView = findViewById(R.id.date_view)
        dateView.setOnClickListener {
            showDatePicker()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun showDatePicker() {
        val dateView: TextView = findViewById(R.id.date_view)
        dateView.text = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())
        val cal = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "dd.MM.yyyy"
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                dateView.text = sdf.format(cal.time)
            }
        dateView.setOnClickListener {
            DatePickerDialog(
                this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    @SuppressLint("RestrictedApi")
    private fun insertMode() {
        deleteFab.visibility = View.GONE
        doneFab.setOnClickListener {
            insertTodo()
        }
    }

    private fun updateMode(id: Long) {
        todoViewModel.getTodoById(id).observe(this, androidx.lifecycle.Observer { todo ->
            todo?.let {
                todoEditText.setText(todo.title)
                descriptionText.setText(todo.description)
                dateView.text = todo.date
            }
        })
        doneFab.setOnClickListener {
            updateTodo(id)
        }
        deleteFab.setOnClickListener {
            deleteTodo(id)
        }
    }

    private fun insertTodo() {
        val todo = Todo(
            0,
            todoEditText.text.toString(), descriptionText.text.toString(), dateView.text.toString()
        )
        todoViewModel.insert(todo) { finish() }
    }

    private fun updateTodo(id: Long) {
        val todo = Todo(
            id,
            todoEditText.text.toString(), descriptionText.text.toString(), dateView.text.toString()
        )
        todoViewModel.insert(todo) { finish() }
    }

    private fun deleteTodo(id: Long) {
        todoViewModel.delete(id) { finish() }
    }
}





