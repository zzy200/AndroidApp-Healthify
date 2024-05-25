package com.example.healthify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.healthify.viewmodel.MyViewModel


class ShowResultFragment : Fragment() {

    var gender: Int = 0
    var exerciseIntensity: Double = 1.0
    var age: Int = 0
    var weight: Double = 0.0
    var height: Double = 0.0
    var bmi: Double = 0.0
    var water: Double = 0.0
    var calorieInp: Double = 0.0
    var bmr: Double = 0.0

    lateinit var myViewModel: MyViewModel
    lateinit var text_height: TextView
    lateinit var text_age: TextView
    lateinit var text_weight: TextView
    lateinit var text_bmi1: TextView
    lateinit var text_bmi2: TextView
    lateinit var seekBar: SeekBar
    lateinit var text_water: TextView
    lateinit var text_calories: TextView
    lateinit var text_bmr: TextView
    lateinit var imageButton1: ImageView
    lateinit var imageButton2: ImageView
    lateinit var imageButton3: ImageView
    lateinit var button_waterReminder: Button
    lateinit var button_foodCalories: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_show_result, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //初始化控件
        text_height = view.findViewById(R.id.heightTV)
        text_age = view.findViewById(R.id.ageTV)
        text_weight = view.findViewById(R.id.weightTV)
        myViewModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
        text_bmi1 = view.findViewById(R.id.bmiTV1)
        text_bmi2 = view.findViewById(R.id.bmiTV2)
        seekBar = view.findViewById(R.id.seekBar)
        text_water = view.findViewById(R.id.waterTV)
        text_calories = view.findViewById(R.id.caloriesTV)
        text_bmr = view.findViewById(R.id.bmrTV)
        imageButton1 = view.findViewById(R.id.imageView1)
        imageButton2 = view.findViewById(R.id.imageView2)
        imageButton3 = view.findViewById(R.id.imageView3)
        button_waterReminder = view.findViewById(R.id.waterBtn)
        button_foodCalories = view.findViewById(R.id.caloriesBtn)

        //获取viewmodel数据
        age = myViewModel.age.value!!
        gender = myViewModel.gender.value!!
        exerciseIntensity = myViewModel.exerciseIntensity.value!!
        weight = myViewModel.weight.value!!
        height = myViewModel.height.value!!

        //计算bmi
        weight = myViewModel.weight.value!!
        height = myViewModel.height.value!!
        bmi = weight * 10000 / (height * height)

        //计算需水量
        if (age <= 30) {
            water = (weight * 42 * 2.95) / (28.3 * 100)
        } else if (age > 30 && age!!.toInt() <= 35) {
            water = (weight * 37 * 2.95) / (28.3 * 100)
        } else if (age > 35) {
            water = (weight * 32 * 2.95) / (28.3 * 100)
        }

        //计算bmr
        if(gender == 1) {
            bmr = 9.99 * weight + 6.25 * height - 4.92*age + 5
        } else {
            bmr = 9.99 * weight + 6.25 * height - 4.92*age - 161
        }
        //计算总卡路里需求
        calorieInp = bmr * exerciseIntensity

        imageButton1.setOnClickListener {
            Toast.makeText(activity, "Body Mass Index. \nBMI = Weight/(Height^2)", Toast.LENGTH_LONG).show()
        }
        imageButton2.setOnClickListener {
            Toast.makeText(activity, "Water You Need Daily.", Toast.LENGTH_LONG).show()
        }
        imageButton3.setOnClickListener {
            Toast.makeText(activity, "BMR: Basal Metabolic Rate\n Total Cal = BMR*ActFactor", Toast.LENGTH_LONG).show()
        }

        setDataDisplay()

        //将targetCalorie存入viewmodel
        myViewModel.TargetCalorie.value = calorieInp

        val navController = findNavController()
        button_waterReminder.setOnClickListener {
            navController.navigate(R.id.action_showResultFragment_to_waterReminderFragment)
        }
        button_foodCalories.setOnClickListener {
            navController.navigate(R.id.action_showResultFragment_to_foodCaloriesFragment)
        }
    }
    private fun setDataDisplay() {
        //personal information display
        myViewModel.height.observe(viewLifecycleOwner, Observer { height ->
            text_height.text = "Height: $height"
        })
        myViewModel.age.observe(viewLifecycleOwner) { age ->
            text_age.text = "Age: $age"
        }
        myViewModel.weight.observe(viewLifecycleOwner, Observer { weight ->
            text_weight.text = "Weight: $weight"
        })

        //bmi display
        text_bmi1.text = "Your BMI: ${Math.round(bmi * 10) / 10}"
        text_bmi2.text = when {
            bmi >= 30 -> "Obesity"
            bmi >= 25 && bmi < 30 -> "Overweight"
            bmi <= 18 -> "Under Weight"
            bmi > 18 && bmi < 25 -> "Normal"
            else -> ""
        }
        seekBar.progress = Math.round(bmi).toInt()

        //water display
        text_water.text = String.format("You need: %.1f L/day", water)

        //bmr display
        text_bmr.text = String.format("BMR:%.2f /Cal",bmr)

        //total calories display
        text_calories.text = String.format("Totoal Daily Needs:%.2f /Cal",calorieInp)
    }


}