package com.saltech.urdocs.data

import com.google.firebase.functions.FirebaseFunctions
import com.saltech.urdocs.model.LetterRequest
import kotlinx.coroutines.tasks.await

/**
 * IMPORTANT: Hindi dito nakadikit ang Gemini API key. Ang app tumatawag lang
 * sa Firebase Cloud Function ("generateLetter"), at ang function na yun ang
 * may hawak ng key sa server side. Kung diretso sa Gemini API galing mismo
 * sa app, pwedeng ma-extract ng iba yung key sa APK -- kaya proxy approach.
 */
class GeminiRepository {
    private val functions = FirebaseFunctions.getInstance()

    suspend fun generateLetter(request: LetterRequest): String {
        val payload = hashMapOf(
            "letterType" to request.type.name,
            "fullName" to request.fullName,
            "position" to request.position,
            "company" to request.company,
            "reason" to request.reason,
            "dateNeeded" to request.dateNeeded,
            "extraDetails" to request.extraDetails
        )

        val result = functions
            .getHttpsCallable("generateLetter")
            .call(payload)
            .await()

        @Suppress("UNCHECKED_CAST")
        val data = result.data as? Map<String, Any>
        return data?.get("letterText") as? String
            ?: error("Walang natanggap na letter text mula sa server.")
    }
}
