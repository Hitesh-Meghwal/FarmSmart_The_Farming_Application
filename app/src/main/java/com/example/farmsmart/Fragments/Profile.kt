package com.example.farmsmart.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import com.example.farmsmart.Activity.TaskAdd
import com.example.farmsmart.R
import com.example.farmsmart.databinding.FragmentProfileBinding
import com.google.android.material.appbar.MaterialToolbar

class Profile : Fragment() {
    lateinit var binding: FragmentProfileBinding
    lateinit var toolbar: MaterialToolbar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        toolbar = binding.profiletoolbar
        toolbar()
        binding.taskaddFABbtn.setOnClickListener{
            val taskActivity = Intent(requireContext(),TaskAdd::class.java)
            startActivity(taskActivity)
        }
        return binding.root

    }

    private fun toolbar(){
        toolbar.setOnMenuItemClickListener { menuItem->
            when(menuItem.itemId){
                R.id.logout->{
                    true
                }
                R.id.language->{
                    true
                }
                R.id.chatbot->{
                    true
                }
                R.id.popUpMenu->{
                    val popUpmenu = PopupMenu(requireActivity(),toolbar,Gravity.END)
                    popUpmenu.menuInflater.inflate(R.menu.profile_popupmenu,popUpmenu.menu)
                    popUpmenu.setOnMenuItemClickListener {menuItem->
                        when(menuItem.itemId){

                            else-> false
                        }

                    }
                    popUpmenu.show()
                    true
                }
                else -> false
            }
        }
    }

}