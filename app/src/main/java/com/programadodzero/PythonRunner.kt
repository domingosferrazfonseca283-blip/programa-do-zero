package com.programadodzero

object PythonRunner {
    data class Result(val success: Boolean, val output: String)

    fun run(code: String): Result {
        val variables = mutableMapOf<String, String>()
        val output = mutableListOf<String>()

        try {
            code.lines().forEachIndexed { lineIndex, raw ->
                val line = raw.trim()
                if (line.isBlank() || line.startsWith("#")) return@forEachIndexed
                when {
                    line.startsWith("print(") && line.endsWith(")") -> {
                        val expression = line.removePrefix("print(").removeSuffix(")")
                        output.add(evaluate(expression, variables))
                    }
                    line.matches(Regex("[A-Za-z_][A-Za-z0-9_]*\\s*=\\s*.+")) -> {
                        val parts = line.split("=", limit = 2)
                        variables[parts[0].trim()] = evaluate(parts[1].trim(), variables)
                    }
                    else -> throw IllegalArgumentException("Linha " + (lineIndex + 1) + ": ainda não consigo executar '" + line + "'.")
                }
            }
            return Result(true, if (output.isEmpty()) "Código executado sem saída." else output.joinToString("\n"))
        } catch (e: Exception) {
            return Result(false, e.message ?: "Erro desconhecido.")
        }
    }

    private fun evaluate(expression: String, variables: Map<String, String>): String {
        val value = expression.trim()
        if (value.startsWith("\"") && value.endsWith("\"") && value.length >= 2) return value.substring(1, value.length - 1)
        if (value.startsWith("'") && value.endsWith("'") && value.length >= 2) return value.substring(1, value.length - 1)
        variables[value]?.let { return it }
        if (value.matches(Regex("-?\\d+"))) return value
        val pieces = value.split("+").map { it.trim() }
        if (pieces.size > 1) return pieces.joinToString("") { evaluate(it, variables) }
        throw IllegalArgumentException("Não entendi a expressão: " + value)
    }
}
