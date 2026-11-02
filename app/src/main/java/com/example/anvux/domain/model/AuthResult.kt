package com.example.anvux.domain.model

sealed interface AuthResult {
    data object Success : AuthResult
    data class Failure(val message: String) : AuthResult
}