package ru.mpei.feature_profile.items

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_adapter.BaseItemBinder
import ru.mpei.feature_profile.R
import ru.mpei.feature_profile.databinding.ItemShopBinding
import ru.mpei.feature_profile.items.ShopItem.Companion.ALL_ITEM
import ru.mpei.feature_profile.items.ShopItem.Companion.POPULAR_ITEM

data class ShopItem(
        val id: String
) {
    companion object {
        const val POPULAR_ITEM = "PopularItem"
        const val ALL_ITEM = "RegularItem"
    }
}

interface ShopViewHolder {
    fun update(shopAdapter: BaseAdapter)
}

class ShopViewHolderImpl(
    containerView: View
): RecyclerView.ViewHolder(containerView), ShopViewHolder{

    private val binding = ItemShopBinding.bind(containerView)

    override fun update(shopAdapter: BaseAdapter) {
        binding.recyclerView.apply {
            if (layoutManager == null) layoutManager = GridLayoutManager(context, 2)
            if (adapter == null) adapter = shopAdapter
        }
    }
}

class ShopItemBinder(
    private val adapter: BaseAdapter
) : BaseItemBinder<ShopViewHolder, ShopItem>() {

    override fun bind(vh: ShopViewHolder, model: ShopItem, position: Int) {
        vh.update(adapter)
    }
}

class ShopPopularAdapterItem(
    adapter: BaseAdapter
): AdapterItem<ShopViewHolder, ShopItem>(
    isType = {it is ShopItem && it.id == POPULAR_ITEM },
    layoutRes = R.layout.item_shop,
    itemBinder = ShopItemBinder(adapter),
    viewHolderGenerator = ::ShopViewHolderImpl,
    areItemsTheSame = {a, b -> a.id == b.id}
)

class ShopAllAdapterItem(
    adapter: BaseAdapter
): AdapterItem<ShopViewHolder, ShopItem>(
    isType = {it is ShopItem && it.id == ALL_ITEM },
    layoutRes = R.layout.item_shop,
    itemBinder = ShopItemBinder(adapter),
    viewHolderGenerator = ::ShopViewHolderImpl,
    areItemsTheSame = {a, b -> a.id == b.id}
)