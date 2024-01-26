package com.coding.whatstheweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


//  a01272aa0c5edd71776613ca8f18f261
// https://api.openweathermap.org/data/2.5/weather?q=London&APPID=a01272aa0c5edd71776613ca8f18f261
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}