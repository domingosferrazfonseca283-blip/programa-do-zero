package com.programadodzero

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PythonRunnerTest {

    @Test
    fun somaNumericaFunciona() {
        val result = PythonRunner.run(
            """
            tentativas = 0
            tentativas = tentativas + 1
            tentativas = tentativas + 1
            print(tentativas)
            """.trimIndent()
        )
        assertTrue(result.success)
        assertEquals("2", result.output)
    }

    @Test
    fun whileRepeteEAtualizaContador() {
        val result = PythonRunner.run(
            """
            tentativas = 0
            while tentativas < 3:
                print(tentativas)
                tentativas = tentativas + 1
            """.trimIndent()
        )
        assertTrue(result.success)
        assertEquals("0\n1\n2", result.output)
    }

    @Test
    fun breakInterrompeWhile() {
        val result = PythonRunner.run(
            """
            numero = 0
            while numero < 10:
                numero = numero + 1
                if numero == 3:
                    break
            print(numero)
            """.trimIndent()
        )
        assertTrue(result.success)
        assertEquals("3", result.output)
    }

    @Test
    fun inputPedeDadosQuandoNecessario() {
        val first = PythonRunner.run(
            """
            palpite = int(input("Digite: "))
            print(palpite)
            """.trimIndent()
        )
        assertFalse(first.success)
        assertTrue(first.needsInput)
        assertEquals("Digite: ", first.inputPrompt)

        val second = PythonRunner.run(
            """
            palpite = int(input("Digite: "))
            print(palpite)
            """.trimIndent(),
            listOf("7")
        )
        assertTrue(second.success)
        assertEquals("7", second.output)
    }

    @Test
    fun condicaoComparaNumeros() {
        val result = PythonRunner.run(
            """
            numero = 7
            if numero > 5:
                print("maior")
            else:
                print("menor")
            """.trimIndent()
        )
        assertTrue(result.success)
        assertEquals("maior", result.output)
    }
    @Test
    fun operacoesMatematicasRespeitamOrdemBasica() {
        val result = PythonRunner.run(
            """
            a = 10 - 3
            b = 4 * 2
            c = 20 / 5
            d = 2 + 3 * 4
            print(a)
            print(b)
            print(c)
            print(d)
            """.trimIndent()
        )
        assertTrue(result.success)
        assertEquals("7\n8\n4\n14", result.output)
    }

    @Test
    fun divisaoPorZeroMostraErro() {
        val result = PythonRunner.run("print(10 / 0)")
        assertFalse(result.success)
        assertTrue(result.output.contains("dividir por zero"))
    }

}

