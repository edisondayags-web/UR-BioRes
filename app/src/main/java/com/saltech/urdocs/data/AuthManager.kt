package com.saltech.urdocs.data

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthManager {
    private val auth = FirebaseAuth.getInstance()

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    suspend fun ensureSignedIn(): FirebaseUser {
        auth.currentUser?.let { return it }
        val result = auth.signInAnonymously().await()
        return result.user ?: error("Anonymous sign-in failed: walang user na na-return")
    }

    // Kung guest pa, i-upgrade ang guest account (same uid). Kung hindi, gumawa ng bago.
    suspend fun signUp(email: String, password: String): FirebaseUser {
        val cur = auth.currentUser
        val result = if (cur != null && cur.isAnonymous) {
            cur.linkWithCredential(EmailAuthProvider.getCredential(email, password)).await()
        } else {
            auth.createUserWithEmailAndPassword(email, password).await()
        }
        return result.user ?: error("Sign-up failed")
    }

    suspend fun signIn(email: String, password: String): FirebaseUser {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user ?: error("Login failed")
    }

    suspend fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    // Log out pero bumabalik sa guest para gumana pa rin ang app
    fun signOutToGuest() {
        auth.signOut()
        auth.signInAnonymously()
    }
}
