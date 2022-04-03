package com.example.dailypic.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import com.example.dailypic.R
import kotlinx.android.synthetic.main.fragment_earth_picture.*
import kotlinx.android.synthetic.main.main_fragment.*


class EarthPictureFragment : Fragment() {
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProviders.of(this).get(PictureOfTheDayViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_earth_picture, container, false)
    }

    companion object {
        fun newInstance() = EarthPictureFragment()
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getData()
            .observe(viewLifecycleOwner, Observer<PictureOfTheDayData> { renderData(it) })
    }
    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                earthPicLoadingLayout.visibility = View.GONE
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    toast("Пусто")
                } else {
                    Earth_pic_image_view.load(url) {
                        lifecycle(this@EarthPictureFragment)
                        error(R.drawable.ic_baseline_error_24)
                        placeholder(R.drawable.ic_baseline_no_photography_24)
                    }
                }
            }
            is PictureOfTheDayData.Loading -> {
                earthPicLoadingLayout.visibility = View.VISIBLE
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
}
