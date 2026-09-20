package com.programadodzero

import android.content.Context

object ProgressManager {
    private const val PREFS = "learning_progress"
    private const val XP = "xp"
    private const val ACTIVE_LANGUAGE = "active_language"
    private const val COMPLETED_LESSONS = "completed_lessons"
    private const val COMPLETED_EXERCISES = "completed_exercises"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun getXp(context: Context): Int = prefs(context).getInt(XP, 0)

    fun addXp(context: Context, amount: Int) {
        val p = prefs(context)
        p.edit().putInt(XP, p.getInt(XP, 0) + amount).apply()
    }

    fun getActiveLanguage(context: Context): String? =
        prefs(context).getString(ACTIVE_LANGUAGE, null)

    fun hasActiveLanguage(context: Context): Boolean =
        !getActiveLanguage(context).isNullOrBlank()

    fun isLanguageComplete(context: Context, language: String, total: Int): Boolean {
        return completedCount(context, language, total) == total &&
            completedExerciseCount(context, language, total) == total
    }

    fun canStartLanguage(context: Context, language: String, total: Int): Boolean {
        val active = getActiveLanguage(context)
        return active == null || active == language ||
            isLanguageComplete(context, active, total)
    }

    fun selectLanguage(context: Context, language: String, total: Int): Boolean {
        val active = getActiveLanguage(context)

        if (active == null) {
            prefs(context).edit().putString(ACTIVE_LANGUAGE, language).apply()
            return true
        }

        if (active == language) return true

        if (isLanguageComplete(context, active, total)) {
            prefs(context).edit().putString(ACTIVE_LANGUAGE, language).apply()
            return true
        }

        return false
    }

    private fun key(language: String, item: Int) = "$language:$item"

    fun isLessonCompleted(context: Context, language: String, lesson: Int): Boolean {
        return prefs(context).getStringSet(COMPLETED_LESSONS, emptySet())
            ?.contains(key(language, lesson)) == true
    }

    fun completeLesson(context: Context, language: String, lesson: Int) {
        val p = prefs(context)
        val current = p.getStringSet(COMPLETED_LESSONS, emptySet())?.toMutableSet() ?: mutableSetOf()
        if (current.add(key(language, lesson))) {
            p.edit().putStringSet(COMPLETED_LESSONS, current).apply()
            addXp(context, 25)
        }
    }

    fun completedCount(context: Context, language: String, total: Int): Int {
        return (0 until total).count { isLessonCompleted(context, language, it) }
    }

    fun isExerciseCompleted(context: Context, language: String, exercise: Int): Boolean {
        return prefs(context).getStringSet(COMPLETED_EXERCISES, emptySet())
            ?.contains(key(language, exercise)) == true
    }

    fun completeExercise(context: Context, language: String, exercise: Int): Boolean {
        val p = prefs(context)
        val current = p.getStringSet(COMPLETED_EXERCISES, emptySet())?.toMutableSet() ?: mutableSetOf()
        if (current.add(key(language, exercise))) {
            p.edit().putStringSet(COMPLETED_EXERCISES, current).apply()
            addXp(context, 25)
            return true
        }
        return false
    }

    fun completedExerciseCount(context: Context, language: String, total: Int): Int {
        return (0 until total).count { isExerciseCompleted(context, language, it) }
    }
}
