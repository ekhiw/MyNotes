package xyz.potasyyum.mynotes.repository

import kotlinx.coroutines.flow.Flow
import xyz.potasyyum.mynotes.model.TaskEntity
import xyz.potasyyum.mynotes.model.TaskFilter

interface TaskRepository {
    
    fun getAllTasks(): Flow<List<TaskEntity>>

    fun getPendingTasks(): Flow<List<TaskEntity>>

    fun getCompletedTasks(): Flow<List<TaskEntity>>

    fun getFilteredTasks(taskFilter: TaskFilter): Flow<List<TaskEntity>>

    fun getTotalTaskCount(): Flow<Int>

    fun getCompletedTaskCount(): Flow<Int>
    
    suspend fun insertTask(taskEntity: TaskEntity)
    
    suspend fun updateTask(taskEntity: TaskEntity)
    
    suspend fun deleteTask(taskEntity: TaskEntity)
}