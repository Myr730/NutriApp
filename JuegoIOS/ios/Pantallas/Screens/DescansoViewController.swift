//
//  DescansoViewController.swift
//  Pantallas
//
//  Created by Karol Lozano González on 21/09/25.
//

import UIKit

class DescansoViewController: UIViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let backgroundImage = UIImageView(frame: UIScreen.main.bounds)
        backgroundImage.image = UIImage(named: "fondoej")
        backgroundImage.contentMode = .scaleAspectFill
        self.view.insertSubview(backgroundImage, at: 0)
        
        let flechaImageView = UIImageView(image: UIImage(named: "flecha"))
        flechaImageView.translatesAutoresizingMaskIntoConstraints = false
        flechaImageView.isUserInteractionEnabled = true
        flechaImageView.contentMode = .scaleAspectFit
        view.addSubview(flechaImageView)
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(goToPadres))
        flechaImageView.addGestureRecognizer(tapGesture)
        
        NSLayoutConstraint.activate([
            flechaImageView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: view.bounds.height * 0.02),
            flechaImageView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 25),
            flechaImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.15),
            flechaImageView.heightAnchor.constraint(equalTo: flechaImageView.widthAnchor)
        ])
        
        let textLabel = UILabel()
        textLabel.translatesAutoresizingMaskIntoConstraints = false
        textLabel.textAlignment = .center
        textLabel.numberOfLines = 2
        textLabel.font = UIFont.systemFont(ofSize: 35, weight: .bold)
        textLabel.textColor = UIColor(red: 153/255, green: 79/255, blue: 21/255, alpha: 1.0) // #994F15
        textLabel.text = "¡HORA DE\nMOVERSE!"
        view.addSubview(textLabel)

        NSLayoutConstraint.activate([
            textLabel.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 10),
            textLabel.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -10),
            textLabel.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: -20),
            textLabel.heightAnchor.constraint(equalToConstant: 180)
        ])
        
        let textLabel2 = UILabel()
        textLabel2.translatesAutoresizingMaskIntoConstraints = false
        textLabel2.textAlignment = .center
        textLabel2.numberOfLines = 2
        textLabel2.font = UIFont.systemFont(ofSize: 18, weight: .bold)
        textLabel2.textColor = UIColor(red: 153/255, green: 79/255, blue: 21/255, alpha: 1.0) // #994F15
        textLabel2.text = "¡Llevas 30 minutos jugando, hagamos\n60 seg de ejercicio!"
        view.addSubview(textLabel2)

        NSLayoutConstraint.activate([
            textLabel2.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 10),
            textLabel2.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -10),
            textLabel2.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 50),
            textLabel2.heightAnchor.constraint(equalToConstant: 180)
        ])
        
        let textLabel3 = UILabel()
        textLabel3.translatesAutoresizingMaskIntoConstraints = false
        textLabel3.textAlignment = .center
        textLabel3.numberOfLines = 2
        textLabel3.font = UIFont.systemFont(ofSize: 35, weight: .bold)
        textLabel3.textColor = UIColor(red: 153/255, green: 79/255, blue: 21/255, alpha: 1.0) // #994F15
        textLabel3.text = "1:00"
        view.addSubview(textLabel3)

        NSLayoutConstraint.activate([
            textLabel3.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 10),
            textLabel3.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -10),
            textLabel3.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: 50),
            textLabel3.heightAnchor.constraint(equalToConstant: 180)
        ])
        
        let nutriaImageView = UIImageView(image: UIImage(named: "nutriaEj"))
        nutriaImageView.translatesAutoresizingMaskIntoConstraints = false
        nutriaImageView.contentMode = .scaleAspectFit
        view.addSubview(nutriaImageView)
        
        NSLayoutConstraint.activate([
            nutriaImageView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            nutriaImageView.centerYAnchor.constraint(equalTo: view.centerYAnchor, constant: 100),
            nutriaImageView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 1.75),
            nutriaImageView.heightAnchor.constraint(equalTo: nutriaImageView.widthAnchor, multiplier: 1.0)
        ])
    }
    
    @objc func goToPadres() {
            let padresVC = PadresViewController()
            padresVC.modalPresentationStyle = .fullScreen
            present(padresVC, animated: true, completion: nil)
        }
}
