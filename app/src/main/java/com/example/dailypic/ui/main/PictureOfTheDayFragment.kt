package com.example.dailypic.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.TransitionManager
import android.transition.TransitionSet
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import com.example.dailypic.R
import com.example.dailypic.recycler.RecyclerActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_fragment.*
import kotlin.concurrent.fixedRateTimer

const val IS_BOOTOM_NAVIGATION_VIEW = "NAV_VIEW"

class PictureOfTheDayFragment : Fragment() {
    private var isExpanded = false

    //Ленивая инициализация модели
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProviders.of(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbarSettting)
        val imageView = view.findViewById<EquilateralImageView>(R.id.image_view)
        imageView.setOnClickListener {
            isExpanded = !isExpanded
            TransitionManager.beginDelayedTransition(
                container, TransitionSet()
                    .addTransition(ChangeBounds())
                    .addTransition(ChangeImageTransform())
            )
            val params: ViewGroup.LayoutParams = imageView.layoutParams
            params.height =
                if (isExpanded) ViewGroup.LayoutParams.MATCH_PARENT else
                    ViewGroup.LayoutParams.WRAP_CONTENT
            imageView.layoutParams = params
            imageView.scaleType =
                if (isExpanded) ImageView.ScaleType.CENTER_CROP else
                    ImageView.ScaleType.FIT_CENTER
        }
//        bottomNavigation?.selectedItemId = getItem(IS_BOOTOM_NAVIGATION_VIEW)
        toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.setting -> {
                    fragmentManager?.beginTransaction()?.replace(R.id.container, SettingsFragment())
                        ?.addToBackStack(null)?.commit()

                }
                R.id.openActivityRecycler->{
                    startActivity(
                        Intent(activity, RecyclerActivity::class.java)
                    )
                }
            }
            return@setOnMenuItemClickListener true
        }
        bottomNavigation?.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.DailyPicBNV -> {
                    savePos(it.itemId)
                    fragmentManager?.beginTransaction()
                        ?.replace(R.id.container, PictureOfTheDayFragment())?.addToBackStack(null)
                        ?.commit()
                }
                R.id.EarthPicBNV -> {
                    savePos(it.itemId)
                    fragmentManager?.beginTransaction()
                        ?.replace(R.id.container, EarthPictureFragment())?.addToBackStack(null)
                        ?.commit()
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
        return view
    }

    private fun getItem(isBootomNavigationView: String): Int {
        val sharedPrefBottomNavPos = activity?.getPreferences(Context.MODE_PRIVATE)
        if (sharedPrefBottomNavPos != null) return sharedPrefBottomNavPos.getInt(
            isBootomNavigationView,
            0
        )
        return 0
    }

    private fun savePos(itemId: Int) {
        var sharedPrefBottomNavPos = activity?.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPrefBottomNavPos?.edit()
        editor?.putInt(IS_BOOTOM_NAVIGATION_VIEW, itemId)
        editor?.apply()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getData()
            .observe(viewLifecycleOwner, Observer<PictureOfTheDayData> { renderData(it) })
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                loadingLayout.visibility = View.GONE
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    toast("Пусто")
                } else {
                    image_view.load(url) {
                        lifecycle(this@PictureOfTheDayFragment)
                        error(R.drawable.ic_baseline_error_24)
                        placeholder(R.drawable.ic_baseline_no_photography_24)
                    }
                    textViewTitle.text = serverResponseData.title
                    textViewDescription.text = serverResponseData.explanation
                    textViewTitle.typeface = Typeface.createFromAsset(context?.assets,"font/SpaceQuest-Xj4o.ttf")
                    textViewDescription.typeface = Typeface.createFromAsset(context?.assets,"font/SpaceQuest-Xj4o.ttf")
                }
            }
            is PictureOfTheDayData.Loading -> {
                loadingLayout.visibility = View.VISIBLE
            }
            //Отобразите загрузку
            //showLoading()
            is PictureOfTheDayData.Error -> {
                data.error.message?.let { toast(it) }
            }
        }
    }

    private fun toast(messageToast: String) {
        Toast.makeText(context, messageToast, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
        private var isMain = true
    }
}