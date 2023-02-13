package com.example.androidapp.ui.todo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.androidapp.data.todo.Todos
import com.example.androidapp.databinding.FragmentApiTodoDetailBinding
import com.google.gson.GsonBuilder

class ApiTodoDetailFragment : Fragment() {
    // change this to match your fragment name
    private var _binding: FragmentApiTodoDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // get fragment parameters from previous fragment
    val args: ApiTodoDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentApiTodoDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val JSON_URL = "https://jsonplaceholder.typicode.com/todos"

        //otetaan parametrina pelkkä id, käytetään volleyta ja gsonia ja heataan yksi kommentti rajapinnasta

        val postId = args.id.toString()
        Log.d("TodoDetail", "ID: " + postId)

        //Lisää rajapintaan id, jotta päästään käsiksi haluttuun itemiin
        val NEW_JSON_URL = JSON_URL + "/" + postId
        Log.d("TodoDetail", "NewUrl: " + NEW_JSON_URL)

        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET, NEW_JSON_URL,
            Response.Listener { response ->

                // print the response as a whole
                // we can use GSON to modify this response into something more usable
                val gson = GsonBuilder().setPrettyPrinting().create()
                val item : Todos = gson.fromJson(response, Todos::class.java)

                //Printataan valitun itemin tiedot ja laitetaan ne paikalleen fragmentissa
                Log.d("TodoDetail", "ID: " + item.id.toString() + " title: " + item.title.toString() + " status: " + item.completed.toString())

                binding.textViewGetTodoId.text = item.id.toString()
                binding.textViewGetTodoTitle.text = item.title.toString()
                binding.textViewGetTodoCompleted.text = item.completed.toString()
            },
            Response.ErrorListener {
                // typically this is a connection error
                Log.d("TodoDetail", it.toString())
            })
        {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {

                // basic headers for the data
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json; charset=utf-8"
                return headers
            }
        }

        // Add the request to the RequestQueue. This has to be done in both getting and sending new data.
        // if using this in an activity, use "this" instead of "context"
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)

        // the binding -object allows you to access views in the layout, textviews etc.

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}