package com.tlnacl.reactiveapp.ui.detail

import com.tlnacl.reactiveapp.businesslogic.model.ProductDetail
import com.tlnacl.reactiveapp.uniflow.data.UIState

/**
 * Created by tlnacl on 11/07/17.
 */
data class ProductDetailsViewState(val data: ProductDetail) : UIState()

