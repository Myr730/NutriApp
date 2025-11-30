//
//  Colors.swift
//  Aventuras con Nutri
//  Created by Victoria García on 21/09/25.

import SwiftUI

// Hacer accesible ColorScheme para AppColor
enum ColorScheme {
    static var current: SwiftUI.ColorScheme {
        // Acceder a la interfaz de usuario de UIKit
        (UITraitCollection.current.userInterfaceStyle == .dark) ? .dark : .light
    }
}

extension Color {
    init(hex: String, alpha: Double = 1.0) {
        var hexString = hex.trimmingCharacters(in: .whitespacesAndNewlines)
        hexString = hexString.replacingOccurrences(of: "#", with: "")
        var int: UInt64 = 0
        Scanner(string: hexString).scanHexInt64(&int)
        let r, g, b: Double
        switch hexString.count {
        case 6:
            r = Double((int >> 16) & 0xFF) / 255.0
            g = Double((int >> 8) & 0xFF) / 255.0
            b = Double(int & 0xFF) / 255.0
        default:
            r = 1; g = 1; b = 1
        }
        self = Color(red: r, green: g, blue: b).opacity(alpha)
    }
}

// Tus constantes de color originales
enum AppColor {
    static let primary    = Color(hex: "#F27C2F") // naranja botón JUGAR
    static let secondary  = Color(hex: "#3BA55D") // verde PADRES
    
    static let bgLight    = Color(hex: "#FFFFFF")
    static let bgDark     = Color(hex: "#0D0D0D")
    static let surfaceLight = Color(hex: "#FFFFFF")
    static let surfaceDark  = Color(hex: "#1A1A1A")

    static let onPrimary = Color(hex: "#FFFFFF")
    static let onBgLight = Color(hex: "#111111")
    static let onBgDark  = Color(hex: "#FFFFFF")
    
    // Acceso directo a los colores del tema
    static var background: Color {
        ColorScheme.current == .dark ? bgDark : bgLight
    }
    static var surface: Color {
        ColorScheme.current == .dark ? surfaceDark : surfaceLight
    }
    static var onBackground: Color {
        ColorScheme.current == .dark ? onBgDark : onBgLight
    }
}
