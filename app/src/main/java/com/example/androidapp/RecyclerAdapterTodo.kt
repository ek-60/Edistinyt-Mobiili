package com.example.androidapp

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.androidapp.databinding.FragmentApiBinding
import com.example.androidapp.databinding.RecyclerviewItemRowBinding
import com.example.androidapp.databinding.RecyclerviewTodoBinding

class RecyclerAdapterTodo(private val todos: List<Todos>) :
    RecyclerView.Adapter<RecyclerAdapterTodo.TodoHolder>() {

    //Alustetaan bindig layout -> recyclerview_item_row.xml
    private var _binding: RecyclerviewTodoBinding? = null
    private val binding get() = _binding!!

    //RecyclerAdapter vaatti että luokassa on toteutettuna:
    //getItemCount, onCreateView, OnBindViewHolder

    //Jotta RecyclerView tietää kuinka monta itemiä listassa on
    override fun getItemCount() = todos.size

    //Tämä funktio kytkee jokaisen yksittäisen
    override fun onBindViewHolder(holder: RecyclerAdapterTodo.TodoHolder, position: Int) {
        //val itemComment = comment[position]
        //holder.bindComment(itemComment)
        val todoComment = todos[position]
        holder.todoComment(todoComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterTodo.TodoHolder {
        _binding = RecyclerviewTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoHolder(binding)
    }

    class TodoHolder(v: RecyclerviewTodoBinding) : RecyclerView.ViewHolder(v.root), View.OnClickListener {

        private var view: RecyclerviewTodoBinding = v
        private var todos: Todos? = null

        init {
            v.root.setOnClickListener(this)
        }

        fun todoComment(todos: Todos) {

            this.todos = todos

            view.textViewTodoTitle.text = todos.title

            if (todos.completed.toString() == "false") {
                view.imageViewTodoClose.setColorFilter(Color.RED)
                view.imageViewTodoClose.setTransitionVisibility(View.VISIBLE)
            }
            else {
                view.imageViewTodoDone.setColorFilter(Color.GREEN)
                view.imageViewTodoDone.setTransitionVisibility(View.VISIBLE)
            }

        }

        override fun onClick(v: View) {
            Log.d("RecyclerTodo", "ID: " + todos?.id.toString())

            view.imageViewShowDetails.setOnClickListener {
                val action = ApiTodoFragmentDirections.actionApiTodoFragmentToApiTodoDetailFragment(todos?.id as Int)
                v.findNavController().navigate(action)
            }
        }

        companion object {

        }
    }

}