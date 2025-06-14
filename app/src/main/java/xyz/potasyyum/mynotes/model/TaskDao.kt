package xyz.potasyyum.mynotes.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import xyz.potasyyum.mynotes.util.ConstUtils.TASK_TABLE_NAME


@Dao
interface TaskDao {
    @Query("SELECT * FROM ${TASK_TABLE_NAME}")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Insert
    suspend fun insert(taskEntity: TaskEntity)

    @Update
    suspend fun update(taskEntity: TaskEntity)

    @Delete
    suspend fun delete(taskEntity: TaskEntity)

    @Query("SELECT * FROM ${TASK_TABLE_NAME} WHERE status = 0")
    fun getPendingTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM ${TASK_TABLE_NAME} WHERE status = 1")
    fun getCompletedTasks(): Flow<List<TaskEntity>>

    @Query("SELECT COUNT(*) FROM ${TASK_TABLE_NAME}")
    fun getTotalTaskCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM ${TASK_TABLE_NAME} WHERE status = 1")
    fun getCompletedTaskCount(): Flow<Int>
}