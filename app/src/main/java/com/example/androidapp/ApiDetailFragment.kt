package com.example.androidapp

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
import com.example.androidapp.databinding.FragmentApiDetailBinding
import com.example.androidapp.databinding.FragmentDataDetailBinding
import com.google.gson.GsonBuilder


class ApiDetailFragment : Fragment() {

    // change this to match your fragment name
    private var _binding: FragmentApiDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // get fragment parameters from previous fragment
    val args: ApiDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentApiDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val JSON_URL = "https://jsonplaceholder.typicode.com/comments"

        //vaihtoehto 1: otetaan parametrina pelkkä id, käytetään volleyta ja gsonia ja heataan yksi kommentti rajapinnasta

        val postId = args.id.toString()
        Log.d("ApiDetailFragment", "ID: " + postId)

        //Lisää rajapintaan id, jotta päästään käsiksi haluttuun itemiin
        val NEW_JSON_URL = JSON_URL + "/" + postId
        Log.d("ApiDetailFragment", "NewUrl: " + NEW_JSON_URL)

        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET, NEW_JSON_URL,
            Response.Listener { response ->

                // print the response as a whole
                // we can use GSON to modify this response into something more usable
                val gson = GsonBuilder().setPrettyPrinting().create()
                val item : Comment = gson.fromJson(response, Comment::class.java)

                //Printataan valitun itemin tiedot ja laitetaan ne paikalleen fragmentissa
                Log.d("ApiDetailFragment", "id: " + item.id.toString() + " email: " + item.email.toString() + " name: " + item.name.toString() + " body: " + item.body.toString())

                binding.textViewGetUserId.text = item.id.toString()
                binding.textViewGetUserName.text = item.name.toString()
                binding.textViewGetUserEmail.text = item.email.toString()
                binding.textViewGetUserBody.text = item.body.toString()

            },
            Response.ErrorListener {
                // typically this is a connection error
                Log.d("ApiDetailFragment", it.toString())
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





        //vaihtoehto 2: laitetaan ApiDetailFragmentille useampi parametri, jossa on muut tiedot kommentista
        //huono puoli: jos data on muuttunut rajapinnassa välissä -> tällä tavalla tiedot voivat olla vanhoja

        //vaihtoehto 3: otetaan vain yksi parametri, joka on tekstimuuttuja. muutetaan kaikki kommentin tiedot JSONiksi
        //GSONin avulla ja siirretään se parametrina ApiDetailFragmentille, jossa se taas puretaan GSONilla. sama ongelma myös
        //data

        // the binding -object allows you to access views in the layout, textviews etc.

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}