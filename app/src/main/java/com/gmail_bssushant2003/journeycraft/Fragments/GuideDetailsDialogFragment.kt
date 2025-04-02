package com.gmail_bssushant2003.journeycraft.Fragments

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.gmail_bssushant2003.journeycraft.Models.Guide
import com.gmail_bssushant2003.journeycraft.R
import com.gmail_bssushant2003.journeycraft.databinding.DialogGuideDetailsBinding

class GuideDetailsDialogFragment(private val guide: Guide) : DialogFragment() {

    private var _binding: DialogGuideDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return requireActivity().let {
            _binding = DialogGuideDetailsBinding.inflate(it.layoutInflater)

//            binding.root.setBackgroundResource(android.R.color.transparent)

            binding.name.text = guide.name ?: "XYZ"
            binding.experience.text = guide.experience.toString()
    //            binding.guideLanguages.text = "Languages: ${it.language ?: "Not specified"}"
            binding.bio.text = guide.bio
            binding.phoneNumber.text = guide.phoneNo
    //            binding.guideLocation.text = "Location: ${it.latitude}, ${it.longitude}"

            val ph = guide.phoneNo

            // Build the AlertDialog
            AlertDialog.Builder(it,R.style.TransparentDialog)
                .setView(binding.root)
                .create()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Prevent memory leaks
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

    }
}
