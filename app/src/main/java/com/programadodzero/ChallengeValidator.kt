package com.programadodzero

/**
 * Regras pedagógicas das práticas de Python.
 * A validação combina execução real com critérios do objetivo de cada aula.
 */
object ChallengeValidator {

    fun validate(lesson: Int, code: String): Boolean {
        if (code.isBlank()) return false

        val execution = when (lesson) {
            7 -> PythonRunner.run(code, listOf("Ana"))
            else -> PythonRunner.run(code)
        }

        if (!execution.success) return false

        return when (lesson) {
            0 -> execution.output.trim() == "Olá, mundo!"
            1 -> hasAssignment(code) && execution.output.isNotBlank()
            2 -> hasTextAndNumberAssignment(code)
            3 -> hasAdultCondition(code) && execution.output.isNotBlank()
            4 -> execution.output.trim() == "0\\n1\\n2\\n3\\n4"
            5 -> hasFunction(code) && execution.output.trim() == "Olá, Ana"
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

    private fun hasAdultCondition(code: String): Boolean =
        code.lines().any { line ->
            val clean = line.trim().replace(" ", "")
            clean.startsWith("if") &&
                (clean.contains(">=18") || clean.contains("18<=") || clean.contains("18<=idade")) &&
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

    private fun hasInputConditionAndOutput(code: String): Boolean =
        code.contains("input(") &&
            code.lines().any { it.trim().startsWith("if ") && it.contains(":") } &&
            code.contains("print(")
}
