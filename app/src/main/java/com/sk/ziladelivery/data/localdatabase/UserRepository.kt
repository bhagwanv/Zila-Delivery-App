package com.sk.ziladelivery.data.localdatabase

import android.content.Context
import android.os.AsyncTask

class UserRepository(context: Context) {

    var db: UserDao = AppDatabase.getInstance(context)?.userDao()!!


    //Fetch All the Users
    fun getAllUsers(): List<UserLatLngModel> {
        return db.gelAllUsers()
    }

    // Insert new user
    fun insertUser(users: UserLatLngModel) {
        insertAsyncTask(db).execute(users)
    }

    // update user
    fun updateUser(users: UserLatLngModel) {
        db.updateUser(users)
    }

    // Delete user
    fun deleteUser(users: UserLatLngModel) {
        db.deleteUser(users)
    }

    fun deleteUserAll() {
        db.deleteUserAll()
    }

    private class insertAsyncTask internal constructor(private val usersDao: UserDao) :
        AsyncTask<UserLatLngModel, Void, Void>() {

        override fun doInBackground(vararg params: UserLatLngModel): Void? {
            usersDao.insertUser(params[0])
            return null
        }
    }
}