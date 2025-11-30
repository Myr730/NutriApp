//
//  MemoramaViewController.swift
//  Pantallas
//
//  Created by Karol Lozano González on 22/09/25.
//
import UIKit
import SwiftUI

class MemoramaViewController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()

        let swiftUIView = MemoryGameView()
        let hostingController = UIHostingController(rootView: swiftUIView)

        addChild(hostingController)
        hostingController.view.frame = view.bounds
        hostingController.view.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        view.addSubview(hostingController.view)
        hostingController.didMove(toParent: self)
        
        addFlecha()
    }

    private func addFlecha() {
        let flecha = UIImageView(image: UIImage(named: "flecha"))
        flecha.translatesAutoresizingMaskIntoConstraints = false
        flecha.isUserInteractionEnabled = true
        flecha.contentMode = .scaleAspectFit
        view.addSubview(flecha)

        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(goToNiveles))
        flecha.addGestureRecognizer(tapGesture)

        NSLayoutConstraint.activate([
            flecha.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: view.bounds.height * 0.02),
            flecha.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 25),
            flecha.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.15),
            flecha.heightAnchor.constraint(equalTo: flecha.widthAnchor)
        ])
    }

    @objc private func goToNiveles() {
        let nivelesVC = NivelesViewController()
        nivelesVC.modalPresentationStyle = .fullScreen
        present(nivelesVC, animated: true, completion: nil)
    }
}

// MARK: - Vista del juego

struct MemoryGameView: View {
    @StateObject var model = MemoryGameModel()

    private let columnsCount = 3
    private let rowsCount    = 4
    private let gridSpacing: CGFloat  = Spacing.s16

    var body: some View {
        AppTheme {
            ZStack {
                Image("background_memory")
                    .resizable()
                    .scaledToFill()
                    .ignoresSafeArea()

                GeometryReader { geo in
                    // ------ CÁLCULO DE TAMAÑOS ESTÉTICOS ------
                    let safeTop    = geo.safeAreaInsets.top
                    let safeBottom = geo.safeAreaInsets.bottom
                    let outerHPad: CGFloat = Spacing.s16
                    
                    // reservamos espacio vertical para título + panel inferior
                    let reservedVertical: CGFloat = 220
                    
                    let availableW = geo.size.width - (outerHPad * 2)
                    let availableH = geo.size.height - safeTop - safeBottom - reservedVertical
                    
                    // ancho permitido por el ancho de pantalla
                    let cardW_fromWidth =
                        (availableW - gridSpacing * CGFloat(columnsCount - 1)) / CGFloat(columnsCount)
                    
                    // alto disponible repartido en las filas
                    let cardH_fromHeight =
                        (availableH - gridSpacing * CGFloat(rowsCount - 1)) / CGFloat(rowsCount)
                    
                    // queremos cartas un poco más altas que anchas
                    let aspect: CGFloat = 1.45        // alto = 1.45 * ancho
                    let cardW_fromHeight = cardH_fromHeight / aspect
                    
                    // tamaño final: ni muy chicas ni enormes
                    let cardW = max(72, min(cardW_fromWidth, cardW_fromHeight))
                    let gridMaxWidth =
                        cardW * CGFloat(columnsCount) + gridSpacing * CGFloat(columnsCount - 1)
                    // -------------------------------------------
                    
                    VStack(spacing: gridSpacing * 1.2) {
                        // Título
                        Text("¡Encuentra las parejas!")
                            .font(TypeScale.titleL)
                            .foregroundColor(AppColor.onBackground)
                            .multilineTextAlignment(.center)
                            .padding(.top, Spacing.s16)
                            .shadow(color: .black.opacity(0.3), radius: 3)

                        // Un poco de aire antes de la grilla
                        Spacer(minLength: gridSpacing * 0.6)

                        // Grilla 3x4
                        LazyVGrid(
                            columns: Array(
                                repeating: GridItem(.flexible(), spacing: gridSpacing),
                                count: columnsCount
                            ),
                            spacing: gridSpacing
                        ) {
                            ForEach($model.cards) { $card in
                                if let index = model.cards.firstIndex(where: { $0.id == card.id }) {
                                    MemoryCardView(card: $card, cardWidth: cardW)
                                        .onTapGesture {
                                            model.chooseCard(at: index)
                                            AudioManager.shared.playButtonSound()
                                        }
                                }
                            }
                        }
                        .frame(maxWidth: gridMaxWidth)
                        .padding(.horizontal, outerHPad)

                        // Un poquito de espacio entre cartas y panel
                        Spacer(minLength: gridSpacing * 0.4)

                        // Panel inferior de estadísticas
                        CardContainer {
                            Text("Estadísticas del Juego")
                                .font(TypeScale.titleM)
                                .padding(.bottom, Spacing.s4)
                            
                            HStack {
                                Text("Movimientos")
                                    .font(TypeScale.bodyM)
                                Spacer()
                                Text("\(model.moves)")
                                    .font(TypeScale.titleM)
                            }
                            .padding(.vertical, 4)
                            
                            HStack {
                                Text("Pares encontrados")
                                    .font(TypeScale.bodyM)
                                Spacer()
                                Text("\(model.matches)/\(model.totalPairs)")
                                    .font(TypeScale.titleM)
                            }
                            .padding(.vertical, 4)
                        }
                        .padding(.horizontal, outerHPad)
                        .padding(.bottom, Spacing.s16)
                    }
                    .frame(maxWidth: .infinity,
                           maxHeight: .infinity,
                           alignment: .top)   // todo queda compacto arriba
                    .ignoresSafeArea(edges: [.bottom])
                }
            }
            .overlay(
                model.showCongratulations
                ? CongratulationsView(
                    moves: model.moves,
                    onPlayAgain: model.initializeGame
                )
                : nil
            )
        }
    }
}

// MARK: - Popup de felicitaciones

struct CongratulationsView: View {
    let moves: Int
    let onPlayAgain: () -> Void

    var body: some View {
        ZStack {
            Color.black.opacity(0.4).ignoresSafeArea()

            VStack(spacing: 20) {
                Image("ic_check")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 80, height: 80)

                Text("¡Felicidades! 🎉")
                    .font(.largeTitle.bold())
                    .foregroundColor(.white)

                Text("¡Completaste el memorama!")
                    .font(TypeScale.titleM)
                    .foregroundColor(.white)

                Text("Movimientos: \(moves)")
                    .font(TypeScale.titleM)
                    .foregroundColor(.white)

                PrimaryButton(title: "Jugar otra vez") {
                    onPlayAgain()
                    AudioManager.shared.playButtonSound()
                }
                .frame(maxWidth: 220)
            }
            .padding(30)
            .background(
                RoundedRectangle(cornerRadius: 24)
                    .fill(AppColor.primary)
                    .shadow(radius: 10)
            )
        }
    }
}

