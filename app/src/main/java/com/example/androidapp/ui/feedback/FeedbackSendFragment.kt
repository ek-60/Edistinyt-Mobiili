package com.example.androidapp.ui.feedback

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.androidapp.R
import com.example.androidapp.databinding.FragmentFeedbackReadBinding
import com.example.androidapp.databinding.FragmentFeedbackSendBinding

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

        // the binding -object allows you to access views in the layout, textviews etc.
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}