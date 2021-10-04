package com.example.dailypic.ui.main

import android.app.TaskStackBuilder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import com.example.dailypic.R
import kotlinx.android.synthetic.main.fragment_settings.*
import com.example.dailypic.MainActivity

import android.content.Intent




class SettingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbarMain)
        val darkThemeOnSwitch = view?.findViewById<SwitchCompat>(R.id.switchCosmicTheme)
        darkThemeOnSwitch?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                toolbar.setBackgroundColor(R.color.ToolbarDarkTheme)
                requireActivity().apply {
                    setTheme(R.style.DarkTheme)
                    recreate()
                }
            }
        }

        return view
    }



    companion object {
        fun newInstance() = SettingsFragment()
    }
}