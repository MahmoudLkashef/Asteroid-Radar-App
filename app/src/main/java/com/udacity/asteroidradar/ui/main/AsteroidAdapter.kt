package com.udacity.asteroidradar.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidListItemBinding
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.model.Asteroid

class AsteroidAdapter:ListAdapter<Asteroid,AsteroidAdapter.AsteroidViewHolder>(DiffCallback()){

    var onItemClicked:((Asteroid)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        val view = LayoutInflater.from(parent.context)
        val asteroidListItemBinding=AsteroidListItemBinding.inflate(view,parent,false)
        return AsteroidViewHolder(asteroidListItemBinding)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val item=getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener(View.OnClickListener {
            onItemClicked?.invoke(item)
        })
    }

    class AsteroidViewHolder(private val binding:AsteroidListItemBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(item:Asteroid){
                binding.asteroid=item
            }
    }
}

class DiffCallback:DiffUtil.ItemCallback<Asteroid>(){
    override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem?.id==newItem?.id
    }

    override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem==newItem
    }

}
