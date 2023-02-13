package com.example.client.ui.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.client.R
import com.example.client.data.Category
import com.example.client.data.Detail
import com.example.client.databinding.FragmentHomeBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.navercorp.nid.NaverIdLoginSDK.applicationContext
//import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {
    private lateinit var viewBinding: FragmentHomeBinding
    private lateinit var listItem: Detail
    private lateinit var selectedCategory: Category

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentHomeBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var graph_stackedBar = view.findViewById<HorizontalBarChart>(R.id.graph_stackedBar)

        //graphBar
        var barChart = view.findViewById<BarChart>(R.id.graph_bar)

        //graphBar 값
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(1f, floatArrayOf(30f, 0f)))
        entries.add(BarEntry(2f, floatArrayOf(40f, 0f)))
        entries.add(BarEntry(3f, floatArrayOf(30f, 0f)))
        entries.add(BarEntry(4f, floatArrayOf(40f, 0f)))
        entries.add(BarEntry(5f, floatArrayOf(30f, 0f)))
        entries.add(BarEntry(6f, floatArrayOf(50f, 10f)))
        entries.add(BarEntry(7f, floatArrayOf(50f, 20f)))

        //average line
        val line = LimitLine(50f, "")
        line.lineWidth = 2f
        line.enableDashedLine(10f, 10f, 0f)
        line.labelPosition = LimitLabelPosition.LEFT_TOP
        line.textSize = 10f
        val leftAxis: YAxis = barChart.getAxisLeft()
        leftAxis.removeAllLimitLines()
        leftAxis.addLimitLine(line)

        //graphBar 데이터셋 초기화
        var set = BarDataSet(entries,"DataSet")
        // graphBar 색 설정
        set.colors = mutableListOf(
            ContextCompat.getColor(applicationContext, R.color.pink),
            ContextCompat.getColor(applicationContext, R.color.red),
        )

        barChart.run {
            description.isEnabled = false // 차트 옆에 별도로 표기되는 description을 안보이게 설정 (false)
            setMaxVisibleValueCount(7) // 최대 보이는 그래프 개수를 7개로 지정
            setPinchZoom(false) // 핀치줌(두손가락으로 줌인 줌 아웃하는것) 설정
            setDrawBarShadow(false) //그래프의 그림자
            setDrawGridBackground(false)//격자구조 넣을건지
            //Y축
            axisLeft.run {
                axisMaximum = 90f
                axisMinimum = 0f
                granularity = 90f
                setDrawLabels(true) // 값 적는거 허용
                setDrawGridLines(true) //격자 라인 활용
                setDrawAxisLine(false) // 축 그리기 설정
                axisLineColor = ContextCompat.getColor(
                    context,
                    R.color.gray
                ) // 축 색깔 설정
                gridColor = ContextCompat.getColor(
                    context,
                    R.color.gray
                ) // 축 아닌 격자 색깔 설정
                textColor = ContextCompat.getColor(
                    context,
                    R.color.gray
                ) // 라벨 텍스트 컬러 설정
                textSize = 13f //라벨 텍스트 크기
                setDrawLabels(false) // 값 셋팅 설정
                addLimitLine(line) //average line
            }
            //X축
            xAxis.run {
                position = XAxis.XAxisPosition.BOTTOM //X축을 아래에
                granularity = 1f // 1 단위만큼 간격 두기
                setDrawAxisLine(true) // 축 그림
                setDrawGridLines(false) // 격자
                textColor = ContextCompat.getColor(context,
                    R.color.gray
                ) //라벨 색상
                textSize = 12f // 텍스트 크기
                valueFormatter = MyXAxisFormatter() // X축 라벨값 바꿔주기 위해 설정
            }
            axisRight.isEnabled = false // 오른쪽 Y축을 안보이게 해줌.
            setTouchEnabled(false) // 그래프 터치해도 아무 변화없게 막음
            animateY(1000) // 밑에서부터 올라오는 애니매이션 적용
            legend.isEnabled = false //차트 범례 설정
        }

        val dataSet :ArrayList<IBarDataSet> = ArrayList()
        dataSet.add(set)
        val data = BarData(dataSet)
        data.barWidth = 0.5f //막대 너비 설정
        barChart.run {
            this.data = data //차트의 데이터를 data로 설정
            setFitBars(true)
            invalidate()
        }

        //graph_stackedBar
//        setupDistributionGraph()
        val entries2 = ArrayList<BarEntry>()
        entries2.add(BarEntry(0f, floatArrayOf(50f, 30f, 20f))) //graph_stackedBar 값

        val set2 = BarDataSet(entries, "")
        set2.colors = mutableListOf(
            ContextCompat.getColor(applicationContext!!,R.color.red),
            ContextCompat.getColor(applicationContext!!,R.color.orange),
            ContextCompat.getColor(applicationContext!!,R.color.yellow),
        ) //graph_stackedBar 차트 색

        val data2 = BarData(set2)
        val xAxis: XAxis = graph_stackedBar.getXAxis()
        data2.setDrawValues(false)
        data2.setBarWidth(3f);
        data2.isHighlightEnabled = false
        graph_stackedBar.data = data
        graph_stackedBar.axisLeft.setDrawGridLines(false)
        graph_stackedBar.xAxis.setDrawGridLines(false)
        graph_stackedBar.description.isEnabled = false
        graph_stackedBar.axisLeft.setDrawLabels(false)
        graph_stackedBar.axisRight.setDrawLabels(false)
        graph_stackedBar.xAxis.setDrawLabels(false)
        graph_stackedBar.legend.isEnabled = false
        graph_stackedBar.invalidate()
        graph_stackedBar.setPinchZoom(false)
        graph_stackedBar.axisRight.isEnabled = false // Y축 안보이게
    }

    //graphBar X축 라벨값
    inner class MyXAxisFormatter : ValueFormatter() {
        private val days = arrayOf("6월","7월","8월","9월","10월","11월","12월")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt()-1) ?: value.toString()
        }
    }

    //graph_stackedBar
//    private fun setupDistributionGraph() {
//        val entries = ArrayList<BarEntry>()
//        entries.add(BarEntry(0f, floatArrayOf(50f, 30f, 20f))) //graph_stackedBar 값
//
//        val set = BarDataSet(entries, "")
//        set.colors = mutableListOf(
//            ContextCompat.getColor(applicationContext!!,R.color.red),
//            ContextCompat.getColor(applicationContext!!,R.color.orange),
//            ContextCompat.getColor(applicationContext!!,R.color.yellow),
//        ) //graph_stackedBar 차트 색
//
//        val data = BarData(set)
//        val xAxis: XAxis = graph_stackedBar.getXAxis()
//        data.setDrawValues(false)
//        data.setBarWidth(3f);
//        data.isHighlightEnabled = false
//        graph_stackedBar.data = data
//        graph_stackedBar.axisLeft.setDrawGridLines(false)
//        graph_stackedBar.xAxis.setDrawGridLines(false)
//        graph_stackedBar.description.isEnabled = false
//        graph_stackedBar.axisLeft.setDrawLabels(false)
//        graph_stackedBar.axisRight.setDrawLabels(false)
//        graph_stackedBar.xAxis.setDrawLabels(false)
//        graph_stackedBar.legend.isEnabled = false
//        graph_stackedBar.invalidate()
//        graph_stackedBar.setPinchZoom(false)
//        graph_stackedBar.axisRight.isEnabled = false // Y축 안보이게
//    }
}