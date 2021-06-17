package ru.mpei.feature_profile.items

/**
*Турлюк Андрей Игоревич
*А-08-17
*/


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import ru.mpei.domain_profile.dto.ProductItem
import ru.mpei.feature_profile.R
import ru.mpei.feature_profile.databinding.ItemProductBinding

//Интерфейс ViewHolder для товара в вписке
interface ProductViewHolder {
    fun setName(name: String)
    fun setPrice(price: String)
    fun setImage(url: String)
    fun setOnClickListener(onClick: () -> Unit)
}

//Реализауия описанного выще интерфейса
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

    override fun setOnClickListener(onClick: () -> Unit) {
        binding.root.setOnClickListener { onClick() }
    }
}

//Связыватель элемента списка и товара
class ProductItemBinder(
    private val onClick: (ProductItem) -> Unit
) : BaseItemBinder<ProductViewHolder, ProductItem>(){
    override fun bind(vh: ProductViewHolder, model: ProductItem, position: Int) {
        vh.setName(model.name)
        vh.setPrice(model.price.toString())
        vh.setImage(model.imageUrl)
        vh.setOnClickListener { onClick(model) }
    }
}

//Элемент списка продукта для адаптера списка
class ProductAdapterItem(
    onClick: (ProductItem) -> Unit
):
    AdapterItem<ProductViewHolder, ProductItem>(
        isType = { it is ProductItem},
        layoutRes = R.layout.item_product,
        itemBinder = ProductItemBinder(onClick),
        viewHolderGenerator = ::ProductViewHolderImpl
    )
