package com.perevodchik.bubblemeet.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private var presenter: LoginPresenter? = null
    private lateinit var registerTextView: TextView
    private lateinit var forgotTextView: TextView
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var loginLogo: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        registerTextView = findViewById(R.id.textViewDontHaveAccount)
        forgotTextView = findViewById(R.id.textViewForgotPassword)
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.login)
        loginLogo = findViewById(R.id.login_logo)

        /*emailEditText.afterTextChanged {
        }

        forgotTextView.apply {
        }*/

        presenter = LoginPresenter(this)
        loginButton.setOnClickListener { presenter!!.onClick(loginButton) }

        val animationOtherViews = AnimationUtils.loadAnimation(this, R.anim.alpha_auth_views)

        registerTextView.setOnClickListener{ startActivity(Intent(this, RegisterActivity::class.java))}

        loginLogo.startAnimation(
            AnimationUtils.loadAnimation(
                this,
                R.anim.logo_trans
            )
        )

        registerTextView.startAnimation(animationOtherViews)
        forgotTextView.startAnimation(animationOtherViews)
        emailEditText.startAnimation(animationOtherViews)
        passwordEditText.startAnimation(animationOtherViews)
        loginButton.startAnimation(animationOtherViews)
    }

}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
