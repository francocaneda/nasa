package com.example.libros

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.libros.model.Libro

class BookAdapter(private val books: List<Libro>) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.tvTitle)
        val authorText: TextView = itemView.findViewById(R.id.tvAuthor)
        val coverImage: ImageView = itemView.findViewById(R.id.ivCover)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val libro = books[position]
        holder.titleText.text = libro.title ?: "Sin título"
        holder.authorText.text = libro.author_name?.joinToString(", ") ?: "Desconocido"

        // Cargar portada usando Coil
        if (libro.cover_i != null) {
            val url = "https://covers.openlibrary.org/b/id/${libro.cover_i}-M.jpg"
            holder.coverImage.load(url)
        } else {
            holder.coverImage.setImageResource(R.drawable.ic_book_placeholder)
        }
    }

    override fun getItemCount(): Int = books.size
}
