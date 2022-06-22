
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

class TodoAdapterOther: RecyclerView.Adapter<TodoAdapterOther.TodoViewHolderOther>() {
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolderOther = TodoViewHolderOther(
        LayoutInflater.from(parent.context).inflate(R.layout.tasks,parent,false)
    )

    private fun toggleStrikeThrough(tvToDoTitle: TextView, isChecked: Boolean ){
        if(isChecked){
            tvToDoTitle.paintFlags = tvToDoTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }else{
            tvToDoTitle.paintFlags = tvToDoTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onBindViewHolder(holder: TodoViewHolderOther, position: Int) {
        //var sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")



        var todo = todoList[position]



        holder.bindView(todo)
        val curTodo = todoList[position]
        holder.itemView.setOnClickListener { onClickItem?.invoke(curTodo) }


        holder.itemView.apply {
            var tvDay = findViewById<TextView>(R.id.tvDay)
            var tvMonthAndYear = findViewById<TextView>(R.id.tvMonthAndYear)
            val tvName = findViewById<TextView>(R.id.tvTittle)
            var tvDescription= findViewById<TextView>(R.id.tvDescription)
            var tvTime = findViewById<TextView>(R.id.tvTime)
            var line = findViewById<LinearLayout>(R.id.llLine)
            val checkBox = findViewById<CheckBox>(R.id.cbDone)
            var task: LinearLayout = findViewById(R.id.task)
            tvName.text = curTodo.tittle
            tvDescription.text = curTodo.description
            tvTime.text = todo.time
            if(curTodo.priority =="high"){
                task.setBackgroundResource(R.drawable.task_red)
                line.setBackgroundResource(R.drawable.line_bg_high)
            }else if(curTodo.priority =="medium"){
                task.setBackgroundResource(R.drawable.task_orange)
                line.setBackgroundResource(R.drawable.line_bg_medium)
            }else if(curTodo.priority =="low"){
                task.setBackgroundResource(R.drawable.task_green)
                line.setBackgroundResource(R.drawable.line_bg_low)
            }else{
                task.setBackgroundResource(R.drawable.task)
                line.setBackgroundResource(R.drawable.line_bg_normal)
            }

            if(todo.state == 1){
                task.setBackgroundResource(R.drawable.task_done)
                line.setBackgroundResource(R.drawable.line_bg_done)
                tvName.setTextColor(getResources().getColor(R.color.done))
                tvDescription.setTextColor(getResources().getColor(R.color.done))
                tvTime.setTextColor(getResources().getColor(R.color.done))
                tvDay.setTextColor(getResources().getColor(R.color.done))
                tvMonthAndYear.setTextColor(getResources().getColor(R.color.done))

            }else{
                tvName.setTextColor(getResources().getColor(R.color.text_black))
                tvDescription.setTextColor(getResources().getColor(R.color.text_black))
                tvTime.setTextColor(getResources().getColor(R.color.text_black))
                tvDay.setTextColor(getResources().getColor(R.color.text_black))
                tvMonthAndYear.setTextColor(getResources().getColor(R.color.text_black))
            }

            checkBox.isChecked  = curTodo.state == 1
            toggleStrikeThrough(tvName,curTodo.state ==1)
            checkBox.setOnCheckedChangeListener { _, isChecked ->

                if (curTodo.state == 0 ) {
                    curTodo.state = 1
                    task.setBackgroundResource(R.drawable.task_done)
                    line.setBackgroundResource(R.drawable.line_bg_done)
                    tvName.setTextColor(getResources().getColor(R.color.done))
                    tvDescription.setTextColor(getResources().getColor(R.color.done))
                    tvTime.setTextColor(getResources().getColor(R.color.done))
                    tvDay.setTextColor(getResources().getColor(R.color.done))
                    tvMonthAndYear.setTextColor(getResources().getColor(R.color.done))


                }else{
                    curTodo.state = 0
                    if(curTodo.priority =="high"){
                        task.setBackgroundResource(R.drawable.task_red)
                        line.setBackgroundResource(R.drawable.line_bg_high)
                    }else if(curTodo.priority =="medium"){
                        task.setBackgroundResource(R.drawable.task_orange)
                        line.setBackgroundResource(R.drawable.line_bg_medium)
                    }else if(curTodo.priority =="low"){
                        task.setBackgroundResource(R.drawable.task_green)
                        line.setBackgroundResource(R.drawable.line_bg_low)
                    }else{
                        task.setBackgroundResource(R.drawable.task)
                        line.setBackgroundResource(R.drawable.line_bg_normal)
                    }
                    tvName.setTextColor(getResources().getColor(R.color.text_black))
                    tvDescription.setTextColor(getResources().getColor(R.color.text_black))
                    tvTime.setTextColor(getResources().getColor(R.color.text_black))
                    tvDay.setTextColor(getResources().getColor(R.color.text_black))
                    tvMonthAndYear.setTextColor(getResources().getColor(R.color.text_black))
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

    class TodoViewHolderOther(var view: View): RecyclerView.ViewHolder(view){
        private var tvTittle = view.findViewById<TextView>(R.id.tvTittle)
        private var tvDescription= view.findViewById<TextView>(R.id.tvDescription)
        private var tvDay= view.findViewById<TextView>(R.id.tvDay)
        private var tvMonthAndYear = view.findViewById<TextView>(R.id.tvMonthAndYear)
        private var tvTime = view.findViewById<TextView>(R.id.tvTime)
        private var task: LinearLayout = view.findViewById(R.id.task)

        var checkBox = view.findViewById<CheckBox>(R.id.cbDone)


        fun bindView(todo:Todo) {

            if(todo.deadline != ""){
                var list = todo.deadline.split("/")
                //id.text = todo.id.toString()

                var months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
                tvDay.text = list[0].toString()//list[0]
                //try {

                //}finally {
                //    tvDay.text = ""
                //}
                tvMonthAndYear.text = months[list[1].toInt()-1]
            }

            tvTittle.text = todo.tittle
            tvDescription.text = todo.description

            tvTime.text = todo.time




        }
    }


}