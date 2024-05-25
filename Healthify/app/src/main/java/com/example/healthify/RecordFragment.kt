package com.example.healthify

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthify.records.RecordAdapter
import com.example.healthify.records.RecordDatabase
import kotlinx.coroutines.launch
import lecho.lib.hellocharts.listener.ComboLineColumnChartOnValueSelectListener
import lecho.lib.hellocharts.model.Axis
import lecho.lib.hellocharts.model.Column
import lecho.lib.hellocharts.model.ColumnChartData
import lecho.lib.hellocharts.model.ComboLineColumnChartData
import lecho.lib.hellocharts.model.Line
import lecho.lib.hellocharts.model.LineChartData
import lecho.lib.hellocharts.model.PointValue
import lecho.lib.hellocharts.model.SubcolumnValue
import lecho.lib.hellocharts.util.ChartUtils
import lecho.lib.hellocharts.view.ComboLineColumnChartView


class RecordFragment : Fragment() {
    private lateinit var db: RecordDatabase
    private lateinit var recyclerView: RecyclerView

    private lateinit var chart: ComboLineColumnChartView
    private lateinit var data: ComboLineColumnChartData

    //
    private val numberOfLines = 1
    private val maxNumberOfLines = 4
    private var numberOfPoints = 12
    private var numColumns = 12
    private val randomNumbersTab = Array(maxNumberOfLines) { FloatArray(numberOfPoints) }

    private var hasAxes = true
    private var hasAxesNames = true
    private var hasPoints = true
    private var hasLines = true
    private var isCubic = false
    private var hasLabels = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_record, container, false)

        // Initialize the database
        db = RecordDatabase.getDatabase(requireContext())

        // Set up the RecyclerView
        recyclerView = view.findViewById(R.id.recordRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        //
        chart = view.findViewById(R.id.chart)!!
        chart.onValueTouchListener = ValueTouchListener()

        lifecycleScope.launch {
            val records = db.recordDao().getAllRecords()
            records.forEach { record ->
                Log.d("Data", "Record: $record")
            }
            recyclerView.adapter = RecordAdapter(records)
            numberOfPoints = RecordAdapter(records).getItemCount()
            numColumns = RecordAdapter(records).itemCount
            //
            val heights = records.map { it.calorie }
            val points = records.map { it.weight }
            generateValues()
            generateData(heights,points)

            chart.setComboLineColumnChartData(data)
        }
        return view
    }

    //
    private inner class ValueTouchListener : ComboLineColumnChartOnValueSelectListener {
        override fun onColumnValueSelected(columnIndex: Int, subcolumnIndex: Int, value: SubcolumnValue) {
            Toast.makeText(activity, "Calorie Consumed: ${value.value} kCal", Toast.LENGTH_SHORT).show()
        }

        override fun onPointValueSelected(lineIndex: Int, pointIndex: Int, value: PointValue) {
            Toast.makeText(activity, "Weight: ${value.y/50} Kg", Toast.LENGTH_SHORT).show()
        }

        override fun onValueDeselected() {
            // Reset any deselected value if needed
        }
    }

    private fun generateValues() {
        for (i in 0 until maxNumberOfLines) {
            for (j in 0 until numberOfPoints) {
                randomNumbersTab[i][j] = (Math.random() * 50f + 5).toFloat()
            }
        }
    }

    private fun generateData(heights: List<Double>, points: List<Double>) {
        // Chart looks the best when line data and column data have similar maximum viewports.
        data = ComboLineColumnChartData(generateColumnData(heights), generateLineData(points))

        if (hasAxes) {
            val axisX = Axis()
            val axisY = Axis().setHasLines(true)
            if (hasAxesNames) {
                axisX.name = "Date"
                axisY.name = "Progress"
            }
            data!!.axisXBottom = axisX
            data!!.axisYLeft = axisY
        } else {
            data!!.axisXBottom = null
            data!!.axisYLeft = null
        }
    }

    private fun generateLineData(points: List<Double>): LineChartData {
        val lines = mutableListOf<Line>()
        for (i in 0 until numberOfLines) {
            val values = mutableListOf<PointValue>()
            for (j in 0 until numberOfPoints) {
                values.add(PointValue(j.toFloat(), points[j].toFloat()*50))
            }
            val line = Line(values)
            line.color = ChartUtils.COLORS[i]
            line.isCubic = isCubic
            line.setHasLabels(hasLabels)
            line.setHasLines(hasLines)
            line.setHasPoints( hasPoints)
            lines.add(line)
        }
        return LineChartData(lines)
    }

    private fun generateColumnData(heights: List<Double>): ColumnChartData {
        val numSubcolumns = 1
        val columns = mutableListOf<Column>()
        for (i in 0 until numColumns) {
            val values = mutableListOf<SubcolumnValue>()
            for (j in 0 until numSubcolumns) {
                values.add(SubcolumnValue(heights[i].toFloat(), ChartUtils.COLOR_GREEN))
            }
            columns.add(Column(values))
        }
        return ColumnChartData(columns)
    }
}