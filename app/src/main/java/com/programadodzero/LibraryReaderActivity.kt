package com.programadodzero

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class LibraryReaderActivity : Activity() {
    companion object { const val EXTRA_BOOK_ID = "book_id" }

    private lateinit var book: LibraryBook
    private var chapterIndex = 0
    private lateinit var chapterText: TextView
    private lateinit var chapterTitle: TextView
    private lateinit var progress: TextView
    private lateinit var favoriteButton: Button
    private lateinit var courseButton: Button
    private lateinit var relatedLessonButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bookId = intent.getStringExtra(EXTRA_BOOK_ID) ?: run { finish(); return }
        book = LibraryRepository.find(bookId) ?: run { finish(); return }

        val prefs = getSharedPreferences("library", MODE_PRIVATE)
        if (book.chapters.isEmpty()) {
            showEmptyResource()
            return
        }
        chapterIndex = prefs.getInt("chapter_" + bookId, 0).coerceIn(0, book.chapters.lastIndex)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(28, 36, 28, 28)
            setBackgroundColor(Color.rgb(15, 23, 42))
        }
        root.addView(TextView(this).apply {
            text = "📖 " + book.title
            textSize = 24f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
        })
        progress = TextView(this).apply {
            textSize = 15f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 8, 0, 10)
        }
        root.addView(progress)
        root.addView(TextView(this).apply {
            text = "Fonte: " + book.source + "  •  Licença: " + book.license
            textSize = 13f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 10)
        })

        favoriteButton = Button(this).apply {
            gravity = Gravity.CENTER
            includeFontPadding = false
            setPadding(16, 10, 16, 10)
            isAllCaps = false
            setOnClickListener {
                val key = "favorite_" + bookId
                val now = !prefs.getBoolean(key, false)
                prefs.edit().putBoolean(key, now).apply()
                updateFavorite()
            }
        }
        root.addView(favoriteButton, LinearLayout.LayoutParams(-1, 52))

        courseButton = Button(this).apply {
            gravity = Gravity.CENTER
            includeFontPadding = false
            setPadding(16, 10, 16, 10)
            text = "🎓 Ir para a aula da linguagem"
            isAllCaps = false
            setOnClickListener {
                startActivity(Intent(this@LibraryReaderActivity, LevelActivity::class.java).apply {
                    putExtra(LevelActivity.EXTRA_LANGUAGE, book.language)
                })
            }
        }
        root.addView(courseButton, LinearLayout.LayoutParams(-1, 52).apply {
            setMargins(0, 6, 0, 10)
        })

        relatedLessonButton = Button(this).apply { text = "🎓 Ir para a aula relacionada"; isAllCaps = false; setOnClickListener { val id = book.chapters[chapterIndex].relatedLessonId ?: return@setOnClickListener; val lesson = ContentRepository.lessonsFor(book.language).firstOrNull { it.id == id } ?: return@setOnClickListener; startActivity(Intent(this@LibraryReaderActivity, LessonActivity::class.java).apply { putExtra(LessonActivity.EXTRA_LANGUAGE, book.language); putExtra(LessonActivity.EXTRA_LEVEL_NUMBER, lesson.level); putExtra(LessonActivity.EXTRA_MODULE, lesson.module); putExtra(LessonActivity.EXTRA_LESSON_ID, lesson.id) }) } }
        root.addView(relatedLessonButton, LinearLayout.LayoutParams(-1, 52).apply { setMargins(0, 6, 0, 6) })

        chapterTitle = TextView(this).apply {
            textSize = 22f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
            setPadding(0, 16, 0, 12)
        }
        chapterText = TextView(this).apply {
            textSize = 17f
            setTextColor(Color.LTGRAY)
            setLineSpacing(0f, 1.15f)
            setPadding(0, 0, 0, 24)
        }

        val scroll = android.widget.ScrollView(this)
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            addView(chapterTitle)
            addView(chapterText)
        }
        scroll.addView(content)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))

        val navigation = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        val previous = Button(this).apply {
            gravity = Gravity.CENTER
            includeFontPadding = false
            setPadding(16, 10, 16, 10)
            text = "← Anterior"
            isAllCaps = false
            setOnClickListener { if (chapterIndex > 0) { chapterIndex--; saveAndRender() } }
        }
        val next = Button(this).apply {
            gravity = Gravity.CENTER
            includeFontPadding = false
            setPadding(16, 10, 16, 10)
            text = "Próximo →"
            isAllCaps = false
            setOnClickListener { if (chapterIndex < book.chapters.lastIndex) { chapterIndex++; saveAndRender() } }
        }
        navigation.addView(previous, LinearLayout.LayoutParams(0, 56, 1f))
        navigation.addView(next, LinearLayout.LayoutParams(0, 56, 1f))
        root.addView(navigation)
        root.addView(Button(this).apply {
            gravity = Gravity.CENTER
            includeFontPadding = false
            setPadding(16, 10, 16, 10)
            text = "← Biblioteca"
            isAllCaps = false
            setOnClickListener { finish() }
        }, LinearLayout.LayoutParams(-1, 54).apply { setMargins(0, 8, 0, 0) })

        setContentView(root)
        render()
    }

    private fun showEmptyResource() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(28, 40, 28, 28)
            setBackgroundColor(Color.rgb(15, 23, 42))
        }
        root.addView(TextView(this).apply {
            text = "📖 " + book.title
            textSize = 24f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
        })
        root.addView(TextView(this).apply {
            text = "\\nFonte: " + book.source + "\\nLicença: " + book.license + "\\n\\nEste recurso foi catalogado, mas ainda não possui capítulos incorporados ao aplicativo."
            textSize = 17f
            setTextColor(Color.LTGRAY)
            setPadding(0, 24, 0, 24)
        })
        root.addView(Button(this).apply {
            gravity = Gravity.CENTER
            includeFontPadding = false
            setPadding(16, 10, 16, 10)
            text = "← Biblioteca"
            isAllCaps = false
            setOnClickListener { finish() }
        })
        setContentView(root)
    }

    private fun saveAndRender() {
        getSharedPreferences("library", MODE_PRIVATE).edit()
            .putInt("chapter_" + book.id, chapterIndex).apply()
        render()
    }

    private fun render() {
        val chapter = book.chapters[chapterIndex]
        progress.text = book.language + "  •  Capítulo " + (chapterIndex + 1) + "/" + book.chapters.size
        chapterTitle.text = chapter.title
        chapterText.text = chapter.content
        relatedLessonButton.visibility = if (chapter.relatedLessonId != null) android.view.View.VISIBLE else android.view.View.GONE
        updateFavorite()
    }

    private fun updateFavorite() {
        val favorite = getSharedPreferences("library", MODE_PRIVATE)
            .getBoolean("favorite_" + book.id, false)
        favoriteButton.text = if (favorite) "⭐ Favorito" else "☆ Adicionar aos favoritos"
    }
}
