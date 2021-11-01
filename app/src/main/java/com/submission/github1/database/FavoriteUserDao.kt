package com.submission.github1.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteUser: FavoriteUser)

    @Delete
    fun delete(favoriteUser: FavoriteUser)

    @Query("SELECT * FROM favoriteUser ORDER BY id ASC")
    fun getListUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT * FROM favoriteUser WHERE login=:username")
    fun getFavUser(username: String): LiveData<FavoriteUser>
}