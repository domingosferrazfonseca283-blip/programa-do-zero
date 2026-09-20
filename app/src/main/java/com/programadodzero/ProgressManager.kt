package com.programadodzero

import android.content.Context

object ProgressManager {
    private const val PREFS = "learning_progress"
    private const val XP = "xp"
    private const val COMPLETED = "completed_lessons"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun getXp(context: Context): Int = prefs(context).getInt(XP, 0)

    fun addXp(context: Context, amount: Int) {
        val p = prefs(context)
        p.edit().putInt(XP, p.getInt(XP, 0) + amount).apply()
    }

    fun isLessonCompleted(context: Context, language: String, lesson: Int): Boolean {
        return prefs(context).getStringSet(COMPLETED, emptySet())
            ?.contains("$language:$lesson") == true
    }

    fun completeLesson(context: Context, language: String, lesson: Int) {
        val p = prefs(context)
        val key = "$language:$lesson"
        val current = p.getStringSet(COMPLETED, emptySet())?.toMutableSet() ?: mutableSetOf()
        if (current.add(key)) {
            p.edit().putStringSet(COMPLETED, current).apply()
            addXp(context, 25)
        }
    }

    fun completedCount(context: Context, language: String, total: Int): Int {
        return (0 until total).count { isLessonCompleted(context, language, it) }
    }
}
