package com.saltech.urdocs.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

/**
 * Same anonymous-auth pattern as UR Call's AuthManager.
 * Ensures every device has a stable uid without requiring
 * a login screen -- keeps the "walang abala" onboarding flow.
 */
class AuthManager {
    private val auth = FirebaseAuth.getInstance()

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    suspend fun ensureSignedIn(): FirebaseUser {
        auth.currentUser?.let { return it }
        val result = auth.signInAnonymously().await()
        return result.user ?: error("Anonymous sign-in failed: walang user na na-return")
    }
}
