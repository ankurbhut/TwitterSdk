package com.twitter.sdk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.twitter.mylibrary.TwitterInit
import com.twitter.mylibrary.TwitterLoginCallback
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import com.twitter.sdk.android.core.identity.TwitterLoginButton

class MainActivity : AppCompatActivity(), TwitterLoginCallback {

    private lateinit var userDetailsLabel: TextView
    private var client: TwitterAuthClient? = null
    private lateinit var twitterLoginButton: TwitterLoginButton
    private lateinit var twitterCustomLoginButton: Button
    private val twitterInit = TwitterInit()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        client = TwitterAuthClient()

        twitterInit.setTwitterLoginListenerCallback(this)

        initUi()
    }

    private fun initUi() {
        //find the id of views
        twitterLoginButton = findViewById(R.id.default_twitter_login_button)
        twitterCustomLoginButton = findViewById(R.id.custom_twitter_login_button)
        userDetailsLabel = findViewById(R.id.txtData)

        twitterInit.defaultLoginTwitter(twitterLoginButton, client!!)

        twitterCustomLoginButton.setOnClickListener {
            twitterInit.customLoginTwitter(this, client!!)
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result to the twitterAuthClient.
        if (client != null) client!!.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result to the login button.
        twitterLoginButton.onActivityResult(requestCode, resultCode, data)
    }

    override fun onTwitterSuccess(
        twitterSession: com.twitter.sdk.android.core.TwitterSession,
        email: String
    ) {
        userDetailsLabel.text = "User Id : ${twitterSession.userId} Screen Name : ${twitterSession.userName} Email Id : $email ".trimIndent()
    }

    override fun onTwitterFailure(error: Exception) {
        Toast.makeText(this@MainActivity, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show()
    }
}