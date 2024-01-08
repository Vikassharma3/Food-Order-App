package com.example.foodorderapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class searchAdapter(private var searchList: MutableList<Searchdataclass>) :
    RecyclerView.Adapter<searchAdapter.searchviewholder>() {

    class searchviewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val searchimg: ImageView = itemView.findViewById(R.id.searchimg)
        var searchtext: TextView = itemView.findViewById(R.id.searchText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): searchviewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return searchviewholder(view)
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filterList: MutableList<Searchdataclass>){
        searchList = filterList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: searchviewholder, position: Int) {
        val search= searchList[position]

        // Optimize image loading with Picasso
        Picasso.get().load(search.searchimg)
            .fit()
            .centerCrop()
            .into(holder.searchimg)

        holder.searchtext.text = search.searchtext

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("popularimg", searchList[position].searchimg)
            bundle.putString("populartext", searchList[position].searchtext)
            bundle.putInt("popularprice", searchList[position].searchprice)
            bundle.putString("popularrating", searchList[position].rating)
            bundle.putString("populardesc", searchList[position].desc)

            // Use the NavController associated with the itemView
            val navController = Navigation.findNavController(holder.itemView)

            navController.navigate(R.id.action_searchFragment_to_deatilFragment, bundle)
        }
    }

}
