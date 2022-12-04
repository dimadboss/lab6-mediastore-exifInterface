package com.example.lab6_mediastore_exifinterface

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lab6_mediastore_exifinterface.data.ExifData
import com.example.lab6_mediastore_exifinterface.data.getGeo
import com.example.lab6_mediastore_exifinterface.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        val exifData = arguments?.getParcelable<ExifData?>("ExifTags") ?: return

        if (exifData.date != null) {
            binding.editDate.setText(exifData.date)
        }
        if (exifData.model != null) {
            binding.editModel.setText(exifData.model)
        }
        if (exifData.device != null) {
            binding.editDevice.setText(exifData.device)
        }

        val geo = exifData.getGeo() ?: return
        binding.editLatitude.setText(geo.latitude.toString())
        binding.editLongitude.setText(geo.longitude.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}