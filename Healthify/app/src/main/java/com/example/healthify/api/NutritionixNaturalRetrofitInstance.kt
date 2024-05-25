package com.example.healthify.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object NutritionixNaturalRetrofitInstance {
    private const val BASE_URL = "https://trackapi.nutritionix.com/"

    val api: NutritionixNaturalApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(NutritionixNaturalApiService::class.java)
    }
}
