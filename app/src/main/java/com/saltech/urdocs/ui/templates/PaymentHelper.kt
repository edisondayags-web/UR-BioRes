package com.saltech.urdocs.ui.templates

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.google.firebase.functions.FirebaseFunctions

fun isTemplateUnlocked(context: Context, templateName: String): Boolean {
    val prefs = context.getSharedPreferences("purchases", Context.MODE_PRIVATE)
    return prefs.getBoolean("unlocked_$templateName", false)
}

fun markTemplateUnlocked(context: Context, templateName: String) {
    val prefs = context.getSharedPreferences("purchases", Context.MODE_PRIVATE)
    prefs.edit().putBoolean("unlocked_$templateName", true).apply()
}

fun startTemplateCheckout(
    context: Context,
    templateName: String,
    amountCentavos: Int = 2000,
    onError: (String) -> Unit = {}
) {
    val data = hashMapOf(
        "templateName" to templateName,
        "amount" to amountCentavos
    )
    FirebaseFunctions.getInstance()
        .getHttpsCallable("createCheckoutSession")
        .call(data)
        .addOnSuccessListener { result ->
            val response = result.data as? Map<*, *>
            val checkoutUrl = response?.get("checkoutUrl") as? String
            if (checkoutUrl != null) {
                val intent = CustomTabsIntent.Builder().build()
                intent.launchUrl(context, Uri.parse(checkoutUrl))
            } else {
                onError("Walang checkout URL")
            }
        }
        .addOnFailureListener { e ->
            onError(e.message ?: "May error sa pagbabayad")
        }
}
