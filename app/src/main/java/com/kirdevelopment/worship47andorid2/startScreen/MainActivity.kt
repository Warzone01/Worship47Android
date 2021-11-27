package com.kirdevelopment.worship47andorid2.startScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.viewpager2.widget.ViewPager2
import com.kirdevelopment.worship47andorid2.R
import com.kirdevelopment.worship47andorid2.auth.AuthActivity
import com.kirdevelopment.worship47andorid2.databinding.ActivityAuthBinding
import com.kirdevelopment.worship47andorid2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTabs()
    }

    // определяем вкладки
    private fun initTabs() {

        val selectedParam: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            2f
        )

        val notSelectedParam: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )

        binding.vpHome.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.vpHome.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                when (position) {

                    0 -> {
                        binding.ivBtnNext.setImageResource(R.drawable.ic_right)
                        binding.ivBtnNext.setOnClickListener {
                            binding.vpHome.currentItem = position + 1
                        }
                        binding.firstIndicator.setBackgroundResource(R.drawable.selected_indicator)
                        binding.firstIndicator.layoutParams = selectedParam
                        selectedParam.setMargins(8,0,8,0)
                        binding.secondIndicator.setBackgroundResource(R.drawable.not_selected_indicator)
                        binding.secondIndicator.layoutParams = notSelectedParam
                        binding.thirdIndicator.setBackgroundResource(R.drawable.not_selected_indicator)
                        binding.thirdIndicator.layoutParams = notSelectedParam
                    }

                    1 -> {
                        binding.ivBtnNext.setImageResource(R.drawable.ic_right)
                        binding.ivBtnNext.setOnClickListener {
                            binding.vpHome.currentItem = position + 1
                        }
                        binding.firstIndicator.setBackgroundResource(R.drawable.not_selected_indicator)
                        binding.firstIndicator.layoutParams = notSelectedParam
                        binding.secondIndicator.setBackgroundResource(R.drawable.selected_indicator)
                        binding.secondIndicator.layoutParams = selectedParam
                        selectedParam.setMargins(8,0,8,0)
                        binding.thirdIndicator.setBackgroundResource(R.drawable.not_selected_indicator)
                        binding.thirdIndicator.layoutParams = notSelectedParam
                    }

                    else -> {
                        binding.ivBtnNext.setImageResource(R.drawable.ic_login)
                        binding.ivBtnNext.setOnClickListener {
                            startActivity(Intent(this@MainActivity,
                                AuthActivity::class.java))
                            finish()
                        }
                        binding.firstIndicator.setBackgroundResource(R.drawable.not_selected_indicator)
                        binding.firstIndicator.layoutParams = notSelectedParam
                        binding.secondIndicator.setBackgroundResource(R.drawable.not_selected_indicator)
                        binding.secondIndicator.layoutParams = notSelectedParam
                        binding.thirdIndicator.setBackgroundResource(R.drawable.selected_indicator)
                        binding.thirdIndicator.layoutParams = selectedParam
                        selectedParam.setMargins(8,0,8,0)
                    }
                }
            }
        })
    }
}