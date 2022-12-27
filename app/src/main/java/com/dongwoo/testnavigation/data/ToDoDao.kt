package com.dongwoo.testnavigation.data

import androidx.compose.ui.input.key.Key.Companion.H
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dongwoo.testnavigation.data.models.ToDoTask
import com.dongwoo.testnavigation.util.Constants.TODO_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {

    @Query("SELECT * FROM $TODO_TABLE_NAME")
    fun getAllTasks(): Flow<List<ToDoTask>>

    @Query("SELECT * FROM $TODO_TABLE_NAME WHERE id=:taskId")
    fun getSelectedTask(taskId: Int): Flow<ToDoTask>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(task: ToDoTask)

    @Update
    suspend fun updateTask(task: ToDoTask)

    @Delete
    suspend fun deleteTask(task: ToDoTask)

    @Query("DELETE FROM $TODO_TABLE_NAME")
    suspend fun deleteAllTasks()

    @Query(
        """
            SELECT * FROM $TODO_TABLE_NAME 
            WHERE title LIKE :searchQuery 
            OR description LIKE :searchQuery
        """
    )
    fun searchDatabase(searchQuery: String): Flow<List<ToDoTask>>

    @Query(
        """
            SELECT * FROM $TODO_TABLE_NAME 
            ORDER BY 
        CASE 
            WHEN priority LIKE 'L%' THEN 1 
            WHEN priority LIKE 'M%' THEN 2 
            WHEN priority LIKE 'H%' THEN 3 
        END
        """
    )
    fun sortByLowPriority(): Flow<List<ToDoTask>>

    @Query(
        """
            SELECT * FROM $TODO_TABLE_NAME 
            ORDER BY
        CASE 
            WHEN priority LIKE 'H%' THEN 1 
            WHEN priority LIKE 'M%' THEN 2 
            WHEN priority LIKE 'L%' THEN 3 
        END
        """
    )
    fun sortByHighPriority(): Flow<List<ToDoTask>>

}