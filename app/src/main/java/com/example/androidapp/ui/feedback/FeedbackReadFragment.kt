package com.example.androidapp.ui.feedback

import android.R
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.androidapp.BuildConfig
import com.example.androidapp.data.comment.Comment
import com.example.androidapp.data.feedback.Feedback
import com.example.androidapp.databinding.FragmentDataReadBinding
import com.example.androidapp.databinding.FragmentFeedbackReadBinding
import com.example.androidapp.ui.api.RecyclerAdapter
import com.example.androidapp.ui.read.DataReadFragmentDirections
import com.google.gson.GsonBuilder
import org.json.JSONObject

class FeedbackReadFragment : Fragment() {
    // change this to match your fragment name
    private var _binding: FragmentFeedbackReadBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedbackReadBinding.inflate(inflater, container, false)
        val root: View = binding.root

        getFeedback()

        binding.buttonSendFeedback.setOnClickListener{ v : View ->
            val action = FeedbackReadFragmentDirections.actionFeedbackReadFragmentToFeedbackSendFragment()
            v.findNavController().navigate(action)
        }

        // the binding -object allows you to access views in the layout, textviews etc.
        return root
    }

    var feedback: List<Feedback> = emptyList()

    fun getFeedback() {
        val API_KEY : String = BuildConfig.ACCESS_TOKEN

        // this is the url where we want to get our data from
        val JSON_URL = "https://vp8btw7e.directus.app/items/feedback?access_token=${API_KEY}"

        val gson = GsonBuilder().setPrettyPrinting().create()

        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET, JSON_URL,
            Response.Listener { response ->

                // print the response as a whole
                // we can use GSON to modify this response into something more usable
                Log.d("FeedbackRead", response)

                val jObject = JSONObject(response)
                val jArray = jObject.getJSONArray("data")

                feedback = gson.fromJson(jArray.toString(), Array<Feedback>::class.java).toList()

                for (item in feedback) {
                    Log.d("Feedback", item.name.toString())
                }

                //Recyclerview käyttö huomattavasti monimutkaisempaa, mutta silloin kuin dataa on paljon se on järkevämpi vaihtoehto..
                val adapter = ArrayAdapter(activity as Context, R.layout.simple_list_item_1, feedback)
                binding.listViewFeedback.adapter = adapter

            },
            Response.ErrorListener {
                // typically this is a connection error
                Log.d("Feedback", it.toString())
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}