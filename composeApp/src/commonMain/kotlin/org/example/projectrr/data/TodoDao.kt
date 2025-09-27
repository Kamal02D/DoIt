package org.example.projectrr.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.example.projectrr.Constants
import org.example.projectrr.models.Task

@Dao
interface TodoDao {
    @Upsert
    suspend fun insert(item: Task)

    @Delete
    suspend fun delete(item: Task)

    @Query("SELECT count(*) FROM ${Constants.DataBase.Tables.Task.NAME}")
    suspend fun count(): Int

    @Query("SELECT * FROM ${Constants.DataBase.Tables.Task.NAME}")
    fun getAll(): Flow<List<Task>>
}