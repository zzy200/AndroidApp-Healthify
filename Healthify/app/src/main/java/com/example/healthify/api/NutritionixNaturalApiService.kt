package com.example.healthify.api

import com.example.healthify.api.NutrientResponse
import com.example.healthify.api.QueryBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NutritionixNaturalApiService {
    @Headers(
        "Content-Type: application/json",
        "x-app-id: dbfdb079",
        "x-app-key: f27d739c3192c74fb6df09ed4107cc02"
    )
    @POST("v2/natural/nutrients")
    suspend fun getNutrientDetails(@Body query: QueryBody): Response<NutrientResponse>
}

