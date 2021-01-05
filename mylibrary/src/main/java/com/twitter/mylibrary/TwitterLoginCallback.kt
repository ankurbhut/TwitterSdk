package com.twitter.mylibrary

interface TwitterLoginCallback {
    fun onTwitterSuccess(twitterSession: TwitterDataModel)
    fun onTwitterFailure(error: Exception)
}