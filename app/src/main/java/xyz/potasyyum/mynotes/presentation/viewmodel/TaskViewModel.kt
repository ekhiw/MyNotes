package xyz.potasyyum.mynotes.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import xyz.potasyyum.mynotes.model.TaskEntity
import xyz.potasyyum.mynotes.model.TaskFilter
import xyz.potasyyum.mynotes.repository.TaskRepository
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {
    private val _selectedFilter = MutableStateFlow(TaskFilter.ALL)
    val selectedFilter: StateFlow<TaskFilter> = _selectedFilter.asStateFlow()

    var filteredTask : LiveData<List<TaskEntity>> = taskRepository.getFilteredTasks(_selectedFilter.value).asLiveData()
    val totalTaskCount: LiveData<Int> = taskRepository.getTotalTaskCount().asLiveData()
    val completedTaskCount: LiveData<Int> = taskRepository.getCompletedTaskCount().asLiveData()

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

    fun setFilter(filter: TaskFilter) {
        _selectedFilter.value = filter
        viewModelScope.launch {
            filteredTask = taskRepository.getFilteredTasks(_selectedFilter.value).asLiveData()
        }
    }
}