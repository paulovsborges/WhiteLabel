package br.com.douglasmotta.whitelabeltutorial.ui.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.douglasmotta.whitelabeltutorial.databinding.ItemProductBinding
import br.com.douglasmotta.whitelabeltutorial.domain.model.Product
import br.com.douglasmotta.whitelabeltutorial.util.toCurrency
import com.bumptech.glide.Glide

class ProductsListAdapter : ListAdapter<Product, ProductsListAdapter.ProductsViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        return ProductsViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProductsViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Product) {

            Glide.with(itemView)
                .load(item.imageUrl)
                .fitCenter()
                .into(binding.imageProduct)

            binding.productDescription.text = item.description
            binding.productPrice.text = item.price.toCurrency()
        }

        companion object {
            fun create(parent: ViewGroup): ProductsViewHolder {
                val binding =
                    ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ProductsViewHolder(binding)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }
}