package com.tlnacl.reactiveapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tlnacl.reactiveapp.businesslogic.http.ProductBackendApiDecorator
import com.tlnacl.reactiveapp.businesslogic.model.ProductDetail
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class ProductDetailsViewModel(private val api: ProductBackendApiDecorator): ViewModel() {
    private val productDetailsLD = MutableLiveData<ProductDetailsViewState>()
    init {
        Timber.d("init ProductDetailsViewModel")
    }

    fun getProductDetails(): LiveData<ProductDetailsViewState> {
        return productDetailsLD
    }

    // TODO change to viewEvent
    fun doAction(productId: Int) {
        productDetailsLD.value = ProductDetailsViewState.Loading

        viewModelScope.launch {
            try {
                val product = api.getProduct(productId)
                productDetailsLD.value = ProductDetailsViewState.Data(ProductDetail(product, false))
            } catch (e : Exception) {
                productDetailsLD.value = ProductDetailsViewState.Error(e)
            }
        }
    }
}