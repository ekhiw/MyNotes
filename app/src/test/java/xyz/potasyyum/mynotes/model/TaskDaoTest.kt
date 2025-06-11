package xyz.potasyyum.mynotes.model

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class TaskDaoTest {

    private lateinit var database: TaskDatabase
    private lateinit var taskDao: TaskDao

    val pendingTask1 = TaskEntity(title = "Pending Task 1", desc = "", status = false)
    val pendingTask2 = TaskEntity(title = "Pending Task 2", desc = "", status = false)
    val completedTask = TaskEntity(title = "Completed Task", desc = "", status = true)

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            TaskDatabase::class.java
        ).allowMainThreadQueries().build()
        
        taskDao = database.taskDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `insert task and get all tasks should return inserted task`() = runTest {
        val task = TaskEntity(title = "Test Task", desc = "Test Description", status = false)

        taskDao.insert(task)
        val allTasks = taskDao.getAllTasks().first()

        assertEquals(1, allTasks.size)
        assertEquals("Test Task", allTasks[0].title)
        assertEquals("Test Description", allTasks[0].desc)
        assertEquals(false, allTasks[0].status)
    }

    @Test
    fun `insert multiple tasks should return all tasks`() = runTest {
        taskDao.insert(pendingTask1)
        taskDao.insert(pendingTask1)
        taskDao.insert(pendingTask1)
        val allTasks = taskDao.getAllTasks().first()

        assertEquals(3, allTasks.size)
    }

    @Test
    fun `update task should modify existing task`() = runTest {
        taskDao.insert(pendingTask1)
        val insertedTask = taskDao.getAllTasks().first()[0]

        val updatedTask = insertedTask.copy(title = "Task 2", desc = "Desc 2", status = true)
        taskDao.update(updatedTask)
        val allTasks = taskDao.getAllTasks().first()

        assertEquals(1, allTasks.size)
        assertEquals("Task 2", allTasks[0].title)
        assertEquals("Desc 2", allTasks[0].desc)
        assertEquals(true, allTasks[0].status)
    }

    @Test
    fun `delete task should remove task from database`() = runTest {
        taskDao.insert(pendingTask1)
        val insertedTask = taskDao.getAllTasks().first()[0]

        taskDao.delete(insertedTask)
        val allTasks = taskDao.getAllTasks().first()

        assertEquals(0, allTasks.size)
    }

    @Test
    fun `getPendingTasks should return only incomplete tasks`() = runTest {
        taskDao.insert(pendingTask1)
        taskDao.insert(pendingTask2)
        taskDao.insert(completedTask)
        val pendingTasks = taskDao.getPendingTasks().first()

        assertEquals(2, pendingTasks.size)
        assertTrue(pendingTasks.all { !it.status })
    }

    @Test
    fun `getCompletedTasks should return only completed tasks`() = runTest {
        taskDao.insert(pendingTask1)
        taskDao.insert(completedTask)
        taskDao.insert(completedTask)
        val completedTasks = taskDao.getCompletedTasks().first()

        assertEquals(2, completedTasks.size)
        assertTrue(completedTasks.all { it.status })
    }

    @Test
    fun `getTotalTaskCount should return correct count`() = runTest {
        taskDao.insert(pendingTask1)
        taskDao.insert(pendingTask2)
        taskDao.insert(completedTask)
        val totalCount = taskDao.getTotalTaskCount().first()

        assertEquals(3, totalCount)
    }

    @Test
    fun `getCompletedTaskCount should return correct count`() = runTest {
        taskDao.insert(pendingTask1)
        taskDao.insert(pendingTask2)
        taskDao.insert(completedTask)
        val completedCount = taskDao.getCompletedTaskCount().first()

        assertEquals(1, completedCount)
    }
}