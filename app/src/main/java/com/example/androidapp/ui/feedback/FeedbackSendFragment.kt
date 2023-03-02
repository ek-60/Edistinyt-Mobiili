package com.example.androidapp.ui.feedback

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.androidapp.BuildConfig
import com.example.androidapp.R
import com.example.androidapp.data.feedback.Feedback
import com.example.androidapp.databinding.FragmentFeedbackReadBinding
import com.example.androidapp.databinding.FragmentFeedbackSendBinding
import com.google.gson.GsonBuilder
import org.json.JSONObject
import java.io.UnsupportedEncodingException

class FeedbackSendFragment : Fragment() {
    // change this to match your fragment name
    private var _binding: FragmentFeedbackSendBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedbackSendBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.buttonSendFeedbackToApi.setOnClickListener{

            val name = binding.editTextFeedbackName.text.toString()
            val location = binding.editTextFeedbackLocation.text.toString()
            val value = binding.editTextFeedbackValue.text.toString()

            sendFeedback(name, location, value)

        }
        // the binding -object allows you to access views in the layout, textviews etc.
        return root
    }

    //funktio datan lähettämiseen directusiin, ottaa parametrina uuden feedbackin yksityiskohdata
    fun sendFeedback(name : String, location : String, value : String) {
        Log.d("Testi", "${name} - ${location} - ${value}")

        val API_KEY : String = BuildConfig.ACCESS_TOKEN

        // this is the url where we want to get our data from
        val JSON_URL = "https://vp8btw7e.directus.app/items/feedback?access_token=${API_KEY}"

        val gson = GsonBuilder().setPrettyPrinting().create()

        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.POST, JSON_URL,
            Response.Listener { response ->
                Log.d("Testi", "Lähetys directus ok!")
                binding.editTextFeedbackName.setText("")
                binding.editTextFeedbackLocation.setText("")
                binding.editTextFeedbackValue.setText("")

                Toast.makeText(context, "Thank you for your feedback!", Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()

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

            override fun getBody(): ByteArray {
                var body = ByteArray(0)
                try {
                    var newData = ""

                    var f: Feedback = Feedback()
                    f.name = name
                    f.location = location
                    f.value = value

                    newData = gson.toJson(f)
                    body = newData.toByteArray(Charsets.UTF_8)

                    Log.d("Testi", newData)
                }  catch (e: UnsupportedEncodingException) {
                    // problems with converting our data into UTF-8 bytes
                }
                return body
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