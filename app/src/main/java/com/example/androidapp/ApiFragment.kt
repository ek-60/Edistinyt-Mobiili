package com.example.androidapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.androidapp.databinding.FragmentApiBinding
import com.example.androidapp.databinding.FragmentDataDetailBinding
import com.google.gson.GsonBuilder


class ApiFragment : Fragment() {


    // change this to match your fragment name
    private var _binding: FragmentApiBinding? = null
    private lateinit var adapter: RecyclerAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // This is layout managetr recyclerviewe
    private lateinit var linearLayoutManager: LinearLayoutManager


    fun getComments() {
        // this is the url where we want to get our data from
        val JSON_URL = "https://jsonplaceholder.typicode.com/comments"

        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET, JSON_URL,
            Response.Listener { response ->

                // print the response as a whole
                // we can use GSON to modify this response into something more usable
                Log.d("ADVTECH", response)
                val gson = GsonBuilder().setPrettyPrinting().create()
                val rows : List<Comment> = gson.fromJson(response, Array<Comment>::class.java).toList()

                for(item: Comment in rows) {
                    Log.d("AVDTECH", item.email.toString())
                }

                adapter = RecyclerAdapter(rows)
                binding.recyclerViewComments.adapter = adapter

                //adapter = RecyclerAdapter(rows)
                //binding.recyclerViewComments.adapter = adapter

            },
            Response.ErrorListener {
                // typically this is a connection error
                Log.d("ADVTECH", it.toString())
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

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentApiBinding.inflate(inflater, container, false)
        val root: View = binding.root

        linearLayoutManager = LinearLayoutManager(context)
        binding.recyclerViewComments.layoutManager = linearLayoutManager

        binding.buttonGetComments.setOnClickListener {
            getComments()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}