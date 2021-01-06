package com.twitter.sdk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.twitter.TwitterLoginSDK.TwitterLoginResponse
import com.twitter.TwitterLoginSDK.TwitterLogin
import com.twitter.TwitterLoginSDK.TwitterLoginCallback
import com.twitter.sdk.android.core.identity.TwitterLoginButton

class MainActivity : AppCompatActivity(), TwitterLoginCallback {

    private lateinit var userDetailsLabel: TextView
    private lateinit var profileImage: ImageView
    private lateinit var twitterLoginButton: TwitterLoginButton
    private lateinit var twitterCustomLoginButton: Button
    private val twitterLogin = TwitterLogin()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        twitterLogin.setTwitterLoginListenerCallback(this)
        initUi()
    }

    private fun initUi() {
        //find the id of views
        profileImage = findViewById(R.id.imgProfile)
        twitterLoginButton = findViewById(R.id.default_twitter_login_button)
        twitterCustomLoginButton = findViewById(R.id.custom_twitter_login_button)
        userDetailsLabel = findViewById(R.id.txtData)

        twitterLogin.loginWithDefaultButton(twitterLoginButton)

        twitterCustomLoginButton.setOnClickListener {
            twitterLogin.login(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        twitterLogin.onActivityResult(requestCode, resultCode, data)
    }

    override fun onTwitterSuccess(response: TwitterLoginResponse) {
        userDetailsLabel.text =
            String.format("User Id : ${response.id} \nScreen Name : ${response.name} \nEmail Id : ${response.email} ".trimIndent())

        Glide.with(this)
            .load(response.profilePic)
            .apply(RequestOptions().circleCrop())
            .into(profileImage)
    }

    override fun onTwitterFailure(error: Exception) {
        Toast.makeText(
            this@MainActivity,
            "Failed to authenticate. Please try again.",
            Toast.LENGTH_SHORT
        ).show()
    }
}