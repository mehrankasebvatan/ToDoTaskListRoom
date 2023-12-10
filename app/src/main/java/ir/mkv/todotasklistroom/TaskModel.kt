package ir.mkv.todotasklistroom

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_tbl")
data class TaskModel(
    @PrimaryKey(autoGenerate = true) var id: Long,
    var title: String,
    var isCompleted: Boolean,
    var dateTime: String
)
