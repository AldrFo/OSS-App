package ru.mpei.feature_profile.items

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import ru.mpei.domain_profile.dto.ProductItem
import ru.mpei.feature_profile.R
import ru.mpei.feature_profile.databinding.ItemProductBinding

interface ProductViewHolder {
    fun setName(name: String)
    fun setPrice(price: String)
    fun setImage(url: String)
}

class ProductViewHolderImpl(
    containerView: View
) : RecyclerView.ViewHolder(containerView), ProductViewHolder{

    private val binding = ItemProductBinding.bind(containerView)

    override fun setName(name: String) {
        binding.productName.text = name
    }

    override fun setPrice(price: String) {
        binding.productPrice.text = price
    }

    override fun setImage(url: String) {
        Picasso.get()
            .load(url)
            .into(binding.productPhoto)
    }
}

class ProductItemBinder() : BaseItemBinder<ProductViewHolder, ProductItem>(){
    override fun bind(vh: ProductViewHolder, model: ProductItem, position: Int) {
        vh.setName(model.name)
        vh.setPrice(model.price)
        vh.setImage(model.imageUrl)
    }
}

class ProductAdapterItem():
    AdapterItem<ProductViewHolder, ProductItem>(
        isType = { it is ProductItem},
        layoutRes = R.layout.item_product,
        itemBinder = ProductItemBinder(),
        viewHolderGenerator = ::ProductViewHolderImpl
    )
