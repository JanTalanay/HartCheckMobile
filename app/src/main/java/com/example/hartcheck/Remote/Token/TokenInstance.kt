package com.example.hartcheck.Remote.Token

import java.security.SecureRandom
object TokenInstance {
    private var token: String? = null

    fun isTokenAvailable(): Boolean {
        return token != null
    }

    fun getToken(): String? {
        return token
    }

    fun generateToken(length: Int) {
        val random = SecureRandom()
        val tokenBytes = ByteArray(length)
        random.nextBytes(tokenBytes)
        token = tokenBytes.joinToString("") { "%02x".format(it) }
    }
}