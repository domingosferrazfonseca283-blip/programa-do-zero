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


    @Test
    fun escreveLeReabreArquivoVirtual() {
        val result = PythonRunner.run(
            """
            arquivo = open("dados.txt", "w")
            arquivo.write("Olá, arquivo!")
            arquivo.close()
            arquivo = open("dados.txt", "r")
            print(arquivo.read())
            arquivo.close()
            """.trimIndent()
        )
        assertTrue(result.success)
        assertEquals("Olá, arquivo!", result.output)
    }

    @Test
    fun withOpenFechaArquivoAutomaticamente() {
        val result = PythonRunner.run(
            """
            with open("dados.txt", "w") as arquivo:
                arquivo.write("Olá, with!")

            with open("dados.txt", "r") as arquivo:
                print(arquivo.read())
            """.trimIndent()
        )
        assertTrue(result.success)
        assertEquals("Olá, with!", result.output)
    }

    @Test
    fun arquivoSomenteLeituraNaoAceitaWrite() {
        val result = PythonRunner.run(
            """
            arquivo = open("dados.txt", "r")
            arquivo.write("erro")
            """.trimIndent()
        )
        assertFalse(result.success)
    }

    @Test
    fun arquivoFechadoNaoPodeSerUsado() {
        val result = PythonRunner.run(
            """
            arquivo = open("dados.txt", "w")
            arquivo.close()
            arquivo.write("erro")
            """.trimIndent()
        )
        assertFalse(result.success)
    }

    @Test
    fun appendPreservaConteudoAnterior() {
        val result = PythonRunner.run(
            """
            arquivo = open("dados.txt", "w")
            arquivo.write("Olá")
            arquivo.close()

            arquivo = open("dados.txt", "a")
            arquivo.write(", mundo!")
            arquivo.close()

            arquivo = open("dados.txt", "r")
            print(arquivo.read())
            """.trimIndent()
        )
        assertTrue(result.success)
        assertEquals("Olá, mundo!", result.output)
    }

    @Test
    fun classePessoaExecutaMetodo() {
        val result = PythonRunner.run(
            """
            class Pessoa:
                def __init__(self, nome):
                    self.nome = nome

                def apresentar(self):
                    print(self.nome)

            pessoa = Pessoa("Ana")
            pessoa.apresentar()
            """.trimIndent()
        )
        assertTrue(result.success)
        assertEquals("Ana", result.output)
    }


    @Test
    fun desafiosAvancados09A20AceitamSolucoesValidas() {
        val validas = mapOf(
            "python-09" to """
                aluno = {"nome": "Ana", "idade": 20}
                print(aluno["nome"])
            """.trimIndent(),
            "python-10" to """
                nome = "Ana"
                print(nome.upper())
            """.trimIndent(),
            "python-11" to """
                try:
                    numero = int("abc")
                except:
                    print("Entrada inválida")
            """.trimIndent(),
            "python-12" to """
                with open("dados.txt", "w") as arquivo:
                    arquivo.write("Olá, arquivo!")
                with open("dados.txt", "r") as arquivo:
                    print(arquivo.read())
            """.trimIndent(),
            "python-13" to """
                class Pessoa:
                    def __init__(self, nome):
                        self.nome = nome
                    def apresentar(self):
                        print(self.nome)
                pessoa = Pessoa("Ana")
                pessoa.apresentar()
            """.trimIndent(),
            "python-14" to """
                class Conta:
                    def __init__(self, saldo):
                        self.saldo = saldo
                    def depositar(self, valor):
                        self.saldo = self.saldo + valor
                conta = Conta(100)
                conta.depositar(50)
                print(conta.saldo)
            """.trimIndent(),
            "python-15" to """
                total = 0
                for numero in range(1, 6):
                    total = total + numero
                print(total)
            """.trimIndent(),
            "python-16" to """
                alvo = 3
                for numero in range(1, 6):
                    if numero == alvo:
                        print("Encontrado")
            """.trimIndent(),
            "python-17" to """
                idade = int(input("Idade: "))
                if idade >= 0:
                    print("Válida")
                else:
                    print("Inválida")
            """.trimIndent(),
            "python-18" to """
                def dobro(numero):
                    return numero + numero
                print(dobro(5))
            """.trimIndent(),
            "python-19" to """
                for numero in range(5):
                    print(numero)
            """.trimIndent(),
            "python-20" to """
                def calcular_total(preco, quantidade):
                    return preco * quantidade
                print(calcular_total(10, 3))
            """.trimIndent()
        )

        validas.forEach { (id, code) ->
            assertTrue("Falhou: $id", ChallengeValidator.validate(id, code))
        }
    }

    @Test
    fun desafiosAvancadosRejeitamSolucoesIncompletas() {
        assertFalse(
            ChallengeValidator.validate(
                "python-09",
                """
                aluno = {"nome": "Ana", "idade": 20}
                print(aluno)
                """.trimIndent()
            )
        )
        assertFalse(
            ChallengeValidator.validate(
                "python-12",
                """
                arquivo = open("dados.txt", "w")
                arquivo.write("outra mensagem")
                """.trimIndent()
            )
        )
        assertFalse(
            ChallengeValidator.validate(
                "python-16",
                """
                alvo = 3
                for numero in range(1, 6):
                    print("Encontrado")
                """.trimIndent()
            )
        )
        assertFalse(
            ChallengeValidator.validate(
                "python-17",
                """
                idade = int(input("Idade: "))
                print("Válida")
                """.trimIndent()
            )
        )
        assertFalse(
            ChallengeValidator.validate(
                "python-20",
                """
                def calcular_total(preco, quantidade):
                    return 30
                print(calcular_total(10, 3))
                """.trimIndent()
            )
        )
    }


    @Test
    fun avaliacaoFinalSelecionaDezQuestoesEIndicesValidos() {
        val prova = ContentRepository.finalExamFor("🐍  Python")

        assertEquals(10, prova.size)
        assertEquals(10, prova.map { it.id }.distinct().size)

        prova.forEach { question ->
            assertTrue(question.options.isNotEmpty())
            assertTrue(question.answerIndex in question.options.indices)
            assertTrue(question.options[question.answerIndex].isNotBlank())
        }
    }

    @Test
    fun avaliacaoFinalMantemAsMesmasQuestoesNoMesmoDia() {
        val primeira = ContentRepository.finalExamFor("🐍  Python")
        val segunda = ContentRepository.finalExamFor("🐍  Python")

        assertEquals(primeira.map { it.id }, segunda.map { it.id })
        assertEquals(
            primeira.map { it.answerIndex to it.options },
            segunda.map { it.answerIndex to it.options }
        )
    }

}
