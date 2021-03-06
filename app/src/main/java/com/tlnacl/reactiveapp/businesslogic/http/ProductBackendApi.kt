/*
 * Copyright 2016 Hannes Dorfmann.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.tlnacl.reactiveapp.businesslogic.http

import com.tlnacl.reactiveapp.businesslogic.model.Product
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * The Retrofit interface to retrieve data from the backend over http
 * https://raw.githubusercontent.com/tlnacl/reactiveApp/shop/app/server/api/products3.json
 * @author Hannes Dorfmann
 */
interface ProductBackendApi {
    @GET("/tlnacl/ReactShopKotlin/master" + "/app/server/api/products{pagination}.json")
    suspend fun getProducts(@Path("pagination") pagination: Int): List<Product>
}
