package com.twitter.twitterLoginSDK

import android.app.Activity
import android.content.Intent
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import com.twitter.sdk.android.core.identity.TwitterLoginButton
import com.twitter.sdk.android.core.models.User

open class TwitterLogin {
    //twitter auth client required for custom login
    private var listener: TwitterLoginCallback? = null
    private var client: TwitterAuthClient? = null
    private var twitterLoginButton: TwitterLoginButton? = null

    init {
        client = TwitterAuthClient()
    }

    fun getClient(): TwitterAuthClient {
        if (client != null) {
            return client!!
        }
        client = TwitterAuthClient()
        return client!!
    }

    open fun setTwitterLoginListenerCallback(listener: TwitterLoginCallback) {
        this.listener = listener
    }

    open fun loginWithDefaultButton(view: TwitterLoginButton) {
        twitterLoginButton = view
        //check if user is already authenticated or not
        if (getTwitterSession() == null) {
            //if user is not authenticated start authenticating
            view.callback = object :
                Callback<TwitterSession>() {
                override fun success(result: Result<TwitterSession>) {
                    val twitterSession = result.data
                    //call fetch email only when permission is granted
                    manageTwitterLoginResponse(twitterSession, getClient())
                }

                override fun failure(exception: TwitterException) {
                    listener?.onTwitterFailure(exception)
                }
            }
        } else {
            //if user is already authenticated direct call fetch twitter email api
            manageTwitterLoginResponse(getTwitterSession()!!, getClient())
        }
    }

    open fun login(activity: Activity) {
        //check if user is already authenticated or not
        if (getTwitterSession() == null) {
            //if user is not authenticated start authenticating
            getClient().authorize(activity, object : Callback<TwitterSession>() {
                override fun success(result: Result<TwitterSession>) {
                    val twitterSession = result.data
                    //call fetch email only when permission is granted
                    manageTwitterLoginResponse(twitterSession, getClient())
                }

                override fun failure(e: TwitterException) {
                    listener?.onTwitterFailure(e)
                }
            })
        } else {
            //if user is already authenticated direct call fetch twitter email api
            manageTwitterLoginResponse(getTwitterSession()!!, getClient())
        }
    }

    /**
     * Before using this feature, ensure that “Request email addresses from users” is checked for your Twitter app.
     *
     * @param twitterSession user logged in twitter session
     */
    private fun manageTwitterLoginResponse(
        twitterSession: TwitterSession, client: TwitterAuthClient
    ) {
        client.requestEmail(twitterSession, object : Callback<String>() {
            override fun success(result: Result<String>) {
                //here it will give u only email and rest of other information u can get from TwitterSession
                val response = TwitterLoginResponse()
                response.id = twitterSession.userId
                response.name = twitterSession.userName
                response.screenName = twitterSession.userName
                response.email = result.data
                fetchTwitterImage(response) // get profile picture
            }

            override fun failure(exception: TwitterException) {
                listener?.onTwitterFailure(exception)
//                Toast.makeText(this@MainActivity, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * call Verify Credentials API when Twitter Auth is successful else it will go in exception block
     * this metod will provide you User model which contain all user information
     */

    private fun fetchTwitterImage(response: TwitterLoginResponse) {
        if (getTwitterSession() != null) {
            //fetch twitter image with other information if user is already authenticated
            //initialize twitter api client
            val twitterApiClient = TwitterCore.getInstance().apiClient
            //Link for Help : https://developer.twitter.com/en/docs/accounts-and-users/manage-account-settings/api-reference/get-account-verify_credentials
            //pass includeEmail : true if you want to fetch Email as well
            val call = twitterApiClient.accountService.verifyCredentials(true, false, true)
            call.enqueue(object : Callback<User>() {
                override fun success(result: Result<User>) {
                    val user = result.data
                    //NOTE : User profile provided by twitter is very small in size i.e 48*48
                    //Link : https://developer.twitter.com/en/docs/accounts-and-users/user-profile-images-and-banners
                    //so if you want to get bigger size image then do the following:
                    response.name = user.name
                    response.screenName = user.screenName
                    response.profilePic = user.profileImageUrlHttps.replace("_normal", "")
                    response.contributorsEnabled = user.contributorsEnabled
                    response.createdAt = user.createdAt
                    response.defaultProfile = user.defaultProfile
                    response.defaultProfileImage = user.defaultProfileImage
                    response.description = user.description
                    response.entities = user.entities
                    response.favouritesCount = user.favouritesCount
                    response.followRequestSent = user.followRequestSent
                    response.followersCount = user.followersCount
                    response.friendsCount = user.friendsCount
                    response.geoEnabled = user.geoEnabled
                    response.idStr = user.idStr
                    response.isTranslator = user.isTranslator
                    response.lang = user.lang
                    response.listedCount = user.listedCount
                    response.location = user.location
                    response.profileBackgroundColor = user.profileBackgroundColor
                    response.profileBackgroundImageUrl = user.profileBackgroundImageUrl
                    response.profileBackgroundImageUrlHttps = user.profileBackgroundImageUrlHttps
                    response.profileBackgroundTile = user.profileBackgroundTile
                    response.profileBannerUrl = user.profileBannerUrl
                    response.profileImageUrl = user.profileImageUrl
                    response.profileLinkColor = user.profileLinkColor
                    response.profileSidebarBorderColor = user.profileSidebarBorderColor
                    response.profileSidebarFillColor = user.profileSidebarFillColor
                    response.profileTextColor = user.profileTextColor
                    response.profileUseBackgroundImage = user.profileUseBackgroundImage
                    response.protectedUser = user.protectedUser
                    response.showAllInlineMedia = user.showAllInlineMedia
                    response.statusesCount = user.statusesCount
                    response.timeZone = user.timeZone
                    response.url = user.url
                    response.utcOffset = user.utcOffset
                    response.verified = user.verified
                    response.withheldInCountries = user.withheldInCountries
                    response.withheldScope = user.withheldScope
                    listener?.onTwitterSuccess(response)
                    clearTwitterSession()  // after get login detail need to clear active session
                }

                override fun failure(exception: TwitterException) {
                    listener?.onTwitterSuccess(response)
                }
            })
        } else {
            listener?.onTwitterSuccess(response)
        }
    }

    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Pass the activity result to the twitterAuthClient.
        if (client != null) client!!.onActivityResult(requestCode, resultCode, data)
        // Pass the activity result to the login button.
        twitterLoginButton?.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * get authenticates user session
     *
     * @return twitter session
     */
    open fun getTwitterSession(): TwitterSession? {
        //NOTE : if you want to get token and secret too use uncomment the below code
        /*TwitterAuthToken authToken = session.getAuthToken();
        String token = authToken.token;
        String secret = authToken.secret;*/
        return TwitterCore.getInstance().sessionManager.activeSession
    }

    open fun clearTwitterSession() {
        return TwitterCore.getInstance().sessionManager.clearActiveSession()
    }
}