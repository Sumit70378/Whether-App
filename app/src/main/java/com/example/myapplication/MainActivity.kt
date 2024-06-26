package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.myapplication.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private  val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        SearchCity()
        fetchWeatherData("Dehradun")
    }

    private fun SearchCity() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
              
                return true
            }
        })
    }

    private fun fetchWeatherData( cityName:String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        val  response =retrofit.getWeatherData(cityName,"da0075b4bcf3d41b992407620ddc2c06","metric")
        response.enqueue(object : Callback<WeatherApp>{
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                val responseBody = response.body()
              if(response.isSuccessful && responseBody != null){
                  val temperature = responseBody.main.temp.toString()
                  val humidity = responseBody.main.humidity
                  val windSpeed = responseBody.wind.speed
                  val sunRise = responseBody.sys.sunrise
                  val sunSets = responseBody.sys.sunset
                  val seaLevel = responseBody.main.pressure
                  val condition = responseBody.weather.firstOrNull()?.main?: "unknown"
                  val maxTemp = responseBody.main.temp_max
                  val minTemp = responseBody.main.temp_min

                  binding.temp.text="$temperature"
                  binding.weather.text = condition
                  binding.maxtemp.text ="Max Temp: $maxTemp C"
                  binding.mintemp.text ="Min Temp: $minTemp C"
                  binding.humidity.text="$humidity %"
                  binding.windspeed.text="$windSpeed m/s"
                 binding.Sunrise.text ="$sunRise"
                  binding.sunset.text="$sunSets"
                 binding.sealevel.text="$seaLevel hpa"
                  binding.day.text=dayName(System.currentTimeMillis())
                      binding.day.text=date()
                      binding.Cityname.text="$cityName"

                 // Log.d("TAG","onResponse : $ temperature")
              }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
        }
    private fun date(): String {
        val sdf =SimpleDateFormat("dd MMMM yyyy",Locale.getDefault())
        return sdf.format((Date()))
    }

    fun dayName(timestamp: Long): String{
        val sdf = SimpleDateFormat("EEE", Locale.getDefault())
        return  sdf.format((Date()))

    }
}