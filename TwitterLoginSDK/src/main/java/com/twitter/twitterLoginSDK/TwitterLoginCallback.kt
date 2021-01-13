package com.twitter.twitterLoginSDK

interface TwitterLoginCallback {
    fun onTwitterSuccess(response: TwitterLoginResponse)
    fun onTwitterFailure(error: Exception)
}