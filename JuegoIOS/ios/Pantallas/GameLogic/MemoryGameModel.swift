//
//  MemoryGameModel.swift
//  Pantallas
//
//  Created by Victoria García on 28/11/25.
//


import Foundation
import SwiftUI

// NOTA: La struct MemoryCard se mueve a su propio archivo MemoryCard.swift

final class MemoryGameModel: ObservableObject {
    @Published var cards = [MemoryCard]() // Ahora usa la struct del archivo MemoryCard.swift
    @Published var moves = 0
    @Published var matches = 0
    @Published var isChecking = false
    @Published var showCongratulations = false
    let totalPairs = 6
    
    var indexOfSingleFaceUpCard: Int?

    private let cardValues: [String] = [
        "frutas1", "verduras2", "carbohidratos3",
        "origenanimal4", "frutas5", "verduras6"
    ]
    
    init() {
        initializeGame()
    }
    
    func initializeGame() {
        moves = 0
        matches = 0
        isChecking = false
        showCongratulations = false
        indexOfSingleFaceUpCard = nil
        cards.removeAll()

        var cardPairs: [MemoryCard] = []
        var nextID = 0
        
        for value in cardValues.shuffled() {
            cardPairs.append(MemoryCard(id: nextID, value: value))
            nextID += 1
            cardPairs.append(MemoryCard(id: nextID, value: value))
            nextID += 1
        }
        
        cards = cardPairs.shuffled()
    }

    func chooseCard(at index: Int) {
        guard index >= 0, index < cards.count, !isChecking, !cards[index].isFaceUp, !cards[index].isMatched else {
            return
        }

        cards[index].isFaceUp = true
        
        DispatchQueue.main.async { [weak self] in
            guard let self = self else { return }

            if let potentialMatchIndex = self.indexOfSingleFaceUpCard {
                self.isChecking = true
                self.moves += 1
                
                let firstCard = self.cards[potentialMatchIndex]
                let secondCard = self.cards[index]

                if firstCard.value == secondCard.value {
                    self.matches += 1
                    
                    DispatchQueue.main.asyncAfter(deadline: .now() + 0.8) {
                        self.cards[potentialMatchIndex].isMatched = true
                        self.cards[index].isMatched = true
                        self.isChecking = false
                        self.indexOfSingleFaceUpCard = nil
                        
                        AudioManager.shared.playButtonSound()

                        if self.matches == self.totalPairs {
                            self.showCongratulations = true
                        }
                    }
                } else {
                    DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
                        self.cards[potentialMatchIndex].isFaceUp = false
                        self.cards[index].isFaceUp = false
                        self.isChecking = false
                        self.indexOfSingleFaceUpCard = nil
                    }
                }
            } else {
                self.indexOfSingleFaceUpCard = index
            }
        }
    }
}
