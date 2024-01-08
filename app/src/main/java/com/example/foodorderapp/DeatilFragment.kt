package com.example.foodorderapp

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.foodorderapp.databinding.FragmentDeatilBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso

class DeatilFragment : Fragment() {

    private lateinit var binding: FragmentDeatilBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeatilBinding.inflate(inflater, container, false)

        val detailsimg: ImageView = binding.root.findViewById(R.id.detailsimg)
        val detailtext: TextView = binding.root.findViewById(R.id.detailtitile)
        val detailprice: TextView = binding.root.findViewById(R.id.detailprice)
        val detailrating: TextView = binding.root.findViewById(R.id.rating)
        val detaildesc: TextView = binding.root.findViewById(R.id.desc)

        val detailimginputdata = arguments?.getString("popularimg")
        val detailtextinputdata = arguments?.getString("populartext")
        val detailpriceinputdata = arguments?.getInt("popularprice")
        val detailratinginputdata = arguments?.getString("popularrating")
        val detaildescinputdata = arguments?.getString("populardesc")

        Picasso.get().load(detailimginputdata).fit().centerCrop().into(detailsimg)
        detailtext.text = detailtextinputdata
        detailprice.text = "$"+detailpriceinputdata.toString()
        detailrating.text = detailratinginputdata
        detaildesc.text = detaildescinputdata

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        binding.backbtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.addtocart.setOnClickListener {
            val title = arguments?.getString("populartext") ?: ""
            val price = arguments?.getInt("popularprice")?: 0
            val img = arguments?.getString("popularimg") ?: ""
            val cartDatabaseHelper = CartDatabaseHelper(requireContext())
            cartDatabaseHelper.addToCart(title, price, img)
            navController.navigate(R.id.action_deatilFragment_to_cartFragment)
            val bottomNavigationView: BottomNavigationView = requireActivity().findViewById(R.id.bottomnav)
            bottomNavigationView.setSelectedItemId(R.id.cart)
        }
    }
}