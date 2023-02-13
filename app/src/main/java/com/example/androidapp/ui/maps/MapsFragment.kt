package com.example.androidapp.ui.maps

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.androidapp.R
import com.example.androidapp.databinding.FragmentMapsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment(), GoogleMap.OnMarkerClickListener {

    // change this to match your fragment name
    private var _binding: FragmentMapsBinding? = null

    //Jotta päästään googleMap olioon muuallakin
    private lateinit var gMap : GoogleMap

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val callback = OnMapReadyCallback { googleMap ->

        gMap = googleMap

        //kaksi markkeria Sydney ja Rovaniemi

        val sydney = LatLng(-34.0, 151.0)
        var marker1 : Marker? = googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        marker1?.tag = "Sydney"

        //Karttaan markkeri Rovaniemen kohdalle
        val rovaniemi = LatLng(66.50194605901969, 25.7346120323976)
        var marker2 : Marker? = googleMap.addMarker(MarkerOptions().position(rovaniemi).title("Rovaniemi"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rovaniemi, 15f))
        marker2?.tag = "Rovaniemi"

        googleMap.setOnMarkerClickListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // the binding -object allows you to access views in the layout, textviews etc.
        binding.checkBoxZoom.setOnCheckedChangeListener { compoundButton, b ->
            gMap.uiSettings.isZoomControlsEnabled = compoundButton.isChecked
        }

        //vaihdetaan kartan tyyliä valitun ulkoasun mukaan
        binding.radioButtonNormal.setOnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) {
                gMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            }
        }

        binding.radioButtonHybrid.setOnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) {
                gMap.mapType = GoogleMap.MAP_TYPE_HYBRID
            }
        }

        binding.radioButtonTerrain.setOnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) {
                gMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        Log.d("Marker", "Toimiii")

        var coordinates : LatLng = p0.position
        var city = p0.tag.toString()

        Log.d("Marker: ", city + " Lat: " + coordinates.latitude.toString() + " Long: " + coordinates.longitude.toString())

        //val action = MapsFragmentDirections.actionMapsFragmentToMapsDetailFragment(coordinates.latitude.toFloat(), coordinates.longitude.toFloat())

        val action = MapsFragmentDirections.actionMapsFragmentToMapsDetailFragment(coordinates.longitude.toFloat(), coordinates.latitude.toFloat())
        findNavController().navigate(action)

        return false
    }
}