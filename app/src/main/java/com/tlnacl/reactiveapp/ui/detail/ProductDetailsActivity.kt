package com.tlnacl.reactiveapp.ui.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionManager
import coil.load
import com.tlnacl.reactiveapp.AndroidApplication
import com.tlnacl.reactiveapp.Constants
import com.tlnacl.reactiveapp.R
import com.tlnacl.reactiveapp.businesslogic.model.Product
import com.tlnacl.reactiveapp.dataflow.data.ViewState
import com.tlnacl.reactiveapp.dataflow.onStates
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.include_errorview.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by tlnacl on 11/07/17.
 */
class ProductDetailsActivity : AppCompatActivity() {
    val KEY_PRODUCT_ID = "productId"
    private var product: Product? = null
    private var isProductInshoppingCart = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        (application as AndroidApplication).appComponent.inject(this)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Timber.d("onCreate")
        val viewModel = ViewModelProvider(this, viewModelFactory).get(ProductDetailsViewModel::class.java)

        onStates(viewModel) {state ->

            when (state) {
                is ViewState.Loading -> renderLoading()
                is ViewState.Failed -> renderError()
                is ProductDetailsViewState -> renderData(state)
            }
        }

        viewModel.getDetail(intent.getIntExtra(KEY_PRODUCT_ID, 0))
    }

    private fun renderError() {
        TransitionManager.beginDelayedTransition(rootView)
        errorView.visibility = View.VISIBLE
        loadingView.visibility = View.GONE
        detailsView.visibility = View.GONE
    }

    private fun renderData(state: ProductDetailsViewState) {
        TransitionManager.beginDelayedTransition(rootView)
        errorView.visibility = View.GONE
        loadingView.visibility = View.GONE
        detailsView.visibility = View.VISIBLE

        isProductInshoppingCart = state.data.isInShoppingCart
        product = state.data.product
        price.text = "Price: $" + String.format(Locale.US, "%.2f", product?.price)
        description.text = product?.description
        toolbar.title = product?.name
        collapsingToolbar.title = product?.name

        if (isProductInshoppingCart) {
            fab.setImageResource(R.drawable.ic_in_shopping_cart)
        } else {
            fab.setImageResource(R.drawable.ic_add_shopping_cart)
        }

        backdrop.load(Constants.BASE_IMAGE_URL + product?.image)

    }

    private fun renderLoading() {
        TransitionManager.beginDelayedTransition(rootView)
        errorView.visibility = View.GONE
        loadingView.visibility = View.VISIBLE
        detailsView.visibility = View.GONE
    }

}