package com.saltech.urdocs.ui.templates

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

private const val WORKER_URL = "https://paymongo-checkout.edisondayags.workers.dev"

fun isTemplateUnlocked(context: Context, templateName: String): Boolean {
    val prefs = context.getSharedPreferences("purchases", Context.MODE_PRIVATE)
    return prefs.getBoolean("unlocked_" + templateName, false)
}

fun markTemplateUnlocked(context: Context, templateName: String) {
    val prefs = context.getSharedPreferences("purchases", Context.MODE_PRIVATE)
    prefs.edit().putBoolean("unlocked_" + templateName, true).apply()
}

suspend fun startTemplateCheckout(
    context: Context,
    templateName: String,
    amountCentavos: Int = 2000,
    onError: (String) -> Unit = {}
) {
    withContext(Dispatchers.IO) {
        try {
            val url = URL(WORKER_URL)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true

            val body = JSONObject()
            body.put("templateName", templateName)
            body.put("amount", amountCentavos)

            conn.outputStream.use { it.write(body.toString().toByteArray()) }

            val responseCode = conn.responseCode
            val stream = if (responseCode in 200..299) conn.inputStream else conn.errorStream
            val responseText = stream.bufferedReader().use { it.readText() }

            if (responseCode in 200..299) {
                val json = JSONObject(responseText)
                val checkoutUrl = json.getString("checkoutUrl")
                withContext(Dispatchers.Main) {
                    val intent = CustomTabsIntent.Builder().build()
                    intent.launchUrl(context, Uri.parse(checkoutUrl))
                }
            } else {
                withContext(Dispatchers.Main) {
                    onError("Payment error: " + responseText)
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                onError(e.message ?: "May error sa pagbabayad")
            }
        }
    }
}
