package org.example.projectrr.models

import androidx.room.Entity
import androidx.room.PrimaryKey

// todo : string better managemnet here and usages
@Entity(tableName = "Task")
data class Task (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text : String,
    val isDone : Boolean
)