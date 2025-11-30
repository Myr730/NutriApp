//
//  HangmanGame.swift
//  Pantallas
//
//  Created by Victoria García on 28/11/25.
//

//
//  HangmanGame.swift
//  Pantallas
//
//  Creado por Victoria / Karol
//

import Foundation

// Lista de verduras (igual que en Android)
let vegetableList: [String] = [
    "ZANAHORIA",
    "TOMATE",
    "LECHUGA",
    "CEBOLLA",
    "REPOLLO",
    "BROCOLI",
    "ESPINACA",
    "PEPINO",
    "PIMIENTO",
    "CALABAZA",
    "COLIFLOR",
    "RABANO",
    "APIO",
    "BERENJENA",
    "AJO",
    "MAIZ",
    "CHICHARO",
    "ALCACHOFA",
    "ESPARRAGO",
    "CALABACIN"
]

final class HangmanGame {

    // Palabra actual
    private(set) var wordToGuess: String = ""

    // Letras que el jugador ya seleccionó
    private(set) var guessedLetters: Set<Character> = []

    // Letras de pista (se seleccionan al iniciar la ronda)
    private(set) var hintLetters: [Character] = []

    // Errores
    private(set) var incorrectGuesses: Int = 0
    let maxIncorrectGuesses: Int = 6

    // Score acumulado
    var score: Int = 0

    // Palabra con guiones y letras descubiertas
    var maskedWord: String {
        let upperWord = wordToGuess.uppercased()
        return upperWord.map { ch in
            if ch == " " { return " " }
            return guessedLetters.contains(ch) ? String(ch) : "_"
        }.joined(separator: " ")
    }

    // Inicia una ronda nueva y devuelve el texto de pistas
    @discardableResult
    func startNewGame() -> String {
        wordToGuess = vegetableList.randomElement() ?? "BROCOLI"
        wordToGuess = wordToGuess.uppercased()

        guessedLetters.removeAll()
        hintLetters.removeAll()
        incorrectGuesses = 0

        selectHintLetters()

        // Las pistas cuentan como ya descubiertas
        for h in hintLetters {
            guessedLetters.insert(h)
        }

        return hintLetters.map { String($0) }.joined(separator: ", ")
    }

    private func selectHintLetters() {
        let uniqueLetters = Array(Set(wordToGuess.filter { $0.isLetter }))
        guard !uniqueLetters.isEmpty else { return }

        let shuffled = uniqueLetters.shuffled()
        let count = min(2, shuffled.count)
        hintLetters = Array(shuffled.prefix(count))
    }

    /// Procesa una letra. Devuelve true si está en la palabra.
    @discardableResult
    func guessLetter(_ letter: Character) -> Bool {
        let upper = Character(String(letter).uppercased())

        // Si ya la usamos, no hacemos nada
        if guessedLetters.contains(upper) { return wordToGuess.contains(upper) }

        guessedLetters.insert(upper)

        if wordToGuess.contains(upper) {
            // Letra correcta
            if isGameWon() {
                sumarPuntosPorVictoria()
            }
            return true
        } else {
            // Letra incorrecta
            incorrectGuesses += 1
            return false
        }
    }

    func isGameOver() -> Bool {
        return isGameWon() || incorrectGuesses >= maxIncorrectGuesses
    }

    func isGameWon() -> Bool {
        for ch in wordToGuess where ch.isLetter {
            if !guessedLetters.contains(ch) {
                return false
            }
        }
        return true
    }

    private func sumarPuntosPorVictoria() {
        // Estilo similar al de Android: base + bonus por pocos errores - penalización por pistas
        let basePoints = 50
        let errorBonus = (maxIncorrectGuesses - incorrectGuesses) * 5
        let hintPenalty = hintLetters.count * 5
        let total = basePoints + errorBonus - hintPenalty
        score += max(total, 10)   // mínimo 10 puntos
    }
}
