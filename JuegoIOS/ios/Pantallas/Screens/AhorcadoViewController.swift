//
//  AhorcadoViewController.swift
//  Pantallas
//
//  Created by Karol Lozano González on 20/09/25.
//
//

import UIKit

class AhorcadoViewController: UIViewController {

    // MARK: - Modelo de juego
    let game = HangmanGame()
    var currentGameNumber: Int = 1
    let totalGames: Int = 5

    // MARK: - Vistas creadas en código
    private let backgroundImageView = UIImageView()
    private let titleImageView = UIImageView()
    private let backImageView = UIImageView()

    private let wordLabel = UILabel()
    private let hintLabel = UILabel()
    private let gameNumberLabel = UILabel()
    private let scoreLabel = UILabel()
    private let attemptsLabel = UILabel()

    private let hangmanImageView = UIImageView()
    private let lettersStack = UIStackView()
    private var letterButtons: [UIButton] = []

    // MARK: - Ciclo de vida

    override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
        startNewRound()
    }

    // MARK: - Configuración de UI (todo en código)

    private func setupUI() {
        view.backgroundColor = .black

        // Fondo
        backgroundImageView.frame = view.bounds
        backgroundImageView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        backgroundImageView.image = UIImage(named: "fondoahorcado")
        backgroundImageView.contentMode = .scaleAspectFill
        view.addSubview(backgroundImageView)

        // Título ("ADIVINA LA VERDURA" o lo que tengas)
        titleImageView.translatesAutoresizingMaskIntoConstraints = false
        titleImageView.image = UIImage(named: "titulo")   // usa el asset que tengas
        titleImageView.contentMode = .scaleAspectFit
        view.addSubview(titleImageView)

        // Flecha para regresar
        backImageView.translatesAutoresizingMaskIntoConstraints = false
        backImageView.image = UIImage(named: "flecha")
        backImageView.contentMode = .scaleAspectFit
        backImageView.isUserInteractionEnabled = true
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(goToNiveles))
        backImageView.addGestureRecognizer(tapGesture)
        view.addSubview(backImageView)

        // Labels de info (juego / puntos / errores)
        gameNumberLabel.translatesAutoresizingMaskIntoConstraints = false
        scoreLabel.translatesAutoresizingMaskIntoConstraints = false
        attemptsLabel.translatesAutoresizingMaskIntoConstraints = false

        [gameNumberLabel, scoreLabel, attemptsLabel].forEach {
            $0.font = UIFont.systemFont(ofSize: 14, weight: .bold)
            $0.textColor = .white
        }

        let infoStack = UIStackView(arrangedSubviews: [gameNumberLabel, scoreLabel, attemptsLabel])
        infoStack.axis = .horizontal
        infoStack.distribution = .equalSpacing
        infoStack.spacing = 8
        infoStack.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(infoStack)

        // Label de palabra
        wordLabel.translatesAutoresizingMaskIntoConstraints = false
        wordLabel.font = UIFont.systemFont(ofSize: 32, weight: .bold)
        wordLabel.textColor = .white
        wordLabel.textAlignment = .center
        view.addSubview(wordLabel)

        // Label de pistas
        hintLabel.translatesAutoresizingMaskIntoConstraints = false
        hintLabel.font = UIFont.systemFont(ofSize: 16, weight: .bold)
        hintLabel.textColor = .systemYellow
        hintLabel.textAlignment = .center
        view.addSubview(hintLabel)

        // Nutri / personaje
        hangmanImageView.translatesAutoresizingMaskIntoConstraints = false
        hangmanImageView.image = UIImage(named: "nutri2") // o "nutri_guess"
        hangmanImageView.contentMode = .scaleAspectFit
        view.addSubview(hangmanImageView)

        // Teclado de letras
        lettersStack.translatesAutoresizingMaskIntoConstraints = false
        lettersStack.axis = .vertical
        lettersStack.distribution = .fillEqually
        lettersStack.spacing = 6
        view.addSubview(lettersStack)

        createKeyboard()

        // --- Constraints ---
        NSLayoutConstraint.activate([
            // Flecha
            backImageView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 16),
            backImageView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 20),
            backImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.15),
            backImageView.heightAnchor.constraint(equalTo: backImageView.widthAnchor),

            // Título
            titleImageView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 16),
            titleImageView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            titleImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.5),
            titleImageView.heightAnchor.constraint(equalTo: titleImageView.widthAnchor, multiplier: 0.7),

            // Info (Juego / Puntos / Errores)
            infoStack.topAnchor.constraint(equalTo: titleImageView.bottomAnchor, constant: 8),
            infoStack.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            infoStack.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),

            // Palabra
            wordLabel.topAnchor.constraint(equalTo: infoStack.bottomAnchor, constant: 12),
            wordLabel.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            wordLabel.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),

            // Pistas
            hintLabel.topAnchor.constraint(equalTo: wordLabel.bottomAnchor, constant: 4),
            hintLabel.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            hintLabel.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),

            // Personaje
            hangmanImageView.topAnchor.constraint(equalTo: hintLabel.bottomAnchor, constant: 12),
            hangmanImageView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            hangmanImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.6),
            hangmanImageView.heightAnchor.constraint(equalTo: hangmanImageView.widthAnchor),

            // Teclado
            lettersStack.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 20),
            lettersStack.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -20),
            lettersStack.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -20),
            lettersStack.heightAnchor.constraint(equalTo: view.heightAnchor, multiplier: 0.30)
        ])
    }

    private func createKeyboard() {
        let alphabet = Array("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
        let rows = 4
        let lettersPerRow = 7
        let customGreen = UIColor(red: 109/255, green: 137/255, blue: 0/255, alpha: 1.0)

        for i in 0..<rows {
            let rowStack = UIStackView()
            rowStack.axis = .horizontal
            rowStack.distribution = .fillEqually
            rowStack.spacing = 6

            for j in 0..<lettersPerRow {
                let index = i * lettersPerRow + j
                if index < alphabet.count {
                    let letter = alphabet[index]
                    let button = UIButton(type: .system)
                    button.setTitle(String(letter), for: .normal)
                    button.titleLabel?.font = UIFont.systemFont(ofSize: 20, weight: .bold)
                    button.setTitleColor(.white, for: .normal)
                    button.backgroundColor = customGreen
                    button.layer.cornerRadius = 6

                    button.addTarget(self, action: #selector(letterTapped(_:)), for: .touchUpInside)

                    rowStack.addArrangedSubview(button)
                    letterButtons.append(button)
                }
            }

            lettersStack.addArrangedSubview(rowStack)
        }
    }

    // MARK: - Juego

    private func startNewRound() {
        let hintText = game.startNewGame()

        gameNumberLabel.text = "Juego: \(currentGameNumber)/\(totalGames)"
        scoreLabel.text = "Puntos: \(game.score)"
        attemptsLabel.text = "Errores: 0/\(game.maxIncorrectGuesses)"
        hintLabel.text = "Pistas: \(hintText)"

        resetKeyboard()           // pone todos los botones en estado base
        updateKeyboardForHints()  // pinta las letras de pista y las bloquea
        updateWordAndImage()
    }

    private func updateWordAndImage() {
        wordLabel.text = game.maskedWord
        attemptsLabel.text = "Errores: \(game.incorrectGuesses)/\(game.maxIncorrectGuesses)"

        // Si algún día quieres varias imágenes por etapa:
        // let stage = min(game.incorrectGuesses, game.maxIncorrectGuesses)
        // let imageName = "hangman_stage_\(stage)"
        // hangmanImageView.image = UIImage(named: imageName)

        if game.isGameOver() {
            setKeyboardEnabled(false)
            handleEndOfRound()
        } else {
            setKeyboardEnabled(true)
        }
    }

    private func resetKeyboard() {
        let customGreen = UIColor(
            red: 109/255,
            green: 137/255,
            blue: 0/255,
            alpha: 1.0
        )

        letterButtons.forEach { button in
            button.isEnabled = true
            button.alpha = 1.0
            button.backgroundColor = customGreen
        }
    }
    
    /// Pinta las letras que son pistas en otro color y las desactiva
    private func updateKeyboardForHints() {
        let customGreen = UIColor(
            red: 109/255,
            green: 137/255,
            blue: 0/255,
            alpha: 1.0
        )
        let hintColor = UIColor.orange   // el color que quieras para las pistas

        letterButtons.forEach { button in
            guard let text = button.titleLabel?.text,
                  let letter = text.first else { return }

            let upper = Character(String(letter).uppercased())

            if game.hintLetters.contains(upper) {
                // Es una letra de pista
                button.isEnabled = false
                button.alpha = 1.0
                button.backgroundColor = hintColor
            } else {
                // Letra normal, se queda con el color base
                button.isEnabled = true
                button.alpha = 1.0
                button.backgroundColor = customGreen
            }
        }
    }

    private func setKeyboardEnabled(_ enabled: Bool) {
        letterButtons.forEach { button in
            button.isEnabled = enabled
            button.alpha = enabled ? 1.0 : 0.5
        }
    }

    // MARK: - Acciones

    @objc private func letterTapped(_ sender: UIButton) {
        guard let text = sender.titleLabel?.text,
              let letter = text.first else { return }

        sender.isEnabled = false
        sender.alpha = 0.5

        let isCorrect = game.guessLetter(letter)
        sender.backgroundColor = isCorrect ? .systemGreen : .systemRed

        updateWordAndImage()
    }

    // MARK: - Fin de ronda / juego

    private func handleEndOfRound() {
        let title = game.isGameWon() ? "¡Ganaste! 🎉" : "Perdiste 😢"
        let message = "La palabra era: \(game.wordToGuess)"

        scoreLabel.text = "Puntos: \(game.score)"

        let alert = UIAlertController(
            title: title,
            message: message,
            preferredStyle: .alert
        )

        alert.addAction(UIAlertAction(
            title: "Siguiente",
            style: .default,
            handler: { [weak self] _ in
                guard let self = self else { return }
                self.currentGameNumber += 1

                if self.currentGameNumber <= self.totalGames {
                    self.startNewRound()
                } else {
                    self.showFinalAlert()
                }
            }))

        present(alert, animated: true)
    }

    private func showFinalAlert() {
        let alert = UIAlertController(
            title: "Fin del juego",
            message: "Puntuación final: \(game.score)",
            preferredStyle: .alert
        )

        alert.addAction(UIAlertAction(
            title: "Jugar otra vez",
            style: .default,
            handler: { [weak self] _ in
                guard let self = self else { return }
                self.currentGameNumber = 1
                self.game.score = 0
                self.startNewRound()
            }))

        alert.addAction(UIAlertAction(
            title: "Salir",
            style: .cancel,
            handler: { [weak self] _ in
                self?.goToNiveles()
            }))

        present(alert, animated: true)
    }

    // MARK: - Navegación

    @objc func goToNiveles() {
        let nivelesVC = NivelesViewController()
        nivelesVC.modalPresentationStyle = .fullScreen
        present(nivelesVC, animated: true, completion: nil)
    }
}
