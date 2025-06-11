package xyz.potasyyum.mynotes.repository

import kotlinx.coroutines.flow.Flow
import xyz.potasyyum.mynotes.model.TaskEntity

interface TaskRepository {
    
    fun getAllTasks(): Flow<List<TaskEntity>>
    
    suspend fun insertTask(taskEntity: TaskEntity)
    
    suspend fun updateTask(taskEntity: TaskEntity)
    
    suspend fun deleteTask(taskEntity: TaskEntity)
}