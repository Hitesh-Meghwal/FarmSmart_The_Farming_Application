package com.example.farmsmart.Fragments

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.farmsmart.R
import com.example.farmsmart.databinding.FragmentBookVehicleBinding
import com.google.android.material.appbar.MaterialToolbar

@Suppress("UNREACHABLE_CODE", "DEPRECATION")
class BookVehicle : Fragment() {
    lateinit var binding: FragmentBookVehicleBinding
    lateinit var toolbar: Toolbar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBookVehicleBinding.inflate(inflater,container,false)
        toolbar = binding.bookVehicleToolbar
        setHasOptionsMenu(true)
        toolbar()
        return binding.root
    }

    private fun toolbar(){
        toolbar.setOnMenuItemClickListener { menuitem->
            when(menuitem.itemId){
                R.id.locationfilter->{
                    showPopUpMenu()
                    true
                }
                R.id.lowtohigh->{
                    true
                }
                R.id.hightolow ->{
                    true
                }
                else -> false
            }
        }
    }
    private fun showPopUpMenu(){
        val popUpmenu = PopupMenu(requireActivity(), toolbar,Gravity.END)
        popUpmenu.menuInflater.inflate(R.menu.location_menu,popUpmenu.menu)
        popUpmenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                else -> false
            }
        }
        popUpmenu.show()
    }


}