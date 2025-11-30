//
//  MemoryCard.swift
//  Pantallas
//
//  Created by Victoria García on 28/11/25.
//


import Foundation

// Al estar en su propio archivo, esta estructura es accesible a todos los demás archivos.
struct MemoryCard: Identifiable {
    let id: Int
    let value: String // Nombre del asset de la imagen (e.g., "frutas1")
    var isFaceUp: Bool = false
    var isMatched: Bool = false
}
