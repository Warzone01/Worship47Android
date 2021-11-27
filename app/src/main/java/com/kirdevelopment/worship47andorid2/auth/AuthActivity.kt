package com.kirdevelopment.worship47andorid2.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.kirdevelopment.worship47andorid2.R
import com.kirdevelopment.worship47andorid2.databinding.ActivityAuthBinding
import com.kirdevelopment.worship47andorid2.home.HomeActivity
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    var dp1 = 0f
    var screenKey = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dp1 = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 1f, resources.displayMetrics
        )


    }

    override fun onResume() {
        super.onResume()
        initFocus()
        setClicks()
    }

    // устанавливает клики
    private fun setClicks() {
        binding.btnLogin.setOnClickListener {
            if (screenKey == 1) {
                setLoadingSpinner()
            } else {
                when {
                    binding.etEmail.text.isEmpty() -> setErrorUnderLine(binding.underlineEmail)

                    binding.etPassword.text.isEmpty() -> setErrorUnderLine(binding.underlinePassword)

                    else -> runBlocking {
                        setLoadingSpinner()
                        withTimeout(5000L) {
                            startActivity(Intent(this@AuthActivity, HomeActivity::class.java))
                            finish()
                        }
                    }

                }
            }
        }

        binding.btnRegistration.setOnClickListener {
            if (!binding.cpPreloaderAuth.isVisible) {
                when (screenKey) {
                    0 -> initRegistration()

                    else -> initAuth()
                }
            }
        }
    }

    // переходим в регистрацию
    private fun initRegistration() {
        val registerParam: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            (dp1 * 140).toInt()
        )
        binding.tvDoNotHaveAnAcc.text = getText(R.string.already_have_acc)
        binding.btnRegistration.text = getText(R.string.log_in)
        binding.llWelcomeTextContainer.visibility = View.GONE
        binding.tvRegistration.visibility = View.VISIBLE
        binding.clHeader.layoutParams = registerParam
        binding.llInputFirstname.visibility = View.VISIBLE
        binding.llInputName.visibility = View.VISIBLE
        binding.llInputRepeatPassword.visibility = View.VISIBLE
        binding.btnLogin.text = getText(R.string.registration)
        binding.tvForgotPassword.visibility = View.GONE
        screenKey = 1
    }

    // переходим на экран авторизации
    private fun initAuth() {
        val registerParam: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        binding.tvDoNotHaveAnAcc.text = getText(R.string.dont_have_acc)
        binding.btnRegistration.text = getText(R.string.registration)
        binding.btnLogin.text = getText(R.string.log_in)
        binding.llWelcomeTextContainer.visibility = View.VISIBLE
        binding.tvRegistration.visibility = View.GONE
        binding.clHeader.layoutParams = registerParam
        binding.llInputFirstname.visibility = View.GONE
        binding.llInputName.visibility = View.GONE
        binding.llInputRepeatPassword.visibility = View.GONE
        binding.tvForgotPassword.visibility = View.VISIBLE
        screenKey = 0
    }

    // слушает фокус полей ввода
    private fun initFocus() {
        binding.etEmail.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.underlineEmail.setBackgroundResource(R.drawable.focused_under_line)
            } else {
                binding.underlineEmail.setBackgroundResource(R.drawable.under_line)
            }
        }

        binding.etName.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.underlineName.setBackgroundResource(R.drawable.focused_under_line)
            } else {
                binding.underlineName.setBackgroundResource(R.drawable.under_line)
            }
        }

        binding.etFirstname.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.underlineFirstname.setBackgroundResource(R.drawable.focused_under_line)
            } else {
                binding.underlineFirstname.setBackgroundResource(R.drawable.under_line)
            }
        }

        binding.etPassword.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.underlinePassword.setBackgroundResource(R.drawable.focused_under_line)
            } else {
                binding.underlinePassword.setBackgroundResource(R.drawable.under_line)
            }
        }

        binding.etPasswordRepeat.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.underlinePasswordRepeat.setBackgroundResource(R.drawable.focused_under_line)
            } else {
                binding.underlinePasswordRepeat.setBackgroundResource(R.drawable.under_line)
            }
        }
    }

    // выставить ошибку
    private fun setErrorUnderLine(underLine: View) {
        underLine.setBackgroundResource(R.drawable.error_under_line)
    }

    // включить прелоадер
    private fun setLoadingSpinner() {
        binding.btnLogin.visibility = View.GONE
        binding.cpPreloaderAuth.visibility = View.VISIBLE
    }
}