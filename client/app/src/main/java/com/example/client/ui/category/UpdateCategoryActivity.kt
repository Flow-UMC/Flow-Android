package com.example.client.ui.category

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.example.client.R
import com.example.client.api.CategoryRequestData
import com.example.client.api.HttpConnection
import com.example.client.data.model.AppDatabase
import com.example.client.data.Category
import com.example.client.databinding.ActivityUpdateCategoryBinding
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class UpdateCategoryActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityUpdateCategoryBinding
    private lateinit var category: Category
    private val httpConnection : HttpConnection = HttpConnection()

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityUpdateCategoryBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val categoryIntent = intent
        val categoryId : Int = categoryIntent.getIntExtra("categoryId",1)

        val roomDb = AppDatabase.getInstance(this)
        category = roomDb!!.CategoryDao().selectById(categoryId)
        var typeId = category.typeId

        viewBinding.updateCategoryButton.text = getText(R.string.finish_button)
        viewBinding.updateCategoryIcon.setImageResource(category.image)
        viewBinding.updateCategoryIconName.text = category.name
        viewBinding.updateCategoryName.setText(category.name)

        when(typeId){
            1 -> viewBinding.updateCategoryType.check(R.id.updateCategory_type1)
            else -> viewBinding.updateCategoryType.check(R.id.updateCategory_type2)
        }
        // 지출 수입 radio button
        if(category.isUserCreated){
            viewBinding.updateCategoryType.setOnCheckedChangeListener{ radioGroup, checkedId ->
                when(checkedId){
                    R.id.updateCategory_type1 ->{
                        viewBinding.updateCategoryType.check(R.id.updateCategory_type1)
                        typeId = 1
                    }
                    R.id.updateCategory_type2 -> {
                        viewBinding.updateCategoryType.check(R.id.updateCategory_type2)
                        typeId = 2
                    }
                }
            }
        } else{
            // 기본 카테고리인 경우 이름만 변경 가능. type은 변경할 수 없음.
            viewBinding.updateCategoryType1.isEnabled = false
            viewBinding.updateCategoryType2.isEnabled = false

        }
        // 닫기 버튼
        viewBinding.updateCategoryBackButton.setOnClickListener(){
            super.onBackPressed()
        }
        viewBinding.updateCategoryName.doOnTextChanged { text, start, before, count ->
            viewBinding.updateCategoryIconName.text = text
        }
        // 수정 완료하기 버튼
        viewBinding.updateCategoryButton.setOnClickListener(){
            val newName : String  = viewBinding.updateCategoryName.text.toString().trim()
            when {
                newName == "" -> {
                    Toast.makeText(this, "카테고리 이름을 입력해주세요", Toast.LENGTH_SHORT).show()
                }
                (roomDb.CategoryDao().selectByName(newName) != 0) and (newName != viewBinding.updateCategoryIconName.text) -> {
                    Toast.makeText(this, "같은 이름의 카테고리가 있습니다\n     다른 이름을 입력해주세요", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    //카테고리 수정하기
                    httpConnection.updateCategory(this,roomDb,1,categoryId, CategoryRequestData(newName,typeId))

                    val intent = Intent(this, SettingCategoryActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

}