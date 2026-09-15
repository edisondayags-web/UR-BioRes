package com.saltech.urdocs.drafts

import android.content.Context
import com.google.gson.Gson

data class DraftEntry(
    val id: String,
    val type: String,
    val label: String,
    val timestamp: Long,
    val json: String
)

object DraftManager {
    private const val PREFS = "ur_drafts"
    private const val KEY_INDEX = "draft_index"
    private val gson = Gson()

    fun <T> saveDraft(context: Context, type: String, label: String, data: T) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val id = "draft_${System.currentTimeMillis()}"
        val entry = DraftEntry(
            id = id,
            type = type,
            label = label.ifBlank { "Untitled" },
            timestamp = System.currentTimeMillis(),
            json = gson.toJson(data)
        )
        val index = getAllDrafts(context).toMutableList()
        index.add(0, entry)
        prefs.edit().putString(KEY_INDEX, gson.toJson(index)).apply()
    }

    fun getAllDrafts(context: Context): List<DraftEntry> {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val raw = prefs.getString(KEY_INDEX, null) ?: return emptyList()
        return try {
            val arr = gson.fromJson(raw, Array<DraftEntry>::class.java)
            arr.toList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun <T> loadDraft(context: Context, id: String, classOfT: Class<T>): T? {
        val entry = getAllDrafts(context).find { it.id == id } ?: return null
        return try {
            gson.fromJson(entry.json, classOfT)
        } catch (e: Exception) {
            null
        }
    }

    fun deleteDraft(context: Context, id: String) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val index = getAllDrafts(context).filterNot { it.id == id }
        prefs.edit().putString(KEY_INDEX, gson.toJson(index)).apply()
    }
}
