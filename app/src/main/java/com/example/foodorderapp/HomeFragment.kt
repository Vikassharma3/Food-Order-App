package com.example.foodorderapp

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapp.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.BufferedReader
import java.io.InputStreamReader

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var recyclerViewCategory: RecyclerView
    private lateinit var recyclerViewPopular: RecyclerView
    private lateinit var categoryList: ArrayList<Categorydataclass>
    private lateinit var categoryAdapter: categoryAdapter
    private lateinit var popularList: ArrayList<Populardataclass>
    private lateinit var popularAdapter: popularAdapter
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        binding.searchbtn.clearFocus()

        // Adjusted the following lines to use the Fragment's view
        recyclerViewCategory = view.findViewById(R.id.categoryrv)
        recyclerViewPopular = view.findViewById(R.id.popularfoodrv)

        binding.searchbtn.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_searchFragment)
            val bottomNavigationView: BottomNavigationView = requireActivity().findViewById(R.id.bottomnav)
            bottomNavigationView.setSelectedItemId(R.id.search)
        }

        binding.searchimg.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_searchFragment)
        }

        category()
        popularFood()
    }


    private fun category() {
        recyclerViewCategory.setHasFixedSize(true)
        recyclerViewCategory.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        recyclerViewCategory.setItemViewCacheSize(20) // Set cache size
        categoryList = ArrayList()

        categoryAddDataToList()

        categoryAdapter = categoryAdapter(categoryList)
        recyclerViewCategory.adapter = categoryAdapter

        categoryAdapter.onItemClick = { category ->
            popularList.clear()
            popularAdapter.notifyDataSetChanged()
            popularAddDataToList(category.categoryTxt)
        }
    }

    private fun categoryAddDataToList() {
        try {
            val inputStream = InputStreamReader(requireContext().assets.open("category.csv"))
            val reader = BufferedReader(inputStream)
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                val row: List<String> = line!!.split(",")
                categoryList.add(Categorydataclass(row[1], row[0]))
            }

            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    class GridSpacingItemDecoration(
        private val spanCount: Int,
        private val spacing: Int,
        private val includeEdge: Boolean
    ) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            val column = position % spanCount

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount
                outRect.right = (column + 1) * spacing / spanCount
                if (position < spanCount) {
                    outRect.top = spacing
                }
                outRect.bottom = spacing
            } else {
                outRect.left = column * spacing / spanCount
                outRect.right = spacing - (column + 1) * spacing / spanCount
                if (position >= spanCount) {
                    outRect.top = spacing
                }
            }
        }
    }

    private fun popularFood() {
        recyclerViewPopular.setHasFixedSize(true)

        val dpValue = 10 // the value in dp
        val metrics = resources.displayMetrics
        val px = (dpValue * metrics.density).toInt()
        val spacing = (10 * resources.displayMetrics.density).toInt()



        // Use GridLayoutManager with vertical orientation
        val layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        recyclerViewPopular.layoutManager = layoutManager

        recyclerViewPopular.addItemDecoration(GridSpacingItemDecoration(2, spacing, true))

        popularList = ArrayList()

        popularAddDataToList("All")

        popularAdapter = popularAdapter(popularList, findNavController())
        recyclerViewPopular.adapter = popularAdapter

        // Optionally, you can set the item view cache size to optimize performance
        recyclerViewPopular.setItemViewCacheSize(20)
    }

    private fun popularAddDataToList(categoryName: String) {
        try {
            val inputStream = InputStreamReader(requireContext().assets.open("popular_food.csv"))
            val reader = BufferedReader(inputStream)
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                val row: List<String> = line!!.split(",")
                if (row[0].contains(categoryName))
                {
                    popularList.add(Populardataclass(row[2], row[1], row[3].toInt(), row[4], row[5]))
                }else if (categoryName.contains("All")){
                    popularList.add(Populardataclass(row[2], row[1], row[3].toInt(), row[4], row[5]))
                }
            }

            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
