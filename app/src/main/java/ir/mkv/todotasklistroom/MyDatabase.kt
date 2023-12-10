package ir.mkv.todotasklistroom

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, exportSchema = false, entities = [TaskModel::class])
abstract class MyDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao


}