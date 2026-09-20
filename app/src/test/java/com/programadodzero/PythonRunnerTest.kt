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
    fun funcaoComParametroERetornoFunciona() {
        val result = PythonRunner.run(
            """
            def saudar(nome):
                return "Olá, " + nome
            print(saudar("Carlos"))
            """.trimIndent()
        )
        assertTrue(result.success)
        assertEquals("Olá, Carlos", result.output)
    }

    @Test
    fun listaEIndiceFuncionam() {
        val result = PythonRunner.run(
            """
            frutas = ["maçã", "banana"]
            print(frutas)
            print(frutas[1])
            """.trimIndent()
        )
        assertTrue(result.success)
        assertEquals("[maçã, banana]\nbanana", result.output)
    }

    @Test
    fun solucoesComNomesDiferentesContinuamFuncionando() {
        val result = PythonRunner.run(
            """
            pessoa = "João"
            idadeAtual = 21
            if idadeAtual >= 18:
                print("Pode entrar")
            """.trimIndent()
        )
        assertTrue(result.success)
        assertEquals("Pode entrar", result.output)
    }

    @Test
    fun condicoesAndOrENotFuncionam() {
        val result = PythonRunner.run(
            """
            idade = 20
            temDocumento = 1
            bloqueado = 0
            if idade >= 18 and temDocumento == 1:
                print("entrada")
            if idade < 18 or temDocumento == 1:
                print("documento")
            if not bloqueado:
                print("liberado")
            """.trimIndent()
        )
        assertTrue(result.success)
        assertEquals("entrada\ndocumento\nliberado", result.output)
    }

    @Test
    fun valoresBooleanosFuncionamEmCondicoes() {
        val result = PythonRunner.run(
            """
            ativo = True
            bloqueado = False
            if ativo and not bloqueado:
                print("ativo")
            if bloqueado:
                print("não deveria aparecer")
            """.trimIndent()
        )
        assertTrue(result.success)
        assertEquals("ativo", result.output)
    }

    @Test
    fun divisaoPorZeroMostraErro() {
        val result = PythonRunner.run("print(10 / 0)")
        assertFalse(result.success)
        assertTrue(result.output.contains("dividir por zero"))
    }

}
class ChallengeValidatorTest {

    @Test
    fun aceitaSolucaoDaAula1() {
        assertTrue(ChallengeValidator.validate(0, """print("Olá, mundo!")"""))
    }

    @Test
    fun rejeitaAula1SemMensagemEsperada() {
        assertFalse(ChallengeValidator.validate(0, """print("Oi!")"""))
    }

    @Test
    fun aceitaVariavelComNomeDiferente() {
        assertTrue(ChallengeValidator.validate(1, """pessoa = "Ana"\nprint(pessoa)"""))
    }

    @Test
    fun aceitaTextoENumero() {
        assertTrue(ChallengeValidator.validate(2, """nome = "Ana"\nidade = 20"""))
    }

    @Test
    fun rejeitaAula3SemCondicaoDeMaioridade() {
        assertFalse(ChallengeValidator.validate(3, """idade = 20\nprint("adulto")"""))
    }

    @Test
    fun exigeSequenciaDeZeroAQuatro() {
        assertTrue(ChallengeValidator.validate(4, """for numero in range(5):\n    print(numero)"""))
        assertFalse(ChallengeValidator.validate(4, """for numero in range(4):\n    print(numero)"""))
    }

    @Test
    fun exigeFuncaoQueRealmenteProduzaSaudacao() {
        assertTrue(ChallengeValidator.validate(5, """def cumprimentar(nome):\n    return "Olá, " + nome\nprint(cumprimentar("Ana"))"""))
        assertFalse(ChallengeValidator.validate(5, """def cumprimentar(nome):\n    return "Olá"\nprint("Ana")"""))
    }

    @Test
    fun exigeListaComPeloMenosDoisItens() {
        assertTrue(ChallengeValidator.validate(6, """itens = ["a", "b"]\nprint(itens)"""))
        assertFalse(ChallengeValidator.validate(6, """itens = ["a"]\nprint(itens)"""))
    }

    @Test
    fun exigeEntradaCondicaoESaidaNoProjeto() {
        assertTrue(ChallengeValidator.validate(7, """nome = input("Nome: ")\nif nome:\n    print("Olá, " + nome)"""))
        assertFalse(ChallengeValidator.validate(7, """nome = input("Nome: ")\nprint(nome)"""))
    }
}
