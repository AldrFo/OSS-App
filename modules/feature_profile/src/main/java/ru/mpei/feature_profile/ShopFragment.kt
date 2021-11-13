package ru.mpei.feature_profile

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.AddScreenForward
import kekmech.ru.common_navigation.ClearBackStack
import kekmech.ru.common_navigation.Router
import org.koin.android.ext.android.inject
import ru.mpei.domain_profile.dto.ProfileItem
import ru.mpei.feature_profile.databinding.FragmentShopBinding
import ru.mpei.feature_profile.items.ProductAdapterItem
import ru.mpei.feature_profile.items.ShopAllAdapterItem
import ru.mpei.feature_profile.items.ShopItem
import ru.mpei.feature_profile.items.ShopItem.Companion.ALL_ITEM
import ru.mpei.feature_profile.items.ShopItem.Companion.POPULAR_ITEM
import ru.mpei.feature_profile.items.ShopPopularAdapterItem
import ru.mpei.feature_profile.mvi.*
import ru.mpei.feature_profile.mvi.ProfileEvent.*

class ShopFragment(private val profileData: ProfileItem): BaseFragment<ProfileEvent, ProfileEffect, ProfileState, ProfileFeature>() {

    //Объект-помощник для перехода между фрагментами
    private val router: Router by inject()
    //Объект-помощник для доступа к элементам разметки
    private val binding by viewBinding(FragmentShopBinding::bind)
    private val shopFeatureFactory: ProfileFeatureFactory by inject()

    //ID используемой разметки
    override var layoutId = R.layout.fragment_shop

    //Событие, вызываемое при инициализауии фрагмента
    override val initEvent: ProfileEvent get() = Wish.System.InitShop

    override fun createFeature(): ProfileFeature = shopFeatureFactory.createWithData(profileData)

    //Номер текущего отображаемого списка
    private var listPos: Int = 0

    //Адаптеры для имеющихся списков
    private val popularAdapter: BaseAdapter by fastLazy { createAdapter() }
    private val allAdapter: BaseAdapter by fastLazy { createAdapter() }
    private val viewPagerAdapter by fastLazy {
        BaseAdapter(
            ShopPopularAdapterItem(popularAdapter),
            ShopAllAdapterItem(allAdapter)
        ).apply {
            update(listOf(ShopItem(id = POPULAR_ITEM), ShopItem(id = ALL_ITEM)))
        }
    }

    //Метод, вызываемый при внутреннем создании фрагмента
    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        with(binding){

            with(fragmentShopToolbar) {
                //Устанавливаем иконку кнопки возврата внутри тулбара
                setNavigationIcon(R.drawable.ic_arrow_back)
                //Устанавливаем метод, вызываемый по нажатию кнопки возврата
                setNavigationOnClickListener { router.executeCommand(ClearBackStack()) }
            }

            //Устанавливаем адаптер для карусели списков
            shopViewPager.adapter = viewPagerAdapter

            //Устанавливаем метод, вызываемый при прокрутке карусели
            shopViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    feature.accept(Wish.OnShopPageChange(position))
                    listPos = position
                }
            })

            //Устанавливаем события при нажатии на кнопки селектора отображаемого списка
            selectorPopular.setOnClickListener { feature.accept(Wish.OnShopPageChange(0)) }
            selectorAll.setOnClickListener { feature.accept(Wish.OnShopPageChange(1)) }

            //Устанавливем цветовую схему анимации обновления списка
            swipeRefresh.setColorSchemeResources(R.color.mpei_blue)
            swipeRefresh.setOnRefreshListener {
                if (listPos == 0) {
                    feature.accept(Wish.LoadPopularProducts)
                } else {
                    feature.accept(Wish.LoadAllProducts)
                }
            }
        }
    }

    //Метод, вызываемый при каждом событии внутри фрагмента
    override fun render(state: ProfileState) {
        //Устанавливаем свежие списки продуктов
        popularAdapter.update(state.shopPopularProductsList)
        allAdapter.update(state.shopAllProductsList)
        //Устанавливаем текущий номер селектора списков
        binding.shopViewPager.currentItem = state.selectedShopPage
        //Обновляем внешний вид селектора списков
        renderTabView(state)
    }

    //Метод обработки эффектов внутри фрагмента
    override fun handleEffect(effect: ProfileEffect) = when(effect) {
        is ProfileEffect.AllProductsLoaded -> binding.swipeRefresh.isRefreshing = false
        is ProfileEffect.PopularProductsLoaded -> binding.swipeRefresh.isRefreshing = false
        is ProfileEffect.ShowError -> Toast.makeText(context, "Возникла проблема: "+effect.throwable.localizedMessage, Toast.LENGTH_SHORT).show()
        else -> {}
    }

    //Метод обновления внешнего вида селектора списков
    private fun renderTabView(state: ProfileState) = with(binding) {
        val selectedColor = resources.getColor(R.color.mpei_blue)
        val defaultColor = resources.getColor(R.color.mpei_white)
        if (state.selectedShopPage == 0) {
            selectorPopular.backgroundTintList = ColorStateList.valueOf(selectedColor)
            selectorPopular.setTextColor(defaultColor)
            selectorAll.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
            selectorAll.setTextColor(selectedColor)
            emptyListLabel.isVisible = state.shopPopularProductsList.isEmpty()
            emptyListLabel.visibility = View.GONE
        } else {
            selectorPopular.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
            selectorPopular.setTextColor(selectedColor)
            selectorAll.backgroundTintList = ColorStateList.valueOf(selectedColor)
            selectorAll.setTextColor(defaultColor)
            emptyListLabel.isVisible = state.shopAllProductsList.isEmpty()
            emptyListLabel.visibility = View.GONE
        }
    }

    //Метод создания адаптеров списков
    private fun createAdapter() = BaseAdapter(
        ProductAdapterItem {
            val bundle = Bundle()
            bundle.putSerializable("data", it)
            bundle.putSerializable("profile", profileData)
            val fragment = ProductFragment()
            fragment.arguments = bundle
            router.executeCommand(AddScreenForward { fragment })
        }
    )

    override fun onResume() {
        super.onResume()
        feature.accept(Wish.LoadPopularProducts)
        feature.accept(Wish.LoadAllProducts)
    }
}