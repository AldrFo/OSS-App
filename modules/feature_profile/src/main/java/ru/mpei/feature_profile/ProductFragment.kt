package ru.mpei.feature_profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_navigation.ClearBackStack
import kekmech.ru.common_navigation.PopUntil
import kekmech.ru.common_navigation.Router
import org.koin.android.ext.android.inject
import ru.mpei.domain_profile.dto.ProductItem
import ru.mpei.domain_profile.dto.ProfileItem
import ru.mpei.feature_profile.databinding.FragmentProductBinding

class ProductFragment: Fragment(R.layout.fragment_product) {

    private val router: Router by inject()
    private val binding by viewBinding(FragmentProductBinding::bind)

    private lateinit var productData: ProductItem
    private lateinit var profileData: ProfileItem

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productData = arguments?.get("data") as ProductItem
        profileData = arguments?.get("profile") as ProfileItem

        with(binding) {
            with(fragmentProductToolbar) {
                setNavigationIcon(R.drawable.ic_arrow_back)
                setNavigationOnClickListener { router.executeCommand( PopUntil(ShopFragment::class) ) }
            }
            fragmentProductToolbarText.text = productData.name

            fragmentProductName.text = productData.name
            fragmentProductPrice.text = productData.price.toString()
            fragmentProductDescription.text = if (productData.description.isEmpty()) "Описание отсутствует" else productData.description

            Picasso.get()
                .load(productData.imageUrl)
                .into(fragmentProductPhoto)

            btnBuy.setOnClickListener {
                if( enoughMoney() ){
                    if ( enoughQuantity() ) {
                        val popup = BuyProductPopup(profile = profileData, product = productData)
                        popup.show(parentFragmentManager, "purchaseDialog")
                    } else {
                        Toast.makeText(context, "К сожалению товар закончился, попробуйте еще раз позднее.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(context, "У вас недостаточно баллов для покупки этого товара. Выполните еще задания, чтобы купить этот товар.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun enoughMoney(): Boolean = (profileData.capital - productData.price >= 0)
    private fun enoughQuantity(): Boolean = (productData.quantity > 0)



}