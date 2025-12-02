package com.kiki.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kiki.post.databinding.ItemBookBinding

class BookAdapter(
    private var list: List<Book>,
    private val onItemClick: ((Book) -> Unit)? = null
) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: ItemBookBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Book) {
            binding.apply {
                tvTitle.text = book.title
                tvReleaseDate.text = book.releaseDate

                // Load image using Glide
                Glide.with(binding.root.context)
                    .load(book.cover)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(ivCover)

                // Set click listener
                root.setOnClickListener {
                    onItemClick?.invoke(book)
                }
            }
        }
    }
}