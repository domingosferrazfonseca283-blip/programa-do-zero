package com.programadodzero

/**
 * Regras pedagógicas das práticas de Python.
 * A validação combina execução real com critérios do objetivo de cada aula.
 */
object ChallengeValidator {

    fun validate(lesson: Int, code: String): Boolean {
        if (code.isBlank()) return false

        val execution = when (lesson) {
            3 -> null
            7 -> PythonRunner.run(code, listOf("Ana"))
            else -> PythonRunner.run(code)
        }

        if (!execution.success) return false

        return when (lesson) {
            0 -> execution.output.trim() == "Olá, mundo!"
            1 -> hasAssignment(code) && execution.output.isNotBlank()
            2 -> hasTextAndNumberAssignment(code)
            3 -> validateAgeChallenge(code)

            4 -> validateLoopChallenge(code)
            5 -> hasFunction(code) && execution.output.trim().contains("Ana")
            6 -> hasListWithAtLeastTwoItems(code, execution.output)
            7 -> hasInputConditionAndOutput(code) && execution.output.isNotBlank()
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

    private fun hasFunction(code: String): Boolean =
        code.lines().any { it.trim().startsWith("def ") && it.contains("(") && it.contains("):") } &&
            code.contains("return")

    private fun hasListWithAtLeastTwoItems(code: String, output: String): Boolean {
        val listAssignment = code.lines().any { line ->
            val clean = line.trim()
            clean.contains("[") && clean.contains("]") && clean.contains("=")
        }
        val listOutput = output.trim()
        return listAssignment && listOutput.startsWith("[") &&
            listOutput.endsWith("]") && listOutput.contains(",")
    }

    private fun hasInputConditionAndOutput(code: String): Boolean {
        val inputVariable = code.lines()
            .map { it.trim() }
            .mapNotNull { line ->
                val match = Regex("^([A-Za-z_][A-Za-z0-9_]*)\\s*=\\s*input\\(").find(line)
                match?.groupValues?.get(1)
            }
            .firstOrNull()

        return inputVariable != null &&
            code.lines().any { line ->
                val clean = line.trim()
                clean.startsWith("if ") &&
                    clean.contains(":") &&
                    clean.contains(inputVariable)
            } &&
            code.contains("print(")
    }
}
