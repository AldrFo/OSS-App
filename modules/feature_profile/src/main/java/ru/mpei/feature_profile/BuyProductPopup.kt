package ru.mpei.feature_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.squareup.picasso.Picasso
import kekmech.ru.common_android.viewbinding.viewBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mpei.domain_profile.ProfileApi
import ru.mpei.domain_profile.dto.ProductItem
import ru.mpei.domain_profile.dto.ProfileItem
import ru.mpei.feature_profile.databinding.PopupBuyProductBinding

class BuyProductPopup(private val profile: ProfileItem, private val product: ProductItem): DialogFragment() {

    private lateinit var binding: PopupBuyProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return layoutInflater.inflate(R.layout.popup_buy_product, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PopupBuyProductBinding.bind(view)
        with(binding){

            Picasso.get()
                .load(product.imageUrl)
                .into(popupShopProductImage)

            popupShopProductName.text = product.name
            popupShopProductPrice.text = product.price.toString()
            popupShopProductText.text = getString(R.string.purchase_text).format(profile.capital.toString())
            btnBuyProduct.setOnClickListener {
                val retrofit: Retrofit = Retrofit.Builder()
                    .baseUrl("http://cy37212.tmweb.ru/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val service = retrofit.create(ProfileApi::class.java)

                val call = service.buyProduct(userId = profile.id.toString(), productId = product.id.toString())

                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        okFrame.visibility = View.VISIBLE
                        purchaseSendFrame.visibility = View.GONE
                        successPurchaseText.text = getString(R.string.success_purchase_text).format((profile.capital - product.price).toString())
                        btnReturnToShop.setOnClickListener { dismiss() }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                    }
                })

            }

            btnCancelBuyProduct.setOnClickListener { dismiss() }

        }
    }

}