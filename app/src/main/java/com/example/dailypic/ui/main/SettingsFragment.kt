package com.example.dailypic.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.dailypic.R


const val IS_SWITCH_KEY = "SWITCH_KEY"

class SettingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val darkThemeOnSwitch = view?.findViewById<SwitchCompat>(R.id.switchCosmicTheme)
        darkThemeOnSwitch?.isChecked = getFlag(IS_SWITCH_KEY)
        darkThemeOnSwitch?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                saveFlag(isChecked)
requireActivity().recreate()
                context?.setTheme(R.style.DarkTheme)
            } else {
                saveFlag(isChecked)
                requireActivity().recreate()
            }
        }

        return view
    }

    fun getFlag(key: String): Boolean {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        if (sharedPref != null) return sharedPref.getBoolean(key, false)
        return false
    }

    private fun saveFlag(flag: Boolean) {
        val shardePrefSwitch = activity?.getPreferences(Context.MODE_PRIVATE)
        val editor = shardePrefSwitch?.edit()
        editor?.putBoolean(IS_SWITCH_KEY, flag)
        editor?.apply()
    }


    companion object {
        fun newInstance() = SettingsFragment()
    }
}