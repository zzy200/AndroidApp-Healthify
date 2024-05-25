package com.example.healthify


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.healthify.api.NutritionixNaturalRetrofitInstance
import com.example.healthify.api.QueryBody
import com.example.healthify.viewmodel.MyViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue
import lecho.lib.hellocharts.view.PieChartView


class FoodCaloriesFragment: Fragment(), View.OnClickListener {

    private lateinit var foodNameEditText: AutoCompleteTextView
    private lateinit var calculateButton: Button
    private lateinit var caloriesTextView: TextView
    private lateinit var fatTextView: TextView
    private lateinit var carbTextView: TextView
    private lateinit var proteinTextView: TextView
    private lateinit var loseWeightBtn: Button
    private lateinit var stayFitBtn: Button
    private lateinit var gainWeightBtn: Button
    private lateinit var targetBar: ProgressBar
    private lateinit var currentBar: ProgressBar
    private lateinit var addBtn: ImageButton
    private lateinit var targetCalorieTV: TextView
    private lateinit var currentCalorieTV: TextView
    private lateinit var saveBtn: Button

    private var foodName: String = ""
    private var targetCal: Double = 0.0
    private var currentCal: Double = 0.0
    private var Cal: Double = 0.0
    private var Fat: Double = 0.0
    private var Carb: Double = 0.0
    private var Prot: Double = 0.0
    private lateinit var pieChartView:PieChartView
    lateinit var myViewModel: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_food_calories, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //初始化控件
        foodNameEditText = view.findViewById(R.id.foodNameText)
        calculateButton = view.findViewById(R.id.calculateBtn)
        caloriesTextView = view.findViewById(R.id.textView16)
        fatTextView = view.findViewById(R.id.textView17)
        carbTextView = view.findViewById(R.id.textView18)
        proteinTextView = view.findViewById(R.id.textView19)
        pieChartView = view.findViewById(R.id.ingredients_piechart)
        loseWeightBtn = view.findViewById(R.id.loseWeightBtn)
        stayFitBtn = view.findViewById(R.id.stayFitBtn)
        gainWeightBtn = view.findViewById(R.id.gainWeightBtn)
        myViewModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
        targetBar = view.findViewById(R.id.TargetCalorieBar)
        currentBar = view.findViewById(R.id.CurrentCalorieBar)
        addBtn = view.findViewById(R.id.imageButton)
        targetCalorieTV = view.findViewById(R.id.textView3)
        currentCalorieTV = view.findViewById(R.id.textView4)
        saveBtn = view.findViewById(R.id.saveBtn)


        //从viewmodel读出targetCalorie
        targetCal = myViewModel.TargetCalorie.value!!

        //初始设置进度bar
        targetBar.progress = ((targetCal/(targetCal + 300))*100).toInt()


        // 创建下拉列表数据
        foodNameEditText.threshold = 0
        val suggestions = arrayOf("Apple", "Banana", "Pork", "Chicken", "Rice", "Cola", "Milk")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, suggestions)
        foodNameEditText.setAdapter(adapter)

        //设置查询按钮响应事件
        calculateButton.setOnClickListener {
            foodName = foodNameEditText.text.toString()
            fetchNutritionData(foodName)

        }
        //设置添加食物按钮响应事件
        addBtn.setOnClickListener {
            currentCal += Cal;
            currentBar.progress = ((currentCal/targetCal)*100).toInt()
            Toast.makeText(requireContext(), "100 grams of $foodName has been added to your diet record", Toast.LENGTH_SHORT).show()
            targetCalorieTV.text = String.format("Target: %.1f",targetCal)
            currentCalorieTV.text = String.format("Current: %.1f",currentCal)
        }

        //设置目标按钮响应事件
        loseWeightBtn.setOnClickListener(this)
        stayFitBtn.setOnClickListener(this)
        gainWeightBtn.setOnClickListener(this)


        //设置记录数据按钮响应
        val navController = findNavController()
        saveBtn.setOnClickListener {
            //将今日摄入卡路里存入viewmodel
            myViewModel.TodayCalorie.value = currentCal
            navController.navigate(R.id.action_foodCaloriesFragment_to_recordConfirmFragment)
        }
    }

    private fun fetchNutritionData(foodName: String) {
        val queryBody = QueryBody(query = foodName)
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    NutritionixNaturalRetrofitInstance.api.getNutrientDetails(queryBody)
                }

                if (response.isSuccessful && response.body() != null) {
                    val nutritionData = response.body()!!.foods[0]

                    Cal = (nutritionData.calories)*100 / (nutritionData.servingWeightGrams)
                    Fat = (nutritionData.totalFat)*100 / (nutritionData.servingWeightGrams)
                    Carb = (nutritionData.totalCarbohydrate)*100 / (nutritionData.servingWeightGrams)
                    Prot = (nutritionData.protein)*100 / (nutritionData.servingWeightGrams)

                    caloriesTextView.text = String.format("Calorie: %.1f/100g",(nutritionData.calories)*100 / (nutritionData.servingWeightGrams))
                    fatTextView.text = String.format("Fat: %.1fg / 100g",(nutritionData.totalFat)*100 / (nutritionData.servingWeightGrams))
                    carbTextView.text = String.format("Carbs: %.1fg / 100g",(nutritionData.totalCarbohydrate)*100 / (nutritionData.servingWeightGrams))
                    proteinTextView.text = String.format("Protein: %.1fg / 100g",(nutritionData.protein)*100 / (nutritionData.servingWeightGrams))

                    val pieData: MutableList<SliceValue> = ArrayList()
                    pieData.add(SliceValue((Fat).toFloat(), ContextCompat.getColor(requireContext(),R.color.piechart_background1)).setLabel("脂肪"))
                    pieData.add(SliceValue((Carb).toFloat(), ContextCompat.getColor(requireContext(),R.color.piechart_background3)).setLabel("碳水化合物"))
                    pieData.add(SliceValue((Prot).toFloat(), ContextCompat.getColor(requireContext(),R.color.piechart_background2)).setLabel("蛋白质"))
                    pieData.add(SliceValue((100-Fat-Carb-Prot).toFloat(), ContextCompat.getColor(requireContext(),R.color.piechart_background4)).setLabel("其它"))
                    val pieChartData = PieChartData(pieData)
                    pieChartData.setHasLabels(true).valueLabelTextSize = 14
                    pieChartData
                        .setHasCenterCircle(false)
                    pieChartView!!.setPieChartData(pieChartData)
                } else {
                    caloriesTextView.text = "Data not found"
                }
            } catch (e: Exception) {
                caloriesTextView.text = "Error: ${e.message}"
            }
        }
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.loseWeightBtn -> {
                targetBar.progress = (((targetCal - 300)/(targetCal + 300))*100).toInt()
                updateButtonColors(R.id.loseWeightBtn)
            }
            R.id.stayFitBtn -> {
                targetBar.progress = ((targetCal/(targetCal + 300))*100).toInt()
                updateButtonColors(R.id.stayFitBtn)
            }
            R.id.gainWeightBtn -> {
                targetBar.progress = 100
                updateButtonColors(R.id.gainWeightBtn)
            }
        }
    }

    private fun updateButtonColors(selectedButtonId: Int) {
        val buttons = arrayOf(loseWeightBtn,stayFitBtn,gainWeightBtn)
        buttons.forEach { button ->
            if (button?.id == selectedButtonId) {
                button.setTextColor(Color.parseColor("#FFCC00"))
            } else {
                button?.setTextColor(Color.WHITE)
            }
        }
    }


}