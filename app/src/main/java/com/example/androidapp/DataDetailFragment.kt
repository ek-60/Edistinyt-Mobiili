package com.example.androidapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.androidapp.databinding.FragmentDataDetailBinding
import com.example.androidapp.databinding.FragmentDataReadBinding

class DataDetailFragment : Fragment() {
    // change this to match your fragment name
    private var _binding: FragmentDataDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // get fragment parameters from previous fragment
    val args: DataDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDataDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // print out the given parameter into logs
        Log.d("Testi", "Parametri" + args.id.toString())

        // the binding -object allows you to access views in the layout, textviews etc.

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}