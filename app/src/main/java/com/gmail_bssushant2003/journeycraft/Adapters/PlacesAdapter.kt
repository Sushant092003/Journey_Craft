package com.gmail_bssushant2003.journeycraft.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gmail_bssushant2003.journeycraft.Models.Items
import com.gmail_bssushant2003.journeycraft.databinding.IndividualItemPlanBinding
import com.gmail_bssushant2003.journeycraft.databinding.IndividualLocationBinding

class PlacesAdapter(private val context: Context, private val placesList: ArrayList<String>) :
    RecyclerView.Adapter<PlacesAdapter.MyViewHolder>() {


        inner class MyViewHolder(val binding: IndividualItemPlanBinding) : RecyclerView.ViewHolder(binding.root) {

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = IndividualItemPlanBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return placesList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.namePlace.text = placesList[position]
    }


}
