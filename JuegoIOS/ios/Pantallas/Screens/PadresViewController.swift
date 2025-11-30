//
//  PadresViewController.swift
//  Pantallas
//
//  Created by Karol Lozano González on 21/09/25.
//

import UIKit

class PadresViewController: UIViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        view.backgroundColor = UIColor(red: 255/255, green: 144/255, blue: 41/255, alpha: 1.0)
        
        let flechaImageView = UIImageView(image: UIImage(named: "flecha"))
        flechaImageView.translatesAutoresizingMaskIntoConstraints = false
        flechaImageView.isUserInteractionEnabled = true
        flechaImageView.contentMode = .scaleAspectFit
        view.addSubview(flechaImageView)
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(goToHome))
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
        textLabel.font = UIFont.systemFont(ofSize: 38, weight: .bold)
        textLabel.textColor = .white
        textLabel.text = "PADRES"
        view.addSubview(textLabel)
        
        NSLayoutConstraint.activate([
            textLabel.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 10),
            textLabel.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -10),
            textLabel.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: -35),
            textLabel.heightAnchor.constraint(equalToConstant: 180)
        ])
        setupBanners()
    }
    
    private func setupBanners() {
        let banners: [(name: String, selector: Selector, offset: CGFloat)] = [
            ("progreso", #selector(goToProgress), -180),
            ("consejos", #selector(goToHome), -10), //Creo que falta
            ("tiempo",   #selector(goToTiempo), 160) //Creo que falta
        ]
        
        for banner in banners {
            let bannerImageView = UIImageView(image: UIImage(named: banner.name))
            bannerImageView.translatesAutoresizingMaskIntoConstraints = false
            bannerImageView.contentMode = .scaleAspectFit
            bannerImageView.isUserInteractionEnabled = true
            view.addSubview(bannerImageView)
            
            let tapGesture = UITapGestureRecognizer(target: self, action: banner.selector)
            bannerImageView.addGestureRecognizer(tapGesture)
            
            // 3. Add constraints
            NSLayoutConstraint.activate([
                bannerImageView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 35),
                bannerImageView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -35),
                bannerImageView.centerYAnchor.constraint(equalTo: view.centerYAnchor, constant: banner.offset),
                bannerImageView.heightAnchor.constraint(equalTo: bannerImageView.widthAnchor, multiplier: 0.3)
            ])
        }
    }
        
    @objc func goToHome() {
            let homeVC = ViewController()
            homeVC.modalPresentationStyle = .fullScreen
            present(homeVC, animated: true, completion: nil)
        }
    @objc func goToProgress() {
            let progressVC = ParentsViewController()
            progressVC.modalPresentationStyle = .fullScreen
            present(progressVC, animated: true, completion: nil)
        }
    @objc func goToTiempo() {
            let tiempoVC = DescansoViewController()
            tiempoVC.modalPresentationStyle = .fullScreen
            present(tiempoVC, animated: true, completion: nil)
        }
}
