package com.example.healthify

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.healthify.viewmodel.MyViewModel


class FirstFragment : Fragment() {

    var gender: Int = 2
    var exerciseIntensity: Double = 1.0
    var age: Int = 0
    var weight: Double = 0.0
    var height: Double = 0.0
    lateinit var myViewModel: MyViewModel

    lateinit var weightInputText: EditText
    lateinit var heightInputText: EditText
    lateinit var radioGroup_activity: RadioGroup
    lateinit var radioGroup_gender: RadioGroup
    lateinit var button_analyze: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_first_fragment, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //初始化控件
        weightInputText = view.findViewById(R.id.weightInputText)
        heightInputText = view.findViewById(R.id.heightInputText)
        radioGroup_activity = view.findViewById(R.id.radioGroup_activity)
        radioGroup_gender = view.findViewById(R.id.radioGroup_gender)
        button_analyze = view.findViewById(R.id.button_analyze)
        // 从 SharedPreferences 加载上一次的数据
        loadData()

        //初始化ViewModel
        myViewModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)

        //初始化navController
        val navController = findNavController()


        //设置性别按钮组响应事件
        radioGroup_gender.setOnCheckedChangeListener { group, checkedId ->
            //checkedId为当前选中按钮的id
            gender = when (checkedId) {
                R.id.radioButton_famale -> 0
                R.id.radioButton_male -> 1
                else -> 2
            }
        }
        //设置运动按钮组响应事件
        radioGroup_activity.setOnCheckedChangeListener { _, checkedId ->
            exerciseIntensity = when (checkedId) {
                R.id.radioButton_noAct -> 1.2
                R.id.radioButton_fewAct -> 1.375
                R.id.radioButton_mediumAct -> 1.55
                R.id.radioButton_dailyAct -> 1.725
                else -> 1.0
            }
        }
        //设置numberpicker显示
        val numberPicker: NumberPicker = view.findViewById(R.id.numberPicker)
        // 设置最小值和最大值
        numberPicker.minValue = 0
        numberPicker.maxValue = 100
        // 设置当前值
        numberPicker.value = age
        numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            age = newVal
        }

        //结果分析按钮响应事件
        button_analyze.setOnClickListener() {
            //edittext输入转化为身高体重值
            weight = weightInputText.text.toString().toDouble()
            height = heightInputText.text.toString().toDouble()

            //检查数据输入是否合规
            if(age == 0) {
                Toast.makeText(activity, "Please Enter your AGE!", Toast.LENGTH_SHORT).show()
            } else if(weightInputText.text.toString().isEmpty()) {
                Toast.makeText(activity, "Please Enter your WEIGHT in kg!", Toast.LENGTH_SHORT).show()
            } else if(weight < 20 || weight > 250) {
                Toast.makeText(activity, "Please Enter valid WEIGHT in kg!", Toast.LENGTH_SHORT).show()
            } else if(heightInputText.text.toString().isEmpty()) {
                Toast.makeText(activity, "Please Enter your Height in cm!", Toast.LENGTH_SHORT).show()
            } else if(height < 60 || height > 220) {
                Toast.makeText(activity, "Please Enter valid Height in cm!", Toast.LENGTH_SHORT).show()
            }
            else {
                saveData()
                //将数据存入viewmodel
                myViewModel.age.value = age
                myViewModel.gender.value = gender
                myViewModel.height.value = height
                myViewModel.weight.value = weight
                myViewModel.exerciseIntensity.value = exerciseIntensity
                (activity as AppCompatActivity).supportActionBar?.title = "Analysis Results"
                navController.navigate(R.id.action_first_fragment_to_showResultFragment)
            }
        }

    }

    //以下两个函数用于加载上次填写的数据
    // 保存数据到 SharedPreferences
    private fun saveData() {
        val sharedPref = activity?.getSharedPreferences("MyDataPref", Context.MODE_PRIVATE) ?:return
        with(sharedPref.edit()) {
            putInt("gender", gender)
            putFloat("exerciseIntensity", exerciseIntensity.toFloat())
            putInt("age", age)
            putFloat("weight", weight.toFloat())
            putFloat("height", height.toFloat())
            apply()
        }
    }
    // 从 SharedPreferences 加载数据
    private fun loadData() {
        val sharedPref = activity?.getSharedPreferences("MyDataPref",Context.MODE_PRIVATE) ?:return
        gender = sharedPref.getInt("gender", 2)
        exerciseIntensity = sharedPref.getFloat("exerciseIntensity", 1.0f).toDouble()
        age = sharedPref.getInt("age", 0)
        weight = sharedPref.getFloat("weight", 0.0f).toDouble()
        height = sharedPref.getFloat("height", 0.0f).toDouble()
        // 根据加载的数据设置UI
        when (gender) {
            0 -> radioGroup_gender.check(R.id.radioButton_famale)
            1 -> radioGroup_gender.check(R.id.radioButton_male)
        }
        val epsilon = 0.001 // 设置一个小的误差范围
        when {
            (Math.abs(exerciseIntensity - 1.2) < epsilon) -> {
                exerciseIntensity = 1.2
                radioGroup_activity.check(R.id.radioButton_noAct)
            }
            (Math.abs(exerciseIntensity - 1.375) < epsilon) -> {
                exerciseIntensity = 1.375
                radioGroup_activity.check(R.id.radioButton_fewAct)
            }
            (Math.abs(exerciseIntensity - 1.55) < epsilon) -> {
                exerciseIntensity = 1.55
                radioGroup_activity.check(R.id.radioButton_mediumAct)
            }
            (Math.abs(exerciseIntensity - 1.725) < epsilon) -> {
                exerciseIntensity = 1.725
                radioGroup_activity.check(R.id.radioButton_dailyAct)
            }
        }
        weightInputText.setText(weight.toString())
        heightInputText.setText(height.toString())
    }


}