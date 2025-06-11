package xyz.potasyyum.mynotes.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import xyz.potasyyum.mynotes.util.ConstUtils.TASK_TABLE_NAME

@Entity(tableName = TASK_TABLE_NAME)
@Serializable
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @SerialName("title")
    var title: String,
    @SerialName("desc")
    var desc: String,
    @SerialName("status")
    var status: Boolean = false
)
