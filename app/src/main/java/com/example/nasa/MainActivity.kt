package com.example.nasa

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.nasa.databinding.ActivityMainBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val apiKey = "DEMO_KEY" // 🔑 cambia por tu propia API key

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Botón para seleccionar fecha
        binding.buttonSelectDate.setOnClickListener {
            showDatePicker()
        }

        // Configurar WebView para videos
        binding.webViewVideo.webViewClient = WebViewClient()
        binding.webViewVideo.settings.javaScriptEnabled = true
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, y, m, d ->
            val selectedDate = String.format("%04d-%02d-%02d", y, m + 1, d)
            fetchApod(selectedDate)
        }, year, month, day).show()
    }

    private fun fetchApod(date: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.imageViewApod.visibility = View.GONE
        binding.webViewVideo.visibility = View.GONE

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(NasaApiService::class.java)
        val call = service.getApod(apiKey, date)

        call.enqueue(object : Callback<ApodResponse> {
            override fun onResponse(call: Call<ApodResponse>, response: Response<ApodResponse>) {
                binding.progressBar.visibility = View.GONE

                val apod = response.body()
                if (!response.isSuccessful || apod == null) {
                    Toast.makeText(this@MainActivity, "No se pudo obtener la imagen/video", Toast.LENGTH_SHORT).show()
                    return
                }

                binding.textViewTitle.text = apod.title
                binding.textViewDate.text = apod.date
                binding.textViewDescription.text = apod.explanation

                when (apod.media_type) {
                    "image" -> {
                        binding.imageViewApod.visibility = View.VISIBLE
                        Glide.with(this@MainActivity)
                            .load(apod.url)
                            .into(binding.imageViewApod)
                    }
                    "video" -> {
                        binding.webViewVideo.visibility = View.VISIBLE
                        binding.webViewVideo.loadUrl(apod.url)
                    }
                    else -> {
                        Toast.makeText(this@MainActivity, "Tipo de contenido desconocido", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<ApodResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@MainActivity, "Fallo: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
