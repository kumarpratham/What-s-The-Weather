package com.coding.whatstheweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import com.coding.whatstheweather.data.WeatherApp
import com.coding.whatstheweather.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


//  a01272aa0c5edd71776613ca8f18f261
// https://api.openweathermap.org/data/2.5/weather?q=London&APPID=a01272aa0c5edd71776613ca8f18f261
class MainActivity : AppCompatActivity() {
    val appid = "a01272aa0c5edd71776613ca8f18f261"
    lateinit var city : String
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        city = "jaunpur"
        fetchWeatherData(city)
        searchCity()
    }

    private fun searchCity() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    city = query
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    private fun fetchWeatherData(cityName : String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        val response = retrofit.getWeatherData(cityName,appid,"metric")
        response.enqueue(object : Callback<WeatherApp>{
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    bindData(responseBody)
                    updateUI(responseBody)
                }
            }
            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {

            }

        })
    }

    private fun updateUI(responseBody: WeatherApp) {

        when (responseBody.weather[0].main.toString()) {
            "Clear Sky", "Sunny", "Clear" -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
            "Partly Clouds", "Clouds", "Overcast", "Mist", "Foggy", "Haze" -> {
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }
            "Light Rain", "Drizzle", "Moderate Rain", "Showers", "Heavy Rain" -> {
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }
            "Light Snow", "Moderate Snow", "Heavy Snow", "Blizzard" ->{
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }
            else -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
        }
        binding.lottieAnimationView.playAnimation()
    }

    private fun bindData(responseBody: WeatherApp) {
        binding.location.text = city
        binding.temp.text = responseBody.main.temp.toString() + "°C"
        binding.maxTemp.text = responseBody.main.temp.toString() + "°C"
        binding.minTemp.text = responseBody.main.temp.toString() + "°C"
        binding.day.text = dayName(System.currentTimeMillis())
        binding.date.text = date()
        binding.humidity.text = responseBody.main.humidity.toString()
        binding.wind.text = responseBody.wind.speed.toString() + "m/s"
        binding.condition.text = responseBody.weather[0].main.toString()
        binding.sunrise.text = "${time(responseBody.sys.sunrise.toLong())}"
        binding.sunset.text = "${time(responseBody.sys.sunset.toLong())}"
        binding.sea.text = responseBody.main.sea_level.toString()
        binding.conditionHead.text = responseBody.weather[0].main.toString()

    }

    private fun date() : String {
        val sdf = SimpleDateFormat("dd MMMM YYYY", Locale.getDefault())
        return sdf.format(Date())
    }
    private fun time(timestamp: Long) : String {
        val sdf = SimpleDateFormat("HH:MM", Locale.getDefault())
        return sdf.format(Date(timestamp * 1000))
    }
    private fun dayName(timestamp: Long) : String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format(Date())
    }

}