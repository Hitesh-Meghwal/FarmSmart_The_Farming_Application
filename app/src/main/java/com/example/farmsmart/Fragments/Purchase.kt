package com.example.farmsmart.Fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.appcompat.widget.Toolbar
import com.example.farmsmart.R
import com.example.farmsmart.databinding.FragmentPurchaseBinding
import com.google.android.material.appbar.MaterialToolbar

class Purchase : Fragment() {
    lateinit var binding: FragmentPurchaseBinding
    lateinit var toolbar: MaterialToolbar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPurchaseBinding.inflate(inflater,container,false)
        toolbar = binding.purchaseToolbar
        toolbar()
        return binding.root
    }

    private fun toolbar(){
        toolbar.setOnMenuItemClickListener { menuitem->
            when(menuitem.itemId){
                R.id.search->{
                    val searchView = menuitem.actionView as SearchView
                    searchView.queryHint = "Search Products"
                    searchView.setOnQueryTextListener(object : OnQueryTextListener{
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return false
                        }
                        override fun onQueryTextChange(newText: String?): Boolean {
                            return false
                        }
                    })
                    true
                }
                R.id.history->{

                    true
                }
                R.id.cart->{

                    true
                }
                else -> false
            }
        }
    }
}