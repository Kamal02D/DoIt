package org.example.projectrr.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.example.projectrr.Constants


@Entity(tableName = Constants.DataBase.Tables.Task.NAME)
data class Task (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = Constants.DataBase.Tables.Task.Columns.TEXT)
    val text : String,
    @ColumnInfo(name = Constants.DataBase.Tables.Task.Columns.IS_DONE)
    val isDone : Boolean
)