package com.programadodzero

import android.content.Context

object ProgressManager {
    private const val PREFS = "learning_progress"
    private const val XP = "xp"
    private const val ACTIVE_LANGUAGE = "active_language"
    private const val COMPLETED_LESSONS = "completed_lessons"
    private const val COMPLETED_EXERCISES = "completed_exercises"
    private const val COMPLETED_PROJECTS = "completed_projects"
    private const val COMPLETED_REVIEWS = "completed_reviews"
    private const val LAST_STUDY_DAY = "last_study_day"
    private const val STUDY_STREAK = "study_streak"
    private const val DAILY_MISSION_DAY = "daily_mission_day"
    private const val DAILY_MISSION_XP = "daily_mission_xp"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun getXp(context: Context): Int = prefs(context).getInt(XP, 0)

    fun getLevel(context: Context): Int = 1 + getXp(context) / 100

    fun getStudyStreak(context: Context): Int = prefs(context).getInt(STUDY_STREAK, 0)

    fun registerStudyDay(context: Context): Int {
        val p = prefs(context)
        val today = java.time.LocalDate.now().toString()
        val last = p.getString(LAST_STUDY_DAY, null)
        if (last == today) return getStudyStreak(context)
        val yesterday = java.time.LocalDate.now().minusDays(1).toString()
        val streak = if (last == yesterday) getStudyStreak(context) + 1 else 1
        p.edit().putString(LAST_STUDY_DAY, today).putInt(STUDY_STREAK, streak).apply()
        return streak
    }

    data class DailyMission(val title: String, val target: Int, val progress: Int, val rewardXp: Int, val completed: Boolean)

    fun dailyMission(context: Context, language: String): DailyMission {
        val day = java.time.LocalDate.now().toString()
        val p = prefs(context)
        if (p.getString(DAILY_MISSION_DAY, null) != day) {
            p.edit().putString(DAILY_MISSION_DAY, day).putInt(DAILY_MISSION_XP, 0).apply()
        }
        val lessons = ContentRepository.lessonsFor(language).map { it.id }
        val progress = completedCount(context, language, lessons)
        val target = 1
        return DailyMission("Complete 1 aula hoje", target, minOf(progress, target), 30, progress >= target)
    }

    fun claimDailyMission(context: Context, language: String): Boolean {
        val mission = dailyMission(context, language)
        val p = prefs(context)
        if (!mission.completed || p.getInt(DAILY_MISSION_XP, 0) == 1) return false
        p.edit().putInt(DAILY_MISSION_XP, 1).apply()
        addXp(context, mission.rewardXp)
        return true
    }

    fun achievements(context: Context, language: String): List<String> {
        val lessons = ContentRepository.lessonsFor(language).map { it.id }
        val result = mutableListOf<String>()
        if (getXp(context) >= 100) result.add("⭐ Primeiros 100 XP")
        if (completedCount(context, language, lessons) >= 5) result.add("📚 5 aulas concluídas")
        if (completedExerciseCount(context, language, lessons) >= 5) result.add("🧩 5 exercícios concluídos")
        if (completedReviewCount(context, language) >= 5) result.add("🧠 5 revisões acertadas")
        if (getStudyStreak(context) >= 3) result.add("🔥 3 dias de sequência")
        if (lessons.isNotEmpty() && completedCount(context, language, lessons) == lessons.size) result.add("🏆 Trilha concluída")
        return result
    }

    fun xpIntoLevel(context: Context): Int = getXp(context) % 100

    fun recordReview(context: Context, language: String, questionId: String): Boolean {
        val p = prefs(context)
        val current = p.getStringSet(COMPLETED_REVIEWS, emptySet())?.toMutableSet() ?: mutableSetOf()
        if (!current.add(key(language, questionId))) return false
        p.edit().putStringSet(COMPLETED_REVIEWS, current).apply()
        addXp(context, 10)
        return true
    }

    fun isReviewCompleted(context: Context, language: String, questionId: String): Boolean =
        prefs(context).getStringSet(COMPLETED_REVIEWS, emptySet())?.contains(key(language, questionId)) == true

    fun completedReviewCount(context: Context, language: String): Int =
        prefs(context).getStringSet(COMPLETED_REVIEWS, emptySet())?.count { it.startsWith("$language:") } ?: 0

    fun addXp(context: Context, amount: Int) {
        registerStudyDay(context)
        val p = prefs(context)
        p.edit().putInt(XP, p.getInt(XP, 0) + amount).apply()
    }

    fun getActiveLanguage(context: Context): String? =
        prefs(context).getString(ACTIVE_LANGUAGE, null)

    fun hasActiveLanguage(context: Context): Boolean =
        !getActiveLanguage(context).isNullOrBlank()

    fun isLanguageComplete(context: Context, language: String, lessonIds: List<String>): Boolean {
        return completedCount(context, language, lessonIds) == lessonIds.size &&
            completedExerciseCount(context, language, lessonIds) == lessonIds.size
    }

    fun canStartLanguage(context: Context, language: String, lessonIds: List<String>): Boolean {
        val active = getActiveLanguage(context)
        return active == null || active == language ||
            isLanguageComplete(context, active, ContentRepository.lessonsFor(active).map { it.id })
    }

    fun selectLanguage(context: Context, language: String, lessonIds: List<String>): Boolean {
        val active = getActiveLanguage(context)

        if (active == null) {
            prefs(context).edit().putString(ACTIVE_LANGUAGE, language).apply()
            return true
        }

        if (active == language) return true

        if (isLanguageComplete(context, active, lessonIds)) {
            prefs(context).edit().putString(ACTIVE_LANGUAGE, language).apply()
            return true
        }

        return false
    }

    private fun key(language: String, itemId: String) = "$language:$itemId"

    fun isLessonCompleted(context: Context, language: String, lessonId: String): Boolean {
        return prefs(context).getStringSet(COMPLETED_LESSONS, emptySet())
            ?.contains(key(language, lessonId)) == true
    }

    fun completeLesson(context: Context, language: String, lessonId: String) {
        val p = prefs(context)
        val current = p.getStringSet(COMPLETED_LESSONS, emptySet())?.toMutableSet() ?: mutableSetOf()
        if (current.add(key(language, lessonId))) {
            p.edit().putStringSet(COMPLETED_LESSONS, current).apply()
            addXp(context, 25)
        }
    }

    fun completedCount(context: Context, language: String, lessonIds: List<String>): Int {
        return lessonIds.count { isLessonCompleted(context, language, it) }
    }

    fun isExerciseCompleted(context: Context, language: String, exerciseId: String): Boolean {
        return prefs(context).getStringSet(COMPLETED_EXERCISES, emptySet())
            ?.contains(key(language, exerciseId)) == true
    }

    fun completeExercise(context: Context, language: String, exerciseId: String): Boolean {
        val p = prefs(context)
        val current = p.getStringSet(COMPLETED_EXERCISES, emptySet())?.toMutableSet() ?: mutableSetOf()
        if (current.add(key(language, exerciseId))) {
            p.edit().putStringSet(COMPLETED_EXERCISES, current).apply()
            addXp(context, 25)
            return true
        }
        return false
    }

    fun isProjectCompleted(context: Context, project: Int): Boolean =
        prefs(context).getStringSet(COMPLETED_PROJECTS, emptySet())?.contains(project.toString()) == true

    fun completeProject(context: Context, project: Int): Boolean {
        val p = prefs(context)
        val current = p.getStringSet(COMPLETED_PROJECTS, emptySet())?.toMutableSet() ?: mutableSetOf()
        if (current.add(project.toString())) {
            p.edit().putStringSet(COMPLETED_PROJECTS, current).apply()
            addXp(context, 100)
            return true
        }
        return false
    }

    fun completedExerciseCount(context: Context, language: String, lessonIds: List<String>): Int {
        return lessonIds.count { isExerciseCompleted(context, language, it) }
    }
}
