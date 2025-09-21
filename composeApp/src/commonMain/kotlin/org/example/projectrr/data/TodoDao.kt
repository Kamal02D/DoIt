package org.example.projectrr.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.example.projectrr.models.Task

@Dao
interface TodoDao {
    @Insert
    suspend fun insert(item: Task)

    @Query("SELECT count(*) FROM Task")
    suspend fun count(): Int

    @Query("SELECT * FROM Task")
    fun getAllAsFlow(): Flow<List<Task>>
}