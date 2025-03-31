package com.gmail_bssushant2003.journeycraft.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.gmail_bssushant2003.journeycraft.GuidesAndRestaurants.DetailedGuideActivity
import com.gmail_bssushant2003.journeycraft.Models.Guide
import com.gmail_bssushant2003.journeycraft.databinding.IndividualGuidesItemBinding

class GuidesAdapter(var context : Context, var guidesList : ArrayList<Guide>) : RecyclerView.Adapter<GuidesAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding : IndividualGuidesItemBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = IndividualGuidesItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return guidesList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.guideName.text = guidesList[position].name
        holder.binding.guideMobileNumber.text = guidesList[position].phoneNo

        holder.binding.guideCard.setOnClickListener {
            val intent = Intent(context, DetailedGuideActivity::class.java)
            intent.putExtra("guideData", guidesList[position])
            holder.itemView.context.startActivity(intent)
        }

//        holder.binding.expandCard.setOnClickListener {
//            val intent = Intent(Intent.ACTION_DIAL).apply {
//                data = "tel:${guidesList[position].phoneNo}".toUri()
//            }
//            holder.itemView.context.startActivity(intent)
//        }
    }
}