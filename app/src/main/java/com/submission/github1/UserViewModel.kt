package com.submission.github1

import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.msebera.android.httpclient.Header

class UserViewModel : ViewModel() {
    private val client = AsyncHttpClient()
    private val token = "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"
    private val baseUrl = "https://api.github.com"

    private val usersModel = MutableLiveData<UsersModel>()
    val getUsers: LiveData<UsersModel> = usersModel

    private val userModel = MutableLiveData<UserModel>()
    val getUser: LiveData<UserModel> = userModel

    private val _listFollowing = MutableLiveData<List<UserModel>>()
    val listFollowing: LiveData<List<UserModel>> = _listFollowing

    private val _listFollowers = MutableLiveData<List<UserModel>>()
    val listFollowers: LiveData<List<UserModel>> = _listFollowers

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadingFollowing = MutableLiveData<Boolean>(false)
    val isLoadingFollowing: LiveData<Boolean> = _isLoadingFollowing

    private val _isLoadingFollowers = MutableLiveData<Boolean>(false)
    val isLoadingFollowers: LiveData<Boolean> = _isLoadingFollowers

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    fun getDetailUser(username: String) {
        val url = "$baseUrl/users/$username"

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

    fun getListUser(username: String) {
        var url = "$baseUrl/search/users?q=$username"

//        url += when (type) {
//            TypeListUser.SEARCH -> "/search/users?q=$username"
//            TypeListUser.FOLLOWING -> "/users/$username/following"
//            TypeListUser.FOLLOWERS -> "/users/$username/followers"
//        }

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

    fun getListFollowing(username: String) {
        val url = "$baseUrl/users/$username/following"

        _isLoadingFollowing.postValue(true)
        client.addHeader("Authorization", "token $token")
        client.addHeader("User-Agent", "request")
        client.get(url, object: AsyncHttpResponseHandler(Looper.getMainLooper()) {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val response = String(responseBody!!)

                    val listType = Types.newParameterizedType(List::class.java, UserModel::class.java)
                    val adapter: JsonAdapter<List<UserModel>> = moshi.adapter(listType)
                    val result = adapter.fromJson(response)

                    Log.d("SUCCEED", "onSuccess: $result")
                    _listFollowing.postValue(result)
                } catch (e: Exception) {
                    _snackbarText.postValue(Event(e.message.toString()))
                } finally {
                    _isLoadingFollowing.postValue(false)
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                _isLoadingFollowing.postValue(false)
                _snackbarText.postValue(Event(error?.message.toString()))
            }

        })
    }
}