//
//  LicuadoViewController.swift
//  Pantallas
//
//  Created by Karol Lozano González on 21/09/25.
//

import UIKit
import AVFoundation

class FruitImageView: UIImageView {
    var fruitName: String = ""
}

class LicuadoViewController: UIViewController {
    
    var startingCenters: [UIImageView: CGPoint] = [:]
    var fruitStack: UIStackView!
    var trayTimer: Timer?
    var currentlyDraggedFruit: UIImageView?
    var draggedCopy: UIImageView?
    var originalFruit: UIImageView?
    var fruitStackInBanner: UIStackView!
    var trayView: UIView!
    var bannerImageView: UIImageView!
    var botonLicuadoraButton: UIButton!
    var barraImageView: UIImageView!
    var triangleImageView: UIImageView!
    var triangleIsMoving = false
    var lecheImageView: UIImageView!
    var backgroundImage: UIImageView!
    var triangleLeadingConstraint: NSLayoutConstraint!
    var flechaImageView: UIImageView!
    var siguienteImageView: UIImageView!
    var starsImageView: UIImageView!
    
    var blenderPlayer: AVAudioPlayer?
    private var blenderPlayerPrepared = false
    var milkPlayer: AVAudioPlayer?
    private var milkPlayerPrepared = false
    var shinePlayer: AVAudioPlayer?
    private var shinePlayerPrepared = false
    var botonesPlayer: AVAudioPlayer?
    private var botonesPlayerPrepared = false
    var poingPlayer: AVAudioPlayer?
    private var poingPlayerPrepared = false

    var backgroundMusicPlayer: AVAudioPlayer?
    
    var gameTimer: Timer?
    var timeRemaining: Int = 30
    var timerLabel: UILabel!
    var licuadoFinished: Bool = false
    var hasLost = false
    
    var targetFruits: [String] = []
    var remainingTargets: [String] = []
    var pickedCorrect: [String] = []
    var pickedIncorrect: [String] = []
    var fruitsInsideBlender: [FruitImageView] = []

    
    override func viewDidLoad() {
        prepareBlenderSoundInBackground()
        prepareMilkSoundInBackground()
        prepareShineSoundInBackground()
        prepareBotonesSoundInBackground()
        preparePoingSoundInBackground()
        super.viewDidLoad()
        startBackgroundMusic()

        siguienteImageView?.alpha = 0

        backgroundImage = UIImageView(frame: UIScreen.main.bounds)
        backgroundImage.image = UIImage(named: "fondolicuado2")
        backgroundImage.contentMode = .scaleAspectFill
        self.view.insertSubview(backgroundImage, at: 0)
        
        let licuadoraImageView = UIImageView(image: UIImage(named: "licuadora"))
        licuadoraImageView.translatesAutoresizingMaskIntoConstraints = false
        licuadoraImageView.contentMode = .scaleAspectFit
        licuadoraImageView.alpha = 0
        view.addSubview(licuadoraImageView)

        NSLayoutConstraint.activate([
            licuadoraImageView.centerXAnchor.constraint(equalTo: view.centerXAnchor,constant: -104),
            licuadoraImageView.centerYAnchor.constraint(equalTo: view.centerYAnchor, constant: 35),
            licuadoraImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.45),
            licuadoraImageView.heightAnchor.constraint(equalTo: licuadoraImageView.widthAnchor, multiplier: 0.91)
        ])
        
        lecheImageView = UIImageView(image: UIImage(named: "leche"))
        lecheImageView.translatesAutoresizingMaskIntoConstraints = false
        lecheImageView.contentMode = .scaleAspectFit
        lecheImageView.isUserInteractionEnabled = true
        let lecheTap = UITapGestureRecognizer(target: self, action: #selector(lecheTapped))
        lecheImageView.addGestureRecognizer(lecheTap)
        view.addSubview(lecheImageView)

        NSLayoutConstraint.activate([
            lecheImageView.centerXAnchor.constraint(equalTo: view.centerXAnchor,constant: 124),
            lecheImageView.centerYAnchor.constraint(equalTo: view.centerYAnchor, constant: 175),
            lecheImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.45),
            lecheImageView.heightAnchor.constraint(equalTo: lecheImageView.widthAnchor, multiplier: 0.74)
        ])
        
        botonLicuadoraButton = UIButton(type: .custom)
        botonLicuadoraButton.translatesAutoresizingMaskIntoConstraints = false
        botonLicuadoraButton.setImage(UIImage(named: "botonlicuadora"), for: .normal)
        botonLicuadoraButton.imageView?.contentMode = .scaleAspectFit
        botonLicuadoraButton.isUserInteractionEnabled = false
        botonLicuadoraButton.alpha = 0.5
        view.addSubview(botonLicuadoraButton)

        // tap action
        botonLicuadoraButton.addTarget(self, action: #selector(encenderLicuadora), for: .touchUpInside)

        NSLayoutConstraint.activate([
            botonLicuadoraButton.centerXAnchor.constraint(equalTo: view.centerXAnchor, constant: -124),
            botonLicuadoraButton.centerYAnchor.constraint(equalTo: view.centerYAnchor, constant: 175),
            botonLicuadoraButton.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.45),
            botonLicuadoraButton.heightAnchor.constraint(equalTo: botonLicuadoraButton.widthAnchor, multiplier: 0.28)
        ])
        
        siguienteImageView = UIImageView(image: UIImage(named: "siguientelicuado"))
        siguienteImageView.translatesAutoresizingMaskIntoConstraints = false
        siguienteImageView.contentMode = .scaleAspectFit
        siguienteImageView.alpha = 0
        view.addSubview(siguienteImageView)
        
        NSLayoutConstraint.activate([
            siguienteImageView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: view.bounds.height * 0.025),
            siguienteImageView.centerXAnchor.constraint(equalTo: view.centerXAnchor, constant: 72),
            siguienteImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.5),
            siguienteImageView.heightAnchor.constraint(equalTo: siguienteImageView.widthAnchor, multiplier: 0.3)
        ])
        
        let siguienteTap = UITapGestureRecognizer(target: self, action: #selector(restartGame))
        siguienteImageView.isUserInteractionEnabled = true
        siguienteImageView.addGestureRecognizer(siguienteTap)

        
        flechaImageView = UIImageView(image: UIImage(named: "flecha"))
        flechaImageView.translatesAutoresizingMaskIntoConstraints = false
        flechaImageView.isUserInteractionEnabled = true
        flechaImageView.contentMode = .scaleAspectFit
        view.addSubview(flechaImageView)
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(goToNiveles))
        flechaImageView.addGestureRecognizer(tapGesture)
        
        NSLayoutConstraint.activate([
            flechaImageView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: view.bounds.height * 0.02),
            flechaImageView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 25),
            flechaImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.15),
            flechaImageView.heightAnchor.constraint(equalTo: flechaImageView.widthAnchor)
        ])
       
        
        bannerImageView = UIImageView(image: UIImage(named: "banner"))
        bannerImageView.translatesAutoresizingMaskIntoConstraints = false
        bannerImageView.contentMode = .scaleAspectFit
        view.addSubview(bannerImageView)

        NSLayoutConstraint.activate([
            bannerImageView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: -view.bounds.height * 0.01),
            
            bannerImageView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -30),
            
            bannerImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.5),
            bannerImageView.heightAnchor.constraint(equalTo: bannerImageView.widthAnchor, multiplier: 1.0)
        ])
        fruitStackInBanner = UIStackView()
        fruitStackInBanner.translatesAutoresizingMaskIntoConstraints = false
        fruitStackInBanner.axis = .horizontal
        fruitStackInBanner.alignment = .center
        fruitStackInBanner.distribution = .equalSpacing
        fruitStackInBanner.spacing = 11
        bannerImageView.addSubview(fruitStackInBanner)

        NSLayoutConstraint.activate([
            fruitStackInBanner.centerXAnchor.constraint(equalTo: bannerImageView.centerXAnchor),
            fruitStackInBanner.topAnchor.constraint(equalTo: bannerImageView.topAnchor, constant: 95),
            fruitStackInBanner.leadingAnchor.constraint(greaterThanOrEqualTo: bannerImageView.leadingAnchor, constant: 10),
            fruitStackInBanner.trailingAnchor.constraint(lessThanOrEqualTo: bannerImageView.trailingAnchor, constant: -10)
        ])

        let bannerFruitNames = ["fresa", "uva", "manzana", "banana", "naranja"]
        targetFruits = (0..<3).map { _ in bannerFruitNames.randomElement()! }
        remainingTargets = targetFruits

        for name in targetFruits {
            let fruit = UIImageView(image: UIImage(named: name))
            fruit.tag = 1
            fruit.contentMode = .scaleAspectFit
            fruit.translatesAutoresizingMaskIntoConstraints = false
            NSLayoutConstraint.activate([
                fruit.widthAnchor.constraint(equalToConstant: 50),
                fruit.heightAnchor.constraint(equalToConstant: 50)
            ])
            fruitStackInBanner.addArrangedSubview(fruit)
        }
        trayView = UIView()
                trayView.translatesAutoresizingMaskIntoConstraints = false
                trayView.backgroundColor = UIColor(red: 153/255.0, green: 79/255.0, blue: 21/255.0, alpha: 1.0) // #994F15
                view.addSubview(trayView)
                
                NSLayoutConstraint.activate([
                    trayView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor),
                    trayView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
                    trayView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
                    trayView.heightAnchor.constraint(equalToConstant: 120)
                ])
        
        barraImageView = UIImageView(image: UIImage(named: "barralicuado"))
        barraImageView.translatesAutoresizingMaskIntoConstraints = false
        barraImageView.contentMode = .scaleAspectFit
        barraImageView.alpha = 0
        view.addSubview(barraImageView)

        NSLayoutConstraint.activate([
            barraImageView.centerXAnchor.constraint(equalTo: view.centerXAnchor,constant: 0),
            barraImageView.centerYAnchor.constraint(equalTo: view.centerYAnchor, constant: 345),
            barraImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.75),
            barraImageView.heightAnchor.constraint(equalTo: barraImageView.widthAnchor, multiplier: 1.21)
        ])
        
        triangleImageView = UIImageView(image: UIImage(named: "triangulo"))
        triangleImageView.translatesAutoresizingMaskIntoConstraints = false
        triangleImageView.contentMode = .scaleAspectFit
        triangleImageView.alpha = 0   // hidden at start
        view.addSubview(triangleImageView)
        
        triangleLeadingConstraint = triangleImageView.leadingAnchor.constraint(equalTo: barraImageView.leadingAnchor)

        // Position triangle centered vertically on the bar
        NSLayoutConstraint.activate([
            triangleImageView.centerYAnchor.constraint(equalTo: barraImageView.centerYAnchor),
            triangleImageView.widthAnchor.constraint(equalToConstant: 40),
            triangleImageView.heightAnchor.constraint(equalToConstant: 40),
            // Start at the left of the bar
            triangleLeadingConstraint
        ])
        
        starsImageView = UIImageView(image: UIImage(named: "0stars"))
        starsImageView.translatesAutoresizingMaskIntoConstraints = false
        starsImageView.contentMode = .scaleAspectFit
        starsImageView.alpha = 0
        view.addSubview(starsImageView)
        
        NSLayoutConstraint.activate([
            starsImageView.centerYAnchor.constraint(equalTo: view.centerYAnchor, constant: 355),
            starsImageView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            starsImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.7),
            starsImageView.heightAnchor.constraint(equalTo: starsImageView.widthAnchor, multiplier: 0.5)
        ])
        
        fruitStack = UIStackView()
                fruitStack.translatesAutoresizingMaskIntoConstraints = false
                fruitStack.axis = .horizontal
                fruitStack.alignment = .center
                fruitStack.distribution = .equalSpacing
                fruitStack.spacing = 12
                trayView.addSubview(fruitStack)
                
                NSLayoutConstraint.activate([
                    fruitStack.topAnchor.constraint(equalTo: trayView.topAnchor, constant: 10),
                    fruitStack.bottomAnchor.constraint(equalTo: trayView.bottomAnchor, constant: -10),
                    fruitStack.leadingAnchor.constraint(equalTo: trayView.leadingAnchor, constant: 20),
                    fruitStack.trailingAnchor.constraint(equalTo: trayView.trailingAnchor, constant: -20)
                ])
                
                let trayFruitNames = ["fresa", "uva", "manzana", "banana", "naranja"]
                let randomTrayFruits = (0..<5).map { _ in trayFruitNames.randomElement()! }
                
                for name in randomTrayFruits {
                    let fruit = UIImageView(image: UIImage(named: name))
                    fruit.contentMode = .scaleAspectFit
                    fruit.translatesAutoresizingMaskIntoConstraints = false
                    NSLayoutConstraint.activate([
                        fruit.widthAnchor.constraint(equalToConstant: 60),
                        fruit.heightAnchor.constraint(equalToConstant: 60)
                    ])
                    fruitStack.addArrangedSubview(fruit)
                    fruit.isUserInteractionEnabled = true
                    let pan = UIPanGestureRecognizer(target: self, action: #selector(handleDrag(_:)))
                    fruit.addGestureRecognizer(pan)
                }
        startTrayFruitTimer()
        
        let timerContainer = UIView()
        timerContainer.translatesAutoresizingMaskIntoConstraints = false
        timerContainer.backgroundColor = UIColor.black.withAlphaComponent(0.4)
        timerContainer.layer.cornerRadius = 10
        timerContainer.isUserInteractionEnabled = false   // <- IMPORTANT
        view.addSubview(timerContainer)
        
        timerLabel = UILabel()
        timerLabel.translatesAutoresizingMaskIntoConstraints = false
        timerLabel.textColor = .white
        timerLabel.font = UIFont.systemFont(ofSize: 32, weight: .bold)
        timerLabel.textAlignment = .center
        timerLabel.text = "30"   // initial value
        timerContainer.addSubview(timerLabel)

        NSLayoutConstraint.activate([
            timerContainer.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 90),
            timerContainer.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -290),
            timerContainer.widthAnchor.constraint(equalToConstant: 80),
            timerContainer.heightAnchor.constraint(equalToConstant: 60),
                
            timerLabel.centerXAnchor.constraint(equalTo: timerContainer.centerXAnchor),
            timerLabel.centerYAnchor.constraint(equalTo: timerContainer.centerYAnchor)
        ])

        startGameTimer()
    }
    
    @objc func goToNiveles() {
        AudioManager.shared.playButtonSound()
        gameTimer?.invalidate()
        gameTimer = nil

        trayTimer?.invalidate()
        trayTimer = nil
        backgroundMusicPlayer?.stop()
        backgroundMusicPlayer = nil
        milkPlayer?.stop()
        blenderPlayer?.stop()
        poingPlayer?.stop()

        // 3. STOP TRIANGLE ANIMATION
        triangleImageView.layer.removeAllAnimations()
        triangleIsMoving = false

        // 4. PREVENT "handleGameLost()" from randomly triggering
        hasLost = true
        let nivelesVC = NivelesViewController()
        nivelesVC.modalPresentationStyle = .fullScreen
        present(nivelesVC, animated: true, completion: nil)
    }
    
    @objc func restartGame() {
        AudioManager.shared.playButtonSound()
        resetGame()
    }
    
    @objc func encenderLicuadora() {

        if triangleIsMoving == true {
            playBlenderSound()
            licuadoFinished = true
            gameTimer?.invalidate()
            
            botonLicuadoraButton.isUserInteractionEnabled = false
            botonLicuadoraButton.alpha = 0.5
            
            triangleIsMoving = false
            
            let currentX = triangleImageView.layer.presentation()?.frame.origin.x ?? triangleImageView.frame.origin.x
            triangleImageView.layer.removeAllAnimations()

            let barMinX = barraImageView.frame.minX
            triangleLeadingConstraint.constant = currentX - barMinX

            view.layoutIfNeeded()

            
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.01) {
                self.barraImageView.alpha = 0
                self.triangleImageView.alpha = 0
                
                DispatchQueue.main.asyncAfter(deadline: .now() + 3.0) {
                    self.changeToStatsBackground()
                    self.computeFinalFruitResults()
                }
            }
            
        }
    }
    
    @objc func lecheTapped() {
        playMilkSound()
        // Disable leche so it can't be tapped twice
        lecheImageView.isUserInteractionEnabled = false

        // Disable blender button during the transition
        botonLicuadoraButton.isUserInteractionEnabled = false
        botonLicuadoraButton.alpha = 0.5
        
        runFirstPhaseTransition()   // same as OLD first half of encenderLicuadora()
    }
    
    @objc func handleDrag(_ gesture: UIPanGestureRecognizer) {
        guard let fruit = gesture.view as? UIImageView else { return }
        let translation = gesture.translation(in: view)
        switch gesture.state {
        case .began:
            originalFruit = fruit
            let copy = FruitImageView(image: fruit.image)
            copy.fruitName = (fruit as? FruitImageView)?.fruitName ?? ""
            copy.tag = 999
            copy.frame = fruit.superview!.convert(fruit.frame, to: view)
            copy.isUserInteractionEnabled = true
            let pan = UIPanGestureRecognizer(target: self, action: #selector(handleDrag(_:)))
            copy.addGestureRecognizer(pan)
            copy.contentMode = .scaleAspectFit
            view.addSubview(copy)
            draggedCopy = copy
            currentlyDraggedFruit = copy
            startingCenters[copy] = copy.center
            view.bringSubviewToFront(copy)
        case .changed:
            if let copy = draggedCopy, let start = startingCenters[copy]{
                copy.center = CGPoint(x: start.x + translation.x, y: start.y + translation.y)
            }
        case .ended, .cancelled:
            if let copy = draggedCopy {
                startingCenters[copy] = copy.center
            }
            if let original = originalFruit {
                fruitStack.removeArrangedSubview(original)
                original.removeFromSuperview()
            }
            draggedCopy = nil
            originalFruit = nil
            currentlyDraggedFruit = nil
            
        default:
            break
        }
    }
    func startTrayFruitTimer() {
        trayTimer?.invalidate() // stop if already running
        trayTimer = Timer.scheduledTimer(withTimeInterval: 0.7, repeats: true) { [weak self] _ in
            self?.refreshTrayFruits()
        }
    }

    func refreshTrayFruits() {
        let trayFruitNames = ["fresa", "uva", "manzana", "banana", "naranja"]
        
        var updated: [UIImageView] = []

        for fruit in fruitStack.arrangedSubviews as! [UIImageView]{
            if fruit == originalFruit {
                updated.append(fruit)
                continue
            }
            let newName = trayFruitNames.randomElement()!
            let newFruit = FruitImageView(image: UIImage(named: newName))
            newFruit.fruitName = newName
            newFruit.tag = 1
            newFruit.contentMode = .scaleAspectFit
            newFruit.translatesAutoresizingMaskIntoConstraints = false
            NSLayoutConstraint.activate([
                newFruit.widthAnchor.constraint(equalToConstant: 60),
                newFruit.heightAnchor.constraint(equalToConstant: 60)
            ])
            newFruit.isUserInteractionEnabled = true
            newFruit.addGestureRecognizer(UIPanGestureRecognizer(target: self, action: #selector(handleDrag(_:))))
            updated.append(newFruit)
            }
            for v in fruitStack.arrangedSubviews{
                fruitStack.removeArrangedSubview(v)
                v.removeFromSuperview()
            }
            updated.forEach { fruitStack.addArrangedSubview($0) }
    }
    func animateTriangle() {
        view.layoutIfNeeded()     // make sure frames are ready
        let bar = barraImageView!
        let triWidth = triangleImageView.frame.width
        let barWidth = bar.frame.width
        let travelDistance = barWidth - triWidth

        // Infinite back-and-forth animation
        UIView.animateKeyframes(
            withDuration: 1.0,
            delay: 0, options: [.repeat, .autoreverse, .calculationModeLinear],
            animations: {
                UIView.addKeyframe(withRelativeStartTime: 0, relativeDuration: 1.0) {
                    self.triangleLeadingConstraint.constant = travelDistance
                    self.view.layoutIfNeeded()
                }

        }, completion: nil)
    }
    func changeToStatsBackground() {
        
        guard let licuadora = view.subviews.first(where: {
            ($0 as? UIImageView)?.image == UIImage(named: "licuadora")
        }) as? UIImageView else {
            return
        }
        
        let licuadoraFrame = licuadora.superview!.convert(licuadora.frame, to: view)
        for copy in view.subviews where copy.tag == 999 {
            if licuadoraFrame.intersects(copy.frame),
               let f = copy as? FruitImageView {
                fruitsInsideBlender.append(f)
            }
            UIView.animate(withDuration: 0.8) {
                copy.alpha = 0
            } completion: { _ in
                copy.removeFromSuperview()
            }
        }

        let fadeView = UIImageView(frame: backgroundImage.frame)
        fadeView.image = UIImage(named: "fondolicuado4")
        fadeView.contentMode = .scaleAspectFill
        fadeView.alpha = 0
        view.insertSubview(fadeView, aboveSubview: backgroundImage)
        
        UIView.animate(withDuration: 2.5, animations: {
            fadeView.alpha = 1
        }, completion: {_ in
            self.backgroundImage.image = UIImage(named: "fondolicuado4")
            fadeView.removeFromSuperview()
            
            DispatchQueue.main.asyncAfter(deadline: .now() + 5.0) {
                let darkOverlay = UIView(frame: self.view.bounds)
                darkOverlay.tag = 777
                darkOverlay.backgroundColor = UIColor.black.withAlphaComponent(0.60)
                darkOverlay.alpha = 0
                darkOverlay.isUserInteractionEnabled = false
                self.view.insertSubview(darkOverlay, aboveSubview: self.backgroundImage)
                
                // fade it in
                UIView.animate(withDuration: 0.5, animations: {
                    darkOverlay.alpha = 1
                    // Change tray color when darkening background
                    self.trayView.backgroundColor = UIColor(
                        red: 65/255.0,
                        green: 32/255.0,
                        blue: 10/255.0,
                        alpha: 1.0
                    )
                    self.lecheImageView.alpha = 0
                    for view in self.view.subviews where view.tag == 999 {
                        view.alpha = 0
                    }
                    
                }, completion: {_ in
                    for view in self.view.subviews where view.tag == 999 {
                        view.removeFromSuperview()
                    }
                })
            self.showResultsFinalUI()
            }
        })
    }

    func finalBlendQuality() -> String {
        view.layoutIfNeeded()

        let bar = barraImageView!
        let tri = triangleImageView!

        // Convert positions
        let barMinX = bar.frame.minX
        let barMaxX = bar.frame.maxX - tri.frame.width

        let triX = tri.frame.origin.x

        // Normalize to 0–1
        let percent = (triX - barMinX) / (barMaxX - barMinX)

        // Convert to 0–100
        let p = percent * 100

        switch p {
        case 0..<20:
            return "Mala"
        case 20..<40:
            return "Media"
        case 40..<60:
            return "Buena"
        case 60..<80:
            return "Media"
        default:
            return "Mala"
        }
    }
    
    func computeFinalFruitResults() {

        pickedCorrect.removeAll()
        pickedIncorrect.removeAll()

        var tempNeeded = targetFruits

        for fruit in fruitsInsideBlender {
            let name = fruit.fruitName

            if let matchIndex = tempNeeded.firstIndex(of: name) {
                pickedCorrect.append(name)
                tempNeeded.remove(at: matchIndex)
            } else {
                pickedIncorrect.append(name)
            }
        }

        remainingTargets = tempNeeded
    }
    
    func showResultsFinalUI() {
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) {
            self.playShineSound()
        }

        let missing = remainingTargets
        let mezcla = finalBlendQuality()

        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.numberOfLines = 0
        label.textColor = .white
        label.font = UIFont.systemFont(ofSize: 22, weight: .bold)

        label.text =
        """
        Frutas necesarias: \(targetFruits.joined(separator: ", "))

        Correctas: \(pickedCorrect.joined(separator: ", "))

        Incorrectas: \(pickedIncorrect.joined(separator: ", "))

        Faltantes: \(missing.joined(separator: ", "))
        
        Calidad de la mezcla: \(mezcla)
        """

        view.addSubview(label)

        NSLayoutConstraint.activate([
            label.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            label.centerYAnchor.constraint(equalTo: view.centerYAnchor),
            label.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 40),
            label.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -30)
        ])
        
        let stars = calculateStars()
        starsImageView.alpha = 1
        if stars >= 0 && stars <= 5 {
            starsImageView.image = UIImage(named: "\(stars)stars")
        }
        
        UIView.animate(withDuration: 0.5) {
            self.siguienteImageView.alpha = 1
        }

    }
    func startGameTimer() {
        timeRemaining = 30
        licuadoFinished = false
        
        timerLabel.text = "30"

        gameTimer?.invalidate()
        gameTimer = Timer.scheduledTimer(withTimeInterval: 1.0, repeats: true) { [weak self] timer in
            guard let self = self else { return }

            if self.licuadoFinished {
                timer.invalidate()
                return
            }

            self.timeRemaining -= 1
            self.timerLabel.text = "\(self.timeRemaining)"

            if self.timeRemaining <= 0 {
                timer.invalidate()
                self.handleGameLost()
            }
        }
    }
    func handleGameLost() {
        if let player = milkPlayer, player.isPlaying {
            player.stop()
            player.currentTime = 0
        }

        hasLost = true
        trayTimer?.invalidate()
        gameTimer?.invalidate()
        triangleImageView.layer.removeAllAnimations()
        triangleIsMoving = false

        // Disable everything EXCEPT the flecha
        for sub in view.subviews {
            if sub == flechaImageView { continue }
            if sub == siguienteImageView { continue }
            if sub == flechaImageView { continue }
            if sub == siguienteImageView { continue }
            if sub == trayView { continue }
            if sub == fruitStack { continue }

        }
        
        bannerImageView.alpha = 0
        fruitStackInBanner.alpha = 0
            
        trayView.alpha = 0
        fruitStack.alpha = 0

        botonLicuadoraButton.alpha = 0.5
            
        barraImageView.alpha = 0
        triangleImageView.alpha = 0

        lecheImageView.alpha = 0
        
        for copy in view.subviews where copy.tag == 999 {
            copy.removeFromSuperview()
        }
        
        originalFruit = nil

        // Wait 3 seconds before showing "perdiste"
        DispatchQueue.main.asyncAfter(deadline: .now() + 1.5) {
            self.playPoingSound()

            let loseImage = UIImageView(frame: self.view.bounds)
            loseImage.image = UIImage(named: "perdiste")
            loseImage.contentMode = .scaleAspectFill
            loseImage.alpha = 0

            self.view.insertSubview(loseImage, belowSubview: self.flechaImageView)
            self.view.bringSubviewToFront(self.siguienteImageView)

            UIView.animate(withDuration: 0.7) {
                loseImage.alpha = 1.0
            }
            UIView.animate(withDuration: 0.5) {
                self.siguienteImageView.alpha = 1
            }
        }
    }
    func calculateStars() -> Int {
        let correct = !pickedCorrect.isEmpty
        let incorrect = !pickedIncorrect.isEmpty
        let missing = !remainingTargets.isEmpty
        let calidad = finalBlendQuality()  // “Buena”, “Media”, “Mala”
        
        if !correct {
            // EXCEPCIÓN: incorrectas y faltantes y calidad buena → 1 estrella
            if incorrect && missing && calidad == "Buena" {
                return 1
            }
            // Todo lo demás sin correctas → 0 estrellas
            return 0
        }

        var score = 0

        // Correctas
        score += 1

        // Incorrectas / Faltantes
        if !incorrect && !missing {
            score += 2
        }
        else if (incorrect && !missing) || (!incorrect && missing) {
            score += 1
        }
        else {
            score += 0
        }
        
        switch calidad {
            case "Buena": score += 2
            case "Media": score += 1
            default: score += 0
        }
        
        return score
    }
    func resetGame() {

        // --- 1. Reset variables ---
        licuadoFinished = false
        hasLost = false
        fruitsInsideBlender.removeAll()
        pickedCorrect.removeAll()
        pickedIncorrect.removeAll()
        remainingTargets.removeAll()
        targetFruits.removeAll()

        // --- 2. Reset UI ---
        
        starsImageView.alpha = 0

        // Fondo inicial
        backgroundImage.image = UIImage(named: "fondolicuado2")

        // Mostrar elementos ocultos
        botonLicuadoraButton.isUserInteractionEnabled = false
        botonLicuadoraButton.alpha = 0.5
        barraImageView.alpha = 0
        triangleImageView.alpha = 0
        lecheImageView.alpha = 1
        lecheImageView.isUserInteractionEnabled = true
        bannerImageView.alpha = 1
        fruitStackInBanner.alpha = 1
        trayView.alpha = 1
        fruitStack.alpha = 1
        siguienteImageView.alpha = 0
        trayView.backgroundColor = UIColor(red: 153/255.0, green: 79/255.0, blue: 21/255.0, alpha: 1.0) // #994F15
        
        
        // Remove the PERDISTE image if it exists
        for sub in view.subviews {
            if let img = sub as? UIImageView, img.image == UIImage(named: "perdiste") {
                img.removeFromSuperview()
            }
        }
        
        // Remove dark overlay if present
        if let overlay = view.viewWithTag(777) {
            overlay.removeFromSuperview()
        }

        // Eliminar textos de resultados
        for sub in view.subviews {
            if sub is UILabel { sub.removeFromSuperview() }
        }

        // Eliminar copias de frutas
        for copy in view.subviews where copy.tag == 999 {
            copy.removeFromSuperview()
        }

        // --- 3. Reset triángulo ---
        triangleImageView.layer.removeAllAnimations()
        triangleLeadingConstraint.constant = 0
        triangleIsMoving = false

        // --- 4. Regenerar frutas objetivo ---
        fruitStackInBanner.arrangedSubviews.forEach { $0.removeFromSuperview() }

        let bannerFruitNames = ["fresa", "uva", "manzana", "banana", "naranja"]
        targetFruits = (0..<3).map { _ in bannerFruitNames.randomElement()! }
        remainingTargets = targetFruits

        for name in targetFruits {
            let iv = UIImageView(image: UIImage(named: name))
            iv.translatesAutoresizingMaskIntoConstraints = false
            NSLayoutConstraint.activate([
                iv.widthAnchor.constraint(equalToConstant: 50),
                iv.heightAnchor.constraint(equalToConstant: 50)
            ])
            fruitStackInBanner.addArrangedSubview(iv)
        }

        // --- 5. Regenerar frutas de la bandeja ---
        fruitStack.arrangedSubviews.forEach { $0.removeFromSuperview() }

        let trayFruitNames = ["fresa", "uva", "manzana", "banana", "naranja"]
        let randomTray = (0..<5).map { _ in trayFruitNames.randomElement()! }

        for name in randomTray {
            let fruit = UIImageView(image: UIImage(named: name))
            fruit.translatesAutoresizingMaskIntoConstraints = false
            NSLayoutConstraint.activate([
                fruit.widthAnchor.constraint(equalToConstant: 60),
                fruit.heightAnchor.constraint(equalToConstant: 60)
            ])
            fruit.isUserInteractionEnabled = true
            fruit.addGestureRecognizer(UIPanGestureRecognizer(target: self, action: #selector(handleDrag(_:))))
            fruitStack.addArrangedSubview(fruit)
        }
        
        // Restore user interaction for tray and fruits
        trayView.isUserInteractionEnabled = true
        fruitStack.isUserInteractionEnabled = true
        for fruit in fruitStack.arrangedSubviews {
            fruit.isUserInteractionEnabled = true
        }

        // --- 6. Restart timers ---
        startTrayFruitTimer()
        startGameTimer()

    }

    func playBlenderSound() {
        // If already prepared, just play (instant)
        if blenderPlayerPrepared, let player = blenderPlayer {
            player.currentTime = 0
            player.play()
            return
        }

        // Fallback: load synchronously only if preparation somehow failed
        guard let url = Bundle.main.url(forResource: "blender", withExtension: "mp3") else {
            return
        }

        do {
            blenderPlayer = try AVAudioPlayer(contentsOf: url)
            blenderPlayer?.prepareToPlay()
            blenderPlayer?.play()
        } catch {
        }
    }
    
    func playMilkSound() {
        if milkPlayerPrepared, let player = milkPlayer {
            player.currentTime = 0
            player.play()
            return
        }

        // Fallback if not prepared
        guard let url = Bundle.main.url(forResource: "milksound", withExtension: "mp3") else {
            return
        }

        do {
            milkPlayer = try AVAudioPlayer(contentsOf: url)
            milkPlayer?.prepareToPlay()
            milkPlayer?.play()
        } catch {
        }
    }
    func playShineSound() {
        if shinePlayerPrepared, let player = shinePlayer {
            player.currentTime = 0
            player.play()
            return
        }

        // fallback
        guard let url = Bundle.main.url(forResource: "shine", withExtension: "mp3") else { return }

        do {
            shinePlayer = try AVAudioPlayer(contentsOf: url)
            shinePlayer?.prepareToPlay()
            shinePlayer?.play()
        } catch { }
    }

    func playPoingSound() {
        if poingPlayerPrepared, let player = poingPlayer {
            player.currentTime = 0
            player.play()
            return
        }

        guard let url = Bundle.main.url(forResource: "POING", withExtension: "mp3") else { return }

        do {
            poingPlayer = try AVAudioPlayer(contentsOf: url)
            poingPlayer?.prepareToPlay()
            poingPlayer?.play()
        } catch { }
    }

    func runFirstPhaseTransition() {

        // 1. Change background to a licuar
        if let bg = view.subviews.first(where: { $0 is UIImageView }) as? UIImageView {
            bg.image = UIImage(named: "alicuar")
        }

        // 2. Remove ONLY the fruits inside the tray
        for fruit in fruitStack.arrangedSubviews {
            fruitStack.removeArrangedSubview(fruit)
            fruit.removeFromSuperview()
        }

        lecheImageView.alpha = 0

        // 2.5 Remove fruits inside TOP banner
        bannerImageView.alpha = 0
        fruitStackInBanner.alpha = 0

        // Stop spawning fruits
        trayTimer?.invalidate()
        trayTimer = nil

        // Remove drag gestures from fruit copies
        for view in self.view.subviews {
            if view.tag == 999 {
                view.gestureRecognizers?.forEach {
                    if $0 is UIPanGestureRecognizer {
                        view.removeGestureRecognizer($0)
                    }
                }
            }
        }

        barraImageView.alpha = 0
        triangleImageView.alpha = 0

        // After 5 seconds, reveal bar & triangle and enable blender button
        DispatchQueue.main.asyncAfter(deadline: .now() + 5) {
            if self.hasLost { return }

            if let bg = self.view.subviews.first(where: { $0 is UIImageView }) as? UIImageView {
                bg.image = UIImage(named: "fondolicuado3")
            }

            UIView.animate(withDuration: 0.4) {
                self.lecheImageView.alpha = 1
            }
            UIView.animate(withDuration: 0.5) {
                self.barraImageView.alpha = 1
                self.triangleImageView.alpha = 1
            }

            self.triangleIsMoving = true
            self.animateTriangle()

            // NOW final step: enable licuadora button
            self.botonLicuadoraButton.isUserInteractionEnabled = true
            self.botonLicuadoraButton.alpha = 1.0
        }
    }
    func startBackgroundMusic() {
        guard let url = Bundle.main.url(forResource: "musicalicuado", withExtension: "mp3") else {
            return
        }

        do {
            backgroundMusicPlayer = try AVAudioPlayer(contentsOf: url)
            backgroundMusicPlayer?.numberOfLoops = -1 // 🔁 loop infinito
            backgroundMusicPlayer?.volume = 0.8
            backgroundMusicPlayer?.play()
        } catch {
        }
    }

    private func prepareBlenderSoundInBackground() {
        DispatchQueue.global(qos: .utility).async { [weak self] in
            guard let self = self else { return }
            guard let url = Bundle.main.url(forResource: "blender", withExtension: "mp3") else {
                return
            }

            do {
                let player = try AVAudioPlayer(contentsOf: url)
                player.prepareToPlay()   // Warm buffers (this is the expensive part)
                
                DispatchQueue.main.async {
                    self.blenderPlayer = player
                    self.blenderPlayerPrepared = true
                }
            } catch {
            }
        }
    }
    private func prepareMilkSoundInBackground() {
        DispatchQueue.global(qos: .utility).async { [weak self] in
            guard let self = self else { return }
            guard let url = Bundle.main.url(forResource: "MILK", withExtension: "mp3") else {
                return
            }

            do {
                let player = try AVAudioPlayer(contentsOf: url)
                player.prepareToPlay()   // Preload buffers

                DispatchQueue.main.async {
                    self.milkPlayer = player
                    self.milkPlayerPrepared = true
                }
            } catch {
            }
        }
    }
    private func prepareShineSoundInBackground() {
        DispatchQueue.global(qos: .utility).async { [weak self] in
            guard let self = self else { return }
            guard let url = Bundle.main.url(forResource: "shine", withExtension: "mp3") else {
                return
            }

            do {
                let player = try AVAudioPlayer(contentsOf: url)
                player.prepareToPlay()

                DispatchQueue.main.async {
                    self.shinePlayer = player
                    self.shinePlayerPrepared = true
                }
            } catch { }
        }
    }
    private func prepareBotonesSoundInBackground() {
        DispatchQueue.global(qos: .utility).async { [weak self] in
            guard let self = self else { return }
            guard let url = Bundle.main.url(forResource: "Botones", withExtension: "mp3") else {
                return
            }

            do {
                let player = try AVAudioPlayer(contentsOf: url)
                player.prepareToPlay()

                DispatchQueue.main.async {
                    self.botonesPlayer = player
                    self.botonesPlayerPrepared = true
                }
            } catch { }
        }
    }
    private func preparePoingSoundInBackground() {
        DispatchQueue.global(qos: .utility).async { [weak self] in
            guard let self = self else { return }
            guard let url = Bundle.main.url(forResource: "POING", withExtension: "mp3") else {
                return
            }

            do {
                let player = try AVAudioPlayer(contentsOf: url)
                player.prepareToPlay()

                DispatchQueue.main.async {
                    self.poingPlayer = player
                    self.poingPlayerPrepared = true
                }
            } catch { }
        }
    }

}

