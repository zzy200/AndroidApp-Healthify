package com.example.healthify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.healthify.records.RecordData
import com.example.healthify.records.RecordDatabase
import com.example.healthify.viewmodel.MyViewModel
import kotlinx.coroutines.launch


class RecordConfirmFragment : Fragment() {

    lateinit var currentWeightET: EditText
    lateinit var calorieConsumed: EditText
    lateinit var confirmBtn: Button
    lateinit var myViewModel: MyViewModel
    lateinit var db: RecordDatabase

    var weight = 0.0
    var calorie = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_record_confirm, container, false)
        currentWeightET = view.findViewById(R.id.currentWeightET)
        calorieConsumed = view.findViewById(R.id.CalorieConsumedET)
        confirmBtn = view.findViewById(R.id.confirmBtn)
        myViewModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weight = myViewModel.weight.value!!
        calorie = myViewModel.TodayCalorie.value!!
        currentWeightET.setText(weight.toString())
        calorieConsumed.setText(calorie.toString())

        //初始化数据库
        db = RecordDatabase.getDatabase(requireContext())

        confirmBtn.setOnClickListener {
            weight = currentWeightET.text.toString().toDouble()
            calorie = calorieConsumed.text.toString().toDouble()

            //向数据库中插入数据
            lifecycleScope.launch {
                db.recordDao().insert(RecordData(calorie = calorie, weight = weight))

                findNavController().navigate(R.id.action_recordConfirmFragment_to_recordFragment)
            }
        }
    }
}