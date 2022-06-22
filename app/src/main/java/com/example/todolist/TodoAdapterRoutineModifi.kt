package com.example.todolist


import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.Todo
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class TodoAdapterRoutineModifi: RecyclerView.Adapter<TodoAdapterRoutineModifi.TodoViewHolderRoutineModifi>() {
    private var todoList:ArrayList<Todo> = ArrayList()
    private var onCheck:((Todo) -> Unit)? = null
    private var onClickItem:((Todo) -> Unit)? = null
    private var onDeleteItem:((Todo) -> Unit)? = null



    fun addItem(items:ArrayList<Todo>){
        this.todoList = items
        notifyDataSetChanged()
    }

    fun deleteDoneToDos(todo: Todo){

        todoList.remove(todo)
        notifyDataSetChanged()
    }
    fun setOnDeleteItem(callback:(Todo) -> Unit){
        this.onDeleteItem = callback
    }

    fun setOnClickItem(callback:(Todo) -> Unit){
        this.onClickItem = callback
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolderRoutineModifi = TodoViewHolderRoutineModifi(
        LayoutInflater.from(parent.context).inflate(R.layout.routine_task_modifi,parent,false)
    )

    private fun toggleStrikeThrough(tvToDoTitle: TextView, isChecked: Boolean ){
        if(isChecked){
            tvToDoTitle.paintFlags = tvToDoTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }else{
            tvToDoTitle.paintFlags = tvToDoTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onBindViewHolder(holder: TodoViewHolderRoutineModifi, position: Int) {

        var todo = todoList[position]


        holder.bindView(todo)
        val curTodo = todoList[position]

        holder.itemView.setOnClickListener { onClickItem?.invoke(curTodo)  }

        holder.itemView.apply {

            val tvName = findViewById<TextView>(R.id.tvTittle)
            var tvTime = findViewById<TextView>(R.id.tvTime)
            var line = findViewById<LinearLayout>(R.id.llLine)
            var task: LinearLayout = findViewById(R.id.task)
            var ibDel = findViewById<ImageButton>(R.id.ibDel)
            if(curTodo.priority == "School"){
                //task.setBackgroundResource(R.drawable.routine_school)
                line.setBackgroundResource(R.drawable.school)
            }
            if(curTodo.priority == "Productivity"){
                //task.setBackgroundResource(R.drawable.routine_productivity)
                line.setBackgroundResource(R.drawable.productivity)
            }
            if(curTodo.priority == "Freetime"){
                //task.setBackgroundResource(R.drawable.routine_freetime)
                line.setBackgroundResource(R.drawable.freetime)
            }
            if(curTodo.priority == "Workout"){
                //task.setBackgroundResource(R.drawable.routine_workout)
                line.setBackgroundResource(R.drawable.workout)
            }
            if(curTodo.priority == "Sleep"){
                //task.setBackgroundResource(R.drawable.routine_sleep)
                line.setBackgroundResource(R.drawable.sleep)
            }
            if(curTodo.priority == "Food"){
                //task.setBackgroundResource(R.drawable.routine_food)
                line.setBackgroundResource(R.drawable.food)
            }
            if(curTodo.priority == ""){
                //task.setBackgroundResource(R.drawable.routine)
                line.setBackgroundResource(R.drawable.none)
            }
            tvName.text = curTodo.tittle
            tvTime.text = todo.time
            ibDel.setOnClickListener { onDeleteItem?.invoke(curTodo) }




        }
    }

    override fun getItemCount(): Int {
        return todoList.size
    }
    fun countTodos():Int {
        return todoList.size
    }

    class TodoViewHolderRoutineModifi(var view: View): RecyclerView.ViewHolder(view){
        private var tvTittle = view.findViewById<TextView>(R.id.tvTittle)
        private var tvTime = view.findViewById<TextView>(R.id.tvTime)
        private var task = view.findViewById<LinearLayout>(R.id.task)



        fun bindView(todo:Todo) {
            tvTittle.text = todo.tittle
            tvTime.text = todo.time
        }
    }


}