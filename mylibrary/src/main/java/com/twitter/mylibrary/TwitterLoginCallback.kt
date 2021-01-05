package com.twitter.mylibrary

import com.twitter.sdk.android.core.TwitterSession

interface TwitterLoginCallback {
    fun onTwitterSuccess(twitterSession: TwitterSession, email: String)
    fun onTwitterFailure(error: Exception)
}