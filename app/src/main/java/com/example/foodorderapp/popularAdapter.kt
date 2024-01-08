package com.example.foodorderapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class popularAdapter(
    private val popularList: MutableList<Populardataclass>,
    private val navController: NavController
) :
    RecyclerView.Adapter<popularAdapter.popularviewholder>() {

    class popularviewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val popularimg: ImageView = itemView.findViewById(R.id.popularimg)
        var populartext: TextView = itemView.findViewById(R.id.populartext)
        var popularprice: TextView = itemView.findViewById(R.id.popularprice)

        init {
            // Perform findViewById operations here
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): popularviewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.popular_food, parent, false)
        return popularviewholder(view)
    }

    override fun getItemCount(): Int {
        return popularList.size
    }

    override fun onBindViewHolder(holder: popularviewholder, position: Int) {
        val popular = popularList[position]

        // Optimize image loading with Picasso
        Picasso.get().load(popular.popularimg)
            .fit()
            .centerCrop()
            .into(holder.popularimg)

        holder.populartext.text = popular.populartext
        holder.popularprice.text = "$" + popular.popularprice.toString()

        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putString("popularimg", popular.popularimg)
                putString("populartext", popular.populartext)
                putInt("popularprice", popular.popularprice)
                putString("popularrating", popular.rating)
                putString("populardesc", popular.desc)
            }

            // Use the NavController associated with the adapter
            navController.navigate(R.id.action_homeFragment_to_deatilFragment, bundle)
        }
    }
}
