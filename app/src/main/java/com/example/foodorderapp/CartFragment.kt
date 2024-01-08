package com.example.foodorderapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import cartAdapter
import com.example.foodorderapp.databinding.FragmentCartBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class CartFragment : Fragment() {

    private lateinit var cartList: MutableList<CartDataClass>
    private lateinit var binding: FragmentCartBinding
    private lateinit var cartAdapter: cartAdapter
    private lateinit var navController: NavController
    private var COLUMN_PRICE = "price"
    private var TABLE_CART = "cart"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        updateCartItemCount(binding.root)
        updatetotal(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)

        binding.backtomenu.setOnClickListener {
            val bottomNavigationView: BottomNavigationView = requireActivity().findViewById(R.id.bottomnav)
            navController.navigate(R.id.action_cartFragment_to_homeFragment)
            bottomNavigationView.setSelectedItemId(R.id.home)
        }

        // Initialize RecyclerView and Adapter
        cartList = mutableListOf()
        cartAdapter = cartAdapter(cartList) {position ->
            onRemoveItemClick(position)
        }

        binding.cartrv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }

        // Fetch data from the database and update the RecyclerView
        loadDataFromDatabase()

        // Update the cart item count
        updateCartItemCount(binding.root)
        updatetotal(binding.root)
    }


    private fun loadDataFromDatabase() {
        val cartDatabaseHelper = CartDatabaseHelper(requireContext())

        try {
            val fetchedCartList = cartDatabaseHelper.getAllItems()

            // Print the fetched items to log
            Log.d("CartItems", "Fetched items: ${fetchedCartList}")

            cartList.clear()
            cartList.addAll(fetchedCartList)
            cartAdapter.notifyDataSetChanged()
        } catch (e: Exception) {
            Log.e("DatabaseError", "Error loading data from the database: ${e.message}")
        }
    }


    private fun onRemoveItemClick(position : Int) {
        val adjustPosition = cartList[position].id
        val cartDatabaseHelper = CartDatabaseHelper(requireContext())
        cartDatabaseHelper.removeItemFromCart(adjustPosition)
        Log.e("databrother", "${adjustPosition}")

        cartList.removeAt(position)
        cartAdapter.notifyItemRemoved(position)
        cartAdapter.notifyItemRangeChanged(position, cartList.size - position)

        updateCartItemCount(binding.root)
        updatetotal(binding.root)
    }



    private fun updateCartItemCount(root: FrameLayout) {
        val textViewItemCount: TextView = binding.textView4

        // Initialize your CartDatabaseHelper
        val cartDatabaseHelper = CartDatabaseHelper(requireContext())

        try {
            // Get the cart item count from the database
            val cartItemCount = cartDatabaseHelper.getCartItemCount()

            // Update the TextView with the cart item count
            textViewItemCount.text = "Cart Items: $cartItemCount"
        } catch (e: Exception) {
            Log.e("DatabaseError", "Error updating cart item count: ${e.message}")
        }
    }


    private fun updatetotal(root: FrameLayout){
        val total : TextView = binding.total
        val cartDatabaseHelper = CartDatabaseHelper(requireContext())

        val gettotal = "$" + cartDatabaseHelper.getCartTotal()

        total.text = gettotal.toString()
    }
}
