package xyz.potasyyum.mynotes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import xyz.potasyyum.mynotes.model.TaskEntity
import xyz.potasyyum.mynotes.repository.TaskRepository
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {
    
    private val _taskList = MutableStateFlow<List<TaskEntity>>(emptyList())
    val taskList: StateFlow<List<TaskEntity>> = _taskList.asStateFlow()

    init {
        getAllTasks()
    }

    private fun getAllTasks() {
        viewModelScope.launch {
            taskRepository.getAllTasks().collect { tasks ->
                _taskList.value = tasks
            }
        }
    }

    fun addTaskItem(taskEntity: TaskEntity) {
        viewModelScope.launch {
            taskRepository.insertTask(taskEntity)
        }
    }

    fun updateTaskItem(taskEntity: TaskEntity) {
        viewModelScope.launch {
            taskRepository.updateTask(taskEntity)
        }
    }

    fun toggleTask(taskEntity: TaskEntity) {
        viewModelScope.launch {
            val updatedTask = taskEntity.copy(status = !taskEntity.status)
            taskRepository.updateTask(updatedTask)
        }
    }

    fun removeTask(taskEntity: TaskEntity) {
        viewModelScope.launch {
            taskRepository.deleteTask(taskEntity)
        }
    }
}