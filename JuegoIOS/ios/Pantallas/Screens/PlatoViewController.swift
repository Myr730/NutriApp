//
//  PlatoViewController.swift
//  Pantallas
//
//  Created by Karol Lozano González on 21/09/25.
//

import UIKit
import AVFoundation

class PlatoViewController: UIViewController {
    
    let carbohidratosFeedback: [String: String] = [
        "carbohidratos1": "El pan da energía, evita combinarlo con muchas harinas.",
        "carbohidratos2": "Los frijoles aportan proteína vegetal y fibra.",
        "carbohidratos3": "El arroz blanco tiene poca fibra. Recuerda comerlo con verduras.",
        "carbohidratos4": "Las papas cambray son una buena fuente de energía.",
        "carbohidratos5": "La tortilla de maíz es natural y rica en fibra.",
        "carbohidratos6": "El arroz rojo puede llevar aceite. Consúmelo con moderación.",
        "carbohidratos7": "El espagueti aporta energía, pero evita porciones grandes.",
        "carbohidratos8": "El pan integral tiene mucha fibra en porciones pequeñas.",
        "carbohidratos9": "El camote aporta energía y fibra. Es muy buena opción.",
        "carbohidratos10": "El elote es un cereal natural y saludable."
    ]
    
    let frutasFeedback: [String: String] = [
        "frutas1": "La tuna es refrescante y rica en fibra.",
        "frutas2": "El plátano tiene potasio. Excelente antes de actividad física.",
        "frutas3": "La sandía es hidratante y ligera.",
        "frutas4": "La pera es buena para la digestión.",
        "frutas5": "Las uvas son dulces; ten moderación si agregaste postre.",
        "frutas6": "El aguacate tiene grasas saludables.",
        "frutas7": "La fresa es ligera y baja en azúcar.",
        "frutas8": "La manzana es rica en fibra. ¡Buena elección!",
        "frutas9": "El maracuyá es ácido y muy nutritivo.",
        "frutas10": "La naranja aporta vitamina C; evítala si tienes otras azúcares."
    ]
    
    let verdurasFeedback: [String: String] = [
        "verduras1": "Los champiñones son bajos en calorías y tienen muy buen sabor.",
        "verduras2": "La calabaza es ligera y nutritiva.",
        "verduras3": "La cebolla aporta sabor y antioxidantes.",
        "verduras4": "El pimiento rojo aporta mucha vitamina C.",
        "verduras5": "El brócoli es excelente: fibra, vitaminas y minerales.",
        "verduras6": "El tomate es fresco y ligero.",
        "verduras7": "El jalapeño da sabor, pero recuerda agregar más verduras.",
        "verduras8": "La lechuga es fresca, pero ligera; combínala con más verduras.",
        "verduras9": "El nopal es excelente para la digestión y la glucosa.",
        "verduras10": "La zanahoria aporta vitamina A y fibra."
    ]
    
    let origenAnimalFeedback: [String: String] = [
        "origenanimal1": "El bistec aporta proteína, pero también grasa. Cómelo con moderación.",
        "origenanimal2": "El pescado es una excelente proteína baja en grasa. ¡Muy bien!",
        "origenanimal3": "El pollo es una buena fuente de proteína.",
        "origenanimal4": "El camarón es ligero, pero recuerda tomar porciones pequeñas.",
        "origenanimal5": "La salchicha es un procesado, intenta no usarla tan seguido.",
        "origenanimal6": "El huevo estrellado es nutritivo, pero evita usar mucho aceite.",
        "origenanimal7": "El yogurt natural es buena fuente de calcio.",
        "origenanimal8": "El queso Oaxaca es sabroso, pero moderado en grasa.",
        "origenanimal9": "El huevo hervido es excelente opción: proteína sin aceite.",
        "origenanimal10": "El queso amarillo es un procesado. Mejor limitarlo."
    ]
    
    enum FoodCategory: String {
        case frutas, verduras, carbohidratos, origenanimal
    }
    var backgroundImage: UIImageView!
    var backgroundMusicPlayer: AVAudioPlayer?

    var currentCategory: FoodCategory?
    var resumenLabel: UILabel!
    
    var indexes: [FoodCategory: Int] = [
        .frutas: 1,
        .verduras: 1,
        .carbohidratos: 1,
        .origenanimal: 1
    ]
    var platoImageView: UIImageView!
    var blanket: UIImageView!
    
    let maxItems: Int = 10
    var categoryImageView = UIImageView()
    var platitoImageView: UIImageView!
    var leftArrow: UIImageView!
    var rightArrow: UIImageView!
    var leftArrowCenterConstraint: NSLayoutConstraint!
    var rightArrowCenterConstraint: NSLayoutConstraint!
    var foodStack: UIStackView!
    var platoButtons: [FoodCategory: UIImageView] = [:]
    var closeImageView: UIImageView!
    var basuraImageView: UIImageView!
    var siguienteImageView: UIImageView!
    var listoImageView: UIImageView!
    var instruccionesImageView: UIImageView!
    var instruccionesIndex = 1
    var postresImageView: UIImageView!
    var postreIndex: Int = 1
    let maxPostres = 5
    var isShowingPostres = false
    var bebidasImageView: UIImageView!
    var bebidaIndex: Int = 1
    let maxBebidas = 7
    var isShowingBebidas = false
    
    var interactionLocked = false
    var bebidasAndPostresUnlocked = false

    var droppedItems: [UIImageView] = []
    var draggingTempCopy: UIImageView?
    
    var bebidaElegidaImageView: UIImageView!
    var postreElegidoImageView: UIImageView!
    
    var detallesPorAlimento = ""
    
    var servilletaImageView: UIImageView!

    override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
        playBackgroundMusic()
        setupCategories()
        setupArrows()
    }

    func setupCategories() {
        let row1 = UIStackView()
        row1.axis = .horizontal
        row1.alignment = .center
        row1.distribution = .equalSpacing

        let row2 = UIStackView()
        row2.axis = .horizontal
        row2.alignment = .center
        row2.distribution = .equalSpacing
        
        row1.spacing = 25
        row2.spacing = 25

        foodStack.addArrangedSubview(row1)
        foodStack.addArrangedSubview(row2)

        let categories: [(FoodCategory, String)] = [
            (.carbohidratos, "carbohidratos"),
            (.frutas, "frutas"),
            (.origenanimal, "origenanimal"),
            (.verduras, "verduras")
        ]

        for (index, item) in categories.enumerated() {
            let (cat, imageName) = item

            let img = UIImageView(image: UIImage(named: imageName))
            img.isUserInteractionEnabled = true
            img.contentMode = .scaleAspectFill
            img.translatesAutoresizingMaskIntoConstraints = false
            img.widthAnchor.constraint(equalToConstant: 130).isActive = true
            img.heightAnchor.constraint(equalToConstant: 130).isActive = true

            let tap = UITapGestureRecognizer(target: self, action: #selector(didTapCategory))
            img.addGestureRecognizer(tap)
            img.tag = index

            platoButtons[cat] = img

            if index < 2 { row1.addArrangedSubview(img) }
            else { row2.addArrangedSubview(img) }
        }
    }

    @objc func didTapCategory(_ sender: UITapGestureRecognizer) {
        AudioManager.shared.playButtonSound()
        guard let tapped = sender.view else { return }

        let categories: [FoodCategory] = [.carbohidratos, .frutas, .origenanimal, .verduras]
        let selected = categories[tapped.tag]

        showCategory(selected)
    }

    func showCategory(_ category: FoodCategory) {
        currentCategory = category
        foodStack.isUserInteractionEnabled = false

        for (_, icon) in platoButtons { icon.isHidden = true }

        platitoImageView.isHidden = false
        leftArrow.isHidden = false
        rightArrow.isHidden = false
        
        categoryImageView.isHidden = false
        addDragToCategoryImage()
        closeImageView.isHidden = false
        updateCategoryImage()
    }

    func updateCategoryImage() {
        guard let cat = currentCategory else { return }

        let index = indexes[cat] ?? 1
        let imageName = "\(cat.rawValue)\(index)"
        let img = UIImage(named: imageName)
           img?.accessibilityIdentifier = imageName
        categoryImageView.image = UIImage(named: "\(cat.rawValue)\(index)")
    }

    @objc func previousItem() {
        AudioManager.shared.playButtonSound()
        if isShowingBebidas {
            bebidaIndex -= 1
            if bebidaIndex < 1 { bebidaIndex = maxBebidas }
            updateBebidaImage()
            return
        } else if isShowingPostres{
            postreIndex -= 1
            if postreIndex < 1 { postreIndex = maxPostres }
            updatePostreImage()
            return
        }

        guard let cat = currentCategory else { return }

        indexes[cat]! -= 1
        if indexes[cat]! < 1 { indexes[cat] = maxItems }

        updateCategoryImage()
    }

    @objc func nextItem() {
        AudioManager.shared.playButtonSound()
        if isShowingBebidas {
            bebidaIndex += 1
            if bebidaIndex > maxBebidas { bebidaIndex = 1 }
            updateBebidaImage()
            return
        } else if isShowingPostres{
            postreIndex += 1
            if postreIndex > maxPostres { postreIndex = 1 }
            updatePostreImage()
            return
        }
        guard let cat = currentCategory else { return }

        indexes[cat]! += 1
        if indexes[cat]! > maxItems { indexes[cat] = 1 }

        updateCategoryImage()
    }
    
    func setupUI() {

        backgroundImage = UIImageView(frame: UIScreen.main.bounds)
        backgroundImage.image = UIImage(named: "fondoPlato")
        backgroundImage.contentMode = .scaleAspectFill
        view.insertSubview(backgroundImage, at: 0)

        let flechaImageView = UIImageView(image: UIImage(named: "flecha"))
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

        let labelImageView = UIImageView(image: UIImage(named: "armaPlato"))
        labelImageView.translatesAutoresizingMaskIntoConstraints = false
        labelImageView.contentMode = .scaleAspectFit
        view.addSubview(labelImageView)

        NSLayoutConstraint.activate([
            labelImageView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: -view.bounds.height * 0.05),
            labelImageView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -37),
            labelImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.62),
            labelImageView.heightAnchor.constraint(equalTo: labelImageView.widthAnchor, multiplier: 0.8)
        ])

        instruccionesImageView = UIImageView(image: UIImage(named: "instrucciones1"))
        instruccionesImageView.translatesAutoresizingMaskIntoConstraints = false
        instruccionesImageView.contentMode = .scaleAspectFit
        view.addSubview(instruccionesImageView)
        
        NSLayoutConstraint.activate([
            instruccionesImageView.centerYAnchor.constraint(equalTo: view.centerYAnchor, constant: -260),
            instruccionesImageView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -29),
            instruccionesImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.67),
            instruccionesImageView.heightAnchor.constraint(equalTo: instruccionesImageView.widthAnchor, multiplier: 0.83)
        ])

        let trayView = UIView()
        trayView.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(trayView)

        NSLayoutConstraint.activate([
            trayView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -50),
            trayView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            trayView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            trayView.heightAnchor.constraint(equalToConstant: 250)
        ])

        blanket = UIImageView(image: UIImage(named: "blanket"))
        blanket.translatesAutoresizingMaskIntoConstraints = false
        blanket.contentMode = .scaleAspectFill
        trayView.addSubview(blanket)

        NSLayoutConstraint.activate([
            blanket.topAnchor.constraint(equalTo: trayView.topAnchor),
            blanket.bottomAnchor.constraint(equalTo: trayView.bottomAnchor),
            blanket.leadingAnchor.constraint(equalTo: trayView.leadingAnchor),
            blanket.trailingAnchor.constraint(equalTo: trayView.trailingAnchor)
        ])
        
        servilletaImageView = UIImageView(image: UIImage(named: "servilleta"))
        servilletaImageView.translatesAutoresizingMaskIntoConstraints = false
        servilletaImageView.isHidden = true
        servilletaImageView.contentMode = .scaleAspectFit
        view.addSubview(servilletaImageView)

        NSLayoutConstraint.activate([
            servilletaImageView.centerYAnchor.constraint(equalTo: view.centerYAnchor, constant: 83),
            servilletaImageView.centerXAnchor.constraint(equalTo: view.centerXAnchor, constant: 145),
            servilletaImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.7),
            servilletaImageView.heightAnchor.constraint(equalTo: servilletaImageView.widthAnchor, multiplier: 0.7)
        ])

        platoImageView = UIImageView(image: UIImage(named: "plato"))
        platoImageView.translatesAutoresizingMaskIntoConstraints = false
        platoImageView.contentMode = .scaleAspectFit
        view.addSubview(platoImageView)

        NSLayoutConstraint.activate([
            platoImageView.centerYAnchor.constraint(equalTo: view.centerYAnchor, constant: 17),
            platoImageView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            platoImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.7),
            platoImageView.heightAnchor.constraint(equalTo: platoImageView.widthAnchor, multiplier: 0.45)
        ])

        platitoImageView = UIImageView(image: UIImage(named: "platito"))
        platitoImageView.isHidden = true
        platitoImageView.translatesAutoresizingMaskIntoConstraints = false
        platitoImageView.contentMode = .scaleAspectFit
        trayView.addSubview(platitoImageView)

        NSLayoutConstraint.activate([
            platitoImageView.centerXAnchor.constraint(equalTo: trayView.centerXAnchor),
            platitoImageView.centerYAnchor.constraint(equalTo: trayView.centerYAnchor, constant: 10),
            platitoImageView.widthAnchor.constraint(equalTo: trayView.widthAnchor, multiplier: 0.55),
            platitoImageView.heightAnchor.constraint(equalTo: platitoImageView.widthAnchor, multiplier: 0.38)
        ])

        categoryImageView.translatesAutoresizingMaskIntoConstraints = false
        categoryImageView.contentMode = .scaleAspectFit
        categoryImageView.isHidden = true
        trayView.addSubview(categoryImageView)

        NSLayoutConstraint.activate([
            categoryImageView.centerXAnchor.constraint(equalTo: trayView.centerXAnchor),
            categoryImageView.centerYAnchor.constraint(equalTo: trayView.centerYAnchor, constant: -8),
            categoryImageView.widthAnchor.constraint(equalTo: trayView.widthAnchor, multiplier: 0.55),
            categoryImageView.heightAnchor.constraint(equalTo: categoryImageView.widthAnchor, multiplier: 0.58)
        ])
        
        siguienteImageView = UIImageView(image: UIImage(named: "siguiente"))
        let tapSiguiente = UITapGestureRecognizer(target: self, action: #selector(didTapSiguiente))
        siguienteImageView.addGestureRecognizer(tapSiguiente)
        siguienteImageView.isUserInteractionEnabled = true

        siguienteImageView.translatesAutoresizingMaskIntoConstraints = false
        siguienteImageView.contentMode = .scaleAspectFit
        view.addSubview(siguienteImageView)
        
        NSLayoutConstraint.activate([
            siguienteImageView.centerYAnchor.constraint(equalTo: view.centerYAnchor, constant: 107),
            siguienteImageView.centerXAnchor.constraint(equalTo: view.centerXAnchor, constant: -108),
            siguienteImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.7),
            siguienteImageView.heightAnchor.constraint(equalTo: siguienteImageView.widthAnchor, multiplier: 0.26)
        ])

        listoImageView = UIImageView(image: UIImage(named: "listo"))
        listoImageView.alpha = 0
        listoImageView.translatesAutoresizingMaskIntoConstraints = false
        listoImageView.contentMode = .scaleAspectFit
        view.addSubview(listoImageView)
        let tapListo = UITapGestureRecognizer(target: self, action: #selector(didTapListo))
        listoImageView.addGestureRecognizer(tapListo)
        listoImageView.isUserInteractionEnabled = true

        NSLayoutConstraint.activate([
            listoImageView.centerYAnchor.constraint(equalTo: view.centerYAnchor, constant: 105),
            listoImageView.centerXAnchor.constraint(equalTo: view.centerXAnchor, constant: -120),
            listoImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.7),
            listoImageView.heightAnchor.constraint(equalTo: listoImageView.widthAnchor, multiplier: 0.16)
        ])
        
        closeImageView = UIImageView(image: UIImage(named: "close"))
        closeImageView.isHidden = true
        closeImageView.translatesAutoresizingMaskIntoConstraints = false
        closeImageView.contentMode = .scaleAspectFit
        view.addSubview(closeImageView)

        NSLayoutConstraint.activate([
            closeImageView.centerYAnchor.constraint(equalTo: view.centerYAnchor, constant: 103),
            closeImageView.centerXAnchor.constraint(equalTo: view.centerXAnchor, constant: 160),
            closeImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.7),
            closeImageView.heightAnchor.constraint(equalTo: closeImageView.widthAnchor, multiplier: 0.20)
        ])
        
        let closeTap = UITapGestureRecognizer(target: self, action: #selector(closeCategory))
        closeImageView.addGestureRecognizer(closeTap)
        closeImageView.isUserInteractionEnabled = true

        foodStack = UIStackView()
        foodStack.axis = .vertical
        foodStack.alignment = .center
        foodStack.spacing = 10
        foodStack.distribution = .fillEqually
        foodStack.translatesAutoresizingMaskIntoConstraints = false
        trayView.addSubview(foodStack)

        NSLayoutConstraint.activate([
            foodStack.topAnchor.constraint(equalTo: trayView.topAnchor, constant: 30),
            foodStack.leadingAnchor.constraint(equalTo: trayView.leadingAnchor, constant: 10),
            foodStack.trailingAnchor.constraint(equalTo: trayView.trailingAnchor, constant: -10),
            foodStack.bottomAnchor.constraint(equalTo: trayView.bottomAnchor, constant: -40)
        ])
        
        basuraImageView = UIImageView(image: UIImage(named: "basura"))
        basuraImageView.translatesAutoresizingMaskIntoConstraints = false
        basuraImageView.contentMode = .scaleAspectFit
        view.addSubview(basuraImageView)

        NSLayoutConstraint.activate([
            basuraImageView.centerYAnchor.constraint(equalTo: view.centerYAnchor, constant: -105),
            basuraImageView.centerXAnchor.constraint(equalTo: view.centerXAnchor, constant: 125),
            basuraImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.2),
            basuraImageView.heightAnchor.constraint(equalTo: basuraImageView.widthAnchor, multiplier: 1.5)
        ])
        
        bebidaElegidaImageView = UIImageView(image: UIImage(named: "bebida1"))
        bebidaElegidaImageView.translatesAutoresizingMaskIntoConstraints = false
        bebidaElegidaImageView.isHidden = true
        bebidaElegidaImageView.contentMode = .scaleAspectFit
        view.addSubview(bebidaElegidaImageView)

        NSLayoutConstraint.activate([
            bebidaElegidaImageView.centerYAnchor.constraint(equalTo: view.centerYAnchor, constant: -105),
            bebidaElegidaImageView.centerXAnchor.constraint(equalTo: view.centerXAnchor, constant: 125),
            bebidaElegidaImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.37),
            bebidaElegidaImageView.heightAnchor.constraint(equalTo: bebidaElegidaImageView.widthAnchor, multiplier: 1.0)
        ])
        
        postreElegidoImageView = UIImageView(image: UIImage(named: "postre1"))
        postreElegidoImageView.translatesAutoresizingMaskIntoConstraints = false
        postreElegidoImageView.isHidden = true
        postreElegidoImageView.contentMode = .scaleAspectFit
        view.addSubview(postreElegidoImageView)

        NSLayoutConstraint.activate([
            postreElegidoImageView.centerYAnchor.constraint(equalTo: view.centerYAnchor, constant: -85),
            postreElegidoImageView.centerXAnchor.constraint(equalTo: view.centerXAnchor, constant: -141),
            postreElegidoImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.33),
            postreElegidoImageView.heightAnchor.constraint(equalTo: postreElegidoImageView.widthAnchor, multiplier: 1.0)
        ])
        
        bebidasImageView = UIImageView(image: UIImage(named: "bebidas"))
        let tapBebidas = UITapGestureRecognizer(target: self, action: #selector(openBebidas))
        bebidasImageView.addGestureRecognizer(tapBebidas)
        bebidasImageView.isUserInteractionEnabled = true
        bebidasImageView.alpha = 0
        bebidasImageView.translatesAutoresizingMaskIntoConstraints = false
        bebidasImageView.contentMode = .scaleAspectFit
        view.addSubview(bebidasImageView)

        NSLayoutConstraint.activate([
            bebidasImageView.centerYAnchor.constraint(equalTo: view.centerYAnchor, constant: 225),
            bebidasImageView.centerXAnchor.constraint(equalTo: view.centerXAnchor, constant: 80),
            bebidasImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.7),
            bebidasImageView.heightAnchor.constraint(equalTo: bebidasImageView.widthAnchor, multiplier: 0.56)
        ])
        
        postresImageView = UIImageView(image: UIImage(named: "postres"))
        let tapPostres = UITapGestureRecognizer(target: self, action: #selector(openPostres))
        postresImageView.addGestureRecognizer(tapPostres)
        postresImageView.isUserInteractionEnabled = true
        postresImageView.alpha = 0
        postresImageView.translatesAutoresizingMaskIntoConstraints = false
        postresImageView.contentMode = .scaleAspectFit
        view.addSubview(postresImageView)

        NSLayoutConstraint.activate([
            postresImageView.centerYAnchor.constraint(equalTo: view.centerYAnchor, constant: 225),
            postresImageView.centerXAnchor.constraint(equalTo: view.centerXAnchor, constant: -80),
            postresImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.7),
            postresImageView.heightAnchor.constraint(equalTo: postresImageView.widthAnchor, multiplier: 0.56)
        ])
        resumenLabel = UILabel()
        resumenLabel.numberOfLines = 0
        resumenLabel.textAlignment = .left
        resumenLabel.font = UIFont.systemFont(ofSize: 15, weight: .medium)
        resumenLabel.textColor = .white
        resumenLabel.alpha = 0.6
        resumenLabel.translatesAutoresizingMaskIntoConstraints = false
        resumenLabel.isHidden = true
        view.addSubview(resumenLabel)

        NSLayoutConstraint.activate([
            resumenLabel.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 20),
            resumenLabel.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -20),
            resumenLabel.bottomAnchor.constraint(equalTo: view.bottomAnchor, constant: -50)
        ])

    }
    func setupArrows() {
        guard let trayView = foodStack.superview else { return }

        leftArrow = UIImageView(image: UIImage(named: "arrowL"))
        leftArrow.isHidden = true
        leftArrow.translatesAutoresizingMaskIntoConstraints = false
        leftArrow.contentMode = .scaleAspectFit
        leftArrow.isUserInteractionEnabled = true
        trayView.addSubview(leftArrow)
        
        leftArrowCenterConstraint =
                leftArrow.trailingAnchor.constraint(equalTo: categoryImageView.leadingAnchor, constant: 5)

        NSLayoutConstraint.activate([
            leftArrow.centerYAnchor.constraint(equalTo: categoryImageView.centerYAnchor, constant: 20),
            leftArrow.widthAnchor.constraint(equalToConstant: 70),
            leftArrow.heightAnchor.constraint(equalTo: leftArrow.widthAnchor),
            leftArrowCenterConstraint
        ])


        let leftTap = UITapGestureRecognizer(target: self, action: #selector(previousItem))
        leftArrow.addGestureRecognizer(leftTap)

        rightArrow = UIImageView(image: UIImage(named: "arrowR"))
        rightArrow.isHidden = true
        rightArrow.translatesAutoresizingMaskIntoConstraints = false
        rightArrow.contentMode = .scaleAspectFit
        rightArrow.isUserInteractionEnabled = true
        trayView.addSubview(rightArrow)
        
        rightArrowCenterConstraint =
                rightArrow.leadingAnchor.constraint(equalTo: categoryImageView.trailingAnchor, constant: -5)

        NSLayoutConstraint.activate([
            rightArrow.centerYAnchor.constraint(equalTo: categoryImageView.centerYAnchor, constant: 20),
            rightArrow.widthAnchor.constraint(equalToConstant: 70),
            rightArrow.heightAnchor.constraint(equalTo: rightArrow.widthAnchor),
            rightArrowCenterConstraint
        ])

        let rightTap = UITapGestureRecognizer(target: self, action: #selector(nextItem))
        rightArrow.addGestureRecognizer(rightTap)
    }
    func addDragToCategoryImage() {
        categoryImageView.isUserInteractionEnabled = true
        let pan = UIPanGestureRecognizer(target: self, action: #selector(handleDrag(_:)))
        categoryImageView.addGestureRecognizer(pan)
    }
    func updateBebidaImage() {
        let image = UIImage(named: "bebida\(bebidaIndex)")
        categoryImageView.image = image
        bebidaElegidaImageView.image = image
    }
    func updatePostreImage() {
        let image2 = UIImage(named: "postre\(postreIndex)")
        categoryImageView.image = image2
        postreElegidoImageView.image = image2
    }
    func setCategoryImageSize(multiplier: CGFloat) {
        for c in categoryImageView.constraints {
            if c.firstAttribute == .width {
                c.constant = 0
            }
        }
        categoryImageView.widthAnchor.constraint(equalTo: categoryImageView.superview!.widthAnchor, multiplier: multiplier).isActive = true
    }

    @objc func goToNiveles() {
        AudioManager.shared.playButtonSound()
        backgroundMusicPlayer?.stop()
        backgroundMusicPlayer = nil
        let nivelesVC=NivelesViewController()
        nivelesVC.modalPresentationStyle = .fullScreen
        present(nivelesVC, animated: true, completion: nil)
    }
    @objc func closeCategory() {
        AudioManager.shared.playButtonSound()
        categoryImageView.isHidden = true
        leftArrow.isHidden = true
        rightArrow.isHidden = true
        closeImageView.isHidden = true
        
        leftArrowCenterConstraint.constant = 5
        rightArrowCenterConstraint.constant = -5

        isShowingBebidas = false
        isShowingPostres = false

        platitoImageView.isHidden = true

        for (_, icon) in platoButtons {
                icon.isHidden = bebidasAndPostresUnlocked ? true : false
            }

        bebidasImageView.alpha = bebidasAndPostresUnlocked ? 1 : 0
        postresImageView.alpha = bebidasAndPostresUnlocked ? 1 : 0

        foodStack.isUserInteractionEnabled = true
    }


    @objc func handleDrag(_ sender: UIPanGestureRecognizer) {
        if interactionLocked { return }
        let location = sender.location(in: view)

        let draggedView: UIImageView

        if sender.view === categoryImageView {
            if sender.state == .began {
                guard let img = categoryImageView.image else { return }

                let copy = UIImageView(image: img)
                copy.image?.accessibilityIdentifier = categoryImageView.image?.accessibilityIdentifier
                copy.frame = categoryImageView.frame
                copy.isUserInteractionEnabled = true
                copy.contentMode = .scaleAspectFit

                let scale: CGFloat = 0.80
                let originalFrame = categoryImageView.frame
                let newWidth = originalFrame.width * scale
                let newHeight = originalFrame.height * scale
                    
                copy.frame = CGRect(
                    x: originalFrame.midX - newWidth / 2,
                    y: originalFrame.midY - newHeight / 2,
                    width: newWidth,
                    height: newHeight
                )
                
                let pan = UIPanGestureRecognizer(target: self, action: #selector(handleDrag(_:)))
                copy.addGestureRecognizer(pan)

                view.addSubview(copy)
                draggingTempCopy = copy
            }

            draggedView = draggingTempCopy ?? categoryImageView

        } else {
            draggedView = sender.view as! UIImageView
        }

        switch sender.state {
        case .changed:
            draggedView.center = location
            
            let trashFrame = basuraImageView.frame

            let innerTrashFrame = CGRect(
                x: trashFrame.origin.x + trashFrame.size.width * 0.3,
                y: trashFrame.origin.y + trashFrame.size.height * 0.3,
                width: trashFrame.size.width * 0.4,
                height: trashFrame.size.height * 0.4
            )

            let isHoveringTrash = innerTrashFrame.contains(location)

            basuraImageView.image = UIImage(named: isHoveringTrash ? "basura2" : "basura")

        case .ended:
            basuraImageView.image = UIImage(named: "basura")
            let trashFrame = basuraImageView.frame

            let innerTrashFrame = CGRect(
                x: trashFrame.origin.x + trashFrame.size.width * 0.3,
                y: trashFrame.origin.y + trashFrame.size.height * 0.3,
                width: trashFrame.size.width * 0.4,
                height: trashFrame.size.height * 0.4
            )

            let isOnTrash = innerTrashFrame.contains(location)

            if isOnTrash {
                draggedView.removeFromSuperview()
                if let index = droppedItems.firstIndex(of: draggedView) {
                    droppedItems.remove(at: index)
                }

            } else if sender.view === categoryImageView {
                if let copy = draggingTempCopy {
                    droppedItems.append(copy)
                }
            }

            draggingTempCopy = nil

        default:
            break
        }
    }
    @objc func didTapSiguiente() {
        AudioManager.shared.playButtonSound()
        instruccionesIndex = 2
        instruccionesImageView.image = UIImage(named: "instrucciones2")
        
        bebidaElegidaImageView.isHidden = false
        postreElegidoImageView.isHidden = false
        bebidasAndPostresUnlocked = true

        for (_, icon) in platoButtons { icon.isHidden = true }
        closeImageView.isHidden = true
        leftArrow.isHidden = true
        rightArrow.isHidden = true
        categoryImageView.isHidden = true
        platitoImageView.isHidden = true
        basuraImageView.isHidden = true

        UIView.animate(withDuration: 0.3) {
            self.siguienteImageView.alpha = 0
            self.listoImageView.alpha = 1
            self.bebidasImageView.alpha = 1
            self.postresImageView.alpha = 1
        }

        for item in droppedItems { item.isUserInteractionEnabled = false }
        categoryImageView.isUserInteractionEnabled = false

        for item in droppedItems {
            item.isUserInteractionEnabled = false
        }

        categoryImageView.isUserInteractionEnabled = false

    }
    @objc func openBebidas() {
        AudioManager.shared.playButtonSound()
        if interactionLocked { return }
        
        leftArrowCenterConstraint.constant = 35
        rightArrowCenterConstraint.constant = -35
        view.layoutIfNeeded()

        isShowingBebidas = true
        bebidaIndex = 1

        bebidasImageView.alpha = 0
        postresImageView.alpha = 0

        categoryImageView.isHidden = false
        leftArrow.isHidden = false
        rightArrow.isHidden = false
        closeImageView.isHidden = false
        
        setCategoryImageSize(multiplier: 0.70)
        updateBebidaImage()
        addDragToCategoryImage()
    }
    @objc func openPostres() {
        AudioManager.shared.playButtonSound()
        if interactionLocked { return }
        
        leftArrowCenterConstraint.constant = 35
        rightArrowCenterConstraint.constant = -35
        view.layoutIfNeeded()

        isShowingPostres = true
        postreIndex = 1

        bebidasImageView.alpha = 0
        postresImageView.alpha = 0

        categoryImageView.isHidden = false
        leftArrow.isHidden = false
        rightArrow.isHidden = false
        closeImageView.isHidden = false
        
        setCategoryImageSize(multiplier: 0.70)
        updatePostreImage()
        addDragToCategoryImage()
    }
    @objc func didTapListo() {
        AudioManager.shared.playButtonSound()
        backgroundImage.image = UIImage(named: "fondoPlato2")
        instruccionesImageView.image = UIImage(named: "instrucciones3")

            listoImageView.isHidden = true
            servilletaImageView.isHidden = false

            bebidasImageView.isHidden = true
            postresImageView.isHidden = true

            leftArrow.isHidden = true
            rightArrow.isHidden = true

            categoryImageView.isHidden = true
            closeImageView.isHidden = true

            isShowingBebidas = false
            isShowingPostres = false

            interactionLocked = true

            self.bebidaElegidaImageView.translatesAutoresizingMaskIntoConstraints = true
            self.postreElegidoImageView.translatesAutoresizingMaskIntoConstraints = true

            self.bebidaElegidaImageView.removeConstraints(self.bebidaElegidaImageView.constraints)
            self.postreElegidoImageView.removeConstraints(self.postreElegidoImageView.constraints)
            self.platoImageView.translatesAutoresizingMaskIntoConstraints = true
            self.platoImageView.removeConstraints(self.platoImageView.constraints)
            self.blanket.translatesAutoresizingMaskIntoConstraints = true
            self.blanket.removeConstraints(self.blanket.constraints)
        
            platoImageView.image = UIImage(named: "platito")
        
            for item in droppedItems {
                if let id = item.image?.accessibilityIdentifier {
                    if let f = frutasFeedback[id] { detallesPorAlimento += "• \(f)\n" }
                    if let f = verdurasFeedback[id] { detallesPorAlimento += "• \(f)\n" }
                    if let f = carbohidratosFeedback[id] { detallesPorAlimento += "• \(f)\n" }
                    if let f = origenAnimalFeedback[id] { detallesPorAlimento += "• \(f)\n" }
                }
            }


            UIView.animate(withDuration: 0.35) {
                for item in self.droppedItems {
                    item.frame.origin.y += 63
                    item.frame.origin.x -= 15
                }
                self.blanket.frame.origin.y -= 160

                self.bebidaElegidaImageView.frame = CGRect(
                    x: self.bebidaElegidaImageView.frame.origin.x - 17,
                    y: self.bebidaElegidaImageView.frame.origin.y + 35,
                    width: self.bebidaElegidaImageView.frame.size.width * 1.13,
                    height: self.bebidaElegidaImageView.frame.size.height * 1.13
                )

                self.postreElegidoImageView.frame = CGRect(
                    x: self.postreElegidoImageView.frame.origin.x + 9,
                    y: self.postreElegidoImageView.frame.origin.y + 23,
                    width: self.postreElegidoImageView.frame.size.width * 1.14,
                    height: self.postreElegidoImageView.frame.size.height * 1.14
                )

                self.platoImageView.frame = CGRect(
                    x: self.platoImageView.frame.origin.x - 17,
                    y: self.platoImageView.frame.origin.y + 63,
                    width: self.platoImageView.frame.size.width * 1.05,
                    height: self.platoImageView.frame.size.height * 1.05
                )

            }
        let counts = countCategoriesInPlato()
        let feedbackPrincipal = evaluatePlateProportions(counts: counts)
        let feedbackPostre = evaluatePostre()
        let feedbackBebida = evaluateBebida()
        
        let rawText = """
        \(feedbackPrincipal)
        \(feedbackPostre)
        \(feedbackBebida)
        """

        resumenLabel.text = cleanEmptyLines(rawText)

        resumenLabel.isHidden = false
        // Build per-food feedback for the popup
        let finalFeedback = """
        Detalles de tus alimentos:
        \(detallesPorAlimento)
        """

        let alert = UIAlertController(title: "Tu Plato", message: finalFeedback, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
        self.present(alert, animated: true, completion: nil)
    }
    func categoryForImageName(_ name: String) -> PlatoViewController.FoodCategory? {
        if name.contains("frutas") { return .frutas }
        if name.contains("verduras") { return .verduras }
        if name.contains("carbohidratos") { return .carbohidratos }
        if name.contains("origenanimal") { return .origenanimal }
        return nil
    }
    func countCategoriesInPlato() -> [FoodCategory: Int] {
        var counts: [FoodCategory: Int] = [
            .frutas: 0,
            .verduras: 0,
            .carbohidratos: 0,
            .origenanimal: 0
        ]

        for item in droppedItems {
            guard let name = item.image?.accessibilityIdentifier ?? item.image?.description else { continue }
            if let cat = categoryForImageName(name) {
                counts[cat, default: 0] += 1
            }
        }

        return counts
    }
    func evaluatePostre() -> String {
        guard let imgName = postreElegidoImageView.image?.description else { return "" }

        if imgName.contains("postre1") {
            return ""
        }

        return "El postre está bien de vez en cuando; recuerda comer porciones pequeñas."
    }
    func evaluateBebida() -> String {
        guard let imgName = bebidaElegidaImageView.image?.description else { return "" }

        if imgName.contains("bebida1") {
            return ""
        }

        return "Elegiste una bebida azucarada. Procura elegir agua natural para evitar exceso de azúcar."
    }
    func evaluatePlateProportions(counts: [FoodCategory: Int]) -> String {
        let total = max(droppedItems.count, 1)

        let frutas = Double(counts[.frutas] ?? 0) / Double(total)
        let verduras = Double(counts[.verduras] ?? 0) / Double(total)
        let carbs = Double(counts[.carbohidratos] ?? 0) / Double(total)
        let animal = Double(counts[.origenanimal] ?? 0) / Double(total)

        var msg = ""

        // Frutas + Verduras
        if frutas + verduras < 0.40 {
            msg += "Las frutas y verduras deben ser aproximadamente la mitad del plato.\n"
        } else {
            msg += ""
        }
        // Carbohidratos
        if carbs > 0.30 {
            msg += "Intenta que tus carbohidratos no sean más de un cuarto del plato.\n"
        } else if carbs < 0.15 {
            msg += "Te faltan carbohidratos para tener suficiente energía.\n"
        } else {
            msg += ""
        }

        // Origen animal
        if animal > 0.30 {
            msg += "Tienes mucha proteína animal. Recuerda usar porciones pequeñas.\n"
        } else if animal < 0.15 {
            msg += "Tienes poca proteína animal, podrías agregar más si lo necesitas.\n"
        } else {
            msg += ""
        }

        return msg
    }
    func cleanEmptyLines(_ text: String) -> String {
        let lines = text.components(separatedBy: .newlines)
        let nonEmptyLines = lines.filter { !$0.trimmingCharacters(in: .whitespaces).isEmpty }
        return nonEmptyLines.joined(separator: "\n")
    }
    func playBackgroundMusic() {
        guard let url = Bundle.main.url(forResource: "musicaplato", withExtension: "mp3") else {
            return
        }

        do {
            backgroundMusicPlayer = try AVAudioPlayer(contentsOf: url)
            backgroundMusicPlayer?.numberOfLoops = -1    // música en loop
            backgroundMusicPlayer?.volume = 0.6
            backgroundMusicPlayer?.prepareToPlay()
            backgroundMusicPlayer?.play()
        } catch {
        }
    }



}
