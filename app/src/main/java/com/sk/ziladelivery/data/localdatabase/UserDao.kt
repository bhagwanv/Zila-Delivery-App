package com.sk.ziladelivery.data.localdatabase

import androidx.room.*

@Dao
interface UserDao {

    @Insert
    fun insertUser(users: UserLatLngModel)

    @Query("Select * from userLatLngTable")
    fun gelAllUsers(): List<UserLatLngModel>

    @Update
    fun updateUser(users: UserLatLngModel)

    @Delete
    fun deleteUser(users: UserLatLngModel)

    @Query("delete from userLatLngTable")
    fun deleteUserAll()

}