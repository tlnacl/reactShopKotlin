package com.tlnacl.reactiveapp.ui.detail

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.transition.TransitionManager
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tlnacl.reactiveapp.AndroidApplication
import com.tlnacl.reactiveapp.Constants
import com.tlnacl.reactiveapp.R
import com.tlnacl.reactiveapp.businesslogic.model.Product
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

    @BindView(R.id.errorView) lateinit var errorView: View
    @BindView(R.id.loadingView) lateinit var loadingView: View
    @BindView(R.id.detailsView) lateinit var detailsView: View
    @BindView(R.id.price) lateinit var price: TextView
    @BindView(R.id.description) lateinit var description: TextView
    @BindView(R.id.fab) lateinit var fab: FloatingActionButton
    @BindView(R.id.backdrop) lateinit var backdrop: ImageView
    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.root) lateinit var rootView: ViewGroup
    @BindView(R.id.collapsingToolbar) lateinit var collapsingToolbarLayout: CollapsingToolbarLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        ButterKnife.bind(this)
        (application as AndroidApplication).appComponent.inject(this)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Timber.d("onCreate")
        val viewModel = ViewModelProviders.of(this).get(ProductDetailsViewModel::class.java)
        viewModel.productDetailsLD.observe(this, androidx.lifecycle.Observer {
            render(it)
        })

        viewModel.doAction(intent.getIntExtra(KEY_PRODUCT_ID,0))
    }

    override fun render(productDetailsViewState: ProductDetailsViewState) {
        Timber.d("render $productDetailsViewState")

        when(productDetailsViewState){
            is ProductDetailsViewState.Loading -> renderLoading()
            is ProductDetailsViewState.Data -> renderData(productDetailsViewState)
            is ProductDetailsViewState.Error -> renderError()
        }
    }

    private fun renderError() {
        TransitionManager.beginDelayedTransition(rootView)
        errorView.visibility = View.VISIBLE
        loadingView.visibility = View.GONE
        detailsView.visibility = View.GONE
    }

    private fun renderData(state: ProductDetailsViewState.Data) {
        TransitionManager.beginDelayedTransition(rootView)
        errorView.visibility = View.GONE
        loadingView.visibility = View.GONE
        detailsView.visibility = View.VISIBLE

        isProductInshoppingCart = state.data.isInShoppingCart
        product = state.data.product
        price.text = "Price: $" + String.format(Locale.US, "%.2f", product?.price)
        description.text = product?.getDescription()
        toolbar.title = product?.getName()
        collapsingToolbarLayout.title = product?.name

        if (isProductInshoppingCart) {
            fab.setImageResource(R.drawable.ic_in_shopping_cart)
        } else {
            fab.setImageResource(R.drawable.ic_add_shopping_cart)
        }

        Glide.with(this)
                .load(Constants.BASE_IMAGE_URL + product?.image)
                .apply(RequestOptions.centerCropTransform())
                .into(backdrop)
    }

    private fun renderLoading() {
        TransitionManager.beginDelayedTransition(rootView)
        errorView.visibility = View.GONE
        loadingView.visibility = View.VISIBLE
        detailsView.visibility = View.GONE
    }

}