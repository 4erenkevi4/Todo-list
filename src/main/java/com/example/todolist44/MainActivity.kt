package com.example.todolist44


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private val list: ArrayList<Todo> by lazy { arrayListOf<Todo>() }
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var switchTheme: Switch
    private lateinit var todoViewModel: TodoViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private val CHEK_STATE = "CHEK_STATE"
    private val NIGHT_THEME = "NIGHT_THEME"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (chekTheme()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.MyThemeDark)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            setTheme(R.style.MyTheme)
        }
        setContentView(R.layout.activity_main)
        val adapter = TodoAdapter(list) { id ->
            startActivity(Intent(this@MainActivity, EditActivity::class.java).putExtra("id", id))
        }
        todoViewModel = ViewModelProviders.of(this).get(TodoViewModel::class.java)
        todoViewModel.getAllTodos().observe(this, Observer {
            list.clear()
            list.addAll(it!!)
            adapter.notifyDataSetChanged()
        })
        setRecyclerview(adapter)
        setChangeTheme()
        setFab()
    }

    fun setRecyclerview(adapter: TodoAdapter) {
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                todoViewModel.onTaskSwiped(viewHolder.adapterPosition.toLong())
            }
        }).attachToRecyclerView(recyclerView)
    }

    fun setFab() {
        val animForFab = AnimationUtils.loadAnimation(this, R.anim.fab_go)
        fab = findViewById(R.id.fab)
        fab.startAnimation(animForFab)
        fab.setOnClickListener {
            startActivity(Intent(this@MainActivity, EditActivity::class.java))
        }
    }

    private fun setChangeTheme() {
        switchTheme = findViewById(R.id.switch_theme)
        if (chekTheme()) {
            switchTheme.isChecked = true
        }
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                saveTheme(true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                saveTheme(false)
            }
        }
    }

    private fun saveTheme(state: Boolean) {
        val sharedPreferences: SharedPreferences =
            this.getSharedPreferences(CHEK_STATE, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(NIGHT_THEME, state)
        editor.apply()
    }

    private fun chekTheme(): Boolean {
        val sharedPreferences: SharedPreferences =
            this.getSharedPreferences(CHEK_STATE, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(NIGHT_THEME, false)
    }

}


