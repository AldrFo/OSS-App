package ru.mpei.feature_profile

/**
 * Андрей Турлюк
 * А-08-17
 */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.squareup.picasso.Picasso
import kekmech.ru.common_navigation.PopUntil
import kekmech.ru.common_navigation.Router
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mpei.domain_profile.ProfileApi
import ru.mpei.domain_profile.dto.ProductItem
import ru.mpei.domain_profile.dto.UserShopInfoItem
import ru.mpei.feature_profile.databinding.PopupBuyProductBinding

class BuyProductPopup(private val profileId: Int, private val product: ProductItem) : DialogFragment() {

    private val router: Router by inject()

    //Объект-помощник для перехода между фрагментами
    private lateinit var binding: PopupBuyProductBinding

    //Переменная для реализации HTTP запросов
    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://oss.mpei.ru/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //Переменная для создания HTTP запросов к разным адресам с разными параметрами
    private val service = retrofit.create(ProfileApi::class.java)

    //Текущий баланс пользователя
    private var currCapital: Int = 0

    //Метод, вызываемый при создании отображения
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        //Устанавливаем необходимую разметку
        return layoutInflater.inflate(R.layout.popup_buy_product, container)
    }

    //Метод, вызываемый при создании фрагмента
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Устанавливаем переменную для доступа к эелементам разметки
        binding = PopupBuyProductBinding.bind(view)

        //HTTP запрос при инициализации диалогового окна
        val initCall = service.getUserShopInfo(profileId.toString())

        //Выполнение запроса
        initCall.enqueue(object : Callback<UserShopInfoItem> {
            //При успешном выполнении запроса
            override fun onResponse(call: Call<UserShopInfoItem>, response: Response<UserShopInfoItem>) {
                //Устанавливаем текущий баланс и вызываем метод инициализации стандартной разметки
                currCapital = response.body()!!.capital
                successInit()
            }

            //При неудачном выполнении запроса
            override fun onFailure(call: Call<UserShopInfoItem>, t: Throwable) {
                //Вызываем метод инициализации разметки с сообщением об ошибке
                serverError()
            }
        })
    }

    //Метод, вызываемый при успешном инициализирующем запросе
    private fun successInit() {
        with(binding) {

            //Если у пользователя достаточно баллов для покупки
            if (currCapital >= product.price) {
                //Отображаем необходимую разметку и скрываем другие
                purchaseSendFrame.visibility = View.VISIBLE
                okFrame.visibility = View.GONE
                notEnoughMoneyFrame.visibility = View.GONE
                serverErrorFrame.visibility = View.GONE

                //Загружаем фотографию продукта с сервера
                Picasso.get()
                    .load(product.imageUrl)
                    .into(popupShopProductImage)

                //Устанавливем необходимые надписи и слушатели нажатия на кнопку
                popupShopProductName.text = product.name
                popupShopProductPrice.text = product.price.toString()
                popupShopProductText.text = getString(R.string.purchase_text).format(currCapital.toString())
                btnBuyProduct.setOnClickListener {
                    Toast.makeText(context, profileId.toString() + " " + product.id.toString(), Toast.LENGTH_SHORT).show();
                    //При нажатии на кнопку покупки создаем и выполняем соответствующий HTTP запрос
                    val buyCall = service.buyProduct(userId = profileId.toString(), productId = product.id.toString())

                    buyCall.enqueue(object : Callback<UserShopInfoItem> {
                        //В случае успешного запроса отображаем необходимую разметку, устанавливаем надписи и "вешаем" на кнопку
                        // возврата метод закрытия диалогового окна
                        override fun onResponse(call: Call<UserShopInfoItem>, response: Response<UserShopInfoItem>) {
                            okFrame.visibility = View.VISIBLE
                            purchaseSendFrame.visibility = View.GONE
                            notEnoughMoneyFrame.visibility = View.GONE
                            serverErrorFrame.visibility = View.GONE
                            currCapital = response.body()!!.capital
                            successPurchaseText.text = getString(R.string.success_purchase_text).format(currCapital.toString())
                            btnReturnToShop.setOnClickListener {
                                router.executeCommand(PopUntil(ShopFragment::class))
                                dismiss()
                            }
                        }

                        //В случае неудачного запроса показываем разметку с сообщением об ошибке
                        override fun onFailure(call: Call<UserShopInfoItem>, t: Throwable) {
                            serverError()
                        }
                    })

                }

                //При нажатии на отмену покупки закрываем диалоговое окно
                btnCancelBuyProduct.setOnClickListener { dismiss() }

            } else { //Если у пользователя недостаточно баллов для покупки, то отображаем соответствующую разметку,
                //выводим сообщение об ошибке и "вешаем" на кнопку возврата метод закрытия диалогового окна
                purchaseSendFrame.visibility = View.GONE
                okFrame.visibility = View.GONE
                serverErrorFrame.visibility = View.GONE
                notEnoughMoneyFrame.visibility = View.VISIBLE
                notEnoughMoneyText.text = getString(R.string.not_enough_money).format((product.price - currCapital).toString())
                btnNotEnoughMoney.setOnClickListener { dismiss() }
            }

        }
    }

    //Метод, вызываемый вслучае ошибки при запросе к серверу
    private fun serverError() {
        //Отображаем соответствующую разметку, выводим надпись и "вешаем" на кнопку возврата метод хакрытия диалогового окна
        with(binding) {
            serverErrorFrame.visibility = View.VISIBLE
            purchaseSendFrame.visibility = View.GONE
            okFrame.visibility = View.GONE
            notEnoughMoneyFrame.visibility = View.GONE
            serverErrorText.text = getString(R.string.server_error)
            btnServerError.setOnClickListener { dismiss() }
        }
    }

}