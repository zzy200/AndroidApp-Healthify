package com.example.healthify.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel: ViewModel() {
    val gender = MutableLiveData<Int>(0)
    val exerciseIntensity = MutableLiveData<Double>(1.0)
    val age = MutableLiveData<Int>(0)
    val weight = MutableLiveData<Double>(0.0)
    val height = MutableLiveData<Double>(0.0)
    val TargetCalorie = MutableLiveData<Double>(0.0)
    val TodayCalorie = MutableLiveData<Double>(0.0)
}