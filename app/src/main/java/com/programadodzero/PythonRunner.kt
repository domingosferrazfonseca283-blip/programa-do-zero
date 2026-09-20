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
    private class BreakLoop : Exception()
    private class ContinueLoop : Exception()
    private data class FunctionDef(val parameters: List<String>, val body: List<String>)
    private data class ClassDef(val methods: Map<String, FunctionDef>)
    private data class ObjectInstance(val className: String, val attributes: MutableMap<String, String>)
    private data class VirtualFile(val name: String, var content: String = "", var mode: String = "r", var closed: Boolean = false)

    fun run(code: String, inputs: List<String> = emptyList()): Result {
        val variables = mutableMapOf<String, String>()
        val output = mutableListOf<String>()
        val inputIndex = intArrayOf(0)
        val functions = mutableMapOf<String, FunctionDef>()
        val classes = mutableMapOf<String, ClassDef>()
        val objects = mutableMapOf<String, ObjectInstance>()
        val files = mutableMapOf<String, VirtualFile>()
        val lines = code.lines()

        return try {
            executeBlock(lines, 0, lines.size, 0, variables, output, inputs, inputIndex, functions, classes, objects, files)
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
        functions: MutableMap<String, FunctionDef>,
        classes: MutableMap<String, ClassDef>,
        objects: MutableMap<String, ObjectInstance>,
        files: MutableMap<String, VirtualFile>
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

            if (line.startsWith("def ") && line.endsWith(":")) {
                val match = Regex("def\\s+([A-Za-z_][A-Za-z0-9_]*)\\(([^)]*)\\):").matchEntire(line)
                    ?: throw IllegalArgumentException("Linha " + (i + 1) + ": use def nome(parametro):")

                val name = match.groupValues[1]
                val parameters = match.groupValues[2]
                    .split(",")
                    .map { it.trim() }
                    .filter { it.isNotBlank() }

                var cursor = i + 1
                while (cursor < end) {
                    if (lines[cursor].trim().isBlank() || lines[cursor].trim().startsWith("#")) {
                        cursor++
                        continue
                    }
                    if (indentation(lines[cursor]) <= indent) break
                    cursor++
                }

                if (cursor == i + 1) {
                    throw IllegalArgumentException("Linha " + (i + 1) + ": a função precisa ter um bloco indentado.")
                }

                val body = lines.subList(i + 1, cursor).map {
                    it.drop(minOf(it.length, indent + 4))
                }
                functions[name] = FunctionDef(parameters, body)
                i = cursor
                continue
            }

            if (line.startsWith("class ") && line.endsWith(":")) {
                val className = Regex("class\\s+([A-Za-z_][A-Za-z0-9_]*):").matchEntire(line)?.groupValues?.get(1)
                    ?: throw IllegalArgumentException("Linha " + (i + 1) + ": classe inválida.")
                val classEnd = findBlockEnd(lines, i + 1, end, indent)
                val methods = mutableMapOf<String, FunctionDef>()
                var cursor = i + 1
                while (cursor < classEnd) {
                    if (lines[cursor].trim().isBlank() || lines[cursor].trim().startsWith("#")) { cursor++; continue }
                    val m = Regex("def\\s+([A-Za-z_][A-Za-z0-9_]*)\\(([^)]*)\\):").matchEntire(lines[cursor].trim())
                        ?: throw IllegalArgumentException("Linha " + (cursor + 1) + ": método inválido.")
                    val params = m.groupValues[2].split(",").map { it.trim() }.filter { it.isNotBlank() }
                    if (params.firstOrNull() != "self") throw IllegalArgumentException("O método precisa começar com self.")
                    var methodEnd = cursor + 1
                    while (methodEnd < classEnd) {
                        if (lines[methodEnd].trim().isBlank() || lines[methodEnd].trim().startsWith("#")) { methodEnd++; continue }
                        if (indentation(lines[methodEnd]) <= indent + 4) break
                        methodEnd++
                    }
                    methods[m.groupValues[1]] = FunctionDef(params.drop(1), lines.subList(cursor + 1, methodEnd).map { it.drop(minOf(it.length, indent + 8)) })
                    cursor = methodEnd
                }
                classes[className] = ClassDef(methods)
                i = classEnd
                continue
            }

            if (line.startsWith("for ") && line.endsWith(":")) {
                val match = Regex(
                    "for\\s+([A-Za-z_][A-Za-z0-9_]*)\\s+in\\s+range\\((\\d+)(?:\\s*,\\s*(\\d+))?\\):"
                ).matchEntire(line)
                    ?: throw IllegalArgumentException(
                        "Linha " + (i + 1) + ": use for variavel in range(inicio, fim):"
                    )

                val variable = match.groupValues[1]
                val first = match.groupValues[2].toInt()
                val second = match.groupValues[3].takeIf { it.isNotBlank() }?.toInt()

                val rangeStart = if (second == null) 0 else first
                val rangeEnd = second ?: first

                val cursor = findBlockEnd(lines, i + 1, end, indent)

                for (value in rangeStart until rangeEnd) {
                    variables[variable] = value.toString()
                    try {
                        executeBlock(lines, i + 1, cursor, indent + 4, variables, output, inputs, inputIndex, functions, classes, objects, files)
                    } catch (e: BreakLoop) {
                        break
                    } catch (e: ContinueLoop) {
                        continue
                    }
                }

                i = cursor
                continue
            }

            if (line == "try:") {
                val tryEnd = findBlockEnd(lines, i + 1, end, indent)
                var exceptIndex = tryEnd
                while (exceptIndex < end && lines[exceptIndex].trim().isBlank()) exceptIndex++
                if (exceptIndex >= end || indentation(lines[exceptIndex]) != indent ||
                    !lines[exceptIndex].trim().startsWith("except") ||
                    !lines[exceptIndex].trim().endsWith(":")) {
                    throw IllegalArgumentException("Linha " + (i + 1) + ": try precisa de um bloco except.")
                }
                val exceptEnd = findBlockEnd(lines, exceptIndex + 1, end, indent)
                try {
                    executeBlock(lines, i + 1, tryEnd, indent + 4, variables, output, inputs, inputIndex, functions, classes, objects, files)
                } catch (e: InputRequired) {
                    throw e
                } catch (e: BreakLoop) {
                    throw e
                } catch (e: ContinueLoop) {
                    throw e
                } catch (e: ReturnValue) {
                    throw e
                } catch (e: IllegalArgumentException) {
                    executeBlock(lines, exceptIndex + 1, exceptEnd, indent + 4, variables, output, inputs, inputIndex, functions, classes, objects)
                }
                i = exceptEnd
                continue
            }

            if (line.startsWith("while ") && line.endsWith(":")) {
                val condition = line.removePrefix("while ").removeSuffix(":").trim()
                val cursor = findBlockEnd(lines, i + 1, end, indent)
                var iterations = 0

                while (evaluateCondition(condition, variables, inputs, inputIndex, functions, classes, objects)) {
                    if (iterations++ >= 1000) {
                        throw IllegalArgumentException(
                            "Linha " + (i + 1) + ": o while executou muitas vezes. " +
                                "Verifique se a condição termina."
                        )
                    }

                    try {
                        executeBlock(
                            lines, i + 1, cursor, indent + 4,
                            variables, output, inputs, inputIndex, functions, classes, objects
                        )
                    } catch (e: BreakLoop) {
                        break
                    } catch (e: ContinueLoop) {
                        continue
                    }
                }

                i = cursor
                continue
            }

            if (line.startsWith("if ") && line.endsWith(":")) {
                val (nextIndex, executed) = executeIfChain(
                    lines, i, end, indent,
                    variables, output, inputs, inputIndex, functions, classes, objects, files
                )
                i = nextIndex
                if (executed) continue
                continue
            }

            if (line.startsWith("elif ") || line == "else:") {
                return i
            }

            val callMatch = Regex("([A-Za-z_][A-Za-z0-9_]*)\\((.*)\\)").matchEntire(line)
            if (callMatch != null && functions.containsKey(callMatch.groupValues[1])) {
                callFunction(
                    callMatch.groupValues[1],
                    callMatch.groupValues[2],
                    functions,
                    variables,
                    output,
                    inputs,
                    inputIndex,
                    classes,
                    objects
                )
                i++
                continue
            }

            if (line == "break") {
                throw BreakLoop()
            }

            if (line == "continue") {
                throw ContinueLoop()
            }

            val listMethod = Regex("([A-Za-z_][A-Za-z0-9_]*)\\.(append|pop|remove)\\((.*)\\)").matchEntire(line)
            if (listMethod != null) {
                val name = listMethod.groupValues[1]
                val method = listMethod.groupValues[2]
                val argument = listMethod.groupValues[3].trim()
                val rawList = variables[name] ?: throw IllegalArgumentException("Lista não encontrada: " + name)
                if (!rawList.startsWith("[") || !rawList.endsWith("]")) {
                    throw IllegalArgumentException(name + " não é uma lista.")
                }
                val inner = rawList.removePrefix("[").removeSuffix("]").trim()
                val list = if (inner.isBlank()) mutableListOf() else splitArguments(inner).map {
                    evaluate(it, variables, inputs, inputIndex, functions)
                }.toMutableList()
                when (method) {
                    "append" -> {
                        if (argument.isBlank()) throw IllegalArgumentException("append() precisa receber um valor.")
                        list.add(evaluate(argument, variables, inputs, inputIndex, functions))
                    }
                    "remove" -> {
                        val target = evaluate(argument, variables, inputs, inputIndex, functions)
                        if (!list.remove(target)) throw IllegalArgumentException("Valor não encontrado na lista.")
                    }
                    "pop" -> {
                        val index = if (argument.isBlank()) list.lastIndex else
                            evaluate(argument, variables, inputs, inputIndex, functions).toIntOrNull()
                                ?: throw IllegalArgumentException("pop() precisa de um índice inteiro.")
                        if (index !in list.indices) throw IllegalArgumentException("Índice fora da lista.")
                        list.removeAt(index)
                    }
                }
                variables[name] = "[" + list.joinToString(",") + "]"
                i++
                continue
            }

            val fileCall = Regex("([A-Za-z_][A-Za-z0-9_]*)\\.(write|read|close)\\((.*)\\)").matchEntire(line)
            if (fileCall != null) {
                val ref = variables[fileCall.groupValues[1]]
                if (ref != null && files.containsKey(ref)) {
                    val file = files[ref] ?: throw IllegalArgumentException("Arquivo inválido.")
                    if (file.closed) throw IllegalArgumentException("O arquivo está fechado.")
                    when (fileCall.groupValues[2]) {
                        "write" -> {
                            if (file.mode == "r") throw IllegalArgumentException("Arquivo aberto somente para leitura.")
                            file.content += evaluate(fileCall.groupValues[3], variables, inputs, inputIndex, functions, objects, classes, files)
                        }
                        "read" -> output.add(file.content)
                        "close" -> file.closed = true
                    }
                    i++
                    continue
                }
            }

            val methodCall = Regex("([A-Za-z_][A-Za-z0-9_]*)\\.([A-Za-z_][A-Za-z0-9_]*)\\((.*)\\)").matchEntire(line)
            if (methodCall != null) {
                val ref = variables[methodCall.groupValues[1]]
                if (ref != null && objects.containsKey(ref)) {
                    callMethod(
                        ref,
                        methodCall.groupValues[2],
                        methodCall.groupValues[3],
                        variables,
                        objects,
                        classes,
                        output,
                        inputs,
                        inputIndex,
                        functions
                    )
                    i++
                    continue
                }
            }

            if (line.startsWith("return ")) {
                val value = evaluate(
                    line.removePrefix("return ").trim(),
                    variables, inputs, inputIndex, functions, objects, classes
                )
                throw ReturnValue(value)
            }

            if (line.startsWith("print(") && line.endsWith(")")) {
                val expression = line.removePrefix("print(").removeSuffix(")")
                output.add(evaluate(expression, variables, inputs, inputIndex, functions, objects, classes, files))
                i++
                continue
            }

            val attributeAssignment = Regex("([A-Za-z_][A-Za-z0-9_]*)\\.([A-Za-z_][A-Za-z0-9_]*)\\s*=\\s*(.+)").matchEntire(line)
            if (attributeAssignment != null) {
                val ref = variables[attributeAssignment.groupValues[1]]
                    ?: throw IllegalArgumentException("Objeto não encontrado.")
                val obj = objects[ref]
                    ?: throw IllegalArgumentException("Variável não é um objeto.")
                obj.attributes[attributeAssignment.groupValues[2]] =
                    evaluate(attributeAssignment.groupValues[3], variables, inputs, inputIndex, functions, objects, classes, files)
                i++
                continue
            }

            if (line.matches(Regex("[A-Za-z_][A-Za-z0-9_]*\\s*=\\s*.+"))) {
                val parts = line.split("=", limit = 2)
                variables[parts[0].trim()] =
                    evaluate(parts[1].trim(), variables, inputs, inputIndex, functions, objects, classes, files)
                i++
                continue
            }

            throw IllegalArgumentException(
                "Linha " + (i + 1) + ": ainda não consigo executar '" + line + "'."
            )
        }

        return i
    }

    private fun executeIfChain(
        lines: List<String>,
        start: Int,
        end: Int,
        indent: Int,
        variables: MutableMap<String, String>,
        output: MutableList<String>,
        inputs: List<String>,
        inputIndex: IntArray,
        functions: MutableMap<String, FunctionDef>,
        classes: MutableMap<String, ClassDef>,
        objects: MutableMap<String, ObjectInstance>,
        files: MutableMap<String, VirtualFile>
    ): Pair<Int, Boolean> {
        var cursor = start
        var executed = false
        var next = start

        while (cursor < end) {
            val text = lines[cursor].trim()
            if (text.isBlank() || text.startsWith("#")) {
                cursor++
                continue
            }

            val currentIndent = indentation(lines[cursor])
            if (currentIndent != indent) break

            val condition = when {
                text.startsWith("if ") && text.endsWith(":") ->
                    text.removePrefix("if ").removeSuffix(":").trim()
                text.startsWith("elif ") && text.endsWith(":") ->
                    text.removePrefix("elif ").removeSuffix(":").trim()
                else -> null
            }

            if (condition != null) {
                val blockEnd = findBlockEnd(lines, cursor + 1, end, indent)
                if (!executed && evaluateCondition(condition, variables, inputs, inputIndex, functions, classes, objects)) {
                    executeBlock(
                        lines, cursor + 1, blockEnd, indent + 4,
                        variables, output, inputs, inputIndex, functions, classes, objects, files
                    )
                    executed = true
                }
                cursor = blockEnd
                next = cursor
                continue
            }

            if (text == "else:") {
                val blockEnd = findBlockEnd(lines, cursor + 1, end, indent)
                if (!executed) {
                    executeBlock(
                        lines, cursor + 1, blockEnd, indent + 4,
                        variables, output, inputs, inputIndex, functions
                    )
                    executed = true
                }
                cursor = blockEnd
                next = cursor
            }
            break
        }

        return next to executed
    }

    private fun findBlockEnd(lines: List<String>, start: Int, end: Int, parentIndent: Int): Int {
        var cursor = start
        while (cursor < end) {
            val trimmed = lines[cursor].trim()
            if (trimmed.isBlank() || trimmed.startsWith("#")) {
                cursor++
                continue
            }
            if (indentation(lines[cursor]) <= parentIndent) break
            cursor++
        }
        return cursor
    }

    private fun evaluateCondition(
        condition: String,
        variables: Map<String, String>,
        inputs: List<String>,
        inputIndex: IntArray,
        functions: MutableMap<String, FunctionDef>,
        classes: MutableMap<String, ClassDef>,
        objects: MutableMap<String, ObjectInstance>,
        files: MutableMap<String, VirtualFile>
    ): Boolean {
        val text = condition.trim()

        val orParts = splitLogicalOperator(text, "or")
        if (orParts.size > 1) {
            return orParts.any {
                evaluateCondition(it, variables, inputs, inputIndex, functions, classes, objects, files)
            }
        }

        val andParts = splitLogicalOperator(text, "and")
        if (andParts.size > 1) {
            return andParts.all {
                evaluateCondition(it, variables, inputs, inputIndex, functions, classes, objects)
            }
        }

        if (text.startsWith("not ")) {
            return !evaluateCondition(
                text.removePrefix("not ").trim(),
                variables, inputs, inputIndex, functions, classes, objects, files
            )
        }

        val operators = listOf("==", "!=", ">=", "<=", ">", "<")

        for (operator in operators) {
            val parts = text.split(operator, limit = 2)
            if (parts.size == 2) {
                val left = evaluate(parts[0].trim(), variables, inputs, inputIndex, functions, objects, classes, files)
                val right = evaluate(parts[1].trim(), variables, inputs, inputIndex, functions, objects, classes)
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

        val value = evaluate(text, variables, inputs, inputIndex, functions, objects, classes, files)
        return isTruthy(value)
    }

    private fun isTruthy(value: String): Boolean =
        value != "0" && value.lowercase() != "false" && value.isNotEmpty()

    private fun splitLogicalOperator(text: String, operator: String): List<String> {
        val result = mutableListOf<String>()
        var current = StringBuilder()
        var quote: Char? = null
        var depth = 0
        val token = " $operator "

        var index = 0
        while (index < text.length) {
            val char = text[index]

            if ((char == '\'' || char == '"') && (quote == null || quote == char)) {
                quote = if (quote == null) char else null
                current.append(char)
                index++
                continue
            }

            if (quote == null) {
                if (char == '(' || char == '[') depth++
                if (char == ')' || char == ']') depth--

                if (depth == 0 && text.startsWith(token, index)) {
                    result.add(current.toString().trim())
                    current = StringBuilder()
                    index += token.length
                    continue
                }
            }

            current.append(char)
            index++
        }

        result.add(current.toString().trim())
        return if (result.size > 1) result else emptyList()
    }

    private fun evaluate(
        expression: String,
        variables: Map<String, String>,
        inputs: List<String>,
        inputIndex: IntArray,
        functions: MutableMap<String, FunctionDef>,
        objects: MutableMap<String, ObjectInstance> = mutableMapOf(),
        classes: MutableMap<String, ClassDef> = mutableMapOf(),
        files: MutableMap<String, VirtualFile> = mutableMapOf()
    ): String {
        val value = expression.trim()

        val attrRead = Regex("([A-Za-z_][A-Za-z0-9_]*)\.([A-Za-z_][A-Za-z0-9_]*)").matchEntire(value)
        if (attrRead != null) {
            val ref = variables[attrRead.groupValues[1]] ?: throw IllegalArgumentException("Objeto não encontrado.")
            val obj = objects[ref] ?: throw IllegalArgumentException("Variável não é um objeto.")
            return obj.attributes[attrRead.groupValues[2]]
                ?: throw IllegalArgumentException("Atributo não encontrado.")
        }

        val openCall = Regex("open\\((.*)\\)").matchEntire(value)
        if (openCall != null) {
            val args = splitArguments(openCall.groupValues[1])
            if (args.isEmpty()) throw IllegalArgumentException("open() precisa de um nome de arquivo.")
            val name = evaluate(args[0], variables, inputs, inputIndex, functions, objects, classes, files)
            val mode = if (args.size > 1) evaluate(args[1], variables, inputs, inputIndex, functions, objects, classes, files) else "r"
            if (mode !in listOf("r", "w", "a")) throw IllegalArgumentException("Modo de arquivo inválido.")
            val ref = "@file" + files.size
            val existing = files[name]?.content ?: ""
            files[ref] = VirtualFile(name, if (mode == "w") "" else existing, mode)
            return ref
        }

        val classCall = Regex("([A-Za-z_][A-Za-z0-9_]*)\((.*)\)").matchEntire(value)
        if (classCall != null && classes.containsKey(classCall.groupValues[1])) {
            val ref = "@obj" + System.nanoTime()
            objects[ref] = ObjectInstance(classCall.groupValues[1], mutableMapOf())
            val init = classes[classCall.groupValues[1]]?.methods?.get("__init__")
            if (init != null) callMethod(ref, "__init__", classCall.groupValues[2], variables, objects, classes, mutableListOf(), inputs, inputIndex, functions, files)
            return ref
        }

        if (value.startsWith("int(") && value.endsWith(")")) {
            val inner = value.removePrefix("int(").removeSuffix(")")
            return evaluate(inner, variables, inputs, inputIndex, functions).toIntOrNull()?.toString()
                ?: throw IllegalArgumentException("int() precisa receber um número.")
        }

        if (value == "True") return "true"
        if (value == "False") return "false"

        val stringMethod = Regex("""([A-Za-z_][A-Za-z0-9_]*)\.(upper|lower|strip)\(\)""").matchEntire(value)
        if (stringMethod != null) {
            val base = variables[stringMethod.groupValues[1]]
                ?: throw IllegalArgumentException("Variável não encontrada: " + stringMethod.groupValues[1])
            return when (stringMethod.groupValues[2]) {
                "upper" -> base.uppercase()
                "lower" -> base.lowercase()
                "strip" -> base.trim()
                else -> base
            }
        }

        val lenMatch = Regex("""len\(([A-Za-z_][A-Za-z0-9_]*)\)""").matchEntire(value)
        if (lenMatch != null) {
            val base = variables[lenMatch.groupValues[1]]
                ?: throw IllegalArgumentException("Variável não encontrada: " + lenMatch.groupValues[1])
            if (base.startsWith("[") && base.endsWith("]")) {
                val inner = base.removePrefix("[").removeSuffix("]").trim()
                return if (inner.isBlank()) "0" else splitArguments(inner).size.toString()
            }
            if (base.startsWith("{") && base.endsWith("}")) {
                val inner = base.removePrefix("{").removeSuffix("}").trim()
                return if (inner.isBlank()) "0" else splitArguments(inner).size.toString()
            }
            return base.length.toString()
        }

        val dictionaryMatch = Regex("""([A-Za-z_][A-Za-z0-9_]*)\[["']([^"']+)["']\]""").matchEntire(value)
        if (dictionaryMatch != null) {
            val raw = variables[dictionaryMatch.groupValues[1]]
                ?: throw IllegalArgumentException("Dicionário não encontrado: " + dictionaryMatch.groupValues[1])
            val key = dictionaryMatch.groupValues[2]
            val entry = Regex("""["']$key["']\s*:\s*["']([^"']*)["']""").find(raw)
            if (entry != null) return entry.groupValues[1]
            val numeric = Regex("""["']$key["']\s*:\s*(-?\d+)""").find(raw)
            if (numeric != null) return numeric.groupValues[1]
            throw IllegalArgumentException("Chave não encontrada: " + key)
        }

        if (value.startsWith("{") && value.endsWith("}")) {
            val inner = value.substring(1, value.length - 1).trim()
            return if (inner.isBlank()) "{}" else "{" + inner + "}"
        }

        if (value.startsWith("input(") && value.endsWith(")")) {
            val promptExpression = value.removePrefix("input(").removeSuffix(")").trim()
            val prompt = if (promptExpression.isBlank()) {
                ""
            } else {
                evaluate(promptExpression, variables, inputs, inputIndex, functions)
            }

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
            val items = splitArguments(inner).map {
                evaluate(it, variables, inputs, inputIndex, functions)
            }
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

        val functionCall = Regex("([A-Za-z_][A-Za-z0-9_]*)\\((.*)\\)").matchEntire(value)
        if (functionCall != null && functions.containsKey(functionCall.groupValues[1])) {
            return callFunction(
                functionCall.groupValues[1],
                functionCall.groupValues[2],
                functions,
                variables.toMutableMap(),
                mutableListOf(),
                inputs,
                inputIndex,
                classes,
                objects
            )
        }

        variables[value]?.let { return it }
        if (value.matches(Regex("-?\\d+"))) return value

        val arithmetic = evaluateArithmetic(value, variables, inputs, inputIndex, functions, objects, classes, files)
        if (arithmetic != null) return arithmetic

        throw IllegalArgumentException("Não entendi a expressão: " + value)
    }

    
    private fun evaluateArithmetic(
        expression: String,
        variables: Map<String, String>,
        inputs: List<String>,
        inputIndex: IntArray,
        functions: MutableMap<String, FunctionDef>,
        objects: MutableMap<String, ObjectInstance>,
        classes: MutableMap<String, ClassDef>,
        files: MutableMap<String, VirtualFile>
    ): String? {
        val plusMinus = splitOperator(expression, setOf('+', '-'))
        if (plusMinus.size > 1) {
            var result = evaluateArithmetic(
                plusMinus[0].second, variables, inputs, inputIndex, functions, objects, classes, files
            ) ?: evaluate(plusMinus[0].second, variables, inputs, inputIndex, functions, objects, classes)

            for (index in 1 until plusMinus.size) {
                val (operator, term) = plusMinus[index]
                val right = evaluateArithmetic(
                    term, variables, inputs, inputIndex, functions, objects, classes
                ) ?: evaluate(term, variables, inputs, inputIndex, functions, objects, classes)
                val leftNumber = result.toIntOrNull() ?: return null
                val rightNumber = right.toIntOrNull() ?: return null
                result = if (operator == '+') {
                    (leftNumber + rightNumber).toString()
                } else {
                    (leftNumber - rightNumber).toString()
                }
            }
            return result
        }

        val multiplyDivide = splitOperator(expression, setOf('*', '/'))
        if (multiplyDivide.size > 1) {
            var result = evaluateArithmetic(
                multiplyDivide[0].second, variables, inputs, inputIndex, functions, objects, classes
            ) ?: evaluate(multiplyDivide[0].second, variables, inputs, inputIndex, functions, objects, classes)

            for (index in 1 until multiplyDivide.size) {
                val (operator, term) = multiplyDivide[index]
                val right = evaluateArithmetic(
                    term, variables, inputs, inputIndex, functions
                ) ?: evaluate(term, variables, inputs, inputIndex, functions)
                val leftNumber = result.toIntOrNull() ?: return null
                val rightNumber = right.toIntOrNull() ?: return null

                result = when (operator) {
                    '*' -> (leftNumber * rightNumber).toString()
                    '/' -> {
                        if (rightNumber == 0) {
                            throw IllegalArgumentException("Não é possível dividir por zero.")
                        }
                        (leftNumber / rightNumber).toString()
                    }
                    else -> return null
                }
            }
            return result
        }

        return null
    }

    private fun splitOperator(text: String, operators: Set<Char>): List<Pair<Char, String>> {
        val result = mutableListOf<Pair<Char, String>>()
        var current = StringBuilder()
        var quote: Char? = null
        var depth = 0
        var pending: Char? = null

        fun addTerm() {
            val term = current.toString().trim()
            if (term.isNotEmpty()) {
                result.add((pending ?: '+') to term)
                current = StringBuilder()
                pending = null
            }
        }

        for (char in text) {
            if ((char == '\'' || char == '"') && (quote == null || quote == char)) {
                quote = if (quote == null) char else null
                current.append(char)
                continue
            }

            if (quote == null) {
                if (char == '(' || char == '[') depth++
                if (char == ')' || char == ']') depth--

                val unaryMinus = char == '-' && current.toString().trim().isEmpty() &&
                    (result.isEmpty() || pending != null)

                if (depth == 0 && char in operators && !unaryMinus) {
                    addTerm()
                    pending = char
                    continue
                }
            }

            current.append(char)
        }

        addTerm()
        return if (result.size > 1) result else emptyList()
    }

    private fun callFunction(
        name: String,
        argumentsText: String,
        functions: MutableMap<String, FunctionDef>,
        variables: MutableMap<String, String>,
        output: MutableList<String>,
        inputs: List<String>,
        inputIndex: IntArray,
        classes: MutableMap<String, ClassDef>,
        objects: MutableMap<String, ObjectInstance>,
        files: MutableMap<String, VirtualFile>
    ): String {
        val function = functions[name]
            ?: throw IllegalArgumentException("Função não encontrada: " + name)

        val arguments = if (argumentsText.isBlank()) {
            emptyList()
        } else {
            splitArguments(argumentsText)
        }

        if (arguments.size != function.parameters.size) {
            throw IllegalArgumentException("Quantidade de argumentos inválida para " + name)
        }

        val local = variables.toMutableMap()
        function.parameters.forEachIndexed { index, parameter ->
            local[parameter] = evaluate(
                arguments[index], variables, inputs, inputIndex, functions, objects, classes
            )
        }

        return try {
            executeBlock(
                function.body, 0, function.body.size, 0,
                local, output, inputs, inputIndex, functions, classes, objects
            )
            ""
        } catch (e: ReturnValue) {
            e.value
        }
    }

    private fun callMethod(
        ref: String,
        methodName: String,
        argumentsText: String,
        variables: MutableMap<String, String>,
        objects: MutableMap<String, ObjectInstance>,
        classes: MutableMap<String, ClassDef>,
        output: MutableList<String>,
        inputs: List<String>,
        inputIndex: IntArray,
        functions: MutableMap<String, FunctionDef>,
        files: MutableMap<String, VirtualFile>
    ) {
        val obj = objects[ref] ?: throw IllegalArgumentException("Objeto inválido.")
        val method = classes[obj.className]?.methods?.get(methodName)
            ?: throw IllegalArgumentException("Método não encontrado.")
        val args = if (argumentsText.isBlank()) emptyList() else splitArguments(argumentsText)
        if (args.size != method.parameters.size) throw IllegalArgumentException("Quantidade de argumentos inválida.")
        val local = variables.toMutableMap()
        local["self"] = ref
        method.parameters.forEachIndexed { index, parameter ->
            local[parameter] = evaluate(args[index], variables, inputs, inputIndex, functions, objects, classes)
        }
        try {
            executeBlock(method.body, 0, method.body.size, 0, local, output, inputs, inputIndex, functions, classes, objects, files)
        } catch (e: ReturnValue) {
        }
    }

    private fun splitArguments(text: String): List<String> {
        val result = mutableListOf<String>()
        var current = StringBuilder()
        var quote: Char? = null
        var depth = 0

        for (char in text) {
            if ((char == '\'' || char == '"') && (quote == null || quote == char)) {
                quote = if (quote == null) char else null
            }

            if (quote == null) {
                if (char == '(' || char == '[') depth++
                if (char == ')' || char == ']') depth--
            }

            if (char == ',' && quote == null && depth == 0) {
                result.add(current.toString().trim())
                current = StringBuilder()
            } else {
                current.append(char)
            }
        }

        if (current.isNotBlank()) result.add(current.toString().trim())
        return result
    }

    private fun splitPlus(text: String): List<String> {
        val result = mutableListOf<String>()
        var current = StringBuilder()
        var quote: Char? = null

        for (char in text) {
            if ((char == '\'' || char == '"') && (quote == null || quote == char)) {
                quote = if (quote == null) char else null
            }

            if (char == '+' && quote == null) {
                result.add(current.toString().trim())
                current = StringBuilder()
            } else {
                current.append(char)
            }
        }

        result.add(current.toString().trim())
        return result
    }

    private fun indentation(line: String): Int {
        return line.takeWhile { it == ' ' }.length
    }
}
