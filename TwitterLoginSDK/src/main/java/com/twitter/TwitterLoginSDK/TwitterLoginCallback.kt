package com.twitter.TwitterLoginSDK

interface TwitterLoginCallback {
    fun onTwitterSuccess(response: TwitterLoginResponse)
    fun onTwitterFailure(error: Exception)
}