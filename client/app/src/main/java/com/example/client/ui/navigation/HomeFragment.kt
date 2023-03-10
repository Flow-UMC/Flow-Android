package com.example.client.ui.navigation

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.client.APIObject
import com.example.client.R
import com.example.client.api.AnalysisResponseByList
import com.example.client.api.AnalysisResponseData
import com.example.client.api.api
import com.example.client.data.model.AppDatabase
import com.example.client.data.Category
import com.example.client.data.Detail
import com.example.client.databinding.FragmentHomeBinding
import com.example.client.ui.setting.SettingBudgetSettingActivity
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
import kotlinx.coroutines.InternalCoroutinesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

//import kotlinx.android.synthetic.main.fragment_home.*

@InternalCoroutinesApi @RequiresApi(Build.VERSION_CODES.O)
class HomeFragment : Fragment() {
    private lateinit var viewBinding: FragmentHomeBinding
    private lateinit var listItem: Detail
    private lateinit var selectedCategory: Category
    private val request: api = APIObject.getInstance().create(api::class.java)
    private lateinit var bottomNavigationActivity : BottomNavigationActivity
    private lateinit var roomDb : AppDatabase

    override fun onAttach(context: Context) {
        super.onAttach(context)
        bottomNavigationActivity = context as BottomNavigationActivity
        roomDb = AppDatabase.getInstance(bottomNavigationActivity)!!
    }

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
        var month  = LocalDate.now().monthValue
        val userId = roomDb.UserDao().getUserId()
        getAnalysis(userId,month)
        viewBinding.modifyText.setOnClickListener {
            val intent = Intent(bottomNavigationActivity,SettingBudgetSettingActivity::class.java)
            intent.putExtra("pageId",0)
            startActivity(intent)
        }
        viewBinding.modifyArrow.setOnClickListener {
            val intent = Intent(bottomNavigationActivity,SettingBudgetSettingActivity::class.java)
            intent.putExtra("pageId",0)
            startActivity(intent)
        }
        viewBinding.leftArrow.setOnClickListener {
            if(month-1>0){
                month -=1
                getAnalysis(userId,month)
            } else{
                Toast.makeText(bottomNavigationActivity,"?????? ????????? ??????????????? ????????? ??? ????????????",Toast.LENGTH_SHORT).show()
            }
        }
        viewBinding.rightArrow.setOnClickListener {
            if(month+1<=LocalDate.now().monthValue){
                month +=1
                getAnalysis(userId,month)
            } else{
                Toast.makeText(bottomNavigationActivity,"?????????????????? ????????? ??? ????????????",Toast.LENGTH_SHORT).show()
            }
        }

        var graphStackedBar = viewBinding.graphStackedBar
        var barChart = viewBinding.graphBar
        //graphBar ???
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(1f, floatArrayOf(30f, 0f))) // ?????? pink ?????? red
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

        //graphBar ???????????? ?????????
        var set = BarDataSet(entries,"DataSet")
        // graphBar ??? ??????
        set.colors = mutableListOf(
            ContextCompat.getColor(applicationContext, R.color.pink),
            ContextCompat.getColor(applicationContext, R.color.red),
        )

        barChart.run {
            description.isEnabled = false // ?????? ?????? ????????? ???????????? description??? ???????????? ?????? (false)
            setMaxVisibleValueCount(7) // ?????? ????????? ????????? ????????? 7?????? ??????
            setPinchZoom(false) // ?????????(?????????????????? ?????? ??? ???????????????) ??????
            setDrawBarShadow(false) //???????????? ?????????
            setDrawGridBackground(false)//???????????? ????????????
            //Y???
            axisLeft.run {
                axisMaximum = 90f
                axisMinimum = 0f
                granularity = 90f
                setDrawAxisLine(false)// ??? ????????? ??????
                textColor= ContextCompat.getColor(
                    context,
                    R.color.gray
                )// ?????? ????????? ?????? ??????
                setDrawLabels(false)// ??? ?????? ??????
                addLimitLine(line)//average line
            }
            //X???
            xAxis.run {
                position = XAxis.XAxisPosition.BOTTOM //X?????? ?????????
                granularity = 1f // 1 ???????????? ?????? ??????
                setDrawAxisLine(true) // ??? ??????
                setDrawGridLines(false) // ??????
                textColor = ContextCompat.getColor(context,
                    R.color.gray
                ) //?????? ??????
                textSize = 12f // ????????? ??????
                valueFormatter = MyXAxisFormatter(month) // X??? ????????? ???????????? ?????? ??????
            }
            axisRight.isEnabled = false // ????????? Y?????? ???????????? ??????.
            setTouchEnabled(false) // ????????? ???????????? ?????? ???????????? ??????
            animateY(1000) // ??????????????? ???????????? ??????????????? ??????
            legend.isEnabled = false //?????? ?????? ??????
        }

        val dataSet :ArrayList<IBarDataSet> = ArrayList()
        dataSet.add(set)
        val data = BarData(dataSet)
        data.barWidth = 0.5f //?????? ?????? ??????
        barChart.run {
            this.data = data //????????? ???????????? data??? ??????
            setFitBars(true)
            invalidate()
        }

        //graphStackedBar ?????? ?????????
        val entries2 = ArrayList<BarEntry>()
        entries2.add(BarEntry(0f, floatArrayOf(50f, 30f, 10f,10f))) //graph_stackedBar ???

        val set2 = BarDataSet(entries2, "")
        set2.colors = mutableListOf(
            ContextCompat.getColor(applicationContext,R.color.red),
            ContextCompat.getColor(applicationContext,R.color.orange),
            ContextCompat.getColor(applicationContext,R.color.yellow),
            ContextCompat.getColor(applicationContext,R.color.lightgray5),
        ) //graph_stackedBar ?????? ???

        val data2 = BarData(set2)
        data2.setDrawValues(false)
        data2.barWidth = 5f
        data2.isHighlightEnabled = false
        graphStackedBar.data = data2
        graphStackedBar.axisLeft.setDrawGridLines(false)
        graphStackedBar.xAxis.setDrawGridLines(false)
        graphStackedBar.description.isEnabled = false
        graphStackedBar.axisLeft.setDrawLabels(false)
        graphStackedBar.axisRight.setDrawLabels(false)
        graphStackedBar.xAxis.setDrawLabels(false)
        graphStackedBar.legend.isEnabled = false
        graphStackedBar.invalidate()
        graphStackedBar.setPinchZoom(false)
        graphStackedBar.axisLeft.isEnabled= false
        graphStackedBar.axisLeft.axisMaximum= 100f
        graphStackedBar.axisLeft.axisMinimum= 0f
        graphStackedBar.xAxis.setDrawAxisLine(false)
    }

    //graphBar X??? ?????????
    inner class MyXAxisFormatter(private val month : Int) : ValueFormatter() {
        private var days = ArrayList<String>()
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            if (month<8){
                days = arrayListOf("1???","2???","3???","4???","5???","6???","7???")
            }else{
                days = arrayListOf("6???","7???","8???","9???","10???","11???","12???")
            }
            return days.getOrNull(value.toInt()-1) ?: value.toString()
        }
    }
    private fun getAnalysis(userId:Int, month : Int,) {
        val call = request.getAnalysis(userId, month)
        viewBinding.month.text = "${month}???"
        viewBinding.progressBar.visibility = View.VISIBLE
        viewBinding.homeContent.visibility = View.GONE
        call.enqueue(object: Callback<AnalysisResponseByList> {
            override fun onResponse(call: Call<AnalysisResponseByList>, response: Response<AnalysisResponseByList>)  {
                if (response.body()!!.isSuccess){
                    val response : AnalysisResponseData= response.body()!!.result
                    viewBinding.month.text = "${month}???"
                    viewBinding.budgetBtn.text = "?????? ${response.budget}???"
                    viewBinding.spendBudget.text = "${response.consumption}"
                    viewBinding.budgetPercent.text = "${response.percent}%"
                    if(month == LocalDate.now().monthValue)
                        viewBinding.date.text = "${month}.1 ~ ${month}.${LocalDate.now().dayOfMonth}"
                    else{
                        val date = LocalDate.of(LocalDate.now().year,month,1)
                        viewBinding.date.text = "${month}.1 ~ ${month}.${date.withDayOfMonth(date.lengthOfMonth()).dayOfMonth}"
                    }
                    if(response.consumption > response.lastConsumption){
                        viewBinding.compareWithPreviousBudget.text = "${response.consumption-response.lastConsumption}"
                        viewBinding.compareWithPreviousBudgetText.text = "??? ??? ?????? ????????????"
                    }
                    else{
                        viewBinding.compareWithPreviousBudget.text = "${response.lastConsumption-response.consumption}"
                        viewBinding.compareWithPreviousBudgetText.text = "??? ?????? ?????? ????????????"
                    }
                }
                else{
                    Toast.makeText(bottomNavigationActivity, "??????????????? ???????????? ???????????????\n    ????????? ?????? ??????????????????", Toast.LENGTH_SHORT).show()
                }
                println(response.body()?.message)
                viewBinding.homeContent.visibility = View.VISIBLE
                viewBinding.progressBar.visibility = View.GONE
            }
            override fun onFailure(call: Call<AnalysisResponseByList>, t: Throwable) {
                viewBinding.homeContent.visibility = View.VISIBLE
                viewBinding.progressBar.visibility = View.GONE
                Toast.makeText(bottomNavigationActivity, "??????????????? ???????????? ???????????????\n    ????????? ?????? ??????????????????", Toast.LENGTH_SHORT).show()
            }
        })
    }
}