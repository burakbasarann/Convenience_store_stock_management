package com.basaran.casestudy.repository.user

import com.basaran.casestudy.data.model.User

interface UserRepository {
    suspend fun registerUser(username: String, password: String): String?
    suspend fun loginUser(username: String, password: String): User?
}
