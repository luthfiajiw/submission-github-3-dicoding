package com.submission.github1.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class FavoriteUser (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var login: String? = null,

    @ColumnInfo(name = "avatar_url")
    var avatarUrl: String? = null
) : Parcelable