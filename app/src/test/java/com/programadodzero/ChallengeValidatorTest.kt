package com.programadodzero

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ChallengeValidatorTest {

    @Test
    fun validaAula1PorId() {
        assertTrue(ChallengeValidator.validate("python-01", """print("Olá, mundo!")"""))
        assertFalse(ChallengeValidator.validate("python-01", """print("Oi!")"""))
    }

    @Test
    fun validaAula4ComExecucaoReal() {
        val codigo = """
            idade = int(input("Idade: "))
            if idade >= 18:
                print("Maior de idade")
            else:
                print("Menor de idade")
        """.trimIndent()

        assertTrue(ChallengeValidator.validate("python-04", codigo))
    }

    @Test
    fun validaAula6ComFuncaoReal() {
        val codigo = """
            def cumprimentar(nome):
                return "Olá, " + nome
            print(cumprimentar("Ana"))
        """.trimIndent()

        assertTrue(ChallengeValidator.validate("python-06", codigo))
    }

    @Test
    fun validaAula7ExigeSegundoItemDaLista() {
        val correta = """
            nomes = ["Ana", "Bruno"]
            print(nomes)
            print(nomes[1])
        """.trimIndent()

        val incorreta = """
            nomes = ["Ana", "Bruno"]
            print(nomes)
        """.trimIndent()

        assertTrue(ChallengeValidator.validate("python-07", correta))
        assertFalse(ChallengeValidator.validate("python-07", incorreta))
    }

    @Test
    fun validaAula12ComArquivoVirtual() {
        val codigo = """
            with open("dados.txt", "w") as arquivo:
                arquivo.write("Olá, arquivo!")

            with open("dados.txt", "r") as arquivo:
                print(arquivo.read())
        """.trimIndent()

        assertTrue(ChallengeValidator.validate("python-12", codigo))
    }

    @Test
    fun validaAula13ComObjetoPessoa() {
        val codigo = """
            class Pessoa:
                def __init__(self, nome):
                    self.nome = nome

            pessoa = Pessoa("Ana")
            print(pessoa.nome)
        """.trimIndent()

        assertTrue(ChallengeValidator.validate("python-13", codigo))
    }

    @Test
    fun validaAula14ComContaEDeposito() {
        val codigo = """
            class Conta:
                def __init__(self, saldo):
                    self.saldo = saldo

                def depositar(self, valor):
                    self.saldo = self.saldo + valor

            conta = Conta(100)
            conta.depositar(50)
            print(conta.saldo)
        """.trimIndent()

        assertTrue(ChallengeValidator.validate("python-14", codigo))
    }
}
