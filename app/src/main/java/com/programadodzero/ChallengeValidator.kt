package com.programadodzero

/**
 * Regras pedagógicas das práticas de Python.
 * A validação combina execução real com critérios do objetivo de cada aula.
 */
object ChallengeValidator {

    fun validate(exerciseId: String, code: String): Boolean {
        if (code.isBlank()) return false

        return when (exerciseId) {
            "python-04" -> validateAgeChallenge(code)
            "python-08" -> validateProjectChallenge(code)
            "python-09" -> validateAdvancedChallenge(exerciseId, code)
            "python-10" -> validateAdvancedChallenge(exerciseId, code)
            "python-11" -> validateAdvancedChallenge(exerciseId, code)
            "python-12" -> validateAdvancedChallenge(exerciseId, code)
            "python-13" -> validateAdvancedChallenge(exerciseId, code)
            "python-14" -> validateAdvancedChallenge(exerciseId, code)
            else -> {
                val execution = PythonRunner.run(code)
                if (!execution.success) return false

                when (exerciseId) {
                    "python-01" -> execution.output.trim() == "Olá, mundo!"
                    "python-02" -> hasAssignment(code) && execution.output.isNotBlank()
                    "python-03" -> hasTextAndNumberAssignment(code)
                    "python-05" -> validateLoopChallenge(code)
                    "python-06" -> validateFunctionChallenge(code)
                    "python-07" -> validateListChallenge(code, execution.output)
                    else -> false
                }
            }
        }
    }

    private fun validateAdvancedChallenge(exerciseId: String, code: String): Boolean {
        return when (exerciseId) {
            "python-09" -> code.contains("{") && code.contains("}") &&
                code.contains("\"nome\"") && Regex("""\[[\"']nome[\"']\]""").containsMatchIn(code)
            "python-10" -> code.contains(".upper()") && code.contains("print(")
            "python-11" -> code.contains("try:") && code.contains("except")
            "python-12" -> code.contains("open(") || (code.contains("dados.txt") && code.contains("#"))
            "python-13" -> code.contains("class Pessoa") &&
                code.contains("__init__") && code.contains("self.nome") &&
                code.contains("Pessoa(")
            "python-14" -> code.contains("class Conta") &&
                code.contains("__init__") && code.contains("self.saldo") &&
                code.contains("Conta(")
            else -> false
        }
    }

    private fun hasAssignment(code: String): Boolean =
        code.lines().any { line ->
            val clean = line.trim()
            clean.isNotEmpty() && !clean.startsWith("#") &&
                clean.contains("=") && !clean.contains("==") &&
                !clean.startsWith("if ") && !clean.startsWith("elif ")
        }

    private fun hasTextAndNumberAssignment(code: String): Boolean {
        val assignments = code.lines()
            .map { it.trim() }
            .filter { it.contains("=") && !it.contains("==") }

        val hasText = assignments.any { it.contains("\"") || it.contains("'") }
        val hasNumber = assignments.any {
            it.substringAfter("=", "").trim().toIntOrNull() != null
        }
        return hasText && hasNumber
    }

    private fun validateAgeChallenge(code: String): Boolean {
        if (!hasAdultCondition(code)) return false

        val minor = PythonRunner.run(code, listOf("17"))
        if (!minor.success || minor.output.isBlank()) return false

        val adult = PythonRunner.run(code, listOf("20"))
        if (!adult.success || adult.output.isBlank()) return false

        return minor.output.trim() != adult.output.trim()
    }

    private fun hasAdultCondition(code: String): Boolean =
        code.lines().any { line ->
            val clean = line.trim().replace(" ", "")
            val isIfLine = clean.startsWith("if") && clean.endsWith(":")
            val comparesWithEighteen = clean.contains(">=18") || clean.contains("18<=")
            isIfLine && comparesWithEighteen
        }

    private fun validateLoopChallenge(code: String): Boolean {
        if (!hasRangeLoop(code)) return false

        val result = PythonRunner.run(code)
        if (!result.success) return false

        val lines = result.output.lines().map { it.trim() }
        if (lines.size < 3) return false

        val numbers = lines.mapNotNull { it.toIntOrNull() }
        if (numbers.size != lines.size) return false

        return numbers.zipWithNext().all { (a, b) -> b == a + 1 }
    }

    private fun hasRangeLoop(code: String): Boolean =
        code.lines().any { line ->
            val clean = line.trim()
            clean.startsWith("for ") &&
                clean.contains("range(") &&
                clean.endsWith(":")
        }

    private fun validateFunctionChallenge(code: String): Boolean {
        val function = Regex(
            """^\s*def\s+([A-Za-z_][A-Za-z0-9_]*)\s*\(\s*([A-Za-z_][A-Za-z0-9_]*)\s*\):\s*$""",
            RegexOption.MULTILINE
        ).find(code) ?: return false

        val functionName = function.groupValues[1]
        val parameter = function.groupValues[2]

        if (!hasFunction(code)) return false

        val functionBody = code.substring(function.range.last + 1)
            .lineSequence()
            .takeWhile { it.isBlank() || it.startsWith(" ") }
            .joinToString("\n")

        if (!functionBody.contains("return")) return false
        if (!Regex("""\breturn\s+.*\b$parameter\b""").containsMatchIn(functionBody)) {
            return false
        }

        val callPattern = Regex(
            """\b$functionName\s*\(\s*["']Ana["']\s*\)"""
        )
        if (!callPattern.containsMatchIn(code)) return false

        val ana = PythonRunner.run(code)
        if (!ana.success || ana.output.isBlank()) return false

        val carlosCode = code
            .replace("\"Ana\"", "\"Carlos\"")
            .replace("'Ana'", "'Carlos'")

        val carlos = PythonRunner.run(carlosCode)
        if (!carlos.success || carlos.output.isBlank()) return false

        return ana.output.trim() != carlos.output.trim() &&
            carlos.output.contains("Carlos")
    }

    private fun hasFunction(code: String): Boolean =
        code.lines().any { it.trim().startsWith("def ") && it.contains("(") && it.contains("):") } &&
            code.contains("return")

    private fun validateListChallenge(code: String, output: String): Boolean {
        val listMatch = code.lines()
            .map { it.trim() }
            .mapNotNull { line ->
                Regex("^([A-Za-z_][A-Za-z0-9_]*)\\s*=\\s*\\[(.*)]$").find(line)
            }
            .firstOrNull { match ->
                match.groupValues[2]
                    .split(",")
                    .count { it.trim().isNotEmpty() } >= 2
            }
            ?: return false

        val variableName = listMatch.groupValues[1]
        val items = listMatch.groupValues[2]
            .split(",")
            .map { it.trim().trim('"', '\'') }
            .filter { it.isNotEmpty() }

        if (items.size < 2) return false

        val secondItemExpression = Regex(
            """\b$variableName\s*\[\s*1\s*]"""
        )

        if (!secondItemExpression.containsMatchIn(code)) return false

        val lines = output.lines()
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        if (lines.size < 2) return false

        val printedList = lines.first()
        val printedSecondItem = lines.last()

        val listLooksCorrect = printedList.startsWith("[") &&
            printedList.endsWith("]") &&
            printedList.contains(",")

        if (!listLooksCorrect) return false

        return printedSecondItem == items[1]
    }


    private fun validateProjectChallenge(code: String): Boolean {
        val inputVariable = code.lines()
            .map { it.trim() }
            .mapNotNull { line ->
                Regex("^([A-Za-z_][A-Za-z0-9_]*)\\s*=\\s*input\\(")
                    .find(line)
                    ?.groupValues
                    ?.get(1)
            }
            .firstOrNull()
            ?: return false

        val hasDecision = code.lines().any { line ->
            val clean = line.trim()
            clean.startsWith("if ") &&
                clean.endsWith(":") &&
                clean.contains(inputVariable)
        }

        if (!hasDecision || !code.contains("print(")) return false

        val withValue = PythonRunner.run(code, listOf("Ana"))
        val withoutValue = PythonRunner.run(code, listOf(""))

        if (!withValue.success || !withoutValue.success) return false
        if (withValue.output.isBlank() || withoutValue.output.isBlank()) return false

        return withValue.output.trim() != withoutValue.output.trim()
}
}
