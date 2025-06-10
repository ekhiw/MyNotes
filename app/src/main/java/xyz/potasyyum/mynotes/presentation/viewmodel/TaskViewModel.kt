package xyz.potasyyum.mynotes.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import xyz.potasyyum.mynotes.model.TaskItem

class TaskViewModel : ViewModel() {
    private val _taskList = mutableStateListOf<TaskItem>()
    val taskList : List<TaskItem> get() = _taskList

    fun addTaskItem(taskItem: TaskItem) {

    }

    fun toggleTask(taskItem: TaskItem) {

    }

    fun removeTask(taskItem: TaskItem) {

    }
}