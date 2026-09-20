package com.programadodzero

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tela = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(40, 60, 40, 40)
            setBackgroundColor(Color.rgb(15, 23, 42))
        }

        val titulo = TextView(this).apply {
            text = "👨‍💻 Programação do Zero"
            textSize = 28f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
        }

        val subtitulo = TextView(this).apply {
            text = "Escolha uma linguagem para começar sua jornada"
            textSize = 18f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 20, 0, 24)
        }

        tela.addView(titulo, LinearLayout.LayoutParams(-1, -2))
        tela.addView(subtitulo, LinearLayout.LayoutParams(-1, -2))

        val progresso = TextView(this).apply {
            text = "⭐ ${ProgressManager.getXp(this@MainActivity)} XP"
            textSize = 17f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 20)
        }
        tela.addView(progresso, LinearLayout.LayoutParams(-1, -2))

        val biblioteca = Button(this).apply {
            gravity = Gravity.CENTER
            includeFontPadding = false
            setPadding(16, 10, 16, 10)
            text = "📚 Biblioteca offline"
            textSize = 16f
            isAllCaps = false
            setOnClickListener {
                startActivity(Intent(this@MainActivity, LibraryActivity::class.java))
            }
        }
        tela.addView(biblioteca, LinearLayout.LayoutParams(-1, -2).apply {
            setMargins(0, 0, 0, 10)
        })

        val pratica = Button(this).apply {
            gravity = Gravity.CENTER
            includeFontPadding = false
            setPadding(16, 10, 16, 10)
            text = "🧪 Aulas práticas"
            textSize = 16f
            isAllCaps = false
            setOnClickListener {
                startActivity(Intent(this@MainActivity, PracticalLessonsActivity::class.java).apply {
                    putExtra("language", "🐍  Python")
                })
            }
        }
        tela.addView(pratica, LinearLayout.LayoutParams(-1, -2).apply {
            setMargins(0, 0, 0, 10)
        })

        val editor = Button(this).apply {
            gravity = Gravity.CENTER
            includeFontPadding = false
            setPadding(16, 10, 16, 10)
            text = "⌨️ Praticar código"
            textSize = 16f
            isAllCaps = false
            setOnClickListener {
                startActivity(Intent(this@MainActivity, CodeEditorActivity::class.java).apply {
                    putExtra("language", "🐍  Python")
                })
            }
        }
        tela.addView(editor, LinearLayout.LayoutParams(-1, -2).apply {
            setMargins(0, 0, 0, 10)
        })

        val perfil = Button(this).apply {
            gravity = Gravity.CENTER
            includeFontPadding = false
            setPadding(16, 10, 16, 10)
            text = "🏆 Meu progresso"
            textSize = 16f
            isAllCaps = false
            setOnClickListener {
                startActivity(Intent(this@MainActivity, ProfileActivity::class.java).apply {
                    putExtra("language", "🐍  Python")
                })
            }
        }
        tela.addView(perfil, LinearLayout.LayoutParams(-1, -2).apply {
            setMargins(0, 0, 0, 14)
        })

        val linguagens = listOf(
            "🐍  Python", "🌐  JavaScript", "🔷  TypeScript", "☕  Java",
            "⚙️  C", "🚀  C++", "🦀  Rust", "🐹  Go"
        )

        for (linguagem in linguagens) {
            val botao = Button(this).apply {
            gravity = Gravity.CENTER
            includeFontPadding = false
            setPadding(16, 10, 16, 10)
                text = linguagem
                textSize = 17f
                isAllCaps = false
                setOnClickListener {
                    startActivity(Intent(this@MainActivity, LevelActivity::class.java).apply {
                        putExtra(LevelActivity.EXTRA_LANGUAGE, linguagem)
                    })
                }
            }
            tela.addView(botao, LinearLayout.LayoutParams(-1, -2).apply {
                setMargins(0, 6, 0, 6)
            })
        }

        setContentView(tela)
    }
}
