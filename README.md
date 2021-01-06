<h1 align="center">TwitterLoginSDK</h1>
<p align="center">
  <a href="https://jitpack.io/#ankurbhut/TwiiterSdk"> <img src="https://jitpack.io/v/ankurbhut/TwiiterSdk/month.svg" /></a>
  <a href="https://jitpack.io/#ankurbhut/TwiiterSdk"> <img src="https://jitpack.io/v/ankurbhut/TwiiterSdk.svg" /></a>
</p>

Twitter Login SDK is android library which will help you to integrate social login through twitter with default button and custom button. 
It will help you to complete your integration in small step.
Just create library class instance
and use it or also medify ui as your requirement.

## Installation
Step 1. Add the JitPack repository to your build file
```
allprojects {
    repositories {
	...
	maven { url 'https://jitpack.io' }
}
```
Step 2. Add the dependency
```
dependencies {
    implementation 'com.github.ankurbhut:TwiiterSdk:release_version'
}
```

Step 3. Add This intialization in Application Class.
```
val config = TwitterConfig.Builder(this)
            .logger(DefaultLogger(Log.DEBUG)) //enable logging when app is in debug mode
            .twitterAuthConfig(
                TwitterAuthConfig(
                    resources.getString(R.string.com_twitter_sdk_android_CONSUMER_KEY),
                    resources.getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET)
                )
            ) //pass the created app Consumer KEY and Secret also called API Key and Secret
            .debug(true) //enable debug mode
            .build()
```

## Usage

To **initialize** the sdk class, Use below code and setListeners to
receive the callback.

```kotlin
        private val twitterLogin = TwitterLogin()
     
        twitterLogin.setTwitterLoginListenerCallback(this)
        twitterLoginButton = findViewById(R.id.default_twitter_login_button)
        twitterCustomLoginButton = findViewById(R.id.custom_twitter_login_button)
        userDetailsLabel = findViewById(R.id.txtData)

        twitterLogin.loginWithDefaultButton(twitterLoginButton)

        twitterCustomLoginButton.setOnClickListener {
            twitterLogin.login(this)
        }
```

Add Below Method to get success login data and implemnt CallbackListener.

```kotlin
override fun onTwitterSuccess(response: TwitterLoginResponse) {
        userDetailsLabel.text =
            String.format("User Id : ${response.id} \nScreen Name : ${response.name} \nEmail Id : ${response.email} ".trimIndent())
    }

    override fun onTwitterFailure(error: Exception) {
        Toast.makeText(
            this@MainActivity,
            "Failed to authenticate. Please try again.",
            Toast.LENGTH_SHORT
        ).show()
    }
```

**Set ActivityResult for default Button.**
```
 override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        twitterLogin.onActivityResult(requestCode, resultCode, data)
    }
```

## Author
Maintained by [Ankur Bhut](https://github.com/ankurbhut)

## Contribution
[![GitHub contributors](https://img.shields.io/github/contributors/ankurbhut/TwiiterSdk.svg)](https://github.com/ankurbhut/TwiiterSdk/graphs/contributors)

* Bug reports and pull requests are welcome.
* Make sure you use [square/java-code-styles](https://github.com/square/java-code-styles) to format your code.
