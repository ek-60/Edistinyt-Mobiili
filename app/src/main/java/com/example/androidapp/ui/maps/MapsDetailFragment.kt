package com.example.androidapp.ui.maps

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
import com.example.androidapp.BuildConfig
import com.example.androidapp.data.weather.CityWeather
import com.example.androidapp.databinding.FragmentMapsDetailBinding
import com.google.gson.GsonBuilder

class MapsDetailFragment : Fragment() {

    // change this to match your fragment name
    private var _binding: FragmentMapsDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // get fragment parameters from previous fragment
    val args : MapsDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val API_KEY : String = BuildConfig.OPENWEATHER_API_KEY

        //En tiedä miksi lat tilalle pitää laittaa long arvo ja toiste päin?
        val JSON_URL = "https://api.openweathermap.org/data/2.5/weather?lat=${args.long}&lon=${args.lat}&appid=${API_KEY}&units=metric"

        Log.d("MapsDetail", JSON_URL)
        Log.d("MapsDetail", "LONG" + args.long.toString())
        Log.d("MapsDetail", "LAT" + args.lat.toString())

        val gson = GsonBuilder().setPrettyPrinting().create()

        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET, JSON_URL,
            Response.Listener { response ->

                // print the response as a whole
                // we can use GSON to modify this response into something more usable
                Log.d("MapsDetail", response)

                // muutetaan JSON -> CityWeatheriksi, koska vain yksi objekti JSONissa, käytetään tätä versiota
                var item : CityWeather = gson.fromJson(response, CityWeather::class.java)
                Log.d("MapsDetail", item.main?.temp.toString() + " C")
                Log.d("MapsDetail", item.name.toString())

                // jos käyttöliittymässä olisi esim. TextView tällä id:llä, voimme asettaa
                // nyt helposti siihen dataa, esim. lämpötila
                binding.textViewGetLocation.text = item.name.toString()
                binding.textViewGetWeather.text = item.main?.temp.toString() + " C"

            },
            Response.ErrorListener {
                // typically this is a connection error
                Log.d("MapsDetail", it.toString())
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