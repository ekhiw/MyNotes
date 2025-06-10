package xyz.potasyyum.mynotes.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskItem(
    @SerialName("title")
    var title: String,
    @SerialName("desc")
    var desc: String,
    @SerialName("status")
    var status: Boolean
)
