package xyz.potasyyum.mynotes.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.ImageLoader
import coil3.compose.rememberAsyncImagePainter
import coil3.svg.SvgDecoder
import com.orhanobut.logger.Logger
import xyz.potasyyum.mynotes.R
import xyz.potasyyum.mynotes.model.TaskEntity
import xyz.potasyyum.mynotes.model.TaskFilter
import xyz.potasyyum.mynotes.presentation.viewmodel.TaskViewModel

@Composable
fun TaskView(
    taskViewModel: TaskViewModel = hiltViewModel()
) {
    val taskList by taskViewModel.filteredTask.observeAsState(emptyList())
    val selectedFilter by taskViewModel.selectedFilter.collectAsState()
    val totalTaskCount by taskViewModel.totalTaskCount.observeAsState(0)
    val completedTaskCount by taskViewModel.completedTaskCount.observeAsState(0)
    var showAddDialog by remember { mutableStateOf(false) }
    var dialogEditMode by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<TaskEntity?>(null) }

    Logger.i("EKHIW TASK = ${taskList.joinToString(", ")}")
    
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "My Tasks",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "${totalTaskCount} tasks • ${completedTaskCount} completed",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TaskFilter.entries.forEach { filter ->
                    FilterChip(
                        onClick = { taskViewModel.setFilter(filter) },
                        label = { 
                            Text(
                                text = filter.displayName,
                                style = MaterialTheme.typography.labelMedium
                            ) 
                        },
                        selected = selectedFilter == filter,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            if (taskList.isEmpty()) {
                EmptyTask()
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(taskList) { task ->
                        TaskItem(
                            task = task,
                            onToggleTask = { taskViewModel.toggleTask(it) },
                            onDeleteTask = { taskViewModel.removeTask(it) },
                            onEditTask = { task ->
                                taskToEdit = task
                                dialogEditMode = true
                                showAddDialog = true
                            }
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .shadow(8.dp, RoundedCornerShape(16.dp)),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "add",
                modifier = Modifier.size(24.dp)
            )
        }
    }

    if (showAddDialog) {
        AddTaskDialog(
            isEditMode = dialogEditMode,
            onDismiss = {
                showAddDialog = false
                if (dialogEditMode) {
                    taskToEdit = null
                    dialogEditMode = false;
                }
                        },
            taskToEdit = taskToEdit,
            onAddTask = { title, description ->
                if (dialogEditMode) {
                    taskToEdit?.let { task ->
                        taskViewModel.updateTaskItem(
                            task.copy(title = title, desc = description)
                        )
                    }
                    taskToEdit = null
                    dialogEditMode = false
                } else {
                    taskViewModel.addTaskItem(
                        TaskEntity(title = title, desc = description)
                    )
                }
                showAddDialog = false
            }
        )
    }
}

@Composable
fun TaskItem(
    task: TaskEntity,
    onToggleTask: (TaskEntity) -> Unit,
    onDeleteTask: (TaskEntity) -> Unit,
    onEditTask: (TaskEntity) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.status) 
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
            else 
                MaterialTheme.colorScheme.tertiaryContainer
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.status,
                onCheckedChange = { onToggleTask(task) },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.outline
                )
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                StatusBadge(
                    isCompleted = task.status
                )
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (task.status) FontWeight.Normal else FontWeight.SemiBold,
                    textDecoration = if (task.status) TextDecoration.LineThrough else null,
                    color = if (task.status) 
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    else 
                        MaterialTheme.colorScheme.onSurface
                )
                
                if (task.desc.isNotBlank()) {
                    Text(
                        text = task.desc,
                        style = MaterialTheme.typography.bodyMedium,
                        textDecoration = if (task.status) TextDecoration.LineThrough else null,
                        color = if (task.status) 
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        else 
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Row {
                IconButton(
                    onClick = { onEditTask(task) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "edit",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                IconButton(
                    onClick = { onDeleteTask(task) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "delete",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyTask() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SvgImage(R.drawable.list_woman)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Task empty",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Press + button to add task!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun AddTaskDialog(
    isEditMode: Boolean = false,
    taskToEdit: TaskEntity? = null,
    onDismiss: () -> Unit,
    onAddTask: (String, String) -> Unit
) {
    var title by remember { mutableStateOf(taskToEdit?.title ?: "") }
    var description by remember { mutableStateOf(taskToEdit?.desc ?: "") }
    var titleError by remember { mutableStateOf(false) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = if (isEditMode) "Edit Task" else "Add New Task",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                
                OutlinedTextField(
                    value = title,
                    onValueChange = { 
                        title = it
                        titleError = it.isBlank()
                    },
                    label = { Text("Title *") },
                    isError = titleError,
                    supportingText = if (titleError) {
                        { Text("Title is requried") }
                    } else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                )
                
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (Optional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    minLines = 3,
                    maxLines = 5,
                    shape = RoundedCornerShape(12.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text(
                            "Cancel",
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                onAddTask(title.trim(), description.trim())
                            } else {
                                titleError = true
                            }
                        },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(if (isEditMode) "Update Task" else "Add Task")
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBadge(
    isCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = if (isCompleted) 
            MaterialTheme.colorScheme.primaryContainer
        else 
            MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text = if (isCompleted) "Completed" else "Pending",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = if (isCompleted)
                MaterialTheme.colorScheme.onPrimaryContainer
            else
                MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}


@Composable
fun SvgImage(drawable : Any) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()

    Image(
        painter = rememberAsyncImagePainter(
            model = drawable,
            imageLoader = imageLoader
        ),
        contentDescription = "svg",
        modifier = Modifier.width(width = 52.dp)
    )
}