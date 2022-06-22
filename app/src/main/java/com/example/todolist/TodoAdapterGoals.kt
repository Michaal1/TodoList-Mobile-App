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

class TodoAdapterGoals: RecyclerView.Adapter<TodoAdapterGoals.TodoViewHolderGoals>() {
    private var todoList:ArrayList<Todo> = ArrayList()
    private var onCheck:((Todo) -> Unit)? = null
    private var onClickItem:((Todo) -> Unit)? = null






    fun addItem(items:ArrayList<Todo>){
        this.todoList = items
        notifyDataSetChanged()
    }

    fun deleteDoneToDos(todo: Todo){

        todoList.remove(todo)
        notifyDataSetChanged()
    }
    fun setOnCheck(callback:(Todo) -> Unit){
        this.onCheck = callback
    }
    fun setOnClickItem(callback:(Todo) -> Unit){
        this.onClickItem = callback
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolderGoals = TodoViewHolderGoals(
        LayoutInflater.from(parent.context).inflate(R.layout.goal,parent,false)
    )

    private fun toggleStrikeThrough(tvToDoTitle: TextView, isChecked: Boolean ){
        if(isChecked){
            tvToDoTitle.paintFlags = tvToDoTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }else{
            tvToDoTitle.paintFlags = tvToDoTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onBindViewHolder(holder: TodoViewHolderGoals, position: Int) {
        //var sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")



        var todo = todoList[position]

        //var str = todo.deadline + " " + todo.time
        //deadline = sdf.parse(str)
        //var daysDifference = (today.time - deadline.time)/86400000
        //if( daysDifference ){
        //
        //}

        holder.bindView(todo)
        val curTodo = todoList[position]
        holder.itemView.setOnClickListener { onClickItem?.invoke(curTodo) }


        holder.itemView.apply {

            val tvName = findViewById<TextView>(R.id.tvTittle)
            var tvDescription= findViewById<TextView>(R.id.tvDescription)
            val checkBox = findViewById<CheckBox>(R.id.cbDone)
            var task: LinearLayout = findViewById(R.id.task)
            tvName.text = curTodo.tittle
            tvDescription.text = curTodo.description

            if(curTodo.priority == "School"){
                task.setBackgroundResource(R.drawable.goal_school)
            }
            if(curTodo.priority == "Productivity"){
                task.setBackgroundResource(R.drawable.goal_productivity)
            }
            if(curTodo.priority == "Freetime"){
                task.setBackgroundResource(R.drawable.goal_freetime)
            }
            if(curTodo.priority == "Workout"){
                task.setBackgroundResource(R.drawable.goal_workout)
            }
            if(curTodo.priority == "Sleep"){
                task.setBackgroundResource(R.drawable.goal_sleep)
            }
            if(curTodo.priority == "Food"){
                task.setBackgroundResource(R.drawable.goal_food)
            }
            if(curTodo.priority == ""){
                task.setBackgroundResource(R.drawable.goal)
            }

            checkBox.isChecked  = curTodo.state == 1
            toggleStrikeThrough(tvName,curTodo.state ==1)
            checkBox.setOnCheckedChangeListener { _, isChecked ->

                if (curTodo.state == 0 ) {
                    curTodo.state = 1

                }else{
                    curTodo.state = 0
                }
                toggleStrikeThrough(tvName, isChecked)

                onCheck?.invoke(curTodo)

            }
        }
    }

    override fun getItemCount(): Int {
        return todoList.size
    }
    fun countTodos():Int {
        return todoList.size
    }

    class TodoViewHolderGoals(var view: View): RecyclerView.ViewHolder(view){
        private var tvTittle = view.findViewById<TextView>(R.id.tvTittle)
        private var tvDescription= view.findViewById<TextView>(R.id.tvDescription)

        var checkBox = view.findViewById<CheckBox>(R.id.cbDone)


        fun bindView(todo:Todo) {


            tvTittle.text = todo.tittle
            tvDescription.text = todo.description



        }
    }


}