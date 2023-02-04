package com.udacity.asteroidradar.ui.main

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
    private val TAG: String = "MainFragment"
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentMainBinding
    private lateinit var asteroidAdapter: AsteroidAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        recycleViewSetup()

        asteroidAdapter.onItemClicked = { itemClicked ->
            findNavController().navigate(MainFragmentDirections.actionShowDetail(itemClicked))
        }

        GlobalScope.launch {
            launch {viewModel.getImageOfDay()}
        }

        viewModel.latestData.observe(viewLifecycleOwner, Observer { list ->
            asteroidAdapter.submitList(list)
        })

        viewModel.allData.observe(viewLifecycleOwner, Observer {list->
            asteroidAdapter.submitList(list)
        })

        viewModel.todayData.observe(viewLifecycleOwner, Observer { list ->
            asteroidAdapter.submitList(list)
        })

        viewModel.weekData.observe(viewLifecycleOwner, Observer { list ->
            asteroidAdapter.submitList(list)
        })


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_rent_menu -> {
                viewModel.getTodayData()
            }
            R.id.show_buy_menu -> {
                viewModel.getAllData()
            }
            R.id.show_all_menu -> {
                viewModel.getWeekData()
            }
        }
        return true
    }

    private fun recycleViewSetup() {
        asteroidAdapter = AsteroidAdapter()
        binding.asteroidRecycler.adapter = asteroidAdapter
    }
}
