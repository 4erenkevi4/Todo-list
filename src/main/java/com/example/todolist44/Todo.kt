package com.example.todolist44

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
@Entity(tableName = "todo")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var title: String = "",
    var description: String,
    var date: String
): Serializable