package com.example.dailypic.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import com.example.dailypic.R
import kotlinx.android.synthetic.main.main_fragment.*
import kotlin.concurrent.fixedRateTimer

class PictureOfTheDayFragment : Fragment() {
    //Ленивая инициализация модели
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProviders.of(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbarMain)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.setting -> {
                    fragmentManager?.beginTransaction()?.replace(R.id.container, SettingsFragment())
                        ?.addToBackStack(null)?.commit()

                }
            }
            return@setOnMenuItemClickListener true
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getData()
            .observe(this@PictureOfTheDayFragment, Observer<PictureOfTheDayData> { renderData(it) })
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
                    //Отобразите фото
                    //showSuccess()
                    //Coil в работе: достаточно вызвать у нашего ImageView
                    //нужную extension-функцию и передать ссылку и заглушки для placeholder
                    image_view.load(url) {
                        lifecycle(this@PictureOfTheDayFragment)
                        error(R.drawable.ic_baseline_error_24)
                        placeholder(R.drawable.ic_baseline_no_photography_24)
                    }
                    textViewTitle.text = serverResponseData.title
                    textViewDescription.text = serverResponseData.explanation
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