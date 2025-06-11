package xyz.potasyyum.mynotes.repository

import kotlinx.coroutines.flow.Flow
import xyz.potasyyum.mynotes.model.TaskDao
import xyz.potasyyum.mynotes.model.TaskEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {
    
    override fun getAllTasks(): Flow<List<TaskEntity>> {
        return taskDao.getAllTasks()
    }
    
    override suspend fun insertTask(taskEntity: TaskEntity) {
        taskDao.insert(taskEntity)
    }
    
    override suspend fun updateTask(taskEntity: TaskEntity) {
        taskDao.update(taskEntity)
    }
    
    override suspend fun deleteTask(taskEntity: TaskEntity) {
        taskDao.delete(taskEntity)
    }
}