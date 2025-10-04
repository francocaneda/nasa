package com.example.libros

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libros.model.SearchResponse
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var btnSearch: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializamos vistas
        recyclerView = findViewById(R.id.rvBooks)
        etSearch = findViewById(R.id.etSearch)
        btnSearch = findViewById(R.id.btnSearch)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Búsqueda inicial de prueba
        fetchBooks("harry potter")

        // Búsqueda dinámica desde EditText y Button
        btnSearch.setOnClickListener {
            val query = etSearch.text.toString().trim()
            if (query.isNotEmpty()) {
                fetchBooks(query)
            }
        }
    }

    private fun fetchBooks(query: String) {
        lifecycleScope.launch {
            try {
                val response: SearchResponse = RetrofitInstance.api.searchBooks(query)
                val libros = response.docs ?: emptyList()

                Log.d("API_TEST", "Encontrados: ${libros.size} libros")
                if (libros.isNotEmpty()) {
                    libros.forEach { libro ->
                        Log.d(
                            "API_TEST",
                            "Título: ${libro.title}, Autor: ${libro.author_name?.joinToString()}"
                        )
                    }
                }

                // Actualizamos RecyclerView
                recyclerView.adapter = BookAdapter(libros)

            } catch (e: Exception) {
                Log.e("API_TEST", "Error al buscar libros: ${e.message}")
            }
        }
    }
}
