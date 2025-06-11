package xyz.potasyyum.mynotes.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.orhanobut.logger.Logger
import xyz.potasyyum.mynotes.model.TaskEntity
import xyz.potasyyum.mynotes.presentation.viewmodel.TaskViewModel

@Composable
fun TaskView(
    taskViewModel: TaskViewModel = hiltViewModel()
) {
    val taskList by taskViewModel.taskList.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "My Tasks",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(onClick = {
            Logger.i("EKHIW")
            taskViewModel.addTaskItem(
                TaskEntity(title = "test" , desc = "deskripsi")
            )
        }) {
            Text("TEST")
        }
        
        if (taskList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Task is empty!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(taskList) { task ->
                    TaskItem(
                        task = task,
                        onToggleTask = { taskViewModel.toggleTask(it) },
                        onDeleteTask = { taskViewModel.removeTask(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskItem(
    task: TaskEntity,
    onToggleTask: (TaskEntity) -> Unit,
    onDeleteTask: (TaskEntity) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.status,
                onCheckedChange = { onToggleTask(task) }
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (task.status) TextDecoration.LineThrough else null,
                    color = if (task.status) MaterialTheme.colorScheme.onSurfaceVariant 
                           else MaterialTheme.colorScheme.onSurface
                )
                
                if (task.desc.isNotBlank()) {
                    Text(
                        text = task.desc,
                        style = MaterialTheme.typography.bodyMedium,
                        textDecoration = if (task.status) TextDecoration.LineThrough else null,
                        color = if (task.status) MaterialTheme.colorScheme.onSurfaceVariant 
                               else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            
            IconButton(
                onClick = { onDeleteTask(task) }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}