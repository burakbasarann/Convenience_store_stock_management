package com.basaran.casestudy.repository.user

import com.basaran.casestudy.data.local.dao.UserDao
import com.basaran.casestudy.data.model.User
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {
    override suspend fun registerUser(username: String, password: String): String? {

        val existingUser = userDao.getUserByUsername(username)
        if (existingUser != null) {
            return null
        }

        val userId = UUID.randomUUID().toString()
        val user = User(userId, username, password)
        userDao.insertUser(user)
        return userId
    }

    override suspend fun loginUser(username: String, password: String): User? {
        return userDao.getUser(username, password)
    }
}