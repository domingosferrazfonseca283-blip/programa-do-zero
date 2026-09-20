package com.programadodzero

object PythonRunner {
    data class Result(
        val success: Boolean,
        val output: String,
        val needsInput: Boolean = false,
        val inputPrompt: String = ""
    )

    private class InputRequired(val prompt: String) : Exception()
    private class ReturnValue(val value: String) : Exception()
    private data class FunctionDef(val parameters: List<String>, val body: List<String>)

    fun run(code: String, inputs: List<String> = emptyList()): Result {
        val variables = mutableMapOf<String, String>()
        val output = mutableListOf<String>()
        val inputIndex = intArrayOf(0)
        val lines = code.lines()
        val functions = mutableMapOf<String, FunctionDef>()

        return try {
            executeBlock(lines, 0, lines.size, 0, variables, output, inputs, inputIndex, functions)
            Result(
                true,
                if (output.isEmpty()) "Código executado sem saída." else output.joinToString("\n")
            )
        } catch (e: InputRequired) {
            Result(false, "", true, e.prompt)
        } catch (e: Exception) {
            Result(false, e.message ?: "Erro desconhecido.")
        }
    }

    private fun executeBlock(
        lines: List<String>,
        start: Int,
        end: Int,
        indent: Int,
        variables: MutableMap<String, String>,
        output: MutableList<String>,
        inputs: List<String>,
        inputIndex: IntArray,
        functions: MutableMap<String, FunctionDef>
    ): Int {
        var i = start
        while (i < end) {
            val raw = lines[i]
            if (raw.trim().isBlank() || raw.trim().startsWith("#")) {
                i++
                continue
            }

            val currentIndent = indentation(raw)
            if (currentIndent < indent) return i
            if (currentIndent > indent) {
                throw IllegalArgumentException("Linha " + (i + 1) + ": indentação inesperada.")
            }

            val line = raw.trim()

            if (line.startsWith("def ") && line.endsWith(":")) {\n                val match = Regex("def\\s+([A-Za-z_][A-Za-z0-9_]*)\\(([^)]*)\\):").matchEntire(line)\n                    ?: throw IllegalArgumentException("Linha " + (i + 1) + ": use def nome(parametro):")\n                val name = match.groupValues[1]\n                val parameters = match.groupValues[2].split(",").map { it.trim() }.filter { it.isNotBlank() }\n                var cursor = i + 1\n                while (cursor < end) {\n                    if (lines[cursor].trim().isBlank() || lines[cursor].trim().startsWith("#")) { cursor++; continue }\n                    if (indentation(lines[cursor]) <= indent) break\n                    cursor++\n                }\n                functions[name] = FunctionDef(parameters, lines.subList(i + 1, cursor).map { it.drop(minOf(it.length, indent + 4)) })\n                i = cursor\n                continue\n            }\n            if (line.startsWith("for ") && line.endsWith(":")) {
                val match = Regex("for\\s+([A-Za-z_][A-Za-z0-9_]*)\\s+in\\s+range\\((\\d+)(?:\\s*,\\s*(\\d+))?\\):").matchEntire(line)
                    ?: throw IllegalArgumentException("Linha " + (i + 1) + ": use for variavel in range(inicio, fim):")

                val variable = match.groupValues[1]
                val first = match.groupValues[2].toInt()
                val second = match.groupValues[3].takeIf { it.isNotBlank() }?.toInt() ?: first
                val rangeStart = if (match.groupValues[3].isBlank()) 0 else first
                val rangeEnd = second

                var cursor = i + 1
                while (cursor < end) {
                    if (lines[cursor].trim().isBlank() || lines[cursor].trim().startsWith("#")) {
                        cursor++
                        continue
                    }
                    if (indentation(lines[cursor]) <= indent) break
                    cursor++
                }

                for (value in rangeStart until rangeEnd) {
                    variables[variable] = value.toString()
                    executeBlock(lines, i + 1, cursor, indent + 4, variables, output, inputs, inputIndex, functions)
                }

                i = cursor
                continue
            }

            if (line.startsWith("if ") && line.endsWith(":")) {
                val branches = mutableListOf<Pair<String?, Pair<Int, Int>>>()
                var branchStart = i
                var branchCondition: String? = line.removePrefix("if ").removeSuffix(":").trim()
                var cursor = i + 1

                while (cursor < end) {
                    if (lines[cursor].trim().isBlank() || lines[cursor].trim().startsWith("#")) {
                        cursor++
                        continue
                    }
                    val ci = indentation(lines[cursor])
                    if (ci <= indent) break
                    cursor++
                }

                branches.add(branchCondition to (i + 1 to cursor))
                var scan = cursor
                var elseRange: Pair<Int, Int>? = null

                while (scan < end) {
                    if (lines[scan].trim().isBlank() || lines[scan].trim().startsWith("#")) {
                        scan++
                        continue
                    }
                    val si = indentation(lines[scan])
                    val st = lines[scan].trim()
                    if (si != indent) break

                    if (st.startsWith("elif ") && st.endsWith(":")) {
                        val cond = st.removePrefix("elif ").removeSuffix(":").trim()
                        val bs = scan + 1
                        var be = bs
                        while (be < end) {
                            if (lines[be].trim().isBlank() || lines[be].trim().startsWith("#")) {
                                be++
                                continue
                            }
                            if (indentation(lines[be]) <= indent) break
                            be++
                        }
                        branches.add(cond to (bs to be))
                        scan = be
                    } else if (st == "else:") {
                        val bs = scan + 1
                        var be = bs
                        while (be < end) {
                            if (lines[be].trim().isBlank() || lines[be].trim().startsWith("#")) {
                                be++
                                continue
                            }
                            if (indentation(lines[be]) <= indent) break
                            be++
                        }
                        elseRange = bs to be
                        scan = be
                        break
                    } else {
                        break
                    }
                }

                var executed = false
                for ((condition, range) in branches) {
                    if (evaluateCondition(condition!!, variables)) {
                        executeBlock(lines, range.first, range.second, indent + 4, variables, output, inputs, inputIndex, functions)
                        executed = true
                        break
                    }
                }
                if (!executed && elseRange != null) {
                    executeBlock(lines, elseRange.first, elseRange.second, indent + 4, variables, output, inputs, inputIndex, functions)
                }

                i = scan
                continue
            }

            if (line.startsWith("elif ") || line == "else:") {
                return i
            }

            val callMatch = Regex("([A-Za-z_][A-Za-z0-9_]*)\\((.*)\\)").matchEntire(line)\n            if (callMatch != null && functions.containsKey(callMatch.groupValues[1])) {\n                callFunction(callMatch.groupValues[1], callMatch.groupValues[2], functions, variables, output, inputs, inputIndex)\n                i++\n                continue\n            }\n\n            if (line.startsWith("return ")) {\n                throw ReturnValue(evaluate(line.removePrefix("return ").trim(), variables, inputs, inputIndex, functions))\n            }\n\n            if (line.startsWith("print(") && line.endsWith(")")) {
                val expression = line.removePrefix("print(").removeSuffix(")")
                output.add(evaluate(expression, variables, inputs, inputIndex))
                i++
                continue
            }

            if (line.matches(Regex("[A-Za-z_][A-Za-z0-9_]*\\s*=\\s*.+"))) {
                val parts = line.split("=", limit = 2)
                variables[parts[0].trim()] = evaluate(parts[1].trim(), variables, inputs, inputIndex)
                i++
                continue
            }

            throw IllegalArgumentException(
                "Linha " + (i + 1) + ": ainda não consigo executar '" + line + "'."
            )
        }
        return i
    }

    private fun evaluateCondition(condition: String, variables: Map<String, String>): Boolean {
        val operators = listOf("==", "!=", ">=", "<=", ">", "<")
        for (operator in operators) {
            val parts = condition.split(operator, limit = 2)
            if (parts.size == 2) {
                val left = evaluate(parts[0].trim(), variables, emptyList(), intArrayOf(), mutableMapOf())
                val right = evaluate(parts[1].trim(), variables, emptyList(), intArrayOf(), mutableMapOf())
                val leftNumber = left.toIntOrNull()
                val rightNumber = right.toIntOrNull()

                return when (operator) {
                    "==" -> left == right
                    "!=" -> left != right
                    ">" -> if (leftNumber != null && rightNumber != null) leftNumber > rightNumber else left > right
                    "<" -> if (leftNumber != null && rightNumber != null) leftNumber < rightNumber else left < right
                    ">=" -> if (leftNumber != null && rightNumber != null) leftNumber >= rightNumber else left >= right
                    "<=" -> if (leftNumber != null && rightNumber != null) leftNumber <= rightNumber else left <= right
                    else -> false
                }
            }
        }
        val value = evaluate(condition, variables, emptyList(), intArrayOf(), mutableMapOf())
        return value != "0" && value.lowercase() != "false" && value.isNotEmpty()
    }

    private fun evaluate(
        expression: String,
        variables: Map<String, String>,
        inputs: List<String>,
        inputIndex: IntArray
    ): String {
        val value = expression.trim()

        if (value.startsWith("int(") && value.endsWith(")")) {
            return evaluate(value.removePrefix("int(").removeSuffix(")"), variables, inputs, inputIndex, functions).toIntOrNull()?.toString()
                ?: throw IllegalArgumentException("int() precisa receber um número.")
        }

        if (value.startsWith("input(") && value.endsWith(")")) {
            val promptExpression = value.removePrefix("input(").removeSuffix(")").trim()
            val prompt = if (promptExpression.isBlank()) "" else evaluate(promptExpression, variables, inputs, inputIndex, functions)
            if (inputIndex[0] >= inputs.size) throw InputRequired(prompt)
            return inputs[inputIndex[0]++]
        }

        if (value.startsWith("\"") && value.endsWith("\"") && value.length >= 2) {
            return value.substring(1, value.length - 1)
        }
        if (value.startsWith("'") && value.endsWith("'") && value.length >= 2) {
            return value.substring(1, value.length - 1)
        }

        if (value.startsWith("[") && value.endsWith("]")) {
            val inner = value.substring(1, value.length - 1).trim()
            if (inner.isBlank()) return "[]"
            val items = splitArguments(inner).map { evaluate(it, variables, inputs, inputIndex, functions) }
            return "[" + items.joinToString(",") + "]"
        }

        val indexMatch = Regex("([A-Za-z_][A-Za-z0-9_]*)\\[(\\d+)\\]").matchEntire(value)
        if (indexMatch != null) {
            val listValue = variables[indexMatch.groupValues[1]]
                ?: throw IllegalArgumentException("Lista não encontrada: " + indexMatch.groupValues[1])
            val items = listValue.removePrefix("[").removeSuffix("]")
                .split(",").map { it.trim() }
            val index = indexMatch.groupValues[2].toInt()
            if (index !in items.indices) throw IllegalArgumentException("Índice fora da lista.")
            return items[index]
        }

        variables[value]?.let { return it }
        if (value.matches(Regex("-?\\d+"))) return value

        val pieces = value.split("+").map { it.trim() }
        if (pieces.size > 1) {
            return pieces.joinToString("") { evaluate(it, variables, inputs, inputIndex) }
        }

        throw IllegalArgumentException("Não entendi a expressão: " + value)
    }

    private fun callFunction(name: String, argumentsText: String, functions: MutableMap<String, FunctionDef>, variables: MutableMap<String, String>, output: MutableList<String>, inputs: List<String>, inputIndex: IntArray): String {\n        val function = functions[name] ?: throw IllegalArgumentException("Função não encontrada: " + name)\n        val arguments = if (argumentsText.isBlank()) emptyList() else splitArguments(argumentsText)\n        if (arguments.size != function.parameters.size) throw IllegalArgumentException("Quantidade de argumentos inválida para " + name)\n        val local = variables.toMutableMap()\n        function.parameters.forEachIndexed { index, parameter -> local[parameter] = evaluate(arguments[index], variables, inputs, inputIndex, functions) }\n        return try {\n            executeBlock(function.body, 0, function.body.size, 0, local, output, inputs, inputIndex, functions)\n            ""\n        } catch (e: ReturnValue) { e.value }\n    }\n    private fun splitArguments(text: String): List<String> {
        val result = mutableListOf<String>()
        var current = StringBuilder()
        var quote: Char? = null
        for (char in text) {
            if ((char == '\\'' || char == '"') && (quote == null || quote == char)) {
                quote = if (quote == null) char else null
            }
            if (char == ',' && quote == null) {
                result.add(current.toString().trim())
                current = StringBuilder()
            } else {
                current.append(char)
            }
        }
        if (current.isNotBlank()) result.add(current.toString().trim())
        return result
    }

    private fun indentation(line: String): Int {
        return line.takeWhile { it == ' ' }.length
    }
}
