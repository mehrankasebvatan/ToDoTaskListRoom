package ir.mkv.todotasklistroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AddTaskSheet(val data: TaskModel?) : BottomSheetDialogFragment() {
    private var callback: ((TaskModel) -> Unit)? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sheet_add_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loTask: TextInputLayout = view.findViewById(R.id.loTask)
        val inputTask: TextInputEditText = view.findViewById(R.id.inputTask)
        val tvAdd: TextView = view.findViewById(R.id.tvAdd)


        if (data != null) inputTask.setText(data.title)

        tvAdd.setOnClickListener {
            if (inputTask.text.toString().isEmpty()) {
                loTask.error = "Enter Your New Task!"
            } else {
                val taskModel = TaskModel(
                    title = inputTask.text.toString(),
                    isCompleted = data?.isCompleted ?: false,
                    id = data?.id ?: 0,
                    dateTime = ""
                )
                callback?.invoke(taskModel)
            }
        }

    }


    fun addNewTask(callback: (TaskModel) -> Unit) {
        this.callback = callback
    }

}