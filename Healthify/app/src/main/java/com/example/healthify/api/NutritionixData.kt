package com.example.healthify.api

import com.google.gson.annotations.SerializedName

data class QueryBody(
    @SerializedName("query") val query: String
)

data class NutrientResponse(
    @SerializedName("foods") val foods: List<Food>
)

data class Food(
    @SerializedName("food_name") val foodName: String,
    @SerializedName("brand_name") val brandName: String?,
    @SerializedName("serving_qty") val servingQuantity: Double,
    @SerializedName("serving_unit") val servingUnit: String,
    @SerializedName("serving_weight_grams") val servingWeightGrams: Double,
    @SerializedName("nf_calories") val calories: Double,
    @SerializedName("nf_total_fat") val totalFat: Double,
    @SerializedName("nf_saturated_fat") val saturatedFat: Double,
    @SerializedName("nf_cholesterol") val cholesterol: Double,
    @SerializedName("nf_sodium") val sodium: Double,
    @SerializedName("nf_total_carbohydrate") val totalCarbohydrate: Double,
    @SerializedName("nf_dietary_fiber") val dietaryFiber: Double,
    @SerializedName("nf_sugars") val sugars: Double,
    @SerializedName("nf_protein") val protein: Double,
    @SerializedName("nf_potassium") val potassium: Double,
    @SerializedName("nf_p") val phosphorus: Double,
    @SerializedName("full_nutrients") val fullNutrients: List<FullNutrient>
)

data class FullNutrient(
    @SerializedName("attr_id") val attributeId: Int,
    @SerializedName("value") val value: Double
)
