package ir.mkv.todotasklistroom

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import ir.mkv.todotasklistroom.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var adapter: MainAdapter? = null
    private lateinit var db: MyDatabase
    private var list = mutableListOf<TaskModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createDB()
        handleList()
        events()


    }

    private fun createDB(){
        db = Room.databaseBuilder(this@MainActivity, MyDatabase::class.java, "task_db")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    private fun events() {
        binding.apply {
            btnAdd.setOnClickListener {
                val sheet = AddTaskSheet(null)
                sheet.addNewTask {
                    val res = db.taskDao().addTask(it)
                    if (res != -1L) {
                        sheet.dismiss()
                        handleList()
                    } else Toast.makeText(this@MainActivity, "Error!", Toast.LENGTH_SHORT).show()

                }
                sheet.show(supportFragmentManager, "add")
            }

            toolbar.setOnMenuItemClickListener {
                if (list.isEmpty()) Toast.makeText(
                    this@MainActivity,
                    "List is empty!!",
                    Toast.LENGTH_SHORT
                ).show()
                else {
                    val delete = DeleteTaskSheet("Delete all tasks?")
                    delete.deleteTask {
                        val res = db.taskDao().deleteAllTasks()
                        if (res > 0) {
                            delete.dismiss()
                            Toast.makeText(
                                this@MainActivity,
                                "Delete all tasks successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            handleList()
                        } else Toast.makeText(this@MainActivity, "Error!", Toast.LENGTH_SHORT)
                            .show()
                    }
                    delete.show(supportFragmentManager, "deleteAll")
                }
                return@setOnMenuItemClickListener true

            }

            inputSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    search(s.toString())
                }
            })


        }
    }


    private fun search(text: String) {
        var newList = mutableListOf<TaskModel>()
        if (text.isEmpty()) {
            newList = list
        } else {
            newList.clear()
            for (s in list) {
                if (s.title.contains(text)) newList.add(s)
            }
        }
        adapter?.search(newList)
    }

    private fun handleList() {
        binding.inputSearch.setText("")
        list = db.taskDao().getTasks()
        if (list.isEmpty()) {
            binding.apply {
                txtNoData.visibility = View.VISIBLE
                rvData.visibility = View.GONE
            }
        } else {
            binding.apply {
                adapter = MainAdapter(list) { data, what ->
                    when (what) {
                        "D" -> {

                            val delete = DeleteTaskSheet("Delete this task?")
                            delete.deleteTask {
                                val res = db.taskDao().deleteTask(data)
                                if ((res ?: 0) > 0) {
                                    delete.dismiss()
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Delete SuccessFully!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    handleList()
                                } else Toast.makeText(
                                    this@MainActivity,
                                    "Error!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            delete.show(supportFragmentManager, "delete")


                        }

                        "E" -> {

                            val edit = AddTaskSheet(data)
                            edit.addNewTask { ed ->
                                val res = db.taskDao().editTask(ed)
                                if ((res ?: 0) > 0) {
                                    edit.dismiss()
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Edit successfully!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    handleList()
                                } else Toast.makeText(
                                    this@MainActivity,
                                    "Error!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            edit.show(supportFragmentManager, "edit")


                        }

                        "C" -> {
                            data.isCompleted = !data.isCompleted
                            val res = db.taskDao().editTask(data)
                            if ((res ?: 0) == 0) {
                                for (s in list) {
                                    if (s.id == data.id) {
                                        s.isCompleted = data.isCompleted
                                    }
                                }
                            }
                            adapter?.update()
                        }
                    }
                }
                rvData.adapter = adapter
                txtNoData.visibility = View.GONE
                rvData.visibility = View.VISIBLE
            }

        }
    }
}