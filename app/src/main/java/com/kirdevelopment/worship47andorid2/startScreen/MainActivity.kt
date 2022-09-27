package com.kirdevelopment.worship47andorid2.startScreen

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.kirdevelopment.worship47andorid2.home.HomeActivity
import com.kirdevelopment.worship47andorid2.utils.Constants

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mKey: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mKey = getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    override fun onResume() {
        super.onResume()
        val isNeedAuth = mKey?.getString(Constants.TOKEN, "") == ""
        if (!isNeedAuth) {
            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
            finish()
        } else {
            initTabs()
        }
    }

    // определяем вкладки
    private fun initTabs() {
        binding.vpHome.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.dotsIndicator.attachTo(binding.vpHome)
        binding.vpHome.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                when (position) {

                    0 -> {
                        binding.ivBtnNext.setImageResource(R.drawable.ic_right)
                        binding.ivBtnNext.setOnClickListener {
                            binding.vpHome.currentItem = position + 1
                        }
                    }

                    1 -> {
                        binding.ivBtnNext.setImageResource(R.drawable.ic_right)
                        binding.ivBtnNext.setOnClickListener {
                            binding.vpHome.currentItem = position + 1
                        }
                    }

                    else -> {
                        binding.ivBtnNext.setImageResource(R.drawable.ic_login)
                        binding.ivBtnNext.setOnClickListener {
                            startActivity(Intent(this@MainActivity,
                                AuthActivity::class.java))
                            finish()
                        }
                    }
                }
            }
        })
    }
}