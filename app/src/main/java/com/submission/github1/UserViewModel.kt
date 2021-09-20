package com.submission.github1

import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.msebera.android.httpclient.Header

enum class TypeListUser {
    SEARCH,
    FOLLOWING,
    FOLLOWERS
}

class UserViewModel : ViewModel() {
    private val token = "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"
    private val usersModel = MutableLiveData<UsersModel>()
    val getUsers: LiveData<UsersModel> = usersModel

    private val userModel = MutableLiveData<UserModel>()
    val getUser: LiveData<UserModel> = userModel

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    fun getDetailUser(username: String) {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username"

        _isLoading.postValue(true)
        client.addHeader("Authorization", "token $token")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val result = String(responseBody!!)

                    val jsonAdapter = moshi.adapter(UserModel::class.java)
                    val response = jsonAdapter.fromJson(result)

                    userModel.postValue(response)
                } catch (e: Exception) {
                    _snackbarText.postValue(Event(e.message.toString()))
                } finally {
                    _isLoading.postValue(false)
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                _isLoading.postValue(false)
                _snackbarText.postValue(Event(error?.message.toString()))
            }

        })
    }

    fun getListUser(type: TypeListUser, username: String) {
        val client = AsyncHttpClient()
        var url = "https://api.github.com"

        url += when (type) {
            TypeListUser.SEARCH -> "/search/users?q=$username"
            TypeListUser.FOLLOWING -> "/users/$username/following"
            TypeListUser.FOLLOWERS -> "/users/$username/followers"
        }

        _isLoading.postValue(true)
        client.addHeader("Authorization", "token $token")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler(Looper.getMainLooper()) {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val result = String(responseBody!!)

                    val jsonAdapter = moshi.adapter(UsersModel::class.java)
                    val response = jsonAdapter.fromJson(result)

                    usersModel.postValue(response)
                } catch (e: Exception) {
                    _snackbarText.postValue(Event(e.message.toString()))
                } finally {
                    _isLoading.postValue(false)
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                _isLoading.postValue(false)
                _snackbarText.postValue(Event(error?.message.toString()))
            }

        })
    }
}