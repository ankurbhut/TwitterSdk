package com.twitter.mylibrary

import android.app.Activity
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import com.twitter.sdk.android.core.identity.TwitterLoginButton
import com.twitter.sdk.android.core.models.User
import java.lang.reflect.UndeclaredThrowableException

open class TwitterInit {
    //twitter auth client required for custom login
    private var listener: TwitterLoginCallback? = null

    open fun setTwitterLoginListenerCallback(listener: TwitterLoginCallback) {
        this.listener = listener
    }

    open fun defaultLoginTwitter(view: TwitterLoginButton, client: TwitterAuthClient) {
        //check if user is already authenticated or not
        if (getTwitterSession() == null) {

            //if user is not authenticated start authenticating
            view.callback = object :
                Callback<TwitterSession>() {
                override fun success(result: Result<TwitterSession>) {

                    // Do something with result, which provides a TwitterSession for making API calls
                    val twitterSession = result.data

                    //call fetch email only when permission is granted
                    fetchTwitterEmail(twitterSession, client)
                }

                override fun failure(exception: TwitterException) {
                    // Do something on failure
                   listener?.onTwitterFailure(exception)
                }
            }
        } else {
            //if user is already authenticated direct call fetch twitter email api
            fetchTwitterEmail(getTwitterSession()!!, client)
        }
    }

    open fun customLoginTwitter(activity: Activity, client: TwitterAuthClient) {
        //check if user is already authenticated or not
        if (getTwitterSession() == null) {

            //if user is not authenticated start authenticating
            client!!.authorize(
                activity,
                object : Callback<TwitterSession>() {
                    override fun success(result: Result<TwitterSession>) {

                        // Do something with result, which provides a TwitterSession for making API calls
                        val twitterSession = result.data

                        //call fetch email only when permission is granted
                        fetchTwitterEmail(twitterSession, client)
                    }

                    override fun failure(e: TwitterException) {
                        // Do something on failure
                       listener?.onTwitterFailure(e)
                    }
                })
        } else {
            //if user is already authenticated direct call fetch twitter email api
            fetchTwitterEmail(getTwitterSession()!!, client)
        }
    }

    /**
     * Before using this feature, ensure that “Request email addresses from users” is checked for your Twitter app.
     *
     * @param twitterSession user logged in twitter session
     */
    open fun fetchTwitterEmail(twitterSession: TwitterSession, client: TwitterAuthClient) {
        client.requestEmail(twitterSession, object : Callback<String>() {
            override fun success(result: Result<String>) {
                //here it will give u only email and rest of other information u can get from TwitterSession
                var bean = TwitterDataModel()
                bean.id = twitterSession.userId
                bean.name = twitterSession.userName
                bean.email = result.data
                bean.profilePic = fetchTwitterImage()

                listener?.onTwitterSuccess(bean)

                TwitterCore.getInstance().sessionManager.clearActiveSession()
//                userDetailsLabel.setText("""User Id : ${twitterSession.userId} Screen Name : ${twitterSession.userName} Email Id : ${result.data} """.trimIndent())
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
    open fun fetchTwitterImage(): String {
        //check if user is already authenticated or not
        var imageProfileUrl = ""
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
//                    userDetailsLabel.setText("User Id : ${ user.id } User Name : ${ user.name } Email Id : ${ user.email } Screen Name : ${ user.screenName }".trimIndent())
                    imageProfileUrl = user.profileImageUrl

                    //NOTE : User profile provided by twitter is very small in size i.e 48*48
                    //Link : https://developer.twitter.com/en/docs/accounts-and-users/user-profile-images-and-banners
                    //so if you want to get bigger size image then do the following:
                    imageProfileUrl = imageProfileUrl.replace("_normal", "")

                }

                override fun failure(exception: TwitterException) {
                    UndeclaredThrowableException(exception)
//                    Toast.makeText(MyApplication.instance, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            //if user is not authenticated first ask user to do authentication
//            Toast.makeText(MyApplication.instance, "First to Twitter auth to Verify Credentials.", Toast.LENGTH_SHORT).show()
        }
        return imageProfileUrl
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