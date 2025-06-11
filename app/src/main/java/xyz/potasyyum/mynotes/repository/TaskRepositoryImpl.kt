package xyz.potasyyum.mynotes.repository

import kotlinx.coroutines.flow.Flow
import xyz.potasyyum.mynotes.model.TaskDao
import xyz.potasyyum.mynotes.model.TaskEntity
import xyz.potasyyum.mynotes.model.TaskFilter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {
    
    override fun getAllTasks(): Flow<List<TaskEntity>> {
        return taskDao.getAllTasks()
    }

    override fun getPendingTasks(): Flow<List<TaskEntity>> {
        return taskDao.getPendingTasks()
    }

    override fun getCompletedTasks(): Flow<List<TaskEntity>> {
        return taskDao.getCompletedTasks()
    }

    override fun getFilteredTasks(taskFilter: TaskFilter): Flow<List<TaskEntity>> {
        return when (taskFilter) {
            TaskFilter.ALL -> taskDao.getAllTasks()
            TaskFilter.COMPLETED -> taskDao.getCompletedTasks()
            TaskFilter.PENDING -> taskDao.getPendingTasks()
        }
    }

    override fun getTotalTaskCount(): Flow<Int> {
        return taskDao.getTotalTaskCount()
    }

    override fun getCompletedTaskCount(): Flow<Int> {
        return taskDao.getCompletedTaskCount()
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