package com.gmail_bssushant2003.journeycraft

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gmail_bssushant2003.journeycraft.databinding.IndividualLocationBinding

class MyAdapter(val context: Context, val itemsList: ArrayList<Items>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: IndividualLocationBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = IndividualLocationBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.title.text = itemsList[position].title
        holder.binding.location.text = itemsList[position].location
        holder.binding.image.setImageResource(itemsList[position].image)
    }

}
