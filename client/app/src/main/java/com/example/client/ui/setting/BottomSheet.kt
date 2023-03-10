package com.example.client.ui.setting

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.client.R
import com.example.client.databinding.FragmentBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheet(private var defaultValue : Int) : BottomSheetDialogFragment() {
    private lateinit var viewBinding: FragmentBottomSheetBinding
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewBinding = FragmentBottomSheetBinding.inflate(layoutInflater)
        viewBinding.chooseBtn.text = getText(R.string.select_button)

        viewBinding.startdayPicker.minValue = 1
        viewBinding.startdayPicker.maxValue = 28

        viewBinding.startdayPicker.value = defaultValue
        // 날짜 순환 기능
        viewBinding.startdayPicker.wrapSelectorWheel = true

        viewBinding.closeBtn.setOnClickListener{
            viewBinding.startdayPicker.value = defaultValue
            dismiss()
        }
        viewBinding.chooseBtn.setOnClickListener {
            dismiss()
        }
        return viewBinding.root

    }

    fun getBudgetStartDate():Int{
        return try {
            viewBinding.startdayPicker.value
        }catch(e:IllegalStateException){
            defaultValue
        }catch (e: UninitializedPropertyAccessException){
            defaultValue
        }

    }


}