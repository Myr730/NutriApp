//
//  ClasificarViewController.swift
//  Pantallas
//
//  Created by Karol Lozano González on 21/09/25.

import UIKit

class ClasificarViewController: UIViewController {
    
    // MARK: - Tipos de juego
    
    enum BasketType {
        case daily     // DIARIO
        case sometimes // A VECES
        case rare      // OCASIONAL
    }
    
    struct Food {
        let imageName: String
        let basket: BasketType
        let subtitle: String
    }
    
    // MARK: - UI
    
    private let backgroundImageView = UIImageView()
    
    private let backImageView = UIImageView()
    private let pauseImageView = UIImageView()
    
    private let scoreContainer = UIView()
    private let scoreLabel = UILabel()
    
    private let signImageView = UIImageView()
    private let signTitleLabel = UILabel()
    
    private let fruitImageView = UIImageView()
    
    private let feedbackPanel = UIImageView()
    private let feedbackTitleLabel = UILabel()
    private let feedbackSubtitleLabel = UILabel()
    
    private let dailyBasketImageView = UIImageView()
    private let sometimesBasketImageView = UIImageView()
    private let rareBasketImageView = UIImageView()
    
    private let dailyLabel = UILabel()
    private let sometimesLabel = UILabel()
    private let rareLabel = UILabel()
    
    // MARK: - Estado del juego
    
    private var foods: [Food] = []
    private var currentFoodIndex: Int = 0
    private var score: Int = 0
    
    private var originalFruitCenter: CGPoint = .zero
    
    // MARK: - Ciclo de vida
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        view.backgroundColor = .black
        
        setupBackground()
        setupTopBar()
        setupScore()
        setupSign()
        setupFruit()
        setupBaskets()
        setupFeedbackPanel()
        setupFoods()
        loadCurrentFood()
    }
    
    // MARK: - Setup UI
    
    private func setupBackground() {
        backgroundImageView.translatesAutoresizingMaskIntoConstraints = false
        backgroundImageView.image = UIImage(named: "bg_market")
        backgroundImageView.contentMode = .scaleAspectFill
        view.addSubview(backgroundImageView)
        
        NSLayoutConstraint.activate([
            backgroundImageView.topAnchor.constraint(equalTo: view.topAnchor),
            backgroundImageView.bottomAnchor.constraint(equalTo: view.bottomAnchor),
            backgroundImageView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            backgroundImageView.trailingAnchor.constraint(equalTo: view.trailingAnchor)
        ])
    }
    
    private func setupTopBar() {
        backImageView.translatesAutoresizingMaskIntoConstraints = false
        backImageView.image = UIImage(named: "flecha")
        backImageView.contentMode = .scaleAspectFit
        backImageView.isUserInteractionEnabled = true
        view.addSubview(backImageView)
        
        let backTap = UITapGestureRecognizer(target: self, action: #selector(goToNiveles))
        backImageView.addGestureRecognizer(backTap)
        
        pauseImageView.translatesAutoresizingMaskIntoConstraints = false
        pauseImageView.image = UIImage(named: "btn_pause")
        pauseImageView.contentMode = .scaleAspectFit
        view.addSubview(pauseImageView)
        
        NSLayoutConstraint.activate([
            backImageView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 12),
            backImageView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            backImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.15),
            backImageView.heightAnchor.constraint(equalTo: backImageView.widthAnchor),
            
            pauseImageView.centerYAnchor.constraint(equalTo: backImageView.centerYAnchor),
            pauseImageView.leadingAnchor.constraint(equalTo: backImageView.trailingAnchor, constant: 16),
            pauseImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.16),
            pauseImageView.heightAnchor.constraint(equalTo: pauseImageView.widthAnchor)
        ])
    }
    
    private func setupScore() {
        scoreContainer.translatesAutoresizingMaskIntoConstraints = false
        scoreContainer.backgroundColor = UIColor(red: 0/255, green: 150/255, blue: 60/255, alpha: 1.0)
        scoreContainer.layer.cornerRadius = 18
        scoreContainer.clipsToBounds = true
        view.addSubview(scoreContainer)
        
        scoreLabel.translatesAutoresizingMaskIntoConstraints = false
        scoreLabel.textColor = .white
        scoreLabel.font = UIFont.systemFont(ofSize: 18, weight: .bold)
        scoreLabel.textAlignment = .center
        scoreContainer.addSubview(scoreLabel)
        
        NSLayoutConstraint.activate([
            scoreContainer.centerYAnchor.constraint(equalTo: backImageView.centerYAnchor),
            scoreContainer.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -20),
            
            // 🔴 Banner verde más grande
            scoreContainer.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.46),
            scoreContainer.heightAnchor.constraint(equalToConstant: 55),
            
            scoreLabel.leadingAnchor.constraint(equalTo: scoreContainer.leadingAnchor, constant: 12),
            scoreLabel.trailingAnchor.constraint(equalTo: scoreContainer.trailingAnchor, constant: -12),
            scoreLabel.topAnchor.constraint(equalTo: scoreContainer.topAnchor),
            scoreLabel.bottomAnchor.constraint(equalTo: scoreContainer.bottomAnchor)
        ])
        
        updateScoreLabel()
    }
    
    private func setupSign() {
        signImageView.translatesAutoresizingMaskIntoConstraints = false
        signImageView.image = UIImage(named: "sign_wood")
        signImageView.contentMode = .scaleAspectFit
        view.addSubview(signImageView)
        
        signTitleLabel.translatesAutoresizingMaskIntoConstraints = false
        signTitleLabel.text = "CLASIFICA\nEL ALIMENTO"
        signTitleLabel.numberOfLines = 2
        signTitleLabel.textAlignment = .center
        signTitleLabel.textColor = .white
        signTitleLabel.font = UIFont.systemFont(ofSize: 28, weight: .bold)
        signImageView.addSubview(signTitleLabel)
        
        NSLayoutConstraint.activate([
            signImageView.topAnchor.constraint(equalTo: backImageView.bottomAnchor, constant: 16),
            signImageView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            
            // 🔴 Letrero más ancho y un poco más alto
            signImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.92),
            signImageView.heightAnchor.constraint(equalTo: signImageView.widthAnchor, multiplier: 0.40),
            
            signTitleLabel.leadingAnchor.constraint(equalTo: signImageView.leadingAnchor, constant: 10),
            signTitleLabel.trailingAnchor.constraint(equalTo: signImageView.trailingAnchor, constant: -10),
            signTitleLabel.topAnchor.constraint(equalTo: signImageView.topAnchor, constant: 6),
            signTitleLabel.bottomAnchor.constraint(equalTo: signImageView.bottomAnchor, constant: -8)
        ])
    }
    
    private func setupFruit() {
        fruitImageView.translatesAutoresizingMaskIntoConstraints = false
        fruitImageView.contentMode = .scaleAspectFit
        fruitImageView.isUserInteractionEnabled = true
        view.addSubview(fruitImageView)
        
        let pan = UIPanGestureRecognizer(target: self, action: #selector(handleFruitPan(_:)))
        fruitImageView.addGestureRecognizer(pan)
        
        NSLayoutConstraint.activate([
            fruitImageView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            fruitImageView.centerYAnchor.constraint(equalTo: view.centerYAnchor, constant: -40),
            
            // 🔴 Fruta más grande
            fruitImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.60),
            fruitImageView.heightAnchor.constraint(equalTo: fruitImageView.widthAnchor, multiplier: 0.85)
        ])
        
        view.layoutIfNeeded()
        originalFruitCenter = fruitImageView.center
    }
    
    private func setupBaskets() {
        // Baskets
        dailyBasketImageView.translatesAutoresizingMaskIntoConstraints = false
        sometimesBasketImageView.translatesAutoresizingMaskIntoConstraints = false
        rareBasketImageView.translatesAutoresizingMaskIntoConstraints = false
        
        dailyBasketImageView.image = UIImage(named: "basket_daily")
        sometimesBasketImageView.image = UIImage(named: "basket_daily") // misma imagen
        rareBasketImageView.image = UIImage(named: "basket_rare")
        
        dailyBasketImageView.contentMode = .scaleAspectFit
        sometimesBasketImageView.contentMode = .scaleAspectFit
        rareBasketImageView.contentMode = .scaleAspectFit
        
        view.addSubview(dailyBasketImageView)
        view.addSubview(sometimesBasketImageView)
        view.addSubview(rareBasketImageView)
        
        // Labels
        setupBasketLabel(label: dailyLabel, text: "DIARIO")
        setupBasketLabel(label: sometimesLabel, text: "A VECES")
        setupBasketLabel(label: rareLabel, text: "OCASIONAL")
        
        NSLayoutConstraint.activate([
            // Baskets
            dailyBasketImageView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -20),
            sometimesBasketImageView.bottomAnchor.constraint(equalTo: dailyBasketImageView.bottomAnchor),
            rareBasketImageView.bottomAnchor.constraint(equalTo: dailyBasketImageView.bottomAnchor),
            
            dailyBasketImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.23),
            sometimesBasketImageView.widthAnchor.constraint(equalTo: dailyBasketImageView.widthAnchor),
            rareBasketImageView.widthAnchor.constraint(equalTo: dailyBasketImageView.widthAnchor),
            
            dailyBasketImageView.heightAnchor.constraint(equalTo: dailyBasketImageView.widthAnchor, multiplier: 1.2),
            sometimesBasketImageView.heightAnchor.constraint(equalTo: dailyBasketImageView.heightAnchor),
            rareBasketImageView.heightAnchor.constraint(equalTo: dailyBasketImageView.heightAnchor),
            
            dailyBasketImageView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            rareBasketImageView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            sometimesBasketImageView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            
            // Labels sobre cada canasta
            dailyLabel.centerXAnchor.constraint(equalTo: dailyBasketImageView.centerXAnchor),
            dailyLabel.bottomAnchor.constraint(equalTo: dailyBasketImageView.topAnchor, constant: -6),
            
            sometimesLabel.centerXAnchor.constraint(equalTo: sometimesBasketImageView.centerXAnchor),
            sometimesLabel.bottomAnchor.constraint(equalTo: sometimesBasketImageView.topAnchor, constant: -6),
            
            rareLabel.centerXAnchor.constraint(equalTo: rareBasketImageView.centerXAnchor),
            rareLabel.bottomAnchor.constraint(equalTo: rareBasketImageView.topAnchor, constant: -6)
        ])
    }
    
    private func setupBasketLabel(label: UILabel, text: String) {
        label.translatesAutoresizingMaskIntoConstraints = false
        label.text = text
        label.font = UIFont.systemFont(ofSize: 16, weight: .semibold)
        label.textColor = .white
        label.textAlignment = .center
        view.addSubview(label)
    }
    
    private func setupFeedbackPanel() {
        feedbackPanel.translatesAutoresizingMaskIntoConstraints = false
        feedbackPanel.image = UIImage(named: "feedback_panel_success")
        feedbackPanel.contentMode = .scaleAspectFit
        feedbackPanel.isHidden = true
        view.addSubview(feedbackPanel)
        
        feedbackTitleLabel.translatesAutoresizingMaskIntoConstraints = false
        feedbackTitleLabel.textColor = .white
        feedbackTitleLabel.font = UIFont.systemFont(ofSize: 24, weight: .bold)
        feedbackTitleLabel.textAlignment = .center
        feedbackTitleLabel.text = "¡MUY BIEN!"
        feedbackPanel.addSubview(feedbackTitleLabel)
        
        feedbackSubtitleLabel.translatesAutoresizingMaskIntoConstraints = false
        feedbackSubtitleLabel.textColor = UIColor(red: 1.0, green: 1.0, blue: 0.6, alpha: 1.0)
        feedbackSubtitleLabel.font = UIFont.systemFont(ofSize: 18, weight: .semibold)
        feedbackSubtitleLabel.textAlignment = .center
        feedbackPanel.addSubview(feedbackSubtitleLabel)
        
        NSLayoutConstraint.activate([
            feedbackPanel.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            // 🔴 Panel grande sobre las canastas
            feedbackPanel.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -135),
            feedbackPanel.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.92),
            feedbackPanel.heightAnchor.constraint(equalToConstant: 150),
            
            feedbackTitleLabel.topAnchor.constraint(equalTo: feedbackPanel.topAnchor, constant: 14),
            feedbackTitleLabel.leadingAnchor.constraint(equalTo: feedbackPanel.leadingAnchor, constant: 16),
            feedbackTitleLabel.trailingAnchor.constraint(equalTo: feedbackPanel.trailingAnchor, constant: -16),
            
            feedbackSubtitleLabel.topAnchor.constraint(equalTo: feedbackTitleLabel.bottomAnchor, constant: 6),
            feedbackSubtitleLabel.leadingAnchor.constraint(equalTo: feedbackPanel.leadingAnchor, constant: 16),
            feedbackSubtitleLabel.trailingAnchor.constraint(equalTo: feedbackPanel.trailingAnchor, constant: -16),
            feedbackSubtitleLabel.bottomAnchor.constraint(equalTo: feedbackPanel.bottomAnchor, constant: -18)
        ])
    }
    
    // MARK: - Datos del juego
    
    private func setupFoods() {
        // Usa SOLO nombres que sí existen en tus assets
        let allFoods: [Food] = [
            // Frutas diarias
            Food(imageName: "manzana", basket: .daily, subtitle: "La fruta es diaria"),
            Food(imageName: "naranja", basket: .daily, subtitle: "La fruta es diaria"),
            Food(imageName: "uva", basket: .daily, subtitle: "La fruta es diaria"),
            Food(imageName: "frutas1", basket: .daily, subtitle: "Las frutas son diarias"),
            Food(imageName: "frutas2", basket: .daily, subtitle: "Las frutas son diarias"),
            Food(imageName: "frutas3", basket: .daily, subtitle: "Las frutas son diarias"),
            Food(imageName: "frutas4", basket: .daily, subtitle: "Las frutas son diarias"),
            Food(imageName: "frutas5", basket: .daily, subtitle: "Las frutas son diarias"),
            
            // Verduras diarias
            Food(imageName: "verduras1", basket: .daily, subtitle: "Las verduras son diarias"),
            Food(imageName: "verduras2", basket: .daily, subtitle: "Las verduras son diarias"),
            Food(imageName: "verduras3", basket: .daily, subtitle: "Las verduras son diarias"),
            Food(imageName: "verduras4", basket: .daily, subtitle: "Las verduras son diarias"),
            Food(imageName: "verduras5", basket: .daily, subtitle: "Las verduras son diarias"),
            
            // Origen animal - A veces
            Food(imageName: "origenanimal1", basket: .sometimes, subtitle: "Los alimentos de origen animal son a veces"),
            Food(imageName: "origenanimal2", basket: .sometimes, subtitle: "Los alimentos de origen animal son a veces"),
            Food(imageName: "origenanimal3", basket: .sometimes, subtitle: "Los alimentos de origen animal son a veces"),
            Food(imageName: "origenanimal4", basket: .sometimes, subtitle: "Los alimentos de origen animal son a veces"),
            Food(imageName: "origenanimal5", basket: .sometimes, subtitle: "Los alimentos de origen animal son a veces"),
            
            // Postres - Ocasionales
            Food(imageName: "postre1", basket: .rare, subtitle: "Los postres son ocasionales"),
            Food(imageName: "postre2", basket: .rare, subtitle: "Los postres son ocasionales"),
            Food(imageName: "postre3", basket: .rare, subtitle: "Los postres son ocasionales"),
            Food(imageName: "postre4", basket: .rare, subtitle: "Los postres son ocasionales"),
            Food(imageName: "postre5", basket: .rare, subtitle: "Los postres son ocasionales")
        ]
        
        // Escoge 6 aleatorios para que la partida sea cortita
        foods = Array(allFoods.shuffled().prefix(6))
        currentFoodIndex = 0
        score = 0
        updateScoreLabel()
    }
    
    private func loadCurrentFood() {
        guard currentFoodIndex < foods.count else {
            // Fin del juego
            feedbackPanel.isHidden = false
            feedbackTitleLabel.text = "¡Muy bien!"
            feedbackSubtitleLabel.text = "Terminaste la partida 🥳"
            fruitImageView.isHidden = true
            return
        }
        
        let food = foods[currentFoodIndex]
        fruitImageView.image = UIImage(named: food.imageName)
        fruitImageView.isHidden = false
        
        view.layoutIfNeeded()
        originalFruitCenter = fruitImageView.center
        feedbackPanel.isHidden = true
    }
    
    private func updateScoreLabel() {
        scoreLabel.text = "Puntos: \(score)/\(foods.count)"
    }
    
    // MARK: - Gestos
    
    @objc private func handleFruitPan(_ gesture: UIPanGestureRecognizer) {
        guard currentFoodIndex < foods.count else { return }
        
        let translation = gesture.translation(in: view)
        
        switch gesture.state {
        case .began:
            originalFruitCenter = fruitImageView.center
            
        case .changed:
            fruitImageView.center = CGPoint(
                x: originalFruitCenter.x + translation.x,
                y: originalFruitCenter.y + translation.y
            )
            
        case .ended, .cancelled:
            let location = gesture.location(in: view)
            
            let dailyFrame = dailyBasketImageView.frame.insetBy(dx: -10, dy: -10)
            let sometimesFrame = sometimesBasketImageView.frame.insetBy(dx: -10, dy: -10)
            let rareFrame = rareBasketImageView.frame.insetBy(dx: -10, dy: -10)
            
            let targetBasket: BasketType?
            if dailyFrame.contains(location) {
                targetBasket = .daily
            } else if sometimesFrame.contains(location) {
                targetBasket = .sometimes
            } else if rareFrame.contains(location) {
                targetBasket = .rare
            } else {
                targetBasket = nil
            }
            
            if let selected = targetBasket {
                evaluateDrop(on: selected)
            } else {
                // Regresa a su posición
                UIView.animate(withDuration: 0.25) {
                    self.fruitImageView.center = self.originalFruitCenter
                }
            }
            
        default:
            break
        }
    }
    
    private func evaluateDrop(on basket: BasketType) {
        let food = foods[currentFoodIndex]
        let isCorrect = (basket == food.basket)
        
        if isCorrect {
            score += 1
            updateScoreLabel()
            feedbackTitleLabel.text = "¡MUY BIEN!"
        } else {
            feedbackTitleLabel.text = "¡CASI!"
        }
        
        feedbackSubtitleLabel.text = food.subtitle
        feedbackPanel.isHidden = false
        
        UIView.animate(withDuration: 0.2) {
            self.fruitImageView.center = self.originalFruitCenter
        }
        
        // Siguiente alimento después de un pequeño delay
        DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
            self.currentFoodIndex += 1
            self.loadCurrentFood()
        }
    }
    
    // MARK: - Navegación
    
    @objc private func goToNiveles() {
        let nivelesVC = NivelesViewController()
        nivelesVC.modalPresentationStyle = .fullScreen
        present(nivelesVC, animated: true, completion: nil)
    }
}
