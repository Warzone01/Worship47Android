package com.kirdevelopment.worship47andorid2.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.kirdevelopment.worship47andorid2.R
import com.kirdevelopment.worship47andorid2.databinding.ActivityAuthBinding
import com.kirdevelopment.worship47andorid2.home.HomeActivity
import com.kirdevelopment.worship47andorid2.interactors.AuthInteractor
import com.kirdevelopment.worship47andorid2.models.AuthKey
import com.kirdevelopment.worship47andorid2.utils.Constants.APP_PREFERENCES
import com.kirdevelopment.worship47andorid2.utils.Constants.TOKEN
import kotlinx.coroutines.*

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private val authInteractor = AuthInteractor()
    private val model = AuthViewModel()
    private var mKey: SharedPreferences? = null

    var dp1 = 0f
    var screenKey = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dp1 = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 1f, resources.displayMetrics
        )
        mKey = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    override fun onResume() {
        super.onResume()
        val isNeedAuth = mKey?.getString(TOKEN, "") == ""
        if (!isNeedAuth) {
            startActivity(Intent(this@AuthActivity, HomeActivity::class.java))
            finish()
        }
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

                    else -> auth()
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

    // метод авторизации
    private fun auth() {
        val login: String = binding.etEmail.text.toString()
        val password: String = binding.etPassword.text.toString()
        var key:String?
        GlobalScope.launch(Dispatchers.IO) {
            key = authInteractor.getAuthToken(
                login = login,
                password = password
            )
            withContext(Dispatchers.Main) {
                if (key != null) {
                    model.key.value = key
                    val intent = Intent(this@AuthActivity, HomeActivity::class.java)

                    if (binding.cbRememberMe.isChecked) {
                        val editor = mKey?.edit()
                        editor?.putString(TOKEN, key.toString())
                        editor?.apply()
                    } else {
                        intent.putExtra(TOKEN, key.toString())
                    }
                    setLoadingSpinner()
                    withTimeout(5000L) {
                        startActivity(intent)
                        finish()
                    }
                } else {
                    binding.textError.visibility = View.VISIBLE
                    setErrorUnderLine(binding.underlinePassword)
                    setErrorUnderLine(binding.underlineEmail)
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