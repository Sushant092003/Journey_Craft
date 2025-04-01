package com.gmail_bssushant2003.journeycraft.Fragments

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.gmail_bssushant2003.journeycraft.Models.Guide
import com.gmail_bssushant2003.journeycraft.databinding.DialogGuideDetailsBinding

class GuideDetailsDialogFragment(private val guide: Guide) : DialogFragment() {

    private var _binding: DialogGuideDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return requireActivity().let {
            _binding = DialogGuideDetailsBinding.inflate(it.layoutInflater)

            binding.name.text = guide.name ?: "XYZ"
            binding.experience.text = guide.experience.toString()
    //            binding.guideLanguages.text = "Languages: ${it.language ?: "Not specified"}"
            binding.bio.text = guide.bio
            binding.phoneNumber.text = guide.phoneNo
    //            binding.guideLocation.text = "Location: ${it.latitude}, ${it.longitude}"

            val ph = guide.phoneNo

            // Build the AlertDialog
            AlertDialog.Builder(it)
                .setView(binding.root)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Prevent memory leaks
    }
}
