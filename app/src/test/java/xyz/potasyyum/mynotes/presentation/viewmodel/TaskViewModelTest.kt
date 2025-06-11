package xyz.potasyyum.mynotes.presentation.viewmodel

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.Rule
import xyz.potasyyum.mynotes.model.TaskEntity
import xyz.potasyyum.mynotes.model.TaskFilter
import xyz.potasyyum.mynotes.repository.TaskRepository

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var taskRepository: TaskRepository
    private lateinit var taskViewModel: TaskViewModel

    private val dummyTask = TaskEntity(title = "Test Task", desc = "Desc", status = false)
    private val dummyTask2 = TaskEntity(title = "Test Task2", desc = "Desc2", status = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        taskRepository = mockk<TaskRepository>(relaxed = true)

        every { taskRepository.getFilteredTasks(TaskFilter.ALL) } returns flowOf(listOf(dummyTask))
        every { taskRepository.getTotalTaskCount() } returns flowOf(1)
        every { taskRepository.getCompletedTaskCount() } returns flowOf(0)
        coEvery { taskRepository.insertTask(any()) } returns Unit
        coEvery { taskRepository.updateTask(any()) } returns Unit
        coEvery { taskRepository.deleteTask(any()) } returns Unit
        
        taskViewModel = TaskViewModel(taskRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial filter should be ALL`() {
        assertEquals(TaskFilter.ALL, taskViewModel.selectedFilter.value)
    }

    @Test
    fun `setFilter should update selectedFilter`() = runTest {
        taskViewModel.setFilter(TaskFilter.COMPLETED)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(TaskFilter.COMPLETED, taskViewModel.selectedFilter.value)
        verify(atLeast = 1) { taskRepository.getFilteredTasks(TaskFilter.COMPLETED) }
    }

    @Test
    fun `addTaskItem should call taskRepository insertTask`() = runTest {
        val task = TaskEntity(title = "Test Task", desc = "Test Description")

        taskViewModel.addTaskItem(task)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 1) { taskRepository.insertTask(task) }
    }

    @Test
    fun `updateTaskItem should call taskRepository updateTask`() = runTest {
        val task = TaskEntity(id = 1, title = "Updated Task", desc = "Updated Description")

        taskViewModel.updateTaskItem(task)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 1) { taskRepository.updateTask(task) }
    }

    @Test
    fun `toggleTask should change task status to true when false`() = runTest {
        val task = TaskEntity(id = 1, title = "Test Task", desc = "Test Description", status = false)

        taskViewModel.toggleTask(task)
        testDispatcher.scheduler.advanceUntilIdle()
        
        coVerify(exactly = 1) { taskRepository.updateTask(task.copy(status = true)) }
    }

    @Test
    fun `toggleTask should change task status to false when true`() = runTest {
        val task = TaskEntity(id = 1, title = "Test Task", desc = "Test Description", status = true)

        taskViewModel.toggleTask(task)
        testDispatcher.scheduler.advanceUntilIdle()
        
        coVerify(exactly = 1) { taskRepository.updateTask(task.copy(status = false)) }
    }

    @Test
    fun `removeTask should call taskRepository deleteTask`() = runTest {
        val task = TaskEntity(id = 1, title = "Test Task", desc = "Test Description")

        taskViewModel.removeTask(task)
        testDispatcher.scheduler.advanceUntilIdle()
        
        coVerify(exactly = 1) { taskRepository.deleteTask(task) }
    }
}