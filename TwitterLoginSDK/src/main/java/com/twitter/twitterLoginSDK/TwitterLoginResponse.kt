package com.twitter.twitterLoginSDK

import com.google.gson.annotations.SerializedName
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.models.UserEntities

class TwitterLoginResponse {
    var id: Long? = null
    var name: String? = null
    var screenName: String? = null
    var email: String? = null
    var profilePic: String? = null
    var contributorsEnabled: Boolean = false
    var createdAt: String? = null
    var defaultProfile = false
    var defaultProfileImage = false
    var description: String? = null
    var entities: UserEntities? = null
    var favouritesCount = 0
    var followRequestSent = false
    var followersCount = 0
    var friendsCount = 0
    var geoEnabled = false
    var idStr: String? = null
    var isTranslator = false
    var lang: String? = null
    var listedCount = 0
    var location: String? = null
    var profileBackgroundColor: String? = null
    var profileBackgroundImageUrl: String? = null
    var profileBackgroundImageUrlHttps: String? = null
    var profileBackgroundTile = false
    var profileBannerUrl: String? = null
    var profileImageUrl: String? = null
    var profileLinkColor: String? = null
    var profileSidebarBorderColor: String? = null
    var profileSidebarFillColor: String? = null
    var profileTextColor: String? = null
    var profileUseBackgroundImage = false
    var protectedUser = false
    var showAllInlineMedia = false
    var statusesCount = 0
    var timeZone: String? = null
    var url: String? = null
    var utcOffset = 0
    var verified = false
    var withheldInCountries: List<String>? = null
    var withheldScope: String? = null
}