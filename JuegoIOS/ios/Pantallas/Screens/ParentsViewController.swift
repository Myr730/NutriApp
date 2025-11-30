//
//  ParentsViewController.swift
//  Pantallas
//
//  Created by Karol Lozano González on 22/09/25.
//

import UIKit
import SwiftUI

class ParentsViewController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()

        let swiftUIView = ParentsView()
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

        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(goToPadres))
        flecha.addGestureRecognizer(tapGesture)

        NSLayoutConstraint.activate([
            flecha.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: view.bounds.height * 0.02),
            flecha.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 25),
            flecha.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.15),
            flecha.heightAnchor.constraint(equalTo: flecha.widthAnchor)
        ])
    }

    @objc private func goToPadres() {
        let padresVC=PadresViewController()
        padresVC.modalPresentationStyle = .fullScreen
        present(padresVC, animated: true, completion: nil)
    }
}



struct ParentsView: View {
    // Datos de muestra para el historial
    private let rows: [(date: String, value: String, isAlt: Bool)] = [
        ("15/09/2025", "58.0", true),
        ("16/09/2023", "58.5", false),
        ("17/09/2025", "59.0", true),
        ("18/09/2025", "59.5", false)
    ]

    var body: some View {
        AppTheme {
            ZStack(alignment: .bottomTrailing) {
                // Fondo cálido (sin assets)
                LinearGradient(
                    colors: [
                        Color(.systemYellow).opacity(0.25),
                        Color(.systemOrange).opacity(0.18)
                    ],
                    startPoint: .top, endPoint: .bottom
                )
                .ignoresSafeArea()

                ScrollView {
                    VStack(alignment: .leading, spacing: Spacing.s24) {

                        // Encabezado grande
                        Text("parents_title")
                            .font(TypeScale.titleXL)
                            .shadow(color: .black.opacity(0.3), radius: 6, y: 2)
                            .frame(maxWidth: .infinity, alignment: .center)
                            .padding(.top, Spacing.s8)

                        // Tarjeta: Perfil
                        CardContainer {
                            Text("section_profile_title")
                                .font(TypeScale.titleM)
                                .foregroundStyle(AppColor.primary)
                                .frame(maxWidth: .infinity, alignment: .center)
                                .padding(.bottom, Spacing.s8)

                            Text("profile_name_placeholder")
                                .font(TypeScale.bodyM)
                                .frame(maxWidth: .infinity, alignment: .center)
                        }

                        // Tarjeta: Historial de Peso
                        CardContainer {
                            // Título centrado
                            Text("section_weight_title")
                                .font(TypeScale.titleM)
                                .foregroundStyle(AppColor.primary)
                                .frame(maxWidth: .infinity, alignment: .center)

                            // Encabezados
                            HStack {
                                Text("weight_header_date").font(.headline)
                                Spacer()
                                Text("weight_header_value").font(.headline)
                            }
                            .padding(.vertical, Spacing.s8)
                            .padding(.horizontal, Spacing.s8)
                            .background(AppColor.primary.opacity(0.15))
                            .clipShape(RoundedRectangle(cornerRadius: 8, style: .continuous))

                            // Filas alternadas (verde/rosa suave)
                            VStack(spacing: Spacing.s8) {
                                ForEach(0..<rows.count, id: \.self) { i in
                                    let row = rows[i]
                                    HStack {
                                        Text(row.date)
                                        Spacer()
                                        Text(row.value)
                                    }
                                    .font(TypeScale.bodyM)
                                    .padding(.vertical, Spacing.s8)
                                    .padding(.horizontal, Spacing.s8)
                                    .background(
                                        RoundedRectangle(cornerRadius: 8, style: .continuous)
                                            .fill(row.isAlt
                                                  ? Color.green.opacity(0.12)
                                                  : Color.red.opacity(0.12))
                                    )
                                }
                            }
                        }
                    }
                    .padding(Spacing.s24)
                }

                // FAB (+)
                Button(action: {}) {
                    Image(systemName: "plus")
                        .font(.system(size: 22, weight: .bold))
                        .foregroundStyle(AppColor.onPrimary)
                        .padding(20)
                        .background(Circle().fill(AppColor.primary))
                        .shadow(color: .black.opacity(0.25), radius: 10, y: 6)
                }
                .padding(.trailing, Spacing.s24)
                .padding(.bottom, Spacing.s24)
                .accessibilityLabel("Agregar registro")
            }
        }
    }
}

#Preview {
    ParentsView()
}
