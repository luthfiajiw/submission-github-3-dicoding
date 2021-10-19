package com.submission.github1

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class UsersModel(
    var total_count: Int = 0,
    var incomplete_result: Boolean = false,
    var items: List<UserModel> = listOf<UserModel>()
)

@Parcelize
data class UserModel(
    val id: Int = 0,
    var login: String? = "",
    var avatar_url: String? = "",
    var followers_url: String? = "",
    var following_url: String? = "",
    var name: String? = "",
    var company: String? = "",
    var blog: String? = "",
    var location: String? = "",
    var public_repos: Int = 0,
    var followers: Int = 0,
    var following: Int = 0,
) : Parcelable
