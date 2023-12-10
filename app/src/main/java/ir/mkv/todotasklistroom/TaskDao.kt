package ir.mkv.todotasklistroom

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface TaskDao {
    @Insert
    fun addTask(taskModel: TaskModel): Long

    @Update
    fun editTask(taskModel: TaskModel): Int

    @Delete
    fun deleteTask(taskModel: TaskModel): Int


    @Query("SELECT * FROM task_tbl")
    fun getTasks(): MutableList<TaskModel>


    @Query("DELETE FROM task_tbl")
    fun deleteAllTasks(): Int


}