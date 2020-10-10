package com.danielecampogiani.gltfdemo.androidApp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.danielecampogiani.gltfdemo.shared.Model

class Adapter(
    private val onTap: (Model) -> Unit
) : ListAdapter<Model, Adapter.ViewHolder>(Adapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onTap)
    }

    companion object : DiffUtil.ItemCallback<Model>() {
        override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean =
            oldItem == newItem

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image: ImageView = view.findViewById(R.id.image)
        private val name: TextView = view.findViewById(R.id.name)

        fun bind(model: Model, onTap: (Model) -> Unit) {
            image.load(model.screenshot)
            name.text = model.name

            itemView.setOnClickListener { onTap(model) }
        }
    }
}