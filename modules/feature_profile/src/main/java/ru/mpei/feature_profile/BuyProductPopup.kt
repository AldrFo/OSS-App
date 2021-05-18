package ru.mpei.feature_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import ru.mpei.domain_profile.dto.UserShopInfoItem
import ru.mpei.feature_profile.databinding.PopupBuyProductBinding

class BuyProductPopup(private val profileId: Int, private val product: ProductItem): DialogFragment() {

    private lateinit var binding: PopupBuyProductBinding

    private var retrofit: Retrofit = Retrofit.Builder()
                                        .baseUrl("http://cy37212.tmweb.ru/")
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build()

    private val service = retrofit.create(ProfileApi::class.java)
    private var currCapital: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return layoutInflater.inflate(R.layout.popup_buy_product, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PopupBuyProductBinding.bind(view)
        val initCall = service.getUserShopInfo(profileId.toString())

        initCall.enqueue(object : Callback<UserShopInfoItem> {
            override fun onResponse(call: Call<UserShopInfoItem>, response: Response<UserShopInfoItem>) {
                currCapital = response.body()!!.capital
                successInit()
            }

            override fun onFailure(call: Call<UserShopInfoItem>, t: Throwable) {
                serverError()
            }
        })
    }

    private fun successInit(){
        with(binding) {

            if (currCapital >= product.price) {

                purchaseSendFrame.visibility = View.VISIBLE
                okFrame.visibility = View.GONE
                notEnoughMoneyFrame.visibility = View.GONE
                serverErrorFrame.visibility = View.GONE

                Picasso.get()
                    .load(product.imageUrl)
                    .into(popupShopProductImage)

                popupShopProductName.text = product.name
                popupShopProductPrice.text = product.price.toString()
                popupShopProductText.text = getString(R.string.purchase_text).format(currCapital.toString())
                btnBuyProduct.setOnClickListener {

                    val buyCall = service.buyProduct(userId = profileId.toString(), productId = product.id.toString())

                    buyCall.enqueue(object : Callback<UserShopInfoItem> {
                        override fun onResponse(call: Call<UserShopInfoItem>, response: Response<UserShopInfoItem>) {
                            okFrame.visibility = View.VISIBLE
                            purchaseSendFrame.visibility = View.GONE
                            notEnoughMoneyFrame.visibility = View.GONE
                            serverErrorFrame.visibility = View.GONE
                            currCapital = response.body()!!.capital
                            successPurchaseText.text = getString(R.string.success_purchase_text).format(currCapital.toString())
                            btnReturnToShop.setOnClickListener { dismiss() }
                        }

                        override fun onFailure(call: Call<UserShopInfoItem>, t: Throwable) {
                            serverError()
                        }
                    })

                }

                btnCancelBuyProduct.setOnClickListener { dismiss() }

            } else {
                purchaseSendFrame.visibility = View.GONE
                okFrame.visibility = View.GONE
                serverErrorFrame.visibility = View.GONE
                notEnoughMoneyFrame.visibility = View.VISIBLE
                notEnoughMoneyText.text = getString(R.string.not_enough_money).format((product.price - currCapital).toString())
                btnNotEnoughMoney.setOnClickListener { dismiss() }
            }

        }
    }

    private fun serverError(){

        with(binding){
            serverErrorFrame.visibility = View.VISIBLE
            purchaseSendFrame.visibility = View.GONE
            okFrame.visibility = View.GONE
            notEnoughMoneyFrame.visibility = View.GONE
            serverErrorText.text = getString(R.string.server_error)
            btnServerError.setOnClickListener { dismiss() }
        }

    }

}