package com.example.client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.client.databinding.ActivityUpdateCategoryBinding

class UpdateCategoryActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityUpdateCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityUpdateCategoryBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.updateCategoryButton.text = getText(R.string.finish_button)
        viewBinding.updateCategoryType.check(R.id.updateCategory_type1)
        viewBinding.updateCategoryIcon.setImageResource(R.drawable.ic_category_art)
        viewBinding.updateCategoryIconName.text = "문화·예술"
        viewBinding.updateCategoryType.setOnCheckedChangeListener{ radioGroup, checkedId ->
            when(checkedId){
                R.id.updateCategory_type1 ->{
                    viewBinding.updateCategoryType.check(R.id.updateCategory_type1)
                }
                R.id.updateCategory_type2 -> {
                    viewBinding.updateCategoryType.check(R.id.updateCategory_type2)
                }
            }
        }
    }
}