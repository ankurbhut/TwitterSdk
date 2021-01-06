package com.twitter.mylibrary

interface TwitterLoginCallback {
    fun onTwitterSuccess(response: TwitterLoginResponse)
    fun onTwitterFailure(error: Exception)
}