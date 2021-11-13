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

    //Объект-помощник для перехода между фрагментами
    private val router: Router by inject()
    //Объект-помощник для доступа к элементам разметки
    private val binding by viewBinding(FragmentProductBinding::bind)

    //Переменная с информацией о продукте
    private lateinit var productData: ProductItem
    //Переменная с информацией о пользователе
    private lateinit var profileData: ProfileItem

    //Метод, вызываемый при создании фрагмента
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Инициализируем переменные с информацией о продукте и пользователе
        productData = arguments?.get("data") as ProductItem
        profileData = arguments?.get("profile") as ProfileItem

        with(binding) {
            with(fragmentProductToolbar) {
                //Устанавливаем иконку кнопки возврата внутри тулбара
                setNavigationIcon(R.drawable.ic_arrow_back)
                //Устанавливаем метод, вызываемый по нажатию кнопки возврата
                setNavigationOnClickListener { router.executeCommand( PopUntil(ShopFragment::class) ) }
            }
            //Устанавливем название продукта в надпись на тулбаре
            fragmentProductToolbarText.text = productData.name

            //Устанавливаем надписи с названием, ценом и описанием продукта
            fragmentProductName.text = productData.name
            fragmentProductPrice.text = productData.price.toString()
            fragmentProductDescription.text = if (productData.description.isEmpty()) "Описание отсутствует" else productData.description

            //Загружаем изображение товара
            Picasso.get()
                .load(productData.imageUrl)
                .into(fragmentProductPhoto)

            //Устанавливаем слушатель нажатия на кнопку покупки
            btnBuy.setOnClickListener {
                if ( enoughQuantity() ) { //Если достаточное количество товара,то открываем диалоговое окно покупки
                    val popup = BuyProductPopup(profileId = profileData.id, product = productData)
                    popup.show(parentFragmentManager, "purchaseDialog")
                } else { //Иначе показываем сообщение о нехватке товара
                    Toast.makeText(context, "К сожалению товар закончился, попробуйте еще раз позднее.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    //Метод проверки достаточного количества товара
    private fun enoughQuantity(): Boolean = (productData.quantity > 0)

}