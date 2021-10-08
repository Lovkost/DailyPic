package com.example.dailypic

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.dailypic.ui.main.IS_SWITCH_KEY
import com.example.dailypic.ui.main.PictureOfTheDayFragment



class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        if(getFlag(IS_SWITCH_KEY)){
//            Не понимаю почему не могу зассетить цвет выдает NullPointerException
//            toolbarMain.setBackgroundColor(Color.parseColor("#D52A2663"))
            this.setTheme(R.style.DarkTheme)
        } else this.setTheme(R.style.Theme_DailyPic)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                .commitNow()
        }
    }
    fun getFlag(key:String): Boolean {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        if(sharedPref!=null) return sharedPref.getBoolean(key,false)
        return false
    }
}