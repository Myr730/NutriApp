//
//  MemoryCardView.swift
//  Aventuras con Nutri
//
//  Created by Victoria García on 21/09/25.
//

// Pantallas/Components/MemoryCardView.swift

import SwiftUI

struct MemoryCardView: View {
    // Ya no hay error de visibilidad gracias al nuevo archivo MemoryCard.swift
    @Binding var card: MemoryCard
    let cardWidth: CGFloat
    
    private let aspectRatio: CGFloat = 0.68

    var body: some View {
        let cardHeight = cardWidth / aspectRatio

        ZStack {
            // FRENTE de la carta (Imagen de comida)
            Image(card.value)
                .resizable()
                .scaledToFill()
                .frame(width: cardWidth, height: cardHeight)
                .clipShape(RoundedRectangle(cornerRadius: 12))
                .opacity(card.isFaceUp || card.isMatched ? 1 : 0)
                .rotation3DEffect(.degrees(card.isFaceUp || card.isMatched ? 0 : -180), axis: (x: 0, y: 1, z: 0))

            // REVERSO de la carta (Asset "carta")
            Image("carta")
                .resizable()
                .scaledToFill()
                .frame(width: cardWidth, height: cardHeight)
                .clipShape(RoundedRectangle(cornerRadius: 12))
                .opacity(card.isFaceUp || card.isMatched ? 0 : 1)
                .rotation3DEffect(.degrees(card.isFaceUp || card.isMatched ? 180 : 0), axis: (x: 0, y: 1, z: 0))
        }
        .frame(width: cardWidth, height: cardHeight)
        .shadow(radius: card.isMatched ? 8 : 5)
        .animation(.default.speed(1.5), value: card.isFaceUp)
        .animation(.default, value: card.isMatched)
    }
}
