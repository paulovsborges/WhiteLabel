package br.com.douglasmotta.whitelabeltutorial.ui.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.douglasmotta.whitelabeltutorial.databinding.ItemProductBinding
import br.com.douglasmotta.whitelabeltutorial.domain.model.Product
import br.com.douglasmotta.whitelabeltutorial.util.toCurrency
import com.bumptech.glide.Glide

class ProductsAdapter(
    private val items: List<Product>
) : RecyclerView.Adapter< ProductsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Product) {

            Glide.with(itemView)
                .load(item.imageUrl)
                .fitCenter()
                .into(binding.imageProduct)

            binding.productDescription.text = item.description
            binding.productPrice.text = item.price.toCurrency()
        }
    }
}