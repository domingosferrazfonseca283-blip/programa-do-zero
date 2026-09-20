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
    fun aulaDeIdadeTestaEntradasDiferentes() {
        val codigo = """
            idade = int(input("Digite sua idade: "))
            if idade >= 18:
                print("Maior de idade")
            else:
                print("Menor de idade")
        """.trimIndent()

        val menor = PythonRunner.run(codigo, listOf("17"))
        val adulto = PythonRunner.run(codigo, listOf("20"))

        assertTrue(menor.success)
        assertTrue(adulto.success)
        assertEquals("Menor de idade", menor.output)
        assertEquals("Maior de idade", adulto.output)
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
    fun aceitaCondicaoComFormaEquivalenteDeComparacao() {
        assertTrue(
            ChallengeValidator.validate(
                3,
                """
                idade = 20
                if 18 <= idade:
                    print("Adulto")
                """.trimIndent()
            )
        )
    }



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
    fun aulaDeRepeticaoAceitaSequenciaCrescente() {
        assertTrue(
            ChallengeValidator.validate(
                4,
                """
                for numero in range(3, 7):
                    print(numero)
                """.trimIndent()
            )
        )
    }

    @Test
    fun aulaDeRepeticaoRejeitaSequenciaNaoCrescente() {
        assertFalse(
            ChallengeValidator.validate(
                4,
                """
                for numero in range(5):
                    print(5 - numero)
                """.trimIndent()
            )
        )
    }

    @Test
    fun aulaDeRepeticaoAceitaTresOuMaisNumerosCrescentes() {
        assertTrue(ChallengeValidator.validate(4, """for numero in range(3):\n    print(numero)"""))
        assertTrue(ChallengeValidator.validate(4, """for numero in range(4):\n    print(numero)"""))
    }

    @Test
    fun aulaDeFuncaoAceitaNomeDeFuncaoDiferente() {
        assertTrue(
            ChallengeValidator.validate(
                5,
                """
                def cumprimentar(pessoa):
                    return "Bem-vindo, " + pessoa
                print(cumprimentar("Ana"))
                """.trimIndent()
            )
        )
    }

    @Test
    fun aulaDeFuncaoRejeitaFuncaoQueNaoUsaParametro() {
        assertFalse(
            ChallengeValidator.validate(
                5,
                """
                def cumprimentar(pessoa):
                    return "Olá"
                print(cumprimentar("Ana"))
                """.trimIndent()
            )
        )
    }

    @Test
    fun aulaDeFuncaoVerificaQueResultadoMudaComParametro() {
        assertTrue(
            ChallengeValidator.validate(
                5,
                """
                def cumprimentar(nome):
                    return "Olá, " + nome
                print(cumprimentar("Ana"))
                """.trimIndent()
            )
        )
    }

    @Test
    fun aulaDeFuncaoRejeitaSaidaFixaForaDaFuncao() {
        assertFalse(
            ChallengeValidator.validate(
                5,
                """
                def cumprimentar(nome):
                    return "Olá"
                print("Ana")
                """.trimIndent()
            )
        )
    }

    @Test
    fun listaDaAulaSetePrecisaSerUsadaDeVerdade() {
        assertTrue(
            ChallengeValidator.validate(
                6,
                """
                nomes = ["Ana", "Bruno"]
                print(nomes)
                print(nomes[1])
                """.trimIndent()
            )
        )
    }

    @Test
    fun aulaSeteRejeitaListaSemAcessoAoSegundoItem() {
        assertFalse(
            ChallengeValidator.validate(
                6,
                """
                nomes = ["Ana", "Bruno"]
                print(nomes)
                """.trimIndent()
            )
        )
    }

    @Test
    fun aulaSeteRejeitaSegundoItemQueNaoPertenceALista() {
        assertFalse(
            ChallengeValidator.validate(
                6,
                """
                nomes = ["Ana", "Bruno"]
                print(nomes)
                print("Carlos")
                """.trimIndent()
            )
        )
    }

    @Test
    fun aulaSeteRejeitaListaComApenasUmItem() {
        assertFalse(
            ChallengeValidator.validate(
                6,
                """
                nomes = ["Ana"]
                print(nomes)
                print(nomes[1])
                """.trimIndent()
            )
        )
    }

    @Test
    fun projetoFinalTestaComportamentoComEntradasDiferentes() {
        assertTrue(
            ChallengeValidator.validate(
                7,
                """
                nome = input("Nome: ")
                if nome:
                    print("Olá, " + nome)
                else:
                    print("Digite um nome")
                """.trimIndent()
            )
        )
    }

    @Test
    fun projetoFinalRejeitaCondicaoQueNaoMudaOResultado() {
        assertFalse(
            ChallengeValidator.validate(
                7,
                """
                nome = input("Nome: ")
                if nome:
                    print("Pronto")
                else:
                    print("Pronto")
                """.trimIndent()
            )
        )
    }

    @Test
    fun projetoFinalRejeitaProgramaSemCondicaoBaseadaNaEntrada() {
        assertFalse(
            ChallengeValidator.validate(
                7,
                """
                nome = input("Nome: ")
                print("Olá")
                """.trimIndent()
            )
        )
    }
}
