package com.example.foodorderapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class categoryAdapter(private val categoryList: ArrayList<Categorydataclass>) :
    RecyclerView.Adapter<categoryAdapter.categoryviewholder>() {

    var onItemClick: ((Categorydataclass) -> Unit)? = null
    private var selectedPosition = 0

    class categoryviewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardView)
        val categoryimg: ImageView = itemView.findViewById(R.id.categoryimg)
        var categorytext: TextView = itemView.findViewById(R.id.categorytxt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): categoryviewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_items, parent, false)
        return categoryviewholder(view)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: categoryviewholder, position: Int) {
        val category = categoryList[position]

        // Optimize image loading with Picasso
        Picasso.get().load(category.categoryImg)
            .fit()
            .centerCrop()
            .into(holder.categoryimg)

        holder.categorytext.text = category.categoryTxt

        val currentPosition = holder.adapterPosition

        // Update card background based on selection
        if (currentPosition == selectedPosition) {
            holder.cardView.setCardBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.purple)
            )
        } else {
            holder.cardView.setCardBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.gray)
            )
        }

        // Handle item click
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(category)
            notifyItemChanged(selectedPosition)
            selectedPosition = currentPosition
            notifyItemChanged(selectedPosition)
        }
    }
}
