package com.programadodzero

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

class LibraryActivity : Activity() {
    private lateinit var listContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(28, 40, 28, 28)
            setBackgroundColor(Color.rgb(15, 23, 42))
        }

        root.addView(TextView(this).apply {
            text = "📚 Biblioteca Offline"
            textSize = 27f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
        })
        root.addView(TextView(this).apply {
            text = "Livros didáticos originais para estudar sem internet."
            textSize = 16f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 10, 0, 18)
        })

        val search = EditText(this).apply {
            hint = "🔎 Procurar linguagem, livro ou capítulo"
            setSingleLine(true)
            setTextColor(Color.WHITE)
            setHintTextColor(Color.GRAY)
        }
        root.addView(search, LinearLayout.LayoutParams(-1, 60).apply { setMargins(0, 0, 0, 14) })

        val scroll = android.widget.ScrollView(this)
        listContainer = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        scroll.addView(listContainer)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))

        root.addView(Button(this).apply {
            text = "← Voltar"
            isAllCaps = false
            setOnClickListener { finish() }
        }, LinearLayout.LayoutParams(-1, 58).apply { setMargins(0, 12, 0, 0) })

        search.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                render(LibraryRepository.search(s?.toString().orEmpty()))
            }
            override fun afterTextChanged(s: android.text.Editable?) = Unit
        })

        setContentView(root)
        render(LibraryRepository.books)
    }

    private fun render(books: List<LibraryBook>) {
        listContainer.removeAllViews()
        if (books.isEmpty()) {
            listContainer.addView(TextView(this).apply {
                text = "Nenhum livro encontrado."
                textSize = 17f
                setTextColor(Color.LTGRAY)
                setPadding(0, 20, 0, 20)
            })
            return
        }
        for (book in books) {
            listContainer.addView(Button(this).apply {
                text = book.language + "\n📖 " + book.title + "\n" + book.chapters.size + " capítulos"
                textSize = 16f
                isAllCaps = false
                setOnClickListener {
                    startActivity(Intent(this@LibraryActivity, LibraryReaderActivity::class.java).apply {
                        putExtra(LibraryReaderActivity.EXTRA_BOOK_ID, book.id)
                    })
                }
            }, LinearLayout.LayoutParams(-1, 90).apply { setMargins(0, 6, 0, 6) })
        }
    }
}
