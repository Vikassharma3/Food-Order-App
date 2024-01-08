package com.example.foodorderapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapp.databinding.FragmentSearchBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.BufferedReader
import java.io.InputStreamReader

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var recyclerViewSearch: RecyclerView
    private lateinit var searchAdapter: searchAdapter
    private lateinit var searchList: ArrayList<Searchdataclass>
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.searchText.requestFocus()
        showKeyboard(binding.searchText)


        binding.backbtn.setOnClickListener {
            navController.navigate(R.id.action_searchFragment_to_homeFragment)
            val bottomNavigationView: BottomNavigationView = requireActivity().findViewById(R.id.bottomnav)
            bottomNavigationView.setSelectedItemId(R.id.home)
        }

        binding.searchText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != ""){
                    filterData(s.toString())
                }else{
                    search()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        recyclerViewSearch = view.findViewById(R.id.searchrv)
        search()
    }

    private fun showKeyboard(editText: EditText) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun filterData(filterText: String) {
        val filteredData = ArrayList<Searchdataclass>()

        for (item in searchList) {
            // Filter based on the food name
            if (item.searchtext.contains(filterText, true)) {
                filteredData.add(item)
            }
        }

        searchAdapter.filterList(filteredData)
    }



    private fun search() {
        recyclerViewSearch.setHasFixedSize(true)

        // Use GridLayoutManager with vertical orientation
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recyclerViewSearch.layoutManager = layoutManager

        searchList = ArrayList()
        searchAddDataToList()

        searchAdapter = searchAdapter(searchList)
        recyclerViewSearch.adapter = searchAdapter

        recyclerViewSearch.setItemViewCacheSize(20)
    }

    @SuppressLint("SuspiciousIndentation")
    private fun searchAddDataToList() {
        try {
            val inputStream = InputStreamReader(requireContext().assets.open("popular_food.csv"))
            val reader = BufferedReader(inputStream)
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                val row: List<String> = line!!.split(",")

                   searchList.add(Searchdataclass(row[2], row[1], row[3].toInt(), row[4], row[5]))
                }

            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
