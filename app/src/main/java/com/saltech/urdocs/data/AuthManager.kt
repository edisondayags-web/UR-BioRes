package com.saltech.urdocs.data

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseAuthUserCollisionException
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

    // Google login: kung guest pa, i-upgrade ang guest account (same uid). Kung may account na ang Google, mag-sign in na lang.
    suspend fun signInWithGoogleIdToken(idToken: String): FirebaseUser {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val cur = auth.currentUser
        if (cur != null && cur.isAnonymous) {
            try {
                val r = cur.linkWithCredential(credential).await()
                auth.currentUser?.reload()?.await()
                return r.user ?: error("Link failed")
            } catch (e: FirebaseAuthUserCollisionException) {
                // may account na ang Google na ito
            }
        }
        val r = auth.signInWithCredential(credential).await()
        return r.user ?: error("Login failed")
    }

    // Log out pero bumabalik sa guest para gumana pa rin ang app
    fun signOutToGuest() {
        auth.signOut()
        auth.signInAnonymously()
    }
}
